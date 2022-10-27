package persistence;

import org.json.JSONObject;

// Code has been restructured from given material in JsonSerializationDemo to work with this project.
// In this case, the code is too simple to adapt.

public interface Writeable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
