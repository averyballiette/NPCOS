package OSCore;
import Processes.Process;
import Memory.Memory;

public class TheOS
{
    public static void main(String[] args)
    {
        CentralProcessor cpu = new CentralProcessor();
        Scheduler scheduler = Scheduler.getInstance();
        Memory memory = Memory.getInstance();
        memory.initialize(16);

        Process process01 = new Process("Process01", 5, "Program01.txt");
        Process process02 = new Process("Process02", 7, "Program02.txt");
        scheduler.addProcess(process01);
        scheduler.addProcess(process02);
        //for (int i = 0; i<2; i++)
            //scheduler.addProcess(new Process("TheProcess"));
            //scheduler.addProcess(new Process("TheProcess"+i, new Random().nextInt(10))); //random priority
            //scheduler.addProcess(new Process("TheProcess"+i, 5));  //fixed priority

        cpu.run();

    }
}
