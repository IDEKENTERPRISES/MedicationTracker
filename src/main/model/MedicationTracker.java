package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

// Class that holds a medication list.
public class MedicationTracker implements Writeable {

    private final ArrayList<Drug> medicationList;

    // EFFECTS: Initialises a new MedicationTracker object with a new list.
    public MedicationTracker() {
        medicationList = new ArrayList<>();
    }

    // EFFECTS: Converts the current tracker into JSON and returns it.
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("drugs", drugsToJson());
        return json;
    }

    // EFFECTS: Loops through the medication list and JSONifies each drug, returning the JSON.
    private JSONArray drugsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Drug drug : medicationList) {
            jsonArray.put(drug.toJson());
        }
        return jsonArray;
    }

    // MODIFIES: this
    // EFFECTS: Adds a drug to the tracker, returns true if it contains colliding ingredients with another drug.
    public boolean addDrug(Drug drug) {
        for (String ingredient : drug.getIngredients()) {
            for (Drug existingDrug: medicationList) {
                if (existingDrug.getIngredients().contains(ingredient)) {
                    return true;
                }
            }
        }
        medicationList.add(drug);
        return false;
    }

    // REQUIRES: index >=0 && index < medicationList.size()
    // MODIFIES: this
    // EFFECTS: Removes a drug of given index from the list.
    public boolean removeDrug(int index) {
        if (index >= 0 && index < medicationList.size()) {
            medicationList.remove(index);
            return true;
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: Removes a given drug from the list.
    public void removeDrug(Drug drug) {
        medicationList.remove(drug);
    }

    // EFFECTS: Loops through all drugs in the list and returns the next drug based on local time;
    //          Returns null if no drugs are upcoming TODAY.
    public Drug getNextDrug() {
        LocalTime currentTime = LocalTime.now();
        long nextDrugTime = -1;
        Drug nextDrug = null;
        for (Drug drug: medicationList) {
            for (LocalTime doseTime: drug.getDoseTimes()) {
                long drugTimeDiff = ChronoUnit.MINUTES.between(currentTime, doseTime);
                if (drugTimeDiff >= 0 && (nextDrugTime > drugTimeDiff || nextDrugTime == -1)) {
                    nextDrugTime = drugTimeDiff;
                    nextDrug = drug;
                }
            }
        }
        return nextDrug;
    }

    // EFFECTS: Returns the medication list.
    public ArrayList<Drug> getMedicationList() {
        return medicationList;
    }

}
