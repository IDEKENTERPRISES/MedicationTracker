package persistence;
// Code has been restructured from given material in JsonSerializationDemo to work with this project.

import model.Event;
import model.EventLog;
import model.MedicationTracker;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// Class which writes the JSON equivalent of a MedicationTracker to a file given by the user.
public class JsonWriter {

    private final String savePath;
    private PrintWriter printWriter;

    // EFFECTS: Creates a new JsonWriter object and sets the save path.
    public JsonWriter(String savePath) {
        this.savePath = "./data/" + savePath + ".json";
    }

    // EFFECTS: Creates a new JsonWriter object and sets the save path from the file given.
    public JsonWriter(File saveFile) {
        this.savePath = saveFile.getAbsolutePath() + ".json";
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        printWriter = new PrintWriter(savePath);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of a MedicationTracker to file
    public void write(MedicationTracker tracker) {
        JSONObject json = tracker.toJson();
        printWriter.print(json.toString(4));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        printWriter.close();
    }
}
