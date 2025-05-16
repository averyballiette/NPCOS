package OSCore;

import java.util.HashMap;
import java.util.Map;

public class InstructionSet {
    // 1. Private static variable for the single instance
    private static InstructionSet instance;
    private Map<String, Integer> InstructionTable = new HashMap<>();

    // 2. Private constructor to prevent instantiation
    private InstructionSet() {
        // Constructor logic here
        InstructionTable.put("FETCH_FROM_CACHE", 1);
        InstructionTable.put("WRITE_TO_MEMORY", 2 );
        InstructionTable.put("READ_FROM_MEMORY", 2 );
        InstructionTable.put("ALLOCATE_MEMORY", 2 );
        InstructionTable.put("DEALLOCATE_MEMORY", 2 );
        InstructionTable.put("SEND_DATA_NETWORK", 3);
        InstructionTable.put("GET_DATA_NETWORK", 4);
        InstructionTable.put("WRITE_TO_DISK", 5);
        InstructionTable.put("READ_FROM_DISK", 6);
        InstructionTable.put("PROCESS_DATA", 5);
        InstructionTable.put("PROCESS_COMPLEX_DATA", 10);
    }

    // 3. Public static method to provide global access to the instance
    public static InstructionSet getInstance() {
        // 4. Lazy initialization of the instance
        if (instance == null) {
            instance = new InstructionSet();
        }
        return instance;
    }

    public Map<String, Integer> getInstructionTable()
    {
        return InstructionTable;
    }

    public Integer getInstructionTime(String instruction)
    {
         Integer time = InstructionTable.get(instruction);
         return (time != null) ? time : -99;
    }
}
