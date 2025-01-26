package harish.project.synccity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class AddResourceActivity extends AppCompatActivity {

    private EditText etName, etType, etQuantity;
    private ResourceDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resource);

        etName = findViewById(R.id.etResourceName);
        etType = findViewById(R.id.etResourceType);
        etQuantity = findViewById(R.id.etResourceQuantity);
        dbHelper = new ResourceDbHelper(this);

        MaterialButton btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> saveResource());
    }

    private void saveResource() {
        String name = etName.getText().toString().trim();
        String type = etType.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();

        if (name.isEmpty() || type.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(quantityStr);

        ContentValues values = new ContentValues();
        values.put(ResourceContract.ResourceEntry.COLUMN_RESOURCE_NAME, name);
        values.put(ResourceContract.ResourceEntry.COLUMN_RESOURCE_TYPE, type);
        values.put(ResourceContract.ResourceEntry.COLUMN_RESOURCE_QUANTITY, quantity);

        long newRowId = dbHelper.getWritableDatabase().insert(ResourceContract.ResourceEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving resource", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Resource saved", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
