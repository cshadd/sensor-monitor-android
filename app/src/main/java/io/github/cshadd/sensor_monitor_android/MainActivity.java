package io.github.cshadd.sensor_monitor_android;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    private Animation fadeInAnimation;
    private List<SensorData> sensorDatas;
    private SensorDataAdapter sensorDataAdapter;
    private SensorManager sensorManager;
    private RecyclerView sensorRecycler;
    private Vibrator vibrator;

    public MainActivity() {
        super();
        return;
    }

    private void assignSensor(int type, String overrideName) {
        this.assignSensor(type, overrideName, null, null);
        return;
    }

    private void assignSensor(int type, String overrideName, final String[] valuePrefix, final String[] valueSuffix) {
        if (sensorManager != null) {
        final List<Sensor> sensors = sensorManager.getSensorList(type);
            if (sensors.size() >= 1) {
                final Object[] sensorObjects = sensors.toArray();
                for (int i = 0; i < sensorObjects.length; i++) {
                    final Sensor sensor = (Sensor)sensorObjects[i];
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
                                if (sensorDataAdapter != null) {
                                    if (valuePrefix != null && valueSuffix != null) {
                                        data.setValuePrefix(valuePrefix);
                                        data.setValueSuffix(valueSuffix);
                                    }
                                    data.setValue(arg0.values);

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

    private void fadeInSensors() {
        if (fadeInAnimation != null && sensorRecycler != null) {
            sensorRecycler.startAnimation(fadeInAnimation);
        }
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this.fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        this.currentSensors = new ArrayList<Sensor>();
        this.sensorDatas = new ArrayList<SensorData>();
        this.sensorDataAdapter = new SensorDataAdapter(this.sensorDatas);
        this.sensorManager = (SensorManager)this.getSystemService(SENSOR_SERVICE);
        this.sensorRecycler = this.findViewById(R.id.sensor_recycler);
        this.vibrator = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);

        this.assignSensor(Sensor.TYPE_ACCELEROMETER, "ACCELEROMETER",
                new String[]{"X", "Y", "Z"},
                new String[]{"m/s^2", "m/s^2", "m/s^2"});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.assignSensor(Sensor.TYPE_ACCELEROMETER_UNCALIBRATED, "ACCELEROMETER_UNCALIBRATED",
                    new String[]{"X", "Y", "Z", "X_B", "Y_B", "Z_B"},
                    new String[]{"m/s^2", "m/s^2", "m/s^2", "m/s^2", "m/s^2", "m/s^2"});
        }
        this.assignSensor(Sensor.TYPE_AMBIENT_TEMPERATURE, "AMBIENT_TEMPERATURE",
                new String[]{null},
                new String[]{(char)0x00B0 + "C"});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            this.assignSensor(Sensor.TYPE_GAME_ROTATION_VECTOR, "GAME_ROTATION_VECTOR",
                    new String[]{"X", "Y", "Z", null},
                    new String[]{null, null, null, null});
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.assignSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR, "GEOMAGNETIC_ROTATION_VECTOR",
                    new String[]{"X", "Y", "Z", null, "ACC"},
                    new String[]{null, null, null, null, "rads"});
        }
        this.assignSensor(Sensor.TYPE_GRAVITY, "GRAVITY",
                new String[]{"X", "Y", "Z"},
                new String[]{"m/s^2", "m/s^2", "m/s^2"});
        this.assignSensor(Sensor.TYPE_GYROSCOPE, "GYROSCOPE",
                new String[]{"X", "Y", "Z"},
                new String[]{"rad/s", "rad/s", "rad/s"});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            this.assignSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED, "GYROSCOPE_UNCALIBRATED",
                    new String[]{"X", "Y", "Z", "X_D", "Y_D", "Z_D"},
                    new String[]{"rad/s", "rad/s", "rad/s", "rad/s", "rad/s", "rad/s"});
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.assignSensor(Sensor.TYPE_HEART_BEAT, "HEART_BEAT");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            this.assignSensor(Sensor.TYPE_HEART_RATE, "HEART_RATE");
        }
        this.assignSensor(Sensor.TYPE_LIGHT, "LIGHT",
                new String[]{null},
                new String[]{"lx"});
        this.assignSensor(Sensor.TYPE_LINEAR_ACCELERATION, "LINEAR_ACCELERATION",
                new String[]{"X", "Y", "Z"},
                new String[]{"m/s^2", "m/s^2", "m/s^2"});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.assignSensor(Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT, "LOW_LATENCY_OFFBODY_DETECT");
        }
        this.assignSensor(Sensor.TYPE_MAGNETIC_FIELD, "MAGNETIC_FIELD",
                new String[]{"X", "Y", "Z"},
                new String[]{(char)0x00B5 + "T", (char)0x00B5 + "T", (char)0x00B5 + "T"});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            this.assignSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED, "MAGNETIC_FIELD_UNCALIBRATED",
                    new String[]{"X", "Y", "Z", "X_B", "Y_B", "Z_B"},
                    new String[]{(char)0x00B5 + "T", (char)0x00B5 + "T", (char)0x00B5 + "T",
                            (char)0x00B5 + "T", (char)0x00B5 + "T", (char)0x00B5 + "T"});
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.assignSensor(Sensor.TYPE_MOTION_DETECT, "MOTION_DETECT");
        }
        this.assignSensor(Sensor.TYPE_ORIENTATION, "ORIENTATION",
                new String[]{"AZIMUTH", "PITCH", "ROLL"},
                new String[]{(char)0x00B0 + "", (char)0x00B0 + "", (char)0x00B0 + ""});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.assignSensor(Sensor.TYPE_POSE_6DOF, "POSE_6DOF",
                    new String[]{"X", "Y", "Z", null, "X_T", "Y_T", "Z_T",
                        "D_Q_X", "D_Q_Y", "D_Q_Z", null, "D_T_X", "D_T_Y", "D_T_Z", "SEQ"},
                    new String[]{null, null, null, null, null, null, null, null, null, null,
                            null, null, null, null, null});
        }
        this.assignSensor(Sensor.TYPE_PRESSURE, "PRESSURE",
                new String[]{null},
                new String[]{"hPa"});
        this.assignSensor(Sensor.TYPE_PROXIMITY, "PROXIMITY",
                new String[]{null},
                new String[]{"cm"});
        this.assignSensor(Sensor.TYPE_RELATIVE_HUMIDITY, "RELATIVE_HUMIDITY",
                new String[]{null},
                new String[]{"%"});
        this.assignSensor(Sensor.TYPE_ROTATION_VECTOR, "ROTATION_VECTOR",
                new String[]{"X", "Y", "Z", null, "ACC"},
                new String[]{null, null, null, null, "rads"});
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            this.assignSensor(Sensor.TYPE_SIGNIFICANT_MOTION, "SIGNIFICANT_MOTION");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.assignSensor(Sensor.TYPE_STATIONARY_DETECT, "STATIONARY_DETECT");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.assignSensor(Sensor.TYPE_STEP_COUNTER, "STEP_COUNTER");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.assignSensor(Sensor.TYPE_STEP_DETECTOR, "STEP_DETECTOR");
        }
        this.assignSensor(Sensor.TYPE_TEMPERATURE, "TEMPERATURE",
                new String[]{null},
                new String[]{(char)0x00B0 + "C"});
        this.assignSensor(Sensor.TYPE_ALL, "NO_SENSORS");

        this.sensorRecycler.setAdapter(this.sensorDataAdapter);
        this.sensorRecycler.setLayoutManager(new LinearLayoutManager(this));
        this.sensorDataAdapter.notifyDataSetChanged();

        this.fadeInSensors();
        this.vibrate(500);

        Log.d("Noga", "I love sensors!");
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        final int id = item.getItemId();
        if (id == R.id.action_close) {
            vibrate(100);
            final Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else if (id == R.id.action_sensor_reset) {
            vibrate(500);
            if (this.sensorDatas != null) {
                final Object[] sensorDataObjects = this.sensorDatas.toArray();
                for (int i = 0; i < sensorDataObjects.length; i++) {
                    final SensorData data = (SensorData)sensorDataObjects[i];
                    if (data != null) {
                        data.clear();
                    }
                }
                fadeInSensors();
                Log.d("Noga", "Sensors have been reset!");
            }
        }
        else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.sensorManager != null && this.sensorDatas != null) {
            final Object[] sensorDataObjects = this.sensorDatas.toArray();
            for (int i = 0; i < sensorDataObjects.length; i++) {
                final SensorData data = (SensorData)sensorDataObjects[i];
                if (data != null) {
                    sensorManager.registerListener(data.getListener(), data.getSensor(), 6);
                }
            }
        }
        return;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.sensorManager != null && this.sensorDatas != null) {
            final Object[] sensorDataObjects = this.sensorDatas.toArray();
            for (int i = 0; i < sensorDataObjects.length; i++) {
                final SensorData data = (SensorData)sensorDataObjects[i];
                if (data != null) {
                    this.sensorManager.unregisterListener(data.getListener());
                }
            }
        }
        return;
    }

    private void vibrate(int milliseconds) {
        if (this.vibrator != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
            }
            else {
                this.vibrator.vibrate(milliseconds);
            }
        }
        return;
    }
}