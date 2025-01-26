package harish.project.synccity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectDatabaseHome extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProjectAdapter projectAdapter;
    private SearchView searchView;
    private List<Project> projectList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_database_home);

        // Initialize the SearchView
        searchView = findViewById(R.id.searchView);

        // Initialize the Filter and Sort buttons
        Button btnFilter = findViewById(R.id.btnFilter);
        Button btnSort = findViewById(R.id.btnSort);

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProjects);

        // Initialize your project list (This could be from a database or an API in a real app)
        projectList = new ArrayList<>();

        projectList.add(new Project("Project Civil Department ", "ITER Road, Jagmohan Nagar, Bhubaneshwar", "01/01/2024", "23/04/2024", "finished", "Widening the Road"));
        projectList.add(new Project("Project Electrical Department", "ITER Road, Jagmohan Nagar, Bhubaneshwar", "02/01/2025", "02/02/2025", "future", "Installation of Underground Electrical Transmission Lines"));
        projectList.add(new Project("Project Department of Water Resources", "ITER Road, Jagmohan Nagar, Bhubaneshwar", "03/07/2024", "13/09/2024", "ongoing", "Construction of Underground Sewerage System"));
        projectList.add(new Project("Project Civil Department", "ITER Road, Jagmohan Nagar, Bhubaneshwar", "15/09/2024", "04/11/2024", "future", "Reconstruction of the (Deconstructed) Road"));
        // Initialize the adapter with the project list
        projectAdapter = new ProjectAdapter(projectList);

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
                return true;
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
