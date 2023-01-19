// Import necessary dependencies
import java.io.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindAvgExecTime {
    
    public static void main(String[] args) {        
        avgExecutionTime();
    }

    // Average execution time of the jobs submitted to UMHPC.
    public static void avgExecutionTime() {
        
        try{

            BufferedReader fileIn = new BufferedReader(new FileReader("./data/extracted_log"));
            PrintWriter fileOut = new PrintWriter(new FileWriter("./output/average_exec_time.txt"));

            ArrayList<Integer> uniqueJobID = new ArrayList<>();
            ArrayList<String> startJobIdLines = new ArrayList<>();
            ArrayList<String> endJobIdLines = new ArrayList<>();
            
            Pattern jobIdPattern = Pattern.compile("JobId=(\\d+)");
            Pattern timePattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d)");  

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            
            int jobCount = 0;
            long totalDuration = 0;            
            String line;

            // Get unique IDs
            while ((line = fileIn.readLine()) != null) {
                
                if (line.contains("sched:") || line.contains("sched/backfill:")) {
                    
                    Matcher jobIdMatcher = jobIdPattern.matcher(line);

                    if (jobIdMatcher.find()) {

                        int id = Integer.parseInt(jobIdMatcher.group(1));

                        if (!uniqueJobID.contains(id)) {
                            uniqueJobID.add(id);
                            startJobIdLines.add(line);
                        }
                    }
                }

                if (line.contains("done")) {

                    Matcher jobIdMatcher = jobIdPattern.matcher(line);

                    if (jobIdMatcher.find()) {
                        endJobIdLines.add(line);
                    }
                }
            }
            
            // Calculate average execution time
            for (String startJobIdLine : startJobIdLines) {
                
                for (String endJobIdLine : endJobIdLines) {
                    
                    Matcher startJobIdMatcher = jobIdPattern.matcher(startJobIdLine);
                    Matcher endJobIdMatcher = jobIdPattern.matcher(endJobIdLine);

                    if (startJobIdMatcher.find() && endJobIdMatcher.find()) {

                        int startJobId = Integer.parseInt(startJobIdMatcher.group(1));
                        int endJobId = Integer.parseInt(endJobIdMatcher.group(1));

                        if (startJobId == endJobId) {

                            LocalDateTime startTime = null, endTime = null;
                            Matcher timeMatcher;

                            timeMatcher = timePattern.matcher(startJobIdLine);
                            if (timeMatcher.find()) { startTime = LocalDateTime.parse(timeMatcher.group(1), formatter); }

                            timeMatcher = timePattern.matcher(endJobIdLine);
                            if (timeMatcher.find()) { endTime = LocalDateTime.parse(timeMatcher.group(1), formatter); }
                            
                            totalDuration += java.time.Duration.between(startTime, endTime).toMillis();

                            jobCount++;                            
                            break;
                        }
                    }                 
                }
            }               
            
            if (jobCount > 0) {

                long totalMilliseconds = totalDuration / jobCount;
                long totalSeconds = totalMilliseconds / 1000;
                long totalMinutes = totalSeconds / 60;
                long totalHours = totalMinutes / 60;

                fileOut.printf(
                    "Average Execution Time:\n%s hours %s minutes %s seconds %s milliseconds\n", 
                    totalHours % 24, totalMinutes % 60, totalSeconds % 60, totalMilliseconds % 1000
                );
            }
            
            fileIn.close();
            fileOut.close();
        } 
        catch (FileNotFoundException e) { System.out.println("File was not found."); } 
        catch (IOException e) { System.out.println("Error read from file."); }    
    }
}
