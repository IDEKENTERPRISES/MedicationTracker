package model;

import org.json.JSONObject;

import java.time.LocalTime;
import java.util.ArrayList;

// Class that depicts a drug.
public class Drug {
    private String name;
    private String desc;
    private final ArrayList<String> ingredients;
    private final ArrayList<LocalTime> doseTimes;
    private double dosage;
    private double amountLeft;

    // EFFECTS: Creates a new Drug object with the given parameters. timeToDose is an array.
    public Drug(String name, String desc, ArrayList<LocalTime> timeToDose, double dosage, double initialAmount) {
        this.name = name;
        this.desc = desc;
        this.ingredients = new ArrayList<>();
        this.doseTimes = new ArrayList<>();
        this.doseTimes.addAll(timeToDose);
        this.dosage = dosage;
        amountLeft = initialAmount;
        EventLog.getInstance().logEvent(new Event(String.format("Reloaded drug: %s.", this.name)));
    }

    // EFFECTS: Creates a new Drug object with the given parameters. timeToDose is a single LocalTime object.
    public Drug(String name, String desc, LocalTime timeToDose, double dosage, double initialAmount) {
        this.name = name;
        this.desc = desc;
        this.ingredients = new ArrayList<>();
        this.doseTimes = new ArrayList<>();
        this.addDoseTime(timeToDose);
        this.dosage = dosage;
        amountLeft = initialAmount;
        EventLog.getInstance().logEvent(new Event("Reloaded drug: " + this.name + "."));
    }

    // EFFECTS: Creates a new Drug object with completely empty fields.
    public Drug() {
        this.name = "";
        this.desc = "";
        this.ingredients = new ArrayList<>();
        this.doseTimes = new ArrayList<>();
        this.dosage = 0;
        amountLeft = 0;
        EventLog.getInstance().logEvent(new Event("Created new empty drug."));
    }

    // MODIFIES: this
    // EFFECTS: Changes the name of the drug.
    public void changeName(String newName) {
        EventLog.getInstance().logEvent(new Event(String.format("Drug %s changed name to %s", this.name, newName)));
        this.name = newName;
    }

    // MODIFIES: this
    // EFFECTS: Changes the description of the drug.
    public void changeDesc(String newDesc) {
        EventLog.getInstance().logEvent(new Event(String.format("Drug %s changed description.", this.name)));
        this.desc = newDesc;
    }

    // MODIFIES: this
    // EFFECTS: Adds an ingredient to the drug.
    public void addIngredient(String ingr) {
        ingredients.add(ingr);
        EventLog.getInstance().logEvent(new Event(String.format("Drug %s added ingredient %s.", this.name, ingr)));
    }

    // MODIFIES: this
    // EFFECTS: Removes an ingredient from the drug.
    public void removeIngredient(String ingr) {
        ingredients.remove(ingr);
        EventLog.getInstance().logEvent(new Event(String.format("Drug %s removed ingredient %s.", this.name, ingr)));
    }

    // REQUIRES: newDosage > 0
    // MODIFIES: this
    // EFFECTS: Changes the dosage of the drug.
    public boolean changeDosage(double newDosage) {
        if (newDosage > 0) {
            dosage = newDosage;
            EventLog.getInstance().logEvent(
                    new Event(String.format("Drug %s changed dosage to %fml.", this.name, newDosage))
            );
            return true;
        }
        return false;
    }

    // REQUIRES: newTime to be part of doseTimes
    // MODIFIES: this
    // EFFECTS: Adds a dose time to the drug.
    public void addDoseTime(LocalTime newTime) {
        if (!doseTimes.contains(newTime)) {
            doseTimes.add(newTime);
            EventLog.getInstance().logEvent(
                    new Event(String.format("Drug %s dose time added. (%s)", this.name, newTime.toString()))
            );
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes a dose time from the drug.
    public void removeDoseTime(LocalTime oldTime) {
        doseTimes.remove(oldTime);
        EventLog.getInstance().logEvent(
                new Event(String.format("Drug %s dose time removed. (%s)", this.name, oldTime.toString()))
        );
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: Adds a given amount to the current amount of the drug.
    public boolean increaseAmountLeft(double amount) {
        if (amount > 0) {
            if (amountLeft > amount) {
                amountLeft += amount;
                EventLog.getInstance().logEvent(
                        new Event(String.format("Drug %s amount increased by %fml.", this.name, amount))
                );
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: Sets the amount left directly.
    public void setAmount(double amount) {
        amountLeft = amount;
        EventLog.getInstance().logEvent(
                new Event(String.format("Drug %s amount set to %fml.", this.name, amount))
        );
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: Removes a given amount of the current amount of the drug.
    public boolean decreaseAmountLeft(double amount) {
        if (amount > 0) {
            if (amountLeft > amount) {
                amountLeft -= amount;
                EventLog.getInstance().logEvent(
                        new Event(String.format("Drug %s amount decreased by %fml.", this.name, amount))
                );
                return false;
            } else {
                amountLeft = 0;
                EventLog.getInstance().logEvent(new Event(String.format("Drug %s amount set to 0).", this.name)));
                return true;
            }
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: Removes the normal dose of the drug from the current amount of the drug.
    public boolean decreaseAmountLeft() {
        if (amountLeft > dosage) {
            amountLeft -= dosage;
            return false;
        } else {
            amountLeft = 0;
            EventLog.getInstance().logEvent(new Event(String.format("Drug %s amount set to 0).", this.name)));
            return true;
        }
    }

    // EFFECTS: Returns the name of the drug.
    public String getName() {
        return name;
    }

    // EFFECTS: Returns the description of the drug.
    public String getDesc() {
        return desc;
    }

    // EFFECTS: Returns the ingredients of the drug.
    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    // EFFECTS: Returns the dose times of the drug.
    public ArrayList<LocalTime> getDoseTimes() {
        return doseTimes;
    }

    // EFFECTS: Returns the dosage of the drug.
    public double getDosage() {
        return dosage;
    }

    // EFFECTS: Returns the amount left of the drug.
    public double getAmountLeft() {
        return amountLeft;
    }

    // EFFECTS: Returns the JSON equivalent of the Drug.
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("desc", desc);
        json.put("ingredients", ingredients);
        json.put("doseTimes", doseTimes);
        json.put("dosage", dosage);
        json.put("amountLeft", amountLeft);

        EventLog.getInstance().logEvent(new Event(String.format("Drug %s converted to JSON object.", this.name)));
        return json;
    }
}
