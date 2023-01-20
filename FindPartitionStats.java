import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Scanner;

public class FindPartitionStats {

    public static void main(String[] args) {

        int cpuOpteronCount = 0, cpuEpycCount = 0, gpuK10Count = 0, gpuK40cCount = 0, gpuTitanCount = 0, gpuV100sCount = 0;
        String line;

        try{
            
            Scanner inputStream = new Scanner(new FileInputStream("./data/extracted_log"));
            PrintWriter outputStream = new PrintWriter(new FileOutputStream("./stats/partition_stats.txt"));

            while (inputStream.hasNextLine()) {

                line = inputStream.nextLine();

                if (line.contains("sched") && line.contains("cpu-opteron")) { cpuOpteronCount++; }
                else if (line.contains("sched") && line.contains("cpu-epyc")) { cpuEpycCount++; }
                else if (line.contains("sched") && line.contains("gpu-k10")) { gpuK10Count++; }
                else if (line.contains("sched") && line.contains("gpu-k40c")) { gpuK40cCount++; }
                else if (line.contains("sched") && line.contains("gpu-titan")) { gpuTitanCount++; }
                else if (line.contains("sched") && line.contains("gpu-v100s")) { gpuV100sCount++; }
            }

            inputStream.close();

            outputStream.println("cpu-opteron: " + cpuOpteronCount);
            outputStream.println("cpu-epyc: " + cpuEpycCount);
            outputStream.println("gpu-k10: " + gpuK10Count);
            outputStream.println("gpu-k40c: " + gpuK40cCount);
            outputStream.println("gpu-titan: " + gpuTitanCount);
            outputStream.println("gpu-v100s: " + gpuV100sCount);
            
            outputStream.close();
        }
        catch (FileNotFoundException e) { System.out.println("File was not found"); }
        catch (IOException e) { System.out.println("Problem with file output"); }
    } 
}