import java.util.HashMap;
import java.util.List;

/**
 * Created by robin on 9-2-16.
 */
public class UserPreferences<K,V> extends HashMap<K,V> {
    protected HashMap<Integer, Preference> defaultValue;
    public UserPreferences(HashMap<Integer, Preference> defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void putMap(K k, Preference v) {
        HashMap<Integer, Preference> listValue = (HashMap<Integer, Preference>) this.get(k);
        if (listValue == null){
            listValue = new HashMap<Integer, Preference>();
        }

        listValue.put(v.subject, v);
        V value = (V) listValue;
        super.put(k, value);
    }
}