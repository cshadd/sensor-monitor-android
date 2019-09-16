package io.github.cshadd.sensor_monitor_android;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends AppCompatActivity {
    private List<Sensor> currentSensors;
    private List<SensorData> sensorDatas;
    private SensorDataAdapter sensorDataAdapter;
    private SensorManager sensorManager;

    public MainActivity() {
        super();
        return;
    }

    private void assignSensor(int type, String overrideName) {
        if (sensorManager != null) {
        final List<Sensor> sensors = sensorManager.getSensorList(type);
            if (sensors.size() >= 1) {
                for (int i = 0; i < sensors.size(); i++) {
                    final Sensor sensor = sensors.get(i);
                    if (sensor != null && !this.currentSensors.contains(sensor)) {
                        this.currentSensors.add(sensor);
                        final SensorData data = new SensorData(sensor,true);
                        data.setListener(new SensorEventListener() {
                            @Override
                            public void onAccuracyChanged(Sensor sensor, int i) {
                                return;
                            }

                            @Override
                            public void onSensorChanged(SensorEvent arg0) {
                                data.setValue(arg0.values);
                                if (sensorDataAdapter != null) {
                                    sensorDataAdapter.notifyDataSetChanged();
                                }
                                return;
                            }
                        });
                        if (this.sensorDatas != null) {
                            this.sensorDatas.add(data);
                        }
                    }
                }
            }
            else if (overrideName != null) {
                final SensorData data = new SensorData(null, false, overrideName);
                this.sensorDatas.add(data);
            }
        }
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        this.sensorDatas = new ArrayList<SensorData>();
        this.currentSensors = new ArrayList<Sensor>();

        assignSensor(Sensor.TYPE_ACCELEROMETER, "ACCELEROMETER");
        assignSensor(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED, "ACCELEROMETER_UNCALIBRATED");
        assignSensor(Sensor.TYPE_AMBIENT_TEMPERATURE, "AMBIENT_TEMPERATURE");
        assignSensor(Sensor.TYPE_DEVICE_PRIVATE_BASE, "DEVICE_PRIVATE_BASE");
        assignSensor(Sensor.TYPE_GAME_ROTATION_VECTOR, "GAME_ROTATION_VECTOR");
        assignSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, "GEOMAGNETIC_ROTATION_VECTOR");
        assignSensor(Sensor.TYPE_GRAVITY, "GRAVITY");
        assignSensor(Sensor.TYPE_GYROSCOPE, "GYROSCOPE");
        assignSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED, "GYROSCOPE_UNCALIBRATED");
        assignSensor(Sensor.TYPE_HEART_BEAT, "HEART_BEAT");
        assignSensor(Sensor.TYPE_HEART_RATE, "HEART_RATE");
        assignSensor(Sensor.TYPE_LIGHT, "LIGHT");
        assignSensor(Sensor.TYPE_LINEAR_ACCELERATION, "LINEAR_ACCELERATION");
        assignSensor(Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT, "LOW_LATENCY_OFFBODY_DETECT");
        assignSensor(Sensor.TYPE_MAGNETIC_FIELD, "MAGNETIC_FIELD");
        assignSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED, "MAGNETIC_FIELD_UNCALIBRATED");
        assignSensor(Sensor.TYPE_MOTION_DETECT, "MOTION_DETECT");
        assignSensor(Sensor.TYPE_ORIENTATION, "ORIENTATION");
        assignSensor(Sensor.TYPE_POSE_6DOF, "POSE_6DOF");
        assignSensor(Sensor.TYPE_PRESSURE, "PRESSURE");
        assignSensor(Sensor.TYPE_PROXIMITY, "PROXIMITY");
        assignSensor(Sensor.TYPE_RELATIVE_HUMIDITY, "RELATIVE_HUMIDITY");
        assignSensor(Sensor.TYPE_ROTATION_VECTOR, "ROTATION_VECTOR");
        assignSensor(Sensor.TYPE_SIGNIFICANT_MOTION, "SIGNIFICANT_MOTION");
        assignSensor(Sensor.TYPE_STATIONARY_DETECT, "STATIONARY_DETECT");
        assignSensor(Sensor.TYPE_STEP_COUNTER, "STEP_COUNTER");
        assignSensor(Sensor.TYPE_STEP_DETECTOR, "STEP_DETECTOR");
        assignSensor(Sensor.TYPE_TEMPERATURE, "TEMPERATURE");
        assignSensor(Sensor.TYPE_ALL, "NO_SENSORS");

        this.sensorDataAdapter = new SensorDataAdapter(this.sensorDatas);

        final RecyclerView sensorRecycler = (RecyclerView)findViewById(R.id.sensor_recycler);
        sensorRecycler.setAdapter(this.sensorDataAdapter);
        sensorRecycler.setLayoutManager(new LinearLayoutManager(this));

        final Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        sensorRecycler.startAnimation(fadeInAnimation);

        Log.d("NOGA", "I love sensors!");

        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.sensorManager != null && this.sensorDatas != null) {
            for (int i = 0; i < this.sensorDatas.size(); i++) {
                final SensorData sensorData = this.sensorDatas.get(i);
                if (sensorData != null) {
                    sensorManager.registerListener(sensorData.getListener(), sensorData.getSensor(), 6);
                }
            }
        }
        return;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.sensorManager != null && this.sensorDatas != null) {
            for (int i = 0; i < this.sensorDatas.size(); i++) {
                final SensorData sensorData = this.sensorDatas.get(i);
                if (sensorData != null) {
                    this.sensorManager.unregisterListener(sensorData.getListener());
                }
            }
        }
        return;
    }
}