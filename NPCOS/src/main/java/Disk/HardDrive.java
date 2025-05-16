package Disk;

public class HardDrive {

    protected String manufacturer;
    protected String model;
    protected String serialNumber;

    protected byte[] diskBytes;

    protected MFT mft;
    protected FormatData fmtData;

    public HardDrive(String manufacturer, String model, String serial, Integer driveSize)
    {
        this.manufacturer = manufacturer;
        this.model = model;
        this.serialNumber = serial;
        this.diskBytes = new byte[driveSize * 1048576];
    }
}












