package harish.project.synccity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullName, editTextRegisterEmail, editTextRegisterDOB, editTextRegisterMobile, editTextRegisterPwd, editTextRegisterConfirmPwd;
    private ProgressBar progressBar;
    private RadioGroup radioGroupRegisterGender;
    private DatePickerDialog picker;
    private static final String TAG = "RegisterActivity";

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Register");
        }
        Toast.makeText(this, "You can register now", Toast.LENGTH_LONG).show();

        progressBar = findViewById(R.id.progressBar);
        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDOB = findViewById(R.id.editText_register_dob);
        editTextRegisterMobile = findViewById(R.id.editText_register_mobile);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPwd = findViewById(R.id.editText_register_password_confirm);

        radioGroupRegisterGender = findViewById(R.id.radio_group_register_gender);

        // DatePicker setup
        editTextRegisterDOB.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            picker = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
                editTextRegisterDOB.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
            }, year, month, day);
            picker.show();
        });

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(v -> {
            String textFullName = editTextRegisterFullName.getText().toString().trim();
            String textEmail = editTextRegisterEmail.getText().toString().trim();
            String textDOB = editTextRegisterDOB.getText().toString().trim();
            String textMobile = editTextRegisterMobile.getText().toString().trim();
            String textPwd = editTextRegisterPwd.getText().toString().trim();
            String textConfirmPwd = editTextRegisterConfirmPwd.getText().toString().trim();
            RadioButton radioButtonRegisterGenderSelected = findViewById(radioGroupRegisterGender.getCheckedRadioButtonId());
            String textGender = radioButtonRegisterGenderSelected != null ? radioButtonRegisterGenderSelected.getText().toString() : "";

            if (validateInputs(textFullName, textEmail, textDOB, textMobile, textPwd, textConfirmPwd, textGender)) {
                progressBar.setVisibility(View.VISIBLE);
                registerUser(textFullName, textEmail, textDOB, textGender, textMobile, textPwd);
            }
        });
    }

    private boolean validateInputs(String fullName, String email, String dob, String mobile, String pwd, String confirmPwd, String gender) {
        if (TextUtils.isEmpty(fullName)) {
            showToastAndError(editTextRegisterFullName, "Full name is required");
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            showToastAndError(editTextRegisterEmail, "Email is required");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToastAndError(editTextRegisterEmail, "Valid email is required");
            return false;
        }
        if (TextUtils.isEmpty(dob)) {
            showToastAndError(editTextRegisterDOB, "Date of birth is required");
            return false;
        }
        if (radioGroupRegisterGender.getCheckedRadioButtonId() == -1) {
            showToastAndError(radioGroupRegisterGender, "Gender is required");
            return false;
        }
        if (TextUtils.isEmpty(mobile) || mobile.length() != 10 || !Pattern.compile("[6-9][0-9]{9}").matcher(mobile).matches()) {
            showToastAndError(editTextRegisterMobile, "Valid mobile number is required");
            return false;
        }
        if (TextUtils.isEmpty(pwd)) {
            showToastAndError(editTextRegisterPwd, "Password is required");
            return false;
        }
        if (pwd.length() < 8) {
            showToastAndError(editTextRegisterPwd, "Password must be at least 8 characters");
            return false;
        }
        if (TextUtils.isEmpty(confirmPwd)) {
            showToastAndError(editTextRegisterConfirmPwd, "Confirm password is required");
            return false;
        }
        if (!pwd.equals(confirmPwd)) {
            showToastAndError(editTextRegisterConfirmPwd, "Passwords do not match");
            return false;
        }
        return true;
    }

    private void showToastAndError(View view, String message) {
        if (view instanceof EditText) {
            ((EditText) view).setError(message);
            view.requestFocus();
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void registerUser(String fullName, String email, String dob, String gender, String mobile, String pwd) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser != null) {
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(fullName).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(dob, gender, mobile);
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered users");
                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(task1 -> {
                        progressBar.setVisibility(View.GONE);
                        if (task1.isSuccessful()) {
                            firebaseUser.sendEmailVerification();
                            Toast.makeText(this, "User registered successfully. Verify your email", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(this, UserProfileActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(this, "Registration failed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                progressBar.setVisibility(View.GONE);
                handleRegistrationError(task.getException());
            }
        });
    }

    private void handleRegistrationError(Exception e) {
        if (e instanceof FirebaseAuthWeakPasswordException) {
            showToastAndError(editTextRegisterPwd, "Password is too weak. Please use special characters.");
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            showToastAndError(editTextRegisterEmail, "Invalid email or already registered.");
        } else if (e instanceof FirebaseAuthUserCollisionException) {
            showToastAndError(editTextRegisterEmail, "Email is already registered.");
        } else {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
