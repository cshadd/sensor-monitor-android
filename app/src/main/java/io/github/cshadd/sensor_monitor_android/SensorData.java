package io.github.cshadd.sensor_monitor_android;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;

public class SensorData
        extends Object {
    private boolean available;
    private SensorEventListener listener;
    private Sensor sensor;
    private String sensorNameOverride;
    private float[] value;
    private float[] valueMax;
    private float[] valueMin;

    private SensorData() {
        this(null);
        return;
    }

    private SensorData(Sensor sensor) {
        this(sensor, true, null);
        return;
    }

    public SensorData(Sensor sensor, boolean isAvailable) {
        this(sensor, isAvailable, null);
        return;
    }

    public SensorData(Sensor sensor, boolean isAvailable, String sensorNameOverride) {
        this(sensor, isAvailable, sensorNameOverride, null);
        return;
    }

    public SensorData(Sensor sensor, boolean isAvailable, String sensorNameOverride, SensorEventListener listener) {
        super();
        this.setSensor(sensor);
        this.setAvailable(isAvailable);
        if (sensor != null) {
            this.setValueMax(new float[20]);
            this.setValueMin(new float[20]);
            this.setValue(new float[20]);
        }
        else {
            this.setValueMax(new float[0]);
            this.setValueMin(new float[0]);
            this.setValue(new float[0]);
        }
        this.setSensorNameOverride(sensorNameOverride);
        this.setListener(listener);
        return;
    }

    public boolean getAvailable() {
        return this.available;
    }

    public SensorEventListener getListener() {
        return this.listener;
    }

    public Sensor getSensor() {
        return this.sensor;
    }

    public String getSensorNameOverride() {
        return this.sensorNameOverride;
    }

    public float[] getValue() {
        return this.value;
    }

    public float[] getValueMax() {
        return this.valueMax;
    }

    public float[] getValueMin() {
        return this.valueMin;
    }

    public String name() {
        final Sensor sensor = getSensor();
        if (sensor != null && getSensorNameOverride() == null) {
            return sensor.getName();
        }
        return "" + getSensorNameOverride();
    }

    private void setAvailable(boolean available) {
        this.available = available;
        return;
    }

    public void setListener(SensorEventListener listener) {
        this.listener = listener;
        return;
    }

    private void setSensor(Sensor sensor) {
        this.sensor = sensor;
        return;
    }

    private void setSensorNameOverride(String sensorNameOverride) {
        this.sensorNameOverride = sensorNameOverride;
        return;
    }

    public void setValue(float[] value) {
        this.value = value;
        for (int i = 0; i < value.length; i++) {
            if (value[i] < valueMin[i]) {
                valueMin[i] = value[i];
            }
            if (value[i] > valueMax[i]) {
                valueMax[i] = value[i];
            }
        }
        return;
    }

    private void setValueMax(float[] valueMax) {
        this.valueMax = valueMax;
        return;
    }

    private void setValueMin(float[] valueMin) {
        this.valueMin = valueMin;
        return;
    }
}
