package Processes;

import OSCore.InstructionSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Program
{
    ArrayList<String> code = new ArrayList<>();


    public Program()
    {
        //constructor
    }

    public Program(String fileName)
    {
        // Use try-with-resources to automatically close the file after reading
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            // Read each line and add it to the ArrayList
            while ((line = reader.readLine()) != null)
            {
                code.add(line);
            }
        }
        catch (IOException e)
        {
            // Handle file reading exceptions
            System.out.println("Error reading the file: " + e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    public ArrayList<String> getCode()
    {
        return code;
    }


}
