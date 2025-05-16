package Disk;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class FileDirEntry {

    protected ArrayList<Integer> fileClusters;
    private String fileName;
    private LocalDateTime creationDate;
    private LocalDateTime modifiedDate;
    private Boolean isDirectory;
    private String parentDir;
    protected Integer fileSize;

    public FileDirEntry(String fileName, LocalDateTime creationDate, LocalDateTime modifiedDate,
                        Boolean isDirectory, String parentDir)
    {
        this.fileClusters = new ArrayList<>();
        this.fileName = fileName;
        this.creationDate = creationDate;
        this.modifiedDate = modifiedDate;
        this.isDirectory = isDirectory;
        this.parentDir = parentDir;
        this.fileSize = 0;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<Integer> getFileClusters() {
        return fileClusters;
    }

    public void setFileClusters(ArrayList<Integer> fileClusters) {
        this.fileClusters = fileClusters;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }

    public String getParentDir()
    {
        if (parentDir != null)
            return parentDir;
        else
            return "";
    }

    public void setParentDir(String parentDir) {
        this.parentDir = parentDir;
    }
}
