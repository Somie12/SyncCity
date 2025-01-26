package harish.project.synccity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class TrainingWorkshop extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WorkshopAdapter adapter;
    private List<Workshop> workshopList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_workshop);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize workshop list
        workshopList = new ArrayList<>();
        workshopList.add(new Workshop("Urban Planning Basics", "Introduction to urban planning", "2024-09-01"));
        workshopList.add(new Workshop("Data Sharing Techniques", "Effective data sharing practices", "2024-09-15"));
        // Add more workshops...

        // Set adapter
        adapter = new WorkshopAdapter(workshopList);
        recyclerView.setAdapter(adapter);

        // Register button click
        Button btnRegister = findViewById(R.id.register_button);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle registration logic here
                Toast.makeText(TrainingWorkshop.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
