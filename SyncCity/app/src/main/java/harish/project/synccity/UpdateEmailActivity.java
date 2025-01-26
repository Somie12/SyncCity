package harish.project.synccity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UpdateEmailActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private TextView textViewAuthenticated;
    private String userOldEmail, userNewEmail, userPwd;
    private Button buttonUpdateEmail;
    private EditText editTextNewEmail, editTextPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Update Email");

        progressBar = findViewById(R.id.progressBar);
        editTextPwd = findViewById(R.id.editText_update_email_verify_password);
        editTextNewEmail = findViewById(R.id.editText_update_email_new);
        textViewAuthenticated = findViewById(R.id.textView_update_email_authenticated);
        buttonUpdateEmail = findViewById(R.id.button_update_email);

        buttonUpdateEmail.setEnabled(false);
        editTextNewEmail.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        // Set old email ID on TextView
        if (firebaseUser != null) {
            userOldEmail = firebaseUser.getEmail();
        }
        TextView textViewOldEmail = findViewById(R.id.textView_update_email_old);
        textViewOldEmail.setText(userOldEmail);

        if (firebaseUser.equals("")) {
            Toast.makeText(UpdateEmailActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
        } else {
            reAutheticate(firebaseUser);
        }
    }

    // ReAutheticate / Verify User before updating email
    @SuppressLint("SetTextI18n")
    private void reAutheticate(FirebaseUser firebaseUser) {
        Button buttonVerifyUser = findViewById(R.id.button_authenticate_user);
        buttonVerifyUser.setOnClickListener(v -> {
            // Obtain password for authentication
            userPwd = editTextPwd.getText().toString();

            if(TextUtils.isEmpty(userPwd)) {
                Toast.makeText(UpdateEmailActivity.this, "Password is needed to continue.", Toast.LENGTH_SHORT).show();
                editTextPwd.setError("Please enter your password for authetication");
                editTextPwd.requestFocus();
            }else{
                progressBar.setVisibility(View.VISIBLE);
                AuthCredential credential = EmailAuthProvider.getCredential(userOldEmail , userPwd);

                firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);

                        Toast.makeText(UpdateEmailActivity.this, "Password has been verified. You can update email now.", Toast.LENGTH_LONG).show();

                        // Set TextView to show that user is authenticated
                        textViewAuthenticated.setText("You are authenticated. You can update your email now.");

                        // Disable EditText for password , button to verify user and enable EditText for new Email and Update Email button
                        editTextNewEmail.setEnabled(true);
                        editTextPwd.setEnabled(false);
                        buttonVerifyUser.setEnabled(false);
                        buttonUpdateEmail.setEnabled(true);

                        // Change color of Update Email button
                        buttonUpdateEmail.setBackgroundTintList(ContextCompat.getColorStateList(UpdateEmailActivity.this, R.color.green_blue));

                        buttonUpdateEmail.setOnClickListener(v1 -> {
                            userNewEmail = editTextNewEmail.getText().toString();
                            if (TextUtils.isEmpty(userNewEmail)) {
                                Toast.makeText(UpdateEmailActivity.this, "New email is required", Toast.LENGTH_SHORT).show();
                                editTextNewEmail.setError("Please enter new Email");
                                editTextNewEmail.requestFocus();
                            } else if (!Patterns.EMAIL_ADDRESS.matcher(userNewEmail).matches()) {
                                Toast.makeText(UpdateEmailActivity.this, "Enter valid email id", Toast.LENGTH_SHORT).show();
                                editTextNewEmail.setError("Please provide valid Email");
                                editTextNewEmail.requestFocus();
                            } else {
                                progressBar.setVisibility(View.VISIBLE);
                                updateEmail(firebaseUser);
                            }
                        });
                    } else {
                        try {
                            throw Objects.requireNonNull(task.getException());
                        }catch(Exception e) {
                            Toast.makeText(UpdateEmailActivity.this, e.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

    private void updateEmail(FirebaseUser firebaseUser) {
        firebaseUser.updateEmail(userNewEmail).addOnCompleteListener(task -> {
            if (task.isComplete()){
                //Verify Email
                firebaseUser.sendEmailVerification();

                Toast.makeText(UpdateEmailActivity.this, "Email has been updated. Please verify your new Email", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateEmailActivity.this, UserProfileActivity.class);
                startActivity(intent);
                finish();
            } else {
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (Exception e){
                    Toast.makeText(UpdateEmailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    // Creating Actionbar menu
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu items
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // When any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_refresh) {
            // Refresh Activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);
        }else if(id == R.id.menu_update_profile) {
            Intent intent = new Intent(UpdateEmailActivity.this , UpdateProfileActivity.class);
            startActivity(intent);
        } else if(id == R.id.menu_update_email){
            Toast.makeText(UpdateEmailActivity.this , "menu_settings", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.menu_settings) {
            Toast.makeText(UpdateEmailActivity.this , "menu_settings" , Toast.LENGTH_SHORT).show();
        }else if(id == R.id.menu_change_password) {
            Intent intent = new Intent(UpdateEmailActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        }else if(id == R.id.menu_delete_profile) {
            Intent intent = new Intent(UpdateEmailActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
        }else if(id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(UpdateEmailActivity.this, "Logged Out", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UpdateEmailActivity.this, MainActivity.class);

            // Clear stack to prevent user coming back to UserProfileActivity on pressing back button after logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(UpdateEmailActivity.this, "Something went wrong !", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}