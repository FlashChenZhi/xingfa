package com.wms.vo;

/**
 * Created by zhangbin on 16/9/26.
 */
public class ComboBoxVo {
    private String value;
    private String displayValue;

    public ComboBoxVo() {
    }

    public ComboBoxVo(String value, String displayValue) {
        this.value = value;
        this.displayValue = displayValue;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }
}
