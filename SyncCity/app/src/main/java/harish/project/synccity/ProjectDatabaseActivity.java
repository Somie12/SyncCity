package harish.project.synccity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProjectDatabaseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private harish.project.synccity.ProjectAdapter projectAdapter;
    private SearchView searchView;
    private List<harish.project.synccity.Project> projectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_database);

        searchView = findViewById(R.id.searchView);
        Button btnFilter = findViewById(R.id.btnFilter);
        Button btnSort = findViewById(R.id.btnSort);
        recyclerView = findViewById(R.id.recyclerViewProjects);

        // Initialize project list with new fields
        projectList = new ArrayList<>();
        projectList.add(new harish.project.synccity.Project("Project A", "New York, USA", "01/01/2024", "01/06/2024", "ongoing", "Details A"));
        projectList.add(new harish.project.synccity.Project("Project B", "Los Angeles, USA", "02/01/2024", "02/06/2024", "future", "Details B"));
        projectList.add(new harish.project.synccity.Project("Project C", "Chicago, USA", "03/01/2024", "03/06/2024", "finished", "Details C"));
        projectList.add(new harish.project.synccity.Project("Project D", "Houston, USA", "04/01/2024", "04/06/2024", "ongoing", "Details D"));

        // Initialize the adapter with the project list
        projectAdapter = new harish.project.synccity.ProjectAdapter(projectList);

        // Set the layout manager for the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(projectAdapter);

        // Search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                projectAdapter.filter(newText);
                return false;
            }
        });

        // Filter by status (Example: filter by "ongoing")
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectAdapter.filterByStatus("ongoing");
            }
        });

        // Sort by status
        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectAdapter.sortProjectsByStatus();
            }
        });
    }
}
