package harish.project.synccity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button buttonPwdReset;
    private EditText editTextPwdResetEmail;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private final static String TAG = "ForgotPasswordActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Forgot Password");

        editTextPwdResetEmail = findViewById(R.id.editText_password_reset_email);
        buttonPwdReset = findViewById(R.id.button_password_reset);
        progressBar = findViewById(R.id.progressBar);

        buttonPwdReset.setOnClickListener(v -> {
            String email = editTextPwdResetEmail.getText().toString();

            if(TextUtils.isEmpty(email)) {
                Toast.makeText(ForgotPasswordActivity.this , "Please enter your registered email" , Toast.LENGTH_SHORT).show();
                editTextPwdResetEmail.setError("Email is required");
                editTextPwdResetEmail.requestFocus();
            }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(ForgotPasswordActivity.this , "Enter valid email" , Toast.LENGTH_SHORT).show();
                editTextPwdResetEmail.setError("Valid email is required");
                editTextPwdResetEmail.requestFocus();
            }else {
                progressBar.setVisibility(View.VISIBLE);
                resetPassword(email);
            }
        });
    }

    private void resetPassword(String email) {
        authProfile = FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Toast.makeText(ForgotPasswordActivity.this, "Check mail for password reset link", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ForgotPasswordActivity.this , MainActivity.class);

                // Clear stack to prevent user coming back to UserProfileActivity on perssing back button after logging out
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }else {
                try {
                    throw Objects.requireNonNull(task.getException());
                }catch (FirebaseAuthInvalidUserException e) {
                    editTextPwdResetEmail.setError("User does not exist. Register user.");
                }catch (Exception e) {
                    Log.e(TAG, Objects.requireNonNull(e.getMessage()));
                    Toast.makeText(ForgotPasswordActivity.this, "", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(ForgotPasswordActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }
}