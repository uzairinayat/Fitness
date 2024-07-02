package com.example.fitnesstracker;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepCounterSensor, heartRateSensor;
    private TextView stepCountView, calorieCountView, kmCountView, heartRateView;
    private Button resetButton;

    private int initialStepCount = 0;
    private int initialHeartRate = 0;
    private boolean initialStepCountSet = false;
    private boolean initialHeartRateSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepCountView = findViewById(R.id.stepCountView);
        calorieCountView = findViewById(R.id.calorieCountView);
        kmCountView = findViewById(R.id.kmCountView);
        heartRateView = findViewById(R.id.heartRateView);
        resetButton = findViewById(R.id.btnReset);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);

        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            stepCountView.setText("Sensor Not Available");
        }

        if (heartRateSensor != null) {
            sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            heartRateView.setText("Sensor Not Available");
        }



        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetMetrics();
            }
        });


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (!initialStepCountSet) {
                initialStepCount = (int) event.values[0];
                initialStepCountSet = true;
            }
            int steps = (int) event.values[0] - initialStepCount;
            stepCountView.setText(String.valueOf(steps));
            calorieCountView.setText("Cal: " + calculateCalories(steps));
            kmCountView.setText("Km: " + calculateDistance(steps));
        } else if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            if (!initialHeartRateSet) {
                initialHeartRate = (int) event.values[0];
                initialHeartRateSet = true;
            }
            int heartRate = (int) event.values[0] - initialHeartRate;
            heartRateView.setText("HR: " + heartRate);
        }
    }

    private int calculateCalories(int steps) {
        // Simplified calorie calculation
        return steps / 20;
    }

    private float calculateDistance(int steps) {
        // Simplified distance calculation in kilometers
        return steps * 0.0008f;
    }

    private void resetMetrics() {
        initialStepCount = 0;
        initialHeartRate = 0;
        initialStepCountSet = false;
        initialHeartRateSet = false;
        stepCountView.setText("0");
        calorieCountView.setText("Cal: 0");
        kmCountView.setText("Km: 0");
        heartRateView.setText("HR: 0");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }


}
