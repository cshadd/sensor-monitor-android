package io.github.cshadd.sensor_monitor_android;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SensorDataAdapter
        extends RecyclerView.Adapter<SensorDataAdapter.ViewHolder> {
    private List<SensorData> sensorDataList;

    private SensorDataAdapter() {
        this(null);
        return;
    }

    public SensorDataAdapter(List<SensorData> sensorDataList) {
        super();
        this.sensorDataList = sensorDataList;
        return;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView sensorAvailable;
        private TextView sensorAvailableText;
        private TextView sensorName;
        private TextView sensorValue;
        private TextView sensorValueMax;
        private TextView sensorValueMin;

        private ViewHolder() {
            this(null);
            return;
        }

        public ViewHolder(View itemView) {
            super(itemView);
            this.sensorAvailable = itemView.findViewById(R.id.sensor_available);
            this.sensorAvailableText = itemView.findViewById(R.id.sensor_available_text);
            this.sensorName = itemView.findViewById(R.id.sensor_name);
            this.sensorValue = itemView.findViewById(R.id.sensor_value);
            this.sensorValueMax = itemView.findViewById(R.id.sensor_value_max);
            this.sensorValueMin = itemView.findViewById(R.id.sensor_value_min);
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public SensorDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        final LayoutInflater inflater = LayoutInflater.from(context);

        final View sensorView = inflater.inflate(R.layout.sensor_item, parent, false);

        final ViewHolder viewHolder = new ViewHolder(sensorView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(SensorDataAdapter.ViewHolder viewHolder, int position) {
        if (sensorDataList != null && viewHolder != null) {
            final Resources res = viewHolder.itemView.getContext().getResources();
            final SensorData sensor = sensorDataList.get(position);

            if (sensor.getAvailable()) {
                viewHolder.sensorAvailable.setBackgroundColor(res.getColor(R.color.sensor_available));
                viewHolder.sensorAvailableText.setText(res.getString(R.string.sensor_available));
            }
            else {
                viewHolder.sensorAvailable.setBackgroundColor(res.getColor(R.color.sensor_unavailable));
                viewHolder.sensorAvailableText.setText(res.getString(R.string.sensor_unavailable));
            }
            viewHolder.sensorName.setText(res.getString(R.string.sensor_name, sensor.name()));

            final float[] value = sensor.getValue();
            final float[] valueMax = sensor.getValueMax();
            final float[] valueMin = sensor.getValueMin();
            final String[] valuePrefix = sensor.getValuePrefix();
            final String[] valueSuffix = sensor.getValueSuffix();

            String sensorValueText = "{\n";
            String sensorValueMaxText = "{\n";
            String sensorValueMinText = "{\n";

            if (value != null && valueMax != null
                    && valueMin != null && valuePrefix != null
                    && valueSuffix != null) {
                for (int i = 0; i < value.length; i++) {
                    sensorValueText += valuePrefix[i] + " ~" + roundAvoid(value[i], 2)
                            + " " + valueSuffix[i] + ";\n";
                    sensorValueMaxText += valuePrefix[i] + " ~" + roundAvoid(valueMax[i], 2)
                            + " " + valueSuffix[i] + ";\n";
                    sensorValueMinText += valuePrefix[i] + " ~" + roundAvoid(valueMin[i], 2)
                            + " " + valueSuffix[i] + ";\n";
                }
            }

            sensorValueText += "}";
            sensorValueMaxText += "}";
            sensorValueMinText += "}";

            viewHolder.sensorValue.setText(res.getString(R.string.sensor_value, "\n" + sensorValueText));
            viewHolder.sensorValueMax.setText(res.getString(R.string.sensor_value_max, "\n" + sensorValueMaxText));
            viewHolder.sensorValueMin.setText(res.getString(R.string.sensor_value_min, "\n" + sensorValueMinText));
        }
        return;
    }

    @Override
    public int getItemCount() {
        if (sensorDataList != null) {
            return sensorDataList.size();
        }
        return -1;
    }

    private double roundAvoid(float value, int places) {
        final double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
