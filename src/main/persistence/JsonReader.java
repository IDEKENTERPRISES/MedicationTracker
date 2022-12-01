package persistence;
// Code has been restructured from given material in JsonSerializationDemo to work with this project.

import model.Drug;
import model.Event;
import model.EventLog;
import model.MedicationTracker;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.Stream;

// Class which reads the JSON equivalent of a MedicationTracker from a file given by the user.
public class JsonReader {

    private final String source;

    // EFFECTS: constructs reader to read from source file and sets source file path.
    public JsonReader(String source) {
        this.source = "./data/" + source + ".json";
    }

    // EFFECTS: constructs reader to read from source file and sets source file path.
    public JsonReader(File file) {
        this.source = file.getAbsolutePath();
    }

    // EFFECTS: reads MedicationTracker from file and returns it;
    // if an error occurs, IOException is thrown.
    public MedicationTracker read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseTracker(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses MedicationTracker from JSON object and returns it
    private MedicationTracker parseTracker(JSONObject jsonObject) {
        MedicationTracker tracker = new MedicationTracker();
        addDrugs(tracker, jsonObject);
        return tracker;
    }

    // MODIFIES: tracker
    // EFFECTS: loops through drugs in the tracker's medication list;
    // each drug is then constructed.
    private void addDrugs(MedicationTracker tracker, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("drugs");
        for (Object json : jsonArray) {
            JSONObject nextDrug = (JSONObject) json;
            drugConstruction(tracker, nextDrug);
        }
    }

    // MODIFIES: tracker
    // EFFECTS: gets all values from the JSON Object and adds it to a new Drug Object.
    // doseTimes and ingredients must be looped through as they are arraylists.
    private void drugConstruction(MedicationTracker tracker, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        String desc = jsonObject.getString("desc");
        int dosage = jsonObject.getInt("dosage");
        int amountLeft = jsonObject.getInt("amountLeft");

        ArrayList<LocalTime> doseTimes = new ArrayList<>();
        JSONArray doseTimesArray = jsonObject.getJSONArray("doseTimes");
        for (int i = 0; i < doseTimesArray.length(); i++) {
            LocalTime newTime = LocalTime.parse(doseTimesArray.getString(i));
            doseTimes.add(newTime);
        }

        Drug newDrug = new Drug(name, desc, doseTimes, dosage, amountLeft);

        JSONArray ingredientArray = jsonObject.getJSONArray("ingredients");
        for (int i = 0; i < ingredientArray.length(); i++) {
            newDrug.addIngredient(ingredientArray.getString(i));
        }
        tracker.addDrug(newDrug);
    }

}
