package harish.project.synccity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
//import android.widget.SearchView;
import androidx.appcompat.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ResourceInventory extends AppCompatActivity {

    private ResourceDbHelper dbHelper;
    private RecyclerView recyclerView;
    private ResourceAdapter adapter;
    private List<Resource> resourceList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource_inventory);

        dbHelper = new ResourceDbHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ResourceAdapter(resourceList, this::onResourceClick);
        recyclerView.setAdapter(adapter);

        loadResources();

//        FloatingActionButton fab = findViewById(R.id.fabAddResource);
//
//        fab.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddResourceActivity.class)));

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterResources(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterResources(newText);
                return false;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabAddResource);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ResourceInventory.this, AddResourceActivity.class);
            startActivity(intent);
        });
    }

    private void loadResources() {
        Cursor cursor = dbHelper.getReadableDatabase().query(
                ResourceContract.ResourceEntry.TABLE_NAME,
                null, null, null, null, null, null);

        resourceList.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(ResourceContract.ResourceEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ResourceContract.ResourceEntry.COLUMN_RESOURCE_NAME));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(ResourceContract.ResourceEntry.COLUMN_RESOURCE_TYPE));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ResourceContract.ResourceEntry.COLUMN_RESOURCE_QUANTITY));

            resourceList.add(new Resource(id, name, type, quantity));
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    private void filterResources(String query) {
        List<Resource> filteredList = new ArrayList<>();
        for (Resource resource : resourceList) {
            if (resource.getName().toLowerCase().contains(query.toLowerCase()) ||
                    resource.getType().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(resource);
            }
        }
        adapter = new ResourceAdapter(filteredList, this::onResourceClick);
        recyclerView.setAdapter(adapter);
    }

    private void onResourceClick(Resource resource) {
        Intent intent = new Intent(ResourceInventory.this, EditResourceActivity.class);
        intent.putExtra("RESOURCE_ID", resource.getId());
        startActivity(intent);
    }
}
