package Memory;

import java.util.Date;

public class PageTableEntry
{
    Integer pageNumber;
    Integer owningProcess;
    boolean free;
    boolean modified;
    Date lastModified;
    Integer lastByteOffset;

    public PageTableEntry()
    {
        this.free = true;
        this.modified = false;
        this.lastModified = null;
        this.owningProcess = -1;
        lastByteOffset = 0;
    }

    public boolean isFree()
    {
        return free;
    }

    public void setFree(boolean free)
    {
        this.free = free;
    }

    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public int getPageNumber()
    {
        return pageNumber;
    }

    public void setOwningProcess(int owningProcess)
    {
        this.owningProcess = owningProcess;
    }

    public void setLastByteOffset(Integer lastByteOffset)
    {
        this.lastByteOffset = lastByteOffset;
    }

    public Integer getLastByteOffset()
    {
        return lastByteOffset;
    }

    //mark page as 'dirty'
    public void setModified(boolean modified)
    {
        this.modified = modified;
    }

    public Integer getOwningProcess() {
        return owningProcess;
    }
}
