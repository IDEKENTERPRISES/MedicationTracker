package model;

import java.time.LocalTime;
import java.util.ArrayList;

public class Drug {
    private String name;
    private String desc;
    private ArrayList<String> ingredients;
    private ArrayList<LocalTime> doseTimes;
    private double dosage;

    public Drug(String name, String desc, LocalTime timeToDose, double dosage) {
        this.name = name;
        this.desc = desc;
        this.ingredients = new ArrayList<>();
        this.doseTimes = new ArrayList<>();
        this.doseTimes.add(timeToDose);
        this.dosage = dosage;
    }

    public void changeName(String newName) {
        this.name = newName;
    }

    public void changeDesc(String newDesc) {
        this.desc = newDesc;
    }

    public void changeDosageFreq(LocalTime newTime) {
        doseTimes.add(newTime);
    }

    public void removeDosageFreq(LocalTime oldTime) {
        doseTimes.remove(oldTime);
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
}
