package persistence;

import model.Drug;
import model.MedicationTracker;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.Stream;

public class JsonReader {

    private final String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = "./data/" + source + ".json";
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
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

    // EFFECTS: parses workroom from JSON object and returns it
    private MedicationTracker parseTracker(JSONObject jsonObject) {
        MedicationTracker tracker = new MedicationTracker();
        addDrugs(tracker, jsonObject);
        return tracker;
    }

    // MODIFIES: tracker
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addDrugs(MedicationTracker tracker, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("drugs");
        for (Object json : jsonArray) {
            JSONObject nextDrug = (JSONObject) json;
            drugConstruction(tracker, nextDrug);
        }
    }

    // MODIFIES: tracker
    // EFFECTS: parses thingy from JSON object and adds it to workroom
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
