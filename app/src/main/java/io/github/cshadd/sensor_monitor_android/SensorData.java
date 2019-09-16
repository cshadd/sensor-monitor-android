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
    private String[] valuePrefix;
    private String[] valueSuffix;

    private SensorData() {
        this(null);
        return;
    }

    private SensorData(Sensor sensor) {
        this(sensor, false, null);
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

        this.setValueMax(new float[0]);
        this.setValueMin(new float[0]);
        this.setValuePrefix(new String[0]);
        this.setValueSuffix(new String[0]);
        this.setValue(new float[0]);

        this.setSensorNameOverride(sensorNameOverride);
        this.setListener(listener);
        return;
    }

    public boolean clear() {
        if (this.value != null && this.valueMax != null
                && this.valueMin != null) {
            for (int i = 0; i < this.value.length; i++) {
                this.value[i] = 0;
                this.valueMax[i] = 0;
                this.valueMin[i] = 0;
            }
        }
        return true;
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

    public String[] getValuePrefix() {
        return this.valuePrefix;
    }

    public String[] getValueSuffix() { return this.valueSuffix; }

    public String name() {
        final Sensor sensor = getSensor();
        if (sensor != null && getSensorNameOverride() == null) {
            return sensor.getName();
        }
        return "" + getSensorNameOverride();
    }

    public void setAvailable(boolean available) {
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
        if (this.value != null && this.valueMax != null
            && this.valueMin != null && this.valuePrefix != null
            && this.valueSuffix != null) {
            if (this.value.length != this.valueMax.length
                    || this.value.length != this.valueMin.length
                    || this.value.length != this.valuePrefix.length
                    || this.value.length != this.valueSuffix.length) {
                this.setValueMax(new float[this.value.length]);
                this.setValueMin(new float[this.value.length]);
                this.setValuePrefix(new String[this.value.length]);
                this.setValueSuffix(new String[this.value.length]);
            }
            for (int i = 0; i < this.value.length; i++) {
                if (value[i] < this.valueMin[i]) {
                    this.valueMin[i] = value[i];
                }
                if (value[i] > this.valueMax[i]) {
                    this.valueMax[i] = value[i];
                }
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

    public void setValuePrefix(String[] valuePrefix) {
        this.valuePrefix = valuePrefix;
        return;
    }

    public void setValueSuffix(String[] valueSuffix) {
        this.valueSuffix = valueSuffix;
        return;
    }
}
