package Processes;

import OSCore.InstructionSet;
import OSCore.OSControl;

import java.util.ArrayList;

enum State {None, New, Ready, Running, Waiting, Terminated};

public class Process
{
    Integer processID;
    String processName;
    Program program;
    Integer parentID;
    Integer userID;
    State processState;
    String programCounter;
    double burstTime;  //time remaining to complete execution

    String EAX;
    String EBX;
    String ECX;
    String EDX;
    Integer processPriority;
    ArrayList<Thread> threads;

    public Process(String name)
    {
        processID = OSControl.getInstance().getNEXT_PROCESS_ID();
        burstTime = (double) (Math.random() * 50) + 1;
        processState = State.Ready;
        processName = name;
    }

    public Process(String name, Integer priority)
    {
        processID = OSControl.getInstance().getNEXT_PROCESS_ID();
        burstTime = (double) (Math.random() * 50) + 1;
        processState = State.Ready;
        processName = name;
        processPriority = priority;
    }

    public Process(String name, Integer priority, String fileName)
    {
        processID = OSControl.getInstance().getNEXT_PROCESS_ID();
        processState = State.Ready;
        processName = name;
        processPriority = priority;
        program = new Program(fileName);
        burstTime = calculateBurstTime();
        //ArrayList<PageTableEntry> localMemory;
    }

    public Process(String name, String fileName)
    {
        processID = OSControl.getInstance().getNEXT_PROCESS_ID();
        processState = State.Ready;
        processName = name;
        program = new Program(fileName);
        burstTime = calculateBurstTime();
        //defineMemorySpace();
    }

    public Integer getProcessPriority()
    {
        return processPriority;
    }

    public double getBurstTime()
    {
        return burstTime;
    }

    public void setBurstTime(double burstTime)
    {
        this.burstTime = burstTime;
    }

    public String getProcessName()
    {
        return processName;
    }

    public Integer getProcessID() {
        return processID;
    }

    private double calculateBurstTime(){
        double bTime = 0.0;
        InstructionSet instructionSet = InstructionSet.getInstance();
        //ArrayList<String> code = program.getCode();

        for (String instruction : program.getCode())
        {
            bTime += instructionSet.getInstructionTime(instruction);
        }
        return bTime;
    }

}
