package com.rft.domain;

/**
 * Created with IntelliJ IDEA.
 * User: Zhouyue
 * Date: 13-4-20
 * Time: 下午12:49
 * To change this template use File | Settings | File Templates.
 */
public class FileInfo_RFT {
    private byte[] binData;
    private long fileLength;

    public byte[] getBinData() {
        return binData;
    }

    public void setBinData(byte[] binData) {
        this.binData = binData;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }
}
