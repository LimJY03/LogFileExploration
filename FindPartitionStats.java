// Import necessary dependencies
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.util.Scanner;

public class FindPartitionStats {
    
    public static void main(String[] args) { new FindPartitionStats().exec(); } 

    public void exec() {

        String line;

        try{
            
            Scanner inputStream = new Scanner(new FileInputStream("./data_filtered/start_job.txt"));
            Scanner inputNodeData = new Scanner(new FileInputStream("./output/time_raw.txt"));
            PrintWriter outputStream = new PrintWriter(new FileOutputStream("./stats/partition_stats.txt"), true);

            int cpuOpteronCount = 0, cpuEpycCount = 0, gpuK10Count = 0, gpuK40cCount = 0, gpuTitanCount = 0, gpuV100sCount = 0;
            long cpuOpteron_totalTime = 0, cpuEpyc_totalTime = 0, gpuK10_totalTime = 0, gpuK40c_totalTime = 0, gpuTitan_totalTime = 0, gpuV100s_totalTime = 0;

            while (inputStream.hasNextLine()) {

                line = inputStream.nextLine();

                if (line.contains("cpu-opteron")) { cpuOpteronCount++; }
                else if (line.contains("cpu-epyc")) { cpuEpycCount++; }
                else if (line.contains("gpu-k10")) { gpuK10Count++; }
                else if (line.contains("gpu-k40c")) { gpuK40cCount++; }
                else if (line.contains("gpu-titan")) { gpuTitanCount++; }
                else if (line.contains("gpu-v100s")) { gpuV100sCount++; }
            }

            while (inputNodeData.hasNextLine()) {

                line = inputNodeData.nextLine();

                if (line.contains("cpu-opteron")) { cpuOpteron_totalTime += Long.parseLong(line.split(" ")[1]); }
                else if (line.contains("cpu-epyc")) { cpuEpyc_totalTime += Long.parseLong(line.split(" ")[1]); }
                else if (line.contains("gpu-k10")) { gpuK10_totalTime += Long.parseLong(line.split(" ")[1]); }
                else if (line.contains("gpu-k40c")) { gpuK40c_totalTime += Long.parseLong(line.split(" ")[1]); }
                else if (line.contains("gpu-titan")) { gpuTitan_totalTime += Long.parseLong(line.split(" ")[1]); }
                else if (line.contains("gpu-v100s")) { gpuV100s_totalTime += Long.parseLong(line.split(" ")[1]); }
            }

            outputStream.printf("cpu-opteron: %-4s jobs executed, used %s ms on average.\n", cpuOpteronCount, cpuOpteron_totalTime / cpuOpteronCount);
            outputStream.printf("cpu-epyc:    %-4s jobs executed, used %s ms on average.\n", cpuEpycCount, cpuEpyc_totalTime / cpuEpycCount);
            outputStream.printf("gpu-k10:     %-4s jobs executed, used %s ms on average.\n", gpuK10Count, gpuK10_totalTime / gpuK10Count);
            outputStream.printf("gpu-k40c:    %-4s jobs executed, used %s ms on average.\n", gpuK40cCount, gpuK40c_totalTime / gpuK40cCount);
            outputStream.printf("gpu-titan:   %-4s jobs executed, used %s ms on average.\n", gpuTitanCount, gpuTitan_totalTime / gpuTitanCount);
            outputStream.printf("gpu-v100s:   %-4s jobs executed, used %s ms on average.\n", gpuV100sCount, gpuV100s_totalTime / gpuV100sCount);
            
            inputStream.close();
            inputNodeData.close();
            outputStream.close();
        }
        catch (FileNotFoundException e) { System.out.println("File was not found"); }
    }
}