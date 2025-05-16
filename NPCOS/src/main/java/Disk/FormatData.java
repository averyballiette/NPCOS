package Disk;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatData
{
    protected Cluster[] clusterMap;
    protected Integer clusterSize;
    protected String driveName;
    protected Integer driveSize;
    protected String formattedDate;

    public FormatData(String driveName, Integer numClusters, Integer clusterSize, Integer driveSize)
    {
        this.driveName = driveName;
        this.driveSize = driveSize;
        this.clusterSize = clusterSize;
        //init cluster map
        this.clusterMap = new Cluster[numClusters];
        for(int i = 0; i < numClusters; i++)
        {
            clusterMap[i] = new Cluster(i, true);
        }

        //identify format date and time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.formattedDate = now.format(formatter);
    }

    public String getDriveName() {
        return driveName;
    }

    public Integer getClusterCount(){
        return clusterMap.length;
    }

}
