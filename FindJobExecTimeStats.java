// Import necessary dependencies
import java.io.*;

import java.util.ArrayList;
import java.util.Collections;

public class FindJobExecTimeStats {

    public static void main(String[] args) {

        CommonOps timems = new CommonOps();
        ArrayList<Long> timeArr = new ArrayList<>();
        String line;
            
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader("./output/time_raw.txt"));
            PrintWriter fileOut = new PrintWriter(new FileWriter("./stats/exec_time_stats.txt"), true);             
            while ((line = fileIn.readLine()) != null) { timeArr.add(Long.parseLong(line)); }            
            Collections.sort(timeArr);
            fileOut.println("Execution Time Stats\n=============================================================");           
            fileOut.printf("Minimum    : %s", timems.formatTime(timeArr.get(0)));
            fileOut.printf("Quartile 1 : %s", timems.formatTime(
                (timeArr.size() % 4 != 0) ? 
                (timeArr.get(timeArr.size() / 4)) : 
                (timeArr.get(timeArr.size() / 4 - 1) + timeArr.get(timeArr.size() / 4)) / 2
            ));
            fileOut.printf("Median     : %s", timems.formatTime(
                (timeArr.size() % 2 != 0) ? 
                (timeArr.get(timeArr.size() / 2)) : 
                (timeArr.get(timeArr.size() / 2 - 1) + timeArr.get(timeArr.size() / 2)) / 2
            ));
            fileOut.printf("Quartile 3 : %s", timems.formatTime(
                (timeArr.size() * 3 % 4 != 0) ? 
                (timeArr.get(timeArr.size() * 3 / 4)) : 
                (timeArr.get(timeArr.size() * 3 / 4 - 1) + timeArr.get(timeArr.size() * 3 / 4)) / 2
            ));
            fileOut.printf("Maximum    : %s", timems.formatTime(timeArr.get(timeArr.size() - 1)));
            fileOut.println("=============================================================");
            fileIn.close();
            fileOut.close();
        }
        catch (FileNotFoundException e) { System.out.println("File was not found."); } 
        catch (IOException e) { System.out.println("Error read from file."); }
    }
}