package Memory;

import OSCore.OSControl;
import Processes.Process;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Memory
{
    private int memorySize;  // 2 to the power of X
    private byte[] RAM;
    int pageSize;
    int totalPages;
    Integer allocatedPagesCount = 0;
    ArrayList<PageTableEntry> ptes;

    // Private static instance of the class (eager initialization)
    private static Memory instance;

    // Private constructor to prevent instantiation from outside
    private Memory() {}

    // Public method to provide access to the single instance (lazy initialization)
    public static Memory getInstance() {
        if (instance == null) {
            instance = new Memory();
        }
        return instance;
    }

    //sets initial size of memory and initializes PTEs
    public void initialize(int powerOf2)
    {
        this.memorySize = (int) Math.pow(2, powerOf2); //2 to the input Power
        RAM = new byte[memorySize];
        pageSize = OSControl.getInstance().getMEMORY_PAGE_SIZE();
        totalPages = memorySize / pageSize;
        ptes = new ArrayList<PageTableEntry>(totalPages);
        for (int pageNumber = 0; pageNumber < totalPages; pageNumber++)
        {
            PageTableEntry pte = new PageTableEntry();
            pte.setPageNumber(pageNumber);
            ptes.add(pte); // Add the new PTE to the list
        }
    }

    //find first 'pageCount' free pages. If success calculate and return
    //total address space available to process
    //Return: list of TBEs that describe the virtual/physical mapping
    public ArrayList<TranslationBufferEntry> allocPages(Integer pageCount, Integer processID)
    {
        //find pageCount free pages
        List<PageTableEntry> availablePages = ptes.stream()
                .filter(pte -> pte.isFree())
                .limit(pageCount)
                .collect(Collectors.toList());

        //tag them as being used (not free) and with processID
        for (PageTableEntry pte: availablePages) //claim PTEs with processID and 'free'
        {
            pte.setOwningProcess(processID);
            pte.setFree(false);
        }

        ArrayList<TranslationBufferEntry> tbes = new ArrayList<>(pageCount);
        for(int i=0; i<pageCount; i++)
        {
            TranslationBufferEntry tbe = new TranslationBufferEntry();
            tbe.setVirtualPageNumber(i);  //process local page number
            tbe.setPhysicalPageNumber(availablePages.get(i).getPageNumber()); //physical page number
            tbe.setPhysicalAddress(availablePages.get(i).getPageNumber() * OSControl.getInstance().getMEMORY_PAGE_SIZE());
            tbes.add(tbe);
        }
        return tbes;
    }

    public void deallocateMemory(Integer processID)
    {
        for (PageTableEntry pte : ptes)
        {
            if (pte.getOwningProcess() == processID)
            {
                pte.setFree(true);
                pte.setOwningProcess(-1);
            }
        }
    }

    public void writeToMemory(Integer pageNumber, Integer offset, String data)
    {
        byte[] dataBytes = data.getBytes(); //convert data to byte array
        //calculate physical address from physical page number and offset
        Integer startAddress = pageNumber * OSControl.getInstance().getMEMORY_PAGE_SIZE() + offset;
        System.arraycopy(dataBytes, 0, RAM, startAddress, dataBytes.length); //copy bytes
        //showMemoryPage(pageNumber);
    }

    //show a specific page of memory
    private void showMemoryPage(Integer pageNumber)
    {
        int startAddress = pageNumber * OSControl.getInstance().getMEMORY_PAGE_SIZE();
        for(int i = 0; i < OSControl.getInstance().getMEMORY_PAGE_SIZE(); i++)
        {
            String strByte = String.valueOf(RAM[startAddress+i]);
            if (strByte.equals("0"))
                strByte = "00";
            System.out.print(strByte + ".");
            if(i % 32 == 0)
                System.out.println();
        }
        System.out.println();
    }
}
