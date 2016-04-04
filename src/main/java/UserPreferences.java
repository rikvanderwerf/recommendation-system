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

    public void putMap(K k, Integer nestedKey, Preference v) {
        // find the key in the dictionary
        HashMap<Integer, Preference> preferenceMapping = (HashMap<Integer, Preference>) this.get(k);

        // if the key doesn't exist create a default HashMap
        if (preferenceMapping == null){
            preferenceMapping = new HashMap<Integer, Preference>();
        }

        //add the value to the HashMap
        preferenceMapping.put(nestedKey, v);
        V value = (V) preferenceMapping;
        super.put(k, value);
    }
}