package Memory;

public class TranslationBufferEntry {
    Integer VirtualPageNumber;
    Integer PhysicalPageNumber;
    Integer PhysicalAddress;
    Boolean Dirty;

    public TranslationBufferEntry()
    {
        VirtualPageNumber = 0;
        PhysicalPageNumber = 0;
        PhysicalAddress = PhysicalPageNumber * OSCore.OSControl.getInstance().getMEMORY_PAGE_SIZE();

    }

    public Integer getVirtualPageNumber() {
        return VirtualPageNumber;
    }

    public Integer getPhysicalPageNumber() {
        return PhysicalPageNumber;
    }

    public Integer getPhysicalAddress() {
        return PhysicalAddress;
    }

    public Boolean getDirty() {
        return Dirty;
    }

    public void setVirtualPageNumber(Integer virtualPageNumber) {
        VirtualPageNumber = virtualPageNumber;
    }

    public void setPhysicalPageNumber(Integer physicalPageNumber) {
        PhysicalPageNumber = physicalPageNumber;
    }

    public void setPhysicalAddress(Integer physicalAddress) {
        PhysicalAddress = physicalAddress;
    }

    public void setDirty(Boolean dirty) {
        Dirty = dirty;
    }
}
