package harish.project.synccity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;

import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {

    private Button btnProjectLinks, btnProjectDatabase, btnTaskScheduling, btnResourceInventory, btnUnifiedProjectPhasing, btnDiscussionForum, btnTrainingWorkshops;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnProjectLinks = findViewById(R.id.btn_project_links);
        btnProjectDatabase = findViewById(R.id.btn_project_database);
        btnTaskScheduling = findViewById(R.id.btn_task_scheduling);
        btnResourceInventory = findViewById(R.id.btn_resource_inventory);
        btnUnifiedProjectPhasing = findViewById(R.id.btn_unified_project_phasing);
        btnDiscussionForum = findViewById(R.id.btn_discussion_forum);
        btnTrainingWorkshops = findViewById(R.id.btn_training_workshops);
        View profilePic = findViewById(R.id.profile_pic);


        profilePic.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, UserProfileActivity.class);
            startActivity(intent);
        });

        btnTaskScheduling.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, TaskScheduling.class);
            startActivity(intent);
        });

        btnResourceInventory.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, ResourceInventory.class);
            startActivity(intent);
        });

        btnProjectDatabase.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, ProjectDatabaseHome.class);
            startActivity(intent);
        });

        btnDiscussionForum.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, DiscussionForumMain.class);
            startActivity(intent);
        });

        btnTrainingWorkshops.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, TrainingWorkshop.class);
            startActivity(intent);
        });

        btnUnifiedProjectPhasing.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, UnifiedProjectPhasing.class);
            startActivity(intent);
        });


        Button projectLinkButton = findViewById(R.id.btn_project_links);
        projectLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProjectListDialog();
            }
        });

    }

    private void showProjectListDialog() {
        ProjectRepository repository = new ProjectRepository();
        StringBuilder projectDetails = new StringBuilder();
        for (ProjectLinks project : repository.getProjects()) {
            projectDetails.append("Name: ").append(project.getName()).append("\n")
                    .append("Department: ").append(project.getDept()).append("\n")
                    .append("Location: ").append(project.getLocation()).append("\n")
                    .append("Start Date: ").append(project.getStartDate()).append("\n")
                    .append("End Date: ").append(project.getEndDate()).append("\n")
                    .append("Status: ").append(project.getStatus()).append("\n")
                    .append("Details: ").append(project.getDetails()).append("\n\n");
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Project List");
        builder.setMessage(projectDetails.toString());
        builder.setPositiveButton("OK", null);
        builder.show();
    }

}

