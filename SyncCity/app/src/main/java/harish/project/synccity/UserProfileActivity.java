package harish.project.synccity;

import com.squareup.picasso.Picasso;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Objects;

public class UserProfileActivity extends AppCompatActivity {

    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewDoB, textViewGender, textViewMobile;
    private ProgressBar progressBar;
    private ImageView imageView;
    private FirebaseAuth authProfile;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Home");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        swipeToRefresh();

        textViewWelcome = findViewById(R.id.textView_show_welcome);
        textViewFullName = findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewDoB = findViewById(R.id.textView_show_dob);
        textViewGender = findViewById(R.id.textView_show_gender);
        textViewMobile = findViewById(R.id.textView_show_mobile);
        progressBar = findViewById(R.id.progress_bar);

        imageView = findViewById(R.id.imageView_profile_dp);
        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, UploadProfilePicActivity.class);
            startActivity(intent);
        });

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(UserProfileActivity.this, "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
        } else {
            checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }
    }

    private void swipeToRefresh() {
        swipeContainer = findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(() -> {
            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);
            swipeContainer.setRefreshing(false);
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()) {
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Email not verified")
                .setMessage("Please verify your email now. You cannot login without email verification.")
                .setPositiveButton("Continue", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                })
                .create()
                .show();
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    String fullName = firebaseUser.getDisplayName();
                    String email = firebaseUser.getEmail();
                    String doB = readUserDetails.doB;
                    String gender = readUserDetails.gender;
                    String mobile = readUserDetails.mobile;

                    textViewWelcome.setText("Welcome, " + fullName + "!");
                    textViewFullName.setText(fullName);
                    textViewEmail.setText(email);
                    textViewDoB.setText(doB);
                    textViewGender.setText(gender);
                    textViewMobile.setText(mobile);

                    Uri uri = firebaseUser.getPhotoUrl();
                    Picasso.get()
                            .load(uri)
                            .placeholder(R.drawable.default_profile_image)
                            .error(R.drawable.ic_profile)
                            .into(imageView);
                } else {
                    imageView.setImageResource(R.drawable.default_profile_image);
                    Toast.makeText(UserProfileActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(UserProfileActivity.this);
            return true;
        } else if (id == R.id.menu_refresh) {
            startActivity(getIntent());
            finish();
            overridePendingTransition(0, 0);
            return true;
        } else if (id == R.id.menu_update_profile) {
            startActivity(new Intent(UserProfileActivity.this, UpdateProfileActivity.class));
            finish();
            return true;
        } else if (id == R.id.menu_update_email) {
            startActivity(new Intent(UserProfileActivity.this, UpdateEmailActivity.class));
            finish();
            return true;
        } else if (id == R.id.menu_settings) {
            Toast.makeText(UserProfileActivity.this, "Settings", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_change_password) {
            startActivity(new Intent(UserProfileActivity.this, ChangePasswordActivity.class));
            finish();
            return true;
        } else if (id == R.id.menu_delete_profile) {
            startActivity(new Intent(UserProfileActivity.this, DeleteProfileActivity.class));
            finish();
            return true;
        } else if (id == R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(UserProfileActivity.this, "Logged Out", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        } else {
            Toast.makeText(UserProfileActivity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            return super.onOptionsItemSelected(item);
        }
    }
}
