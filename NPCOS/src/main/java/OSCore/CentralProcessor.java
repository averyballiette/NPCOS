package OSCore;
import Processes.Process;

public class CentralProcessor
{
    Scheduler scheduler;
    OSControl oscontrol;

    public CentralProcessor()
    {
        scheduler = Scheduler.getInstance();
        oscontrol = OSControl.getInstance();
    }

    void run()
    {
        while (!scheduler.isQueueEmpty())
        {
            Process process = scheduler.getNextProcess();

            if(oscontrol.SCHEDULE_TYPE == OSControl.Schedule_Type.ROUND_ROBIN ||
                    oscontrol.SCHEDULE_TYPE == OSControl.Schedule_Type.SHORTEST_FIRST ||
                    oscontrol.SCHEDULE_TYPE == OSControl.Schedule_Type.PRIORITY)
            {
                double burstTime = process.getBurstTime();
                Double quantum = oscontrol.QUANTUM;

                if(burstTime > quantum)
                {
                    burstTime -= quantum;               // simulates CPU work done on process
                    process.setBurstTime(burstTime);  //reset process burstTime
                    System.out.println(process.getProcessName() + " is executing. Remaining: " + process.getBurstTime());
                    scheduler.addProcess(process);      //put process back on scheduler
                }
                else
                {
                    burstTime = 0.0;
                    System.out.println("Process: " + process.getProcessName() + " is completed.");
                }
            }
            else if (oscontrol.SCHEDULE_TYPE == OSControl.Schedule_Type.FIRSTCOMEFIRSTSERVE)
            {
                process.setBurstTime(0.0);
                System.out.println("Process: " + process.getProcessName() + " is completed.");
            }
        }
        System.out.println("No more processes in queue");
    }
}
