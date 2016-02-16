import java.util.Comparator;
import java.util.Map;

public class MapValueComparator implements Comparator<Integer> {

    private Map<Integer, Double> map;

    public MapValueComparator(Map<Integer, Double> map) {
        this.map = map;
    }

    public int compare(Integer a, Integer b) {
        return map.get(a).compareTo(map.get(b));
    }
}