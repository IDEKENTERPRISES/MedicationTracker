package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writeable;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class MedicationTracker implements Writeable {

    private final ArrayList<Drug> medicationList;

    public MedicationTracker() {
        medicationList = new ArrayList<>();
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("drugs", drugsToJson());
        return json;
    }

    private JSONArray drugsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Drug drug : medicationList) {
            jsonArray.put(drug.toJson());
        }

        return jsonArray;
    }

    // Adds a drug to the tracker, returns true if it contains colliding ingredients with another drug.
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

    public void removeDrug(int index) {
        if (index >= 0 && index < medicationList.size()) {
            medicationList.remove(index);
        }
    }

    public void removeDrug(Drug drug) {
        medicationList.remove(drug);
    }

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

    public ArrayList<Drug> getMedicationList() {
        return medicationList;
    }

}
