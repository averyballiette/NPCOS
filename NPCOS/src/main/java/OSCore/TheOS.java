package OSCore;
import Disk.Driver;
import Disk.HardDrive;
import Processes.Process;
import Memory.Memory;

import java.util.Random;

public class TheOS
{
    public static byte[] generatePattern(int count) {
        byte[] result = new byte[count];
        for (int i = 0; i < count; i++) {
            result[i] = (byte) ('A' + (i % 26));
        }
        return result;
    }


    public static void main(String[] args)
    {
        CentralProcessor cpu = new CentralProcessor();
        Scheduler scheduler = Scheduler.getInstance();
        Memory memory = Memory.getInstance();
        memory.initialize(16);

        Process process01 = new Process("Process01", "Program01.txt");
        Process process02 = new Process("Process02", "Program02.txt");
        Process process03 = new Process("Process03", "Program01.txt");

        memory.deallocateMemory(process02.getProcessID());
        Process process04 = new Process("Process04", "Program02.txt");

        scheduler.addProcess(process01);
        scheduler.addProcess(process02);
        scheduler.addProcess(process03);
        scheduler.addProcess(process04);


        Driver driver = new Driver(8);
        Integer drive0 = driver.addDrive("Samsung","44444", "99999", 50);
        driver.format(drive0, "DriveA", 128);
        System.out.println(driver.getDriveInfo(drive0));

        Boolean status;

        status = driver.createDirectory(drive0, "subdir1", "\\");
        status = driver.createDirectory(drive0, "subdir2", "\\");

        status = driver.createFile(drive0, "TestFile1.txt", "subdir1");
        status = driver.createFile(drive0, "TestFile2.txt", "subdir2");
        status = driver.createFile(drive0, "TestFile3.txt", "subdir2");


        status = driver.deleteDirectory(drive0, "subdir2");
        if (status)
            System.out.println("Directory subdir2 deleted");
        else System.out.println("Directory is NOT Empty, not deleted");


        System.out.println(driver.listDriveFiles(drive0));
        System.out.println(driver.listFilesByName(drive0, "Test"));


        byte[] data2 = generatePattern(256);
        status = driver.writeFile(drive0, "TestFile3.txt", data2);

        byte[] fileData = driver.readFile(drive0, "TestFile3.txt");


        cpu.run();

    }
}
