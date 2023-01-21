// Import necessary dependencies
import java.util.Collections;
import java.util.Map.Entry;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class CommonOps {
    
    public LinkedHashMap<String, Integer> sortDescending(LinkedHashMap<String, Integer> dict) {

        List<Entry<String, Integer>> list = new LinkedList<>(dict.entrySet());
        Collections.sort(list, (map1, map2) -> map2.getValue().compareTo(map1.getValue()));
        
        LinkedHashMap<String, Integer> newDict = new LinkedHashMap<>();
        for (Entry<String, Integer> map: list) { newDict.put(map.getKey(), map.getValue()); }

        return newDict;
    }

    public String formatTime(long timeMs) {
        long timeSeconds = timeMs / 1000;
        long timeMinutes = timeSeconds / 60;
        long timeHours = timeMinutes / 60;
        return String.format("%s hours %s minutes %s seconds %s milliseconds\n", timeHours, timeMinutes % 60, timeSeconds % 60, timeMs % 1000);
    }
}
