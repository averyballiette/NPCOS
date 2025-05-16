package Memory;

import java.util.Date;

public class PageTableEntry
{
    Integer pageNumber;
    Integer owningProcess;
    boolean free;
    boolean modified;
    Date lastModified;

    public PageTableEntry()
    {
        this.pageNumber = -1;
        this.owningProcess = -1;
        this.free = true;
        this.modified = false;
        this.lastModified = null;
        this.owningProcess = -1;
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


    //mark page as 'dirty'
    public void setModified(boolean modified)
    {
        this.modified = modified;
    }

    public Integer getOwningProcess() {
        return owningProcess;
    }
}
