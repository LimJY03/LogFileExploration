// Import necessary dependencies
import java.io.*;

import java.util.ArrayList;
import java.util.Collections;

public class FindJobExecTimeStats {

    public static void main(String[] args) {
        
        FindAvgExecTime extract = new FindAvgExecTime();
        ArrayList<Long> timeArr = extract.getTimeArr();
        Collections.sort(timeArr);
            
        try {
            PrintWriter fileOut = new PrintWriter(new FileWriter("./stats/exec_time_stats.txt")); 
            fileOut.println("Execution Time Stats\n=============================================================");           
            fileOut.printf("Minimum    : %s", extract.formatTime(timeArr.get(0)));
            fileOut.printf("Quartile 1 : %s", extract.formatTime(
                (timeArr.size() % 4 != 0) ? 
                (timeArr.get(timeArr.size() / 4)) : 
                (timeArr.get(timeArr.size() / 4 - 1) + timeArr.get(timeArr.size() / 4)) / 2
            ));
            fileOut.printf("Median     : %s", extract.formatTime(
                (timeArr.size() % 2 != 0) ? 
                (timeArr.get(timeArr.size() / 2)) : 
                (timeArr.get(timeArr.size() / 2 - 1) + timeArr.get(timeArr.size() / 2)) / 2
            ));
            fileOut.printf("Quartile 3 : %s", extract.formatTime(
                (timeArr.size() * 3 % 4 != 0) ? 
                (timeArr.get(timeArr.size() * 3 / 4)) : 
                (timeArr.get(timeArr.size() * 3 / 4 - 1) + timeArr.get(timeArr.size() * 3 / 4)) / 2
            ));
            fileOut.printf("Maximum    : %s", extract.formatTime(timeArr.get(timeArr.size() - 1)));
            fileOut.println("=============================================================");
            fileOut.close();
        }
        catch (FileNotFoundException e) { System.out.println("File was not found."); } 
        catch (IOException e) { System.out.println("Error read from file."); }
    }
}