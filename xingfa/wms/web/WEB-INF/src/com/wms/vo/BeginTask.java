package com.wms.vo;

/**
 * Created by Administrator on 2016/10/26.
 */
public class BeginTask {
    private String wcsNo;
    private String systemState;
    private Long remainTaskNumber;

    public String getWcsNo() {
        return wcsNo;
    }

    public void setWcsNo(String wcsNo) {
        this.wcsNo = wcsNo;
    }

    public String getSystemState() {
        return systemState;
    }

    public void setSystemState(String systemState) {
        this.systemState = systemState;
    }

    public Long getRemainTaskNumber() {
        return remainTaskNumber;
    }

    public void setRemainTaskNumber(Long remainTaskNumber) {
        this.remainTaskNumber = remainTaskNumber;
    }
}
