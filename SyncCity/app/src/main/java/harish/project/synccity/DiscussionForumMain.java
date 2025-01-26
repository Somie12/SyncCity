package harish.project.synccity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class DiscussionForumMain extends AppCompatActivity {

    private Spinner forumTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_forum_main);

        forumTypeSpinner = findViewById(R.id.forum_type_spinner);
        //String forumType = getIntent().getStringExtra("FORUM_TYPE");
//        forumType.setOnClickListener(v -> {
//            Intent intent = new Intent(DiscussionForumMain.this, TaskScheduling.class);
//            startActivity(intent);
//        });

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, R.layout.spinner_item, getResources().getStringArray(R.array.forum_type_array)) {

            @Override
            public int getCount() {
                // Show hint "Select Forum Type"
                return super.getCount() - 1; // Exclude the hint item
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        forumTypeSpinner.setAdapter(adapter);
        forumTypeSpinner.setSelection(0); // Set hint as the default selection

        forumTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (position != 0) { // Avoid selecting the hint item
                    String selectedForum = parentView.getItemAtPosition(position).toString();
                    Toast.makeText(DiscussionForumMain.this, "Selected: " + selectedForum, Toast.LENGTH_SHORT).show();

                    // Start TaskScheduling activity
                    Intent intent = new Intent(DiscussionForumMain.this, ForumsPage.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing
            }
        });
    }
}