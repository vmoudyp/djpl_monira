package id.exorty.monira.ui.model;

import com.anychart.chart.common.dataentry.ValueDataEntry;

public class CustomDataEntry extends ValueDataEntry {

    public CustomDataEntry(String x, Number value, Number value2, Number value3) {
        super(x, value);
        setValue("value2", value2);
        setValue("value3", value3);
    }

    public CustomDataEntry(String x, Number value, Number value1) {
        super(x, value);
        setValue("value1", value1);
    }

    public CustomDataEntry(String x, Number value) {
        super(x, value);
    }
}
