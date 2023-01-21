// Import necessary dependencies
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.util.Scanner;
import java.util.LinkedHashMap;

public class FindPartitionStats {
    
    private int cpuOpteronCount = 0, cpuEpycCount = 0, gpuK10Count = 0, gpuK40cCount = 0, gpuTitanCount = 0, gpuV100sCount = 0;
    
    public static void main(String[] args) {
        new FindPartitionStats().exec();
    } 
    
    public LinkedHashMap<String, Integer> getPartitionStats() {

        this.exec();

        LinkedHashMap<String, Integer> arr = new LinkedHashMap<>();
        
        arr.put("cpu-opteron", this.cpuOpteronCount);
        arr.put("cpu-epyc", this.cpuEpycCount);
        arr.put("gpu-k10", this.gpuK10Count);
        arr.put("gpu-k40c", this.gpuK40cCount);
        arr.put("gpu-titan", this.gpuTitanCount);
        arr.put("gpu-v100s", this.gpuV100sCount);

        return arr;
    }

    private void exec() {

        String line;

        try{
            
            Scanner inputStream = new Scanner(new FileInputStream("./data/extracted_log"));
            PrintWriter outputStream = new PrintWriter(new FileOutputStream("./stats/partition_stats.txt"));

            while (inputStream.hasNextLine()) {

                line = inputStream.nextLine();

                if (line.contains("sched") && line.contains("cpu-opteron")) { this.cpuOpteronCount++; }
                else if (line.contains("sched") && line.contains("cpu-epyc")) { this.cpuEpycCount++; }
                else if (line.contains("sched") && line.contains("gpu-k10")) { this.gpuK10Count++; }
                else if (line.contains("sched") && line.contains("gpu-k40c")) { this.gpuK40cCount++; }
                else if (line.contains("sched") && line.contains("gpu-titan")) { this.gpuTitanCount++; }
                else if (line.contains("sched") && line.contains("gpu-v100s")) { this.gpuV100sCount++; }
            }

            outputStream.println("cpu-opteron: " + this.cpuOpteronCount);
            outputStream.println("cpu-epyc: " + this.cpuEpycCount);
            outputStream.println("gpu-k10: " + this.gpuK10Count);
            outputStream.println("gpu-k40c: " + this.gpuK40cCount);
            outputStream.println("gpu-titan: " + this.gpuTitanCount);
            outputStream.println("gpu-v100s: " + this.gpuV100sCount);
            
            inputStream.close();
            outputStream.close();
        }
        catch (FileNotFoundException e) { System.out.println("File was not found"); }
    }
}