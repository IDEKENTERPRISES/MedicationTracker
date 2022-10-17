package model;

import java.time.LocalTime;
import java.util.ArrayList;

public class Drug {
    private String name;
    private String desc;
    private final ArrayList<String> ingredients;
    private final ArrayList<LocalTime> doseTimes;
    private double dosage;
    private double amountLeft;

    public Drug(String name, String desc, LocalTime timeToDose, double dosage, double initialAmount) {
        this.name = name;
        this.desc = desc;
        this.ingredients = new ArrayList<>();
        this.doseTimes = new ArrayList<>();
        this.doseTimes.add(timeToDose);
        this.dosage = dosage;
        amountLeft = initialAmount;
    }

    public void changeName(String newName) {
        this.name = newName;
    }

    public void changeDesc(String newDesc) {
        this.desc = newDesc;
    }

    public void addIngredient(String ingr) {
        ingredients.add(ingr);
    }

    public void removeIngredient(String ingr) {
        ingredients.remove(ingr);
    }

    public boolean changeDosage(double newDosage) {
        if (newDosage > 0) {
            dosage = newDosage;
            return true;
        }
        return false;
    }

    public void addDosageFreq(LocalTime newTime) {
        if (!doseTimes.contains(newTime)) {
            doseTimes.add(newTime);
        }
    }

    public void removeDosageFreq(LocalTime oldTime) {
        doseTimes.remove(oldTime);
    }

    public boolean increaseAmountLeft(double amount) {
        if (amount > 0) {
            if (amountLeft > amount) {
                amountLeft += amount;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean decreaseAmountLeft(double amount) {
        if (amount > 0) {
            if (amountLeft > amount) {
                amountLeft -= amount;
                return false;
            } else {
                amountLeft = 0;
                return true;
            }
        }
        return false;
    }

    public boolean decreaseAmountLeft() {
        if (amountLeft > dosage) {
            amountLeft -= dosage;
            return false;
        } else {
            amountLeft = 0;
            return true;
        }
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public ArrayList<LocalTime> getDoseTimes() {
        return doseTimes;
    }

    public double getDosage() {
        return dosage;
    }

    public double getAmountLeft() {
        return amountLeft;
    }
}
