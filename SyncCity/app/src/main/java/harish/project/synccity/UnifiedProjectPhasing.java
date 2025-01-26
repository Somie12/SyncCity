package harish.project.synccity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class UnifiedProjectPhasing extends AppCompatActivity {

    private Interpreter tflite;
    private EditText inputProjectId, inputPhaseId, inputDuration, inputResources, inputComplexity;
    private TextView resultTextView;
    private Button predictButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unified_project_phasing);

        // Initialize UI components
        inputProjectId = findViewById(R.id.inputProjectId);
        inputPhaseId = findViewById(R.id.inputPhaseId);
        inputDuration = findViewById(R.id.inputDuration);
        inputResources = findViewById(R.id.inputResources);
        inputComplexity = findViewById(R.id.inputComplexity);
        resultTextView = findViewById(R.id.resultTextView);
        predictButton = findViewById(R.id.predictButton);

        // Load TFLite model
        try {
            tflite = new Interpreter(loadModelFile());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading TFLite model!", Toast.LENGTH_SHORT).show();
        }

        // Set button click listener for prediction
        predictButton.setOnClickListener(v -> {
            float[] inputData = new float[5];
            try {
                inputData[0] = Float.parseFloat(inputProjectId.getText().toString());
                inputData[1] = Float.parseFloat(inputPhaseId.getText().toString());
                inputData[2] = Float.parseFloat(inputDuration.getText().toString());
                inputData[3] = Float.parseFloat(inputResources.getText().toString());
                inputData[4] = Float.parseFloat(inputComplexity.getText().toString());

                float prediction = doInference(inputData);
                resultTextView.setText(String.format("Prediction: %s", prediction > 0.5 ? "Successful Phase" : "Unsuccessful Phase"));

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid input data!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("unified_project_phasing_model.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private float doInference(float[] input) {
        float[][] output = new float[1][1];
        tflite.run(input, output);
        return output[0][0];
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tflite != null) {
            tflite.close();
        }
    }
}
