package harish.project.synccity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class DeleteProfileActivity extends AppCompatActivity {

    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private EditText editTextUserPwd;
    private TextView textViewAuntheticated;
    private ProgressBar progressBar;
    private String userPwd;
    private Button buttonReAuthenticate, buttonDeleteUser;
    private static final String TAG = "DeleteProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_profile);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Delete your profile");

        progressBar = findViewById(R.id.progressBar);
        editTextUserPwd = findViewById(R.id.editText_delete_user_pwd);
        textViewAuntheticated = findViewById(R.id.textView_delete_user_authenticated);
        buttonDeleteUser = findViewById(R.id.button_delete_user);
        buttonReAuthenticate = findViewById(R.id.button_delete_user_authenticate);

        // Disable Delete User Button until user is authenticated
        buttonDeleteUser.setEnabled(false);

        authProfile = FirebaseAuth.getInstance();
        firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser != null && firebaseUser.equals("")) {
            Toast.makeText(DeleteProfileActivity.this, "Something went wrong !", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(DeleteProfileActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        } else {
            reAutheticateUser(firebaseUser);
        }
    }

    // ReAuntheticate user before delete profile
    private void reAutheticateUser(FirebaseUser firebaseUser) {
        buttonReAuthenticate.setOnClickListener(view -> {
            userPwd = editTextUserPwd.getText().toString();

            if(TextUtils.isEmpty(userPwd)) {
                Toast.makeText(DeleteProfileActivity.this, "Password is needed", Toast.LENGTH_SHORT).show();
                editTextUserPwd.setError("Enter current password");
                editTextUserPwd.requestFocus();
            }else {
                progressBar.setVisibility(View.VISIBLE);

                // ReAuntheticate user now
                AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(firebaseUser.getEmail()), userPwd);

                firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);

                        // Disable editText for Password
                        editTextUserPwd.setEnabled(false);

                        // Enable Delete User button. Disable autheticate button
                        buttonReAuthenticate.setEnabled(false);
                        buttonDeleteUser.setEnabled(true);

                        // Set TextView to show User is authenticated/verified
                        textViewAuntheticated.setText("You are authenticated to delete your profile permanently.");
                        Toast.makeText(DeleteProfileActivity.this, "Password has been verified. You can delete your profile now.", Toast.LENGTH_SHORT).show();

                        // Update color of delete profile button
                        buttonDeleteUser.setBackgroundTintList(ContextCompat.getColorStateList(DeleteProfileActivity.this, R.color.green_blue));

                        buttonDeleteUser.setOnClickListener(view1 -> showAlertDialog());
                    }else {
                        try{
                            throw Objects.requireNonNull(task.getException());
                        }catch(Exception e){
                            Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                });
            }
        });
    }

    // Show alert before deleting profile permanently
    private void showAlertDialog() {
        // Setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(DeleteProfileActivity.this);
        builder.setTitle("Delete user and related data ?");
        builder.setMessage("Do you really want to delete profile ?");

        // Open Email Apps if User clicks continue button
        builder.setPositiveButton("Continue", (dialog, which) -> deleteUserData(firebaseUser));

        // Return to user profile is user cancels delete profile warning
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            Intent intent = new Intent(DeleteProfileActivity.this, UserProfileActivity.class);
            startActivity(intent);
            finish();
        });

        // Create the AlertDialog
        AlertDialog alertDialog = builder.create();

        // Change the button color of Continue
        alertDialog.setOnShowListener(dialogInterface -> alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.purple_500)));

        // Show the AlertDialog
        alertDialog.show();
    }

    private void deleteUser () {
        firebaseUser.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                authProfile.signOut();
                Toast.makeText(DeleteProfileActivity.this, "User has been deleted!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(DeleteProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (Exception e) {
                    Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    // Delete all the data of user
    private void deleteUserData(FirebaseUser firebaseUser) {
        // Delete display picture with checking if the user has uploaded any pic before deleting
        if(firebaseUser.getPhotoUrl() != null) {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReferenceFromUrl(firebaseUser.getPhotoUrl().toString());
            storageReference.delete().addOnSuccessListener(unused -> {
                Log.d(TAG, "OnSuccess: Photo deleted");

                // Finally delete the user after deleting user data
                deleteUser();
            }).addOnFailureListener(e -> {
                Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                Toast.makeText(DeleteProfileActivity.this,  e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }


        // Delete data from Realtime Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");
        databaseReference.child(firebaseUser.getUid()).removeValue().addOnSuccessListener(unused -> Log.d(TAG, "OnSuccess: User Data Deleted")).addOnFailureListener(e -> {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
            Toast.makeText(DeleteProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    // Creating ActionBar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu items
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // When any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(DeleteProfileActivity.this);
        } else if(id == R.id.menu_refresh) {
            // Refresh Activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);
        }else if(id == R.id.menu_update_profile) {
            Intent intent = new Intent(DeleteProfileActivity.this , UpdateProfileActivity.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.menu_update_email){
            Intent intent = new Intent(DeleteProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
            finish();
        }else if(id == R.id.menu_settings) {
            Toast.makeText(DeleteProfileActivity.this , "menu_settings" , Toast.LENGTH_SHORT).show();
        }else if(id == R.id.menu_change_password) {
            Intent intent = new Intent(DeleteProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
            finish();
        }else if(id == R.id.menu_delete_profile) {
            Intent intent = new Intent(DeleteProfileActivity.this, DeleteProfileActivity.class);
            startActivity(intent);
            finish();
        }else if(id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(DeleteProfileActivity.this, "Logged Out", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(DeleteProfileActivity.this, MainActivity.class);

            // Clear stack to prevent user coming back to UserProfileActivity on pressing back button after logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else {
            Toast.makeText(DeleteProfileActivity.this, "Something went wrong !", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}