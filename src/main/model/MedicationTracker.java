package model;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class MedicationTracker {

    private ArrayList<Drug> medication;

    // Adds a drug to the tracker, returns true if it contains colliding ingredients with another drug.
    public boolean addDrug(Drug drug) {
        medication.add(drug);
        for (String ingredient : drug.getIngredients()) {
            for (Drug existingDrug: medication) {
                if (existingDrug.getIngredients().contains(ingredient)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void removeDrug(int index) {
        medication.remove(index);
    }

    public Drug getNextDrug() {
        LocalTime currentTime = LocalTime.now();
        long nextDrugTime = -1;
        Drug nextDrug = null;
        for (Drug drug: medication) {
            for (LocalTime doseTime: drug.getDoseTimes()) {
                long drugTimeDiff = ChronoUnit.MINUTES.between(currentTime, doseTime);
                if (drugTimeDiff > 0 && (nextDrugTime > drugTimeDiff || nextDrugTime == -1)) {
                    nextDrugTime = drugTimeDiff;
                    nextDrug = drug;
                }
            }
        }
        return nextDrug;
    }

    public ArrayList<Drug> getMedication() {
        return medication;
    }

}
