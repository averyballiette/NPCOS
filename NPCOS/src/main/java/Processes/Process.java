package Processes;

import OSCore.InstructionSet;
import OSCore.OSControl;
import Memory.Memory;
import Memory.TranslationBufferEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

enum State {None, New, Ready, Running, Waiting, Terminated};

public class Process
{
    Integer processID;
    String processName;
    Double burstTime;  //time remaining to complete execution
    State processState;
    Integer processPriority;
    Program program;
    ArrayList<TranslationBufferEntry> tb;
    Map<String, Variable> variables = new HashMap<String, Variable>();;

    Memory memory = Memory.getInstance();
    Integer maxProgramAddress;
    Integer nextAvailableAddress;

    Integer parentID;
    Integer userID;
    String programCounter;

    String EAX;
    String EBX;
    String ECX;
    String EDX;


    //Constructor: process name only
    public Process(String name)
    {
        processID = OSControl.getInstance().getNEXT_PROCESS_ID();
        //burstTime = (double) (Math.random() * 50) + 1;
        burstTime = calculateBurstTime();
        processState = State.Ready;
        processName = name;
        nextAvailableAddress = 0;
    }

    //Constructor: process name and priority
    public Process(String name, Integer priority)
    {
        processID = OSControl.getInstance().getNEXT_PROCESS_ID();
        burstTime = calculateBurstTime();
        processState = State.Ready;
        processName = name;
        processPriority = priority;
        nextAvailableAddress = 0;
    }

    //Constructor: process name, priority and program name
    public Process(String name, Integer priority, String fileName)
    {
        processID = OSControl.getInstance().getNEXT_PROCESS_ID();
        processState = State.Ready;
        processName = name;
        processPriority = priority;
        program = new Program(fileName);
        burstTime = calculateBurstTime();
        nextAvailableAddress = 0;
    }

    //Constructor: process name and program name
    public Process(String name, String fileName)
    {
        processID = OSControl.getInstance().getNEXT_PROCESS_ID();
        processState = State.Ready;
        processName = name;
        program = new Program(fileName);
        burstTime = calculateBurstTime();  //must happen after Program instantiation
        nextAvailableAddress = 100;


        //this is to simulate different processes acquiring
        //different amounts of memory, testing the memory allocation system
        if(processID == 1001 || processID == 1002 || processID == 1003)
        {
            tb = memory.allocPages(2, processID);
            maxProgramAddress = tb.size() * OSControl.getInstance().getMEMORY_PAGE_SIZE();
            allocateVariable("Var123", "XXXX");
        }
        else if (processID == 1004)
        {
            tb = memory.allocPages(4, processID);
            maxProgramAddress = tb.size() * OSControl.getInstance().getMEMORY_PAGE_SIZE();
            allocateVariable("Var4", "44445555");
        }

        maxProgramAddress = tb.size() * OSControl.getInstance().getMEMORY_PAGE_SIZE();

        allocateVariable("VarX", "XXXX");
        allocateVariable("VarY", "YYYYYY");

        String X = getVariable("VarX");
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


    private double calculateBurstTime()
    {
        double bTime = 0.0;
        InstructionSet instructionSet = InstructionSet.getInstance();

        for (String instruction : program.getCode())
        {
            bTime += instructionSet.getInstructionTime(instruction);
        }
        return bTime;
    }


    private Boolean writeToMemory(String data)
    {
        if ((maxProgramAddress - nextAvailableAddress) < data.length())
            return false;
        Integer processPageNumber = nextAvailableAddress/OSControl.getInstance().getMEMORY_PAGE_SIZE();
        Integer pageOffset = nextAvailableAddress % OSControl.getInstance().getMEMORY_PAGE_SIZE();
        Integer physicalPageNumber = tb.get(processPageNumber).getPhysicalPageNumber();
        memory.writeToMemory(physicalPageNumber, pageOffset, data);
        nextAvailableAddress += data.length();
        return true;
    }

    private Boolean allocateVariable(String name, String value)
    {
        if ((maxProgramAddress - nextAvailableAddress) < value.length())
            return false;

        if (variables.containsKey(name))
            return false;
        else {
            Variable variable = new Variable(value);
            variable.setVarAddress(nextAvailableAddress);
            variables.put(name, variable);
            writeToMemory(variables.get(name).getVarValue());
            return true;
        }
    }

    private String getVariable(String name)
    {
        if (variables.containsKey(name))
            return variables.get(name).getVarValue();
        else
            return "";
    }
}
