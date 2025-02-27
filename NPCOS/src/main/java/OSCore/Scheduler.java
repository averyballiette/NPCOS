package OSCore;

import Processes.Process;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Scheduler {
    // Singleton instance
    private static Scheduler instance;
    OSControl oscontrol;
    ArrayList<Process> processQueue;

    //Singleton Methods
    public static Scheduler getInstance() {
        if (instance == null) {
            instance = new Scheduler();
        }
        return instance;
    }

    Scheduler()
    {
        oscontrol = OSControl.getInstance();
        processQueue = new ArrayList<>();
    }

    // Method to add a process to the queue
    public void addProcess(Process process)
    {
        processQueue.add(process);
    }

    // Method to get the next process depending on ScheduleType
    Process getNextProcess()
    {
        Process process = null;

        if(processQueue.isEmpty())
            return null;

        if(oscontrol.SCHEDULE_TYPE == OSControl.Schedule_Type.ROUND_ROBIN)
        {
            process = processQueue.get(0);
            processQueue.remove(0);
        }
        else if (oscontrol.SCHEDULE_TYPE == OSControl.Schedule_Type.PRIORITY)
        {
            //see bottom of this file for original method


            //using Collections.max
            process = Collections.max(processQueue,
                    Comparator.comparingInt((Process p)->p.getProcessPriority())
                            .thenComparingDouble(p-> -p.getBurstTime()));  //negate the bursTime to reverse it

            //using Streams
//            process = processQueue.stream()
//                    .max(Comparator
//                            .comparingInt((Process p) -> p.getProcessPriority())
//                            .thenComparingDouble(p -> -p.getBurstTime()))
//                            .orElse(null);
          }
        else if (oscontrol.SCHEDULE_TYPE == OSControl.Schedule_Type.FIRSTCOMEFIRSTSERVE)
        {
            process = processQueue.get(0); //return first process in queue
        }
        else if (oscontrol.SCHEDULE_TYPE == OSControl.Schedule_Type.SHORTEST_FIRST)
        {
            process = Collections.max(processQueue,
                    Comparator.comparingDouble(p-> -p.getBurstTime()));
        }

        processQueue.remove(process);
        return process;
    }

    public boolean isQueueEmpty()
    {
        return processQueue.isEmpty();
    }
}

//
////Original PRIORITY SCHEDULER
//            if (osControl.SCHEDULE_TYPE == OSControl.Schedule_Type.PRIORITY)
//        {
//        //sort the queue based on Priority of each process
//        //all processes with same/highest priority will be at top of queue
//        Collections.sort(processQueue, new Comparator<Process>()
//{
//    @Override
//    public int compare(Process p1, Process p2) {
//    // Compare the priorities in descending order
//    return Integer.compare(p2.getProcessPriority(), p1.getProcessPriority());
//}
//});
//
////create a new temp list only including processes with the highest priority
////then sort them based on remaining time
////return the one from the top of the list
//int highestPriorty = processQueue.get(0).getProcessPriority();
//
//List<Process> highPriority = processQueue.stream()
//        .filter(process -> process.getProcessPriority() == highestPriorty)
//        .collect(Collectors.toList());
//
//                Collections.sort(highPriority, new Comparator<Process>()
//{
//    @Override
//    public int compare(Process p1, Process p2) {
//    // Compare the remaining times in descending order
//    return Double.compare(p1.getBurstTime(), p2.getBurstTime());
//}
//});
//
//Process process = highPriority.get(0); //get first process on queue
//                processQueue.remove(0); //remove it from the queue
//                return process;
//            }
