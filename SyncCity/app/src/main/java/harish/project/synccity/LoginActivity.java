package harish.project.synccity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextLoginEmail, editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;
    private static final String TAG = "LoginActivity";
    private SwipeRefreshLayout SwipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set the title if ActionBar is available
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login");
        }

        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPwd = findViewById(R.id.editText_login_pwd);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();

        // Reset Password
        TextView textViewLinkResetPwd = findViewById(R.id.textView_forgot_password_link);
        textViewLinkResetPwd.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        });

        // Register
        TextView textViewLinkRegister = findViewById(R.id.textView_register_link);
        textViewLinkRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        // Show/Hide Password
        ImageView imageViewShowHidePwd = findViewById(R.id.imageView_show_hide_pwd);
        imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
        imageViewShowHidePwd.setOnClickListener(v -> {
            if (editTextLoginPwd.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                editTextLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imageViewShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
            } else {
                editTextLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imageViewShowHidePwd.setImageResource(R.drawable.ic_show_pwd);
            }
        });

        // Login User
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, Dashboard.class));
        });

//        buttonLogin.setOnClickListener(v -> {
//            String textEmail = editTextLoginEmail.getText().toString();
//            String textPwd = editTextLoginPwd.getText().toString();
//
//            if (TextUtils.isEmpty(textEmail)) {
//                editTextLoginEmail.setError("Email is required");
//                editTextLoginEmail.requestFocus();
//            } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
//                editTextLoginEmail.setError("Enter a valid email");
//                editTextLoginEmail.requestFocus();
//            } else if (TextUtils.isEmpty(textPwd)) {
//                editTextLoginPwd.setError("Password is required");
//                editTextLoginPwd.requestFocus();
//            } else {
//                progressBar.setVisibility(View.VISIBLE);
//                loginUser(textEmail, textPwd);
//            }
//        });
    }

//    private void loginUser(String email, String pwd) {
//        TextView textViewLinkRegister = findViewById(R.id.textView_login_email);
//        textViewLinkRegister.setOnClickListener(v -> {
//            startActivity(new Intent(LoginActivity.this, Dashboard.class));
//        });
//        authProfile.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(this, task -> {
//            if (task.isSuccessful()) {
//                FirebaseUser firebaseUser = authProfile.getCurrentUser();
//
//                if (firebaseUser != null && firebaseUser.isEmailVerified()) {
//                    Toast.makeText(LoginActivity.this, "You are logged in now", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
//                    finish();
//                } else {
//                    if (firebaseUser != null) {
//                        firebaseUser.sendEmailVerification();
//                    }
//                    authProfile.signOut();
//                    showAlertDialog();
//                }
//            } else {
//                if (task.getException() != null) {
//                    try {
//                        throw task.getException();
//                    } catch (FirebaseAuthInvalidUserException e) {
//                        editTextLoginEmail.setError("User does not exist. Please register.");
//                    } catch (FirebaseAuthInvalidCredentialsException e) {
//                        editTextLoginEmail.setError("Invalid credentials. Please try again.");
//                        editTextLoginEmail.requestFocus();
//                    } catch (Exception e) {
//                        Log.e(TAG, "Login failed: " + e.getMessage());
//                        Toast.makeText(LoginActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
//                }
//            }
//            progressBar.setVisibility(View.GONE);
//        });
//   }


    private void showAlertDialog() {
        // Setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email now. You can not login without email verification");

        // Open Email Apps if User clicks continue button
        builder.setPositiveButton("Continue", (dialog, which) -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Show the AlertDialog
        alertDialog.show();
    }

    // Check if user is already logged in. In such case , straightway take the user to the user's profile.
    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser() != null) {
            Toast.makeText(LoginActivity.this , "Already logged in !" , Toast.LENGTH_SHORT).show();

            // Start the UserProfileActivity
            startActivity(new Intent(LoginActivity.this , UserProfileActivity.class));
            finish(); // Close login activity
        }else {
            Toast.makeText(LoginActivity.this , "You can log in now.", Toast.LENGTH_SHORT).show();
        }
    }
}