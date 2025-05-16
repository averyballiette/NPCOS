package Disk;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Driver {

    private HardDrive[] drives;
    Integer driveIndex = -1;

    public Driver(int driveCount)
    {
        this.drives = new HardDrive[driveCount];
    }

    public void format(Integer driveNumber, String driveName, Integer clusterSize)
    {
        //init Format Data
        Integer driveSize = drives[driveNumber].diskBytes.length;
        Integer numClusters = driveSize/clusterSize;

        drives[driveIndex].fmtData = new FormatData(driveName, numClusters, clusterSize, driveSize);

        //init and add mft to drive
        drives[driveNumber].mft = new MFT(500);
        //add root dir to drive
        FileDirEntry root = new FileDirEntry("\\", LocalDateTime.now(), LocalDateTime.now(), true, null);
        drives[driveNumber].mft.addEntry(root);
    }

    public Integer addDrive(String manufacturer, String model, String serial, Integer sizeInMB)
    {
        HardDrive drive = new HardDrive(manufacturer, model, serial, sizeInMB);
        driveIndex++;
        drives[driveIndex] = drive;
        return driveIndex;
    }


    public String getDriveInfo(Integer driveIndex)
    {
        String result = "";
        result += "Name: " + drives[driveIndex].fmtData.getDriveName() + ", ";
        result += "Size: " + drives[driveIndex].diskBytes.length + ", ";
        result += "Manufacturer: " + drives[driveIndex].manufacturer + ", ";
        result += "Model Num: " + drives[driveIndex].model + ", ";
        result += "Serial Num: " + drives[driveIndex].serialNumber + ", ";
        result += "Formatted Date: " + drives[driveIndex].fmtData.formattedDate + ", ";
        result += "Clusters: " + drives[driveIndex].fmtData.getClusterCount();
        return result;
    }

    public Boolean createFile(Integer driveNumber, String name, String parentFolder)
    {
        //if parentfolder exists, create the file
        if (drives[driveNumber].mft.fileDirEntries.containsKey(parentFolder))
        {
            drives[driveNumber].mft.addEntry(
                    new FileDirEntry(name, LocalDateTime.now(), LocalDateTime.now(), false, parentFolder));
            return true;
        }
        else
        {
            return false;
        }
    }

    public Boolean deleteFile(Integer driveNumber, String name)
    {
        //if file exists, delete it
        if (ifExists(driveNumber, name))
        {
            //set clusters to be available
            ArrayList<Integer> clusterList = drives[driveNumber].mft.fileDirEntries.get(name).getFileClusters();
            for(Integer clusterNum : clusterList)
                drives[driveNumber].fmtData.clusterMap[clusterNum].available = true;

            //remove FileDirEntry from mft
            drives[driveNumber].mft.removeEntry(name);
            return true;
        }
        else {
            return false;
        }
        //need code to mark used clusters as free
    }

    

    public Boolean createDirectory(Integer driveNumber, String name, String parentFolder)
    {
        //if parentfolder exists, create new folder
        if (drives[driveNumber].mft.fileDirEntries.containsKey(parentFolder))
        {
            drives[driveNumber].mft.addEntry(
                    new FileDirEntry(name, LocalDateTime.now(), LocalDateTime.now(), true, parentFolder));
            return true;
        }
        else
        {
            return false;
        }
    }

    public Boolean deleteDirectory(Integer driveNumber, String name)
    {
        //if the directory exists and it is empty
        if (ifExists(driveNumber, name) && isDirEmpty(driveNumber, name))
        {
            drives[driveNumber].mft.removeEntry(name);
            return true;
        }
        else
        {
            return false;
        }
    }

    
    
    public String listDriveFiles(Integer driveNumber)
    {
        //sort the Hashtable by copying to a TreeMap
        Map<String, FileDirEntry> sortedMap = new TreeMap<>(drives[driveNumber].mft.fileDirEntries);

        String result = "";
        for (Map.Entry<String, FileDirEntry> entry : sortedMap.entrySet())
        {
            if (entry.getValue().isDirectory())
                result += "Directory Name: " + entry.getValue().getFileName() + "\n";
            else {
                result += "File Name: " + entry.getValue().getFileName() + "\n";
                result += "\tFile Size: " + entry.getValue().fileSize + "\n";
            }
            result += "\tParent Directory: " + entry.getValue().getParentDir() + "\n";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yy HH:mm");
            String formattedDateTime = entry.getValue().getCreationDate().format(formatter);
            result += "\tCreation Date: " + formattedDateTime + "\n";
        }
        return result;
    }

    public String listFilesByName(Integer driveNumber, String name)
    {
        //sort the Hashtable by copying to a TreeMap
        Map<String, FileDirEntry> sortedMap = new TreeMap<>(drives[driveNumber].mft.fileDirEntries);
//        Map<String, FileDirEntry> sortedMap = new TreeMap<>(Collections.reverseOrder());
//        sortedMap.putAll(drives[driveNumber].mft.fileDirEntries);

        String result = "";
        for (Map.Entry<String, FileDirEntry> entry : sortedMap.entrySet())
        {
            if (entry.getKey().contains(name))
            {
                result += "File Name: " + entry.getValue().getFileName() + "\n";
                result += "\tParent Directory: " + entry.getValue().getParentDir() + "\n";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yy HH:mm");
                String formattedDateTime = entry.getValue().getCreationDate().format(formatter);
                result += "\tCreation Date: " + formattedDateTime + "\n";
            }
        }
        return result;
    }



    private Boolean ifExists(Integer driveNumber, String name)
    {
        if (drives[driveNumber].mft.fileDirEntries.containsKey(name))
            return true;
        else
            return false;
    }

    private Boolean isDirEmpty(Integer driveNumber, String dirName)
    {
        // check all other entries to see if this dir is a parent
        for (FileDirEntry fde : drives[driveNumber].mft.fileDirEntries.values())
        {
            if (fde.getParentDir().contains(dirName))
            {
                //there are other fde's that list directory as its parent
                return false;
            }
        }
        return true;
    }

    private ArrayList<Integer> findFreeCluster(Integer driveNumber, Integer numClusters)
    {
        ArrayList<Integer> clusters = new ArrayList<>(numClusters); //list to contain free clusters

        //iterate through cluster map, find first numClusters free clusters
        for (Cluster cluster : drives[driveNumber].fmtData.clusterMap)
            if (cluster.available)
            {
                clusters.add(cluster.number);
                cluster.available = false; //mark cluster as NOT free
                if (clusters.size() == numClusters)
                    return clusters;
            }
        return new ArrayList<Integer>();//return empty list if no free clusters found
    }

    public Boolean writeFile(Integer driveNumber, String fileName, byte[] fileData)
    {
        Integer clusterSize = drives[driveNumber].fmtData.clusterSize;
        Integer numClusters;

        if(fileData.length % clusterSize == 0)
        {
            numClusters = (fileData.length / clusterSize);
        }
        else
        {
            numClusters = (fileData.length / clusterSize) + 1; //how many clusters required to hold data
        }

        ArrayList<Integer> freeClusters = findFreeCluster(driveNumber, numClusters); //get free cluster list

        //for first numClusters -1, clusters that are full
        int i = 0;
        Integer clusterNum = 0;
        for(i=0; i < numClusters-1; i++)
        {
            clusterNum = freeClusters.get(i);
            //copy from data at cluster beginning to diskBytes at cluster beginning
            System.arraycopy(fileData, i * clusterSize, drives[driveNumber].diskBytes, clusterNum * clusterSize, clusterSize);
            //add clusterNum to file cluster list
            drives[driveNumber].mft.fileDirEntries.get(fileName).fileClusters.add(clusterNum);
        }

        //for last cluster, it is a partially full cluster
        Integer leftOverBytes = fileData.length % clusterSize;
        clusterNum = freeClusters.get(i);
        System.arraycopy(fileData, i * clusterSize, drives[driveNumber].diskBytes, clusterNum * clusterSize, leftOverBytes);
        drives[driveNumber].mft.fileDirEntries.get(fileName).fileClusters.add(clusterNum);
        drives[driveNumber].mft.fileDirEntries.get(fileName).fileSize = fileData.length;

        return true;
    }

    public byte[] readFile(Integer driveNumber, String fileName)
    {
        Integer fileSize = drives[driveNumber].mft.fileDirEntries.get(fileName).fileSize;
        byte[] fileBytes = new byte[fileSize];
        Integer clusterSize = drives[driveNumber].fmtData.clusterSize;

        ArrayList<Integer> clusterNumbers = drives[driveNumber].mft.fileDirEntries.get(fileName).fileClusters;

        int i = 0;
        if(fileSize % clusterSize == 0)
        {
            //Exact even number of clusters
            for (i = 0; i < clusterNumbers.size(); i++)
            {
                Integer clusterNum = clusterNumbers.get(i);
                System.arraycopy(drives[driveNumber].diskBytes,
                        clusterNum * clusterSize, fileBytes,
                        i * clusterSize, clusterSize);
            }
        }
        else
        {
            //Some full clusters and a partial one at the end
            for (i = 0; i < clusterNumbers.size() - 1; i++)
            {
                Integer clusterNum = clusterNumbers.get(i);
                System.arraycopy(drives[driveNumber].diskBytes,
                        clusterNum * clusterSize, fileBytes,
                        i * clusterSize, clusterSize);
            }

            Integer leftOverBytes = fileSize % clusterSize;
            Integer clusterNum = clusterNumbers.get(i);
            System.arraycopy(drives[driveNumber].diskBytes,
                    clusterNum * clusterSize, fileBytes,
                    i * clusterSize, leftOverBytes);
        }

        return fileBytes;
    }
}
