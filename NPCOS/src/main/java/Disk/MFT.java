package Disk;

import java.util.Hashtable;

public class MFT
{
    protected Hashtable<String, FileDirEntry> fileDirEntries;

    public MFT(int iNodeCount)
    {
        this.fileDirEntries = new Hashtable<String, FileDirEntry>(iNodeCount);
    }

    public void addEntry(FileDirEntry fde)
    {
        this.fileDirEntries.put(fde.getFileName(), fde);
    }

    public void removeEntry(String fileName)
    {
        this.fileDirEntries.remove(fileName);
    }
}
