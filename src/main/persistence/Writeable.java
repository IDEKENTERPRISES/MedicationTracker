package persistence;

import org.json.JSONObject;

public interface Writeable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
