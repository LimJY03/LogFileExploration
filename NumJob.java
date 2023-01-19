
package fopasgmt;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumJob {
    
    public static void main(String[] args) {        
//        jobStartEnd();    
        avgExecutionTime();
    }
    
//    public static void printNumJob(int created, int ended){
//        System.out.println("Number of job created: " + created);
//        System.out.println("Number of job ended: " + ended);
//    }
//    
//    public static void jobStartEnd(){
//    //Number of jobs created/ended within a given time range. 
//        try{
//            BufferedReader read = new BufferedReader (new FileReader ("extracted_log"));
//            String line;
//            int created6=0,created7=0,created8=0,created9=0,created10=0,created11=0,created12=0;
//            int ended6=0,ended7=0,ended8=0,ended9=0,ended10=0,ended11=0,ended12=0;
//            
//            while((line = read.readLine())!=null){
//                String month = line.split("-")[1];
//                
//                switch(month){
//                    case "06":
//                        if(line.contains("sched:") || line.contains("sched/backfill:")){created6++;}
//                        if(line.contains("done")){ended6++;}
//                        break;
//                    case "07":
//                        if(line.contains("sched:") || line.contains("sched/backfill:")){created7++;}
//                        if(line.contains("done")){ended7++;}
//                        break;
//                    case "08":
//                        if(line.contains("sched:") || line.contains("sched/backfill:")){created8++;}
//                        if(line.contains("done")){ended8++;}
//                        break;
//                    case "09":
//                        if(line.contains("sched:") || line.contains("sched/backfill:")){created9++;}
//                        if(line.contains("done")){ended9++;}
//                        break;
//                    case "10":
//                        if(line.contains("sched:") || line.contains("sched/backfill:")){created10++;}
//                        if(line.contains("done")){ended10++;}
//                        break;
//                    case "11":
//                        if(line.contains("sched:") || line.contains("sched/backfill:")){created11++;}
//                        if(line.contains("done")){ended11++;}
//                        break; 
//                    case "12":
//                        if(line.contains("sched:") || line.contains("sched/backfill:")){created12++;}
//                        if(line.contains("done")){ended12++;}
//                        break;     
//                }                
//            }
//            System.out.println("June");
//            printNumJob(created6, ended6);
//            System.out.println("\nJuly");
//            printNumJob(created7, ended7);
//            System.out.println("\nAugust");
//            printNumJob(created8, ended8);
//            System.out.println("\nSeptember");
//            printNumJob(created9, ended9);
//            System.out.println("\nOctober");
//            printNumJob(created10, ended10);
//            System.out.println("\nNovember");
//            printNumJob(created11, ended11);
//            System.out.println("\nDecember");
//            printNumJob(created12, ended12);
//            
//            read.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("File was not found.");        
//        } catch (IOException e) {
//            System.out.println("Error read from file.");
//        }
//    }
//
    public static void avgExecutionTime(){
    //Average execution time of the jobs submitted to UMHPC.
        try{
            BufferedReader read = new BufferedReader (new FileReader ("extracted_log"));
            String line;
            ArrayList<Integer> startJobIds = new ArrayList<>();
            ArrayList<String> startJobIdLines = new ArrayList<>();
            ArrayList<String> endJobIdLines = new ArrayList<>();
            
            Pattern jobIdPattern = Pattern.compile("JobId=(\\d+)");
            Pattern timePattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d:\\d\\d:\\d\\d.\\d\\d\\d)");                  
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
            
            int jobCount=0;
            long totalDuration=0;
            
            while((line = read.readLine())!=null){
                if(line.contains("sched:") || line.contains("sched/backfill:")){
                    Matcher jobIdMatcher = jobIdPattern.matcher(line);
                    if(jobIdMatcher.find()){
                        int startJobId = Integer.parseInt(jobIdMatcher.group(1));
                        if(!startJobIds.contains(startJobId)){
                            startJobIds.add(startJobId);
                            String startJobIdLine = line;
                            startJobIdLines.add(startJobIdLine);   
                        }
                    }
                }
                if(line.contains("done")){
                    Matcher jobIdMatcher = jobIdPattern.matcher(line);
                    if(jobIdMatcher.find()){
                        String endJobIdLine = line;
                        endJobIdLines.add(endJobIdLine);                        
                    }
                }
            }
            
            for(String startJobIdLine : startJobIdLines){
                boolean flag = false;
                
                for(String endJobIdLine : endJobIdLines){
                    if(flag)
                        break;
                    
                    Matcher startJobIdMatcher = jobIdPattern.matcher(startJobIdLine);
                    Matcher endJobIdMatcher = jobIdPattern.matcher(endJobIdLine);
                    if(startJobIdMatcher.find() && endJobIdMatcher.find()){
                        int startJobId = Integer.parseInt(startJobIdMatcher.group(1));
                        int endJobId = Integer.parseInt(endJobIdMatcher.group(1));
                        if(startJobId == endJobId){
                    
//                            System.out.println(startJobId);
//                            System.out.println(endJobId);
//                            System.out.println(startJobIdLine);
//                            System.out.println(endJobIdLine);
                            
                            Matcher timeMatcher = timePattern.matcher(startJobIdLine);
                            LocalDateTime startTime = null;
                            if(timeMatcher.find()){
                                startTime = LocalDateTime.parse(timeMatcher.group(1), formatter);
                            }

                            timeMatcher = timePattern.matcher(endJobIdLine);
                            LocalDateTime endTime = null;
                            if(timeMatcher.find()){
                                endTime = LocalDateTime.parse(timeMatcher.group(1), formatter);
                            }
                            
                            long duration = java.time.Duration.between(startTime, endTime).toMillis();
                            totalDuration += duration;
                            jobCount++;
                                                                             
//                            System.out.println(startTime);
//                            System.out.println(endTime);
//                            System.out.println(duration);
//                            System.out.println(jobCount);
                            
                            flag = true;
                        }
                    }                 
                }
            }               
            
            if(jobCount>0){
                System.out.print("Average execution time: ");

                long totalMilliseconds = totalDuration/jobCount;

                long totalSeconds = totalMilliseconds / 1000;
                long seconds = totalSeconds % 60;

                long totalMinutes = totalSeconds / 60;
                long minutes = totalMinutes % 60;

                long totalHours = totalMinutes / 60;
                long hours = totalHours % 24;

                System.out.println(hours + " hours " + minutes + " minutes " + seconds + " seconds " + (totalMilliseconds % 1000) + " milliseconds");
            }
            
            read.close();
        } catch (FileNotFoundException e) {
            System.out.println("File was not found.");        
        } catch (IOException e) {
            System.out.println("Error read from file.");
        }    
    }
}
