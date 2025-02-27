package OSCore;

public class OSControl
{
    //Edit these CONSTANTS to configure the OS
    enum Schedule_Type {ROUND_ROBIN, PRIORITY, FIRSTCOMEFIRSTSERVE, SHORTEST_FIRST};
    Double QUANTUM = 10.0;
    Integer NEXT_PROCESS_ID = 1000;
    Schedule_Type SCHEDULE_TYPE = Schedule_Type.ROUND_ROBIN;
    Integer MEMORY_PAGE_SIZE = 512;

    //Singleton Methods
    private static OSControl instance;
    private OSControl()  //private Singleton constructor
    {
    }
    public static OSControl getInstance()
    {
        if (instance == null)
        {
            instance = new OSControl();
        }
        return instance;
    }

    //Getters/Setters

    public Double getQuantum()
    {
        return QUANTUM;
    }

    public Integer getNEXT_PROCESS_ID()
    {
        return NEXT_PROCESS_ID += 1;
    }

    public Integer getMEMORY_PAGE_SIZE(){ return MEMORY_PAGE_SIZE; }

}
