package Memory;

import OSCore.OSControl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Memory {
    private int memorySize;  // 2 to the power of X
    private byte[] physicalMemory;
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

    //sets initial size of memory
    public void initialize(int powerOf2)
    {
        this.memorySize = (int) Math.pow(2, powerOf2); //2 to the input Power
        physicalMemory = new byte[memorySize];
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
    //Return: pageCount=8, pageSize=512, return=>4096
    public Integer allocatePages(int pageCount, int processID)
    {
        //find list of available pages, limit to pageCount
        List<PageTableEntry> availablePages = ptes.stream()
                .filter(pte -> pte.isFree())
                .limit(pageCount)
                .collect(Collectors.toList());

        //mark the pages as NOT free and with owner ID
        for (PageTableEntry pte: availablePages)
        {
            pte.setOwningProcess(processID);
            pte.setFree(false);
        }

        //if requested number of free pages were found, return total size
        if (availablePages.size() == pageCount) {
            allocatedPagesCount += pageCount;
            return (pageCount * pageSize);
        }
        else
            return 0;
    }

    //walk through all PTEs, set 'free' in all that have matching ProcessID as owner
    public void deallocatePages(int processID)
    {
        Integer deallocatedPageCount = 0;
        for(PageTableEntry pte:ptes)
        {
            if (pte.owningProcess == processID)
            {
                pte.setFree(true);
                pte.setOwningProcess(0);
                deallocatedPageCount += 1;
            }
        }
        allocatedPagesCount -= deallocatedPageCount;
    }

    //Write Byte array to memory
    public void writeToMemory(Integer processID,  byte[] data, Integer address)
    {
        //get list of pages owned by calling process
        List<PageTableEntry> processPages = ptes.stream()
                .filter(pte -> pte.getOwningProcess().equals(processID))
                .collect(Collectors.toList());

        Integer pageNumber = (Integer)address / pageSize;
        Integer pageOffset = (Integer)address % pageSize;
    }







    //Write String to memory
    public void writeToMemory(PageTableEntry pte, String dataString)
    {
        //use pte pagenumber to calculate offset into physical memory
        //write data
        //record last byte written in that page for future writes
        int physicalAddress = 0;
        int firstByteInPage = 0;
        int byteOffset = 0;
        byte[] data = dataString.getBytes();

        for(int i = 0; i<data.length; i++)
        {
            firstByteInPage = pte.pageNumber * pageSize;
            byteOffset = pte.lastByteOffset;
            physicalAddress = firstByteInPage + byteOffset + i;
            physicalMemory[physicalAddress] = data[i];
        }
        ptes.get(pte.pageNumber).setLastByteOffset((byteOffset + data.length));
    }
}
