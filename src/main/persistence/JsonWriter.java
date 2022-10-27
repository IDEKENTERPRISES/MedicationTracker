package persistence;

import model.MedicationTracker;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class JsonWriter {
    private final String savePath;
    private PrintWriter printWriter;

    public JsonWriter(String savePath) {
        this.savePath = "./data/" + savePath + ".json";
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        printWriter = new PrintWriter(savePath);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
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
