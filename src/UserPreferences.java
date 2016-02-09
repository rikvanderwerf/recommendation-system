import java.util.HashMap;
import java.util.List;

/**
 * Created by robin on 9-2-16.
 */
public class UserPreferences<K,V> extends HashMap<K,V> {
    protected List<Preference> defaultValue;
    public UserPreferences(List<Preference> defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void putList(K k, Preference v) {
        List<Preference> listValue = this.defaultValue;
        listValue.add(v);
        V value = (V) listValue;
        super.put(k, value);
    }
}