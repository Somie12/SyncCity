package harish.project.synccity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class EditResourceActivity extends AppCompatActivity {

    private EditText etName, etType, etQuantity;
    private ResourceDbHelper dbHelper;
    private int resourceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_resource);

        etName = findViewById(R.id.etResourceName);
        etType = findViewById(R.id.etResourceType);
        etQuantity = findViewById(R.id.etResourceQuantity);
        dbHelper = new ResourceDbHelper(this);

        Intent intent = getIntent();
        resourceId = intent.getIntExtra("RESOURCE_ID", -1);

        loadResourceDetails();

        MaterialButton btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(v -> updateResource());

        MaterialButton btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(v -> deleteResource());
    }

    private void loadResourceDetails() {
        Cursor cursor = dbHelper.getReadableDatabase().query(
                ResourceContract.ResourceEntry.TABLE_NAME,
                null,
                ResourceContract.ResourceEntry._ID + "=?",
                new String[]{String.valueOf(resourceId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ResourceContract.ResourceEntry.COLUMN_RESOURCE_NAME));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(ResourceContract.ResourceEntry.COLUMN_RESOURCE_TYPE));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(ResourceContract.ResourceEntry.COLUMN_RESOURCE_QUANTITY));

            etName.setText(name);
            etType.setText(type);
            etQuantity.setText(String.valueOf(quantity));
        }
        cursor.close();
    }

    private void updateResource() {
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

        int rowsAffected = dbHelper.getWritableDatabase().update(
                ResourceContract.ResourceEntry.TABLE_NAME,
                values,
                ResourceContract.ResourceEntry._ID + "=?",
                new String[]{String.valueOf(resourceId)});

        if (rowsAffected == 0) {
            Toast.makeText(this, "Error updating resource", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Resource updated", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void deleteResource() {
        int rowsDeleted = dbHelper.getWritableDatabase().delete(
                ResourceContract.ResourceEntry.TABLE_NAME,
                ResourceContract.ResourceEntry._ID + "=?",
                new String[]{String.valueOf(resourceId)});

        if (rowsDeleted == 0) {
            Toast.makeText(this, "Error deleting resource", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Resource deleted", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
