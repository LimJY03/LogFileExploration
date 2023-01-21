// Import necessary dependencies
import java.util.Collections;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class CommonOps {
    
    public LinkedHashMap<String, Integer> sortDescending(LinkedHashMap<String, Integer> dict) {

        List<Map.Entry<String, Integer>> list = new LinkedList<>(dict.entrySet());
        Collections.sort(list, (map1, map2) -> map2.getValue().compareTo(map1.getValue()));
        
        LinkedHashMap<String, Integer> newDict = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> map: list) { newDict.put(map.getKey(), map.getValue()); }

        return newDict;
    }
}
