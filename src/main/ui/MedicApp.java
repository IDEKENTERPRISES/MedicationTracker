package ui;

import model.Drug;
import model.MedicationTracker;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Double.parseDouble;

public class MedicApp {

    private final Scanner scanner;
    private MedicationTracker tracker;
    private JsonWriter jsonWriter;

    private final ArrayList<String> mainOptions = new ArrayList<>(
            Arrays.asList("1 - List Medication List",
                    "2 - Next Drug reminder",
                    "3 - Add Drug",
                    "4 - Remove Drug",
                    "5 - View Specific Drug",
                    "6 - Quit")
    );

    private final ArrayList<String> drugOptions = new ArrayList<>(
            Arrays.asList("1 - List Ingredients",
                    "2 - Change Drug",
                    "3 - Remove Drug",
                    "4 - Add Dosage Time",
                    "5 - Increase/Decrease Amount",
                    "6 - Add/Remove Ingredient",
                    "7 - Back")
    );

    private final ArrayList<String> amountOptions = new ArrayList<>(
            Arrays.asList("1 - Increase Amount",
                    "2 - Decrease Amount",
                    "3 - Cancel")
    );

    public MedicApp() {
        scanner = new Scanner(System.in);

        trackingChoice();
        jsonWriter = new JsonWriter("./data/test.json");
        mainMenu();
    }

    // MODIFIES: this
    // EFFECTS: Asks the user if they would like to load from a save. Else, a new tracker is made.
    private void trackingChoice() {
        System.out.print("Would you like to load from a save? (Y/N): ");
        switch (scanner.nextLine().toLowerCase()) {
            case "y":
                loadJsonData();
                break;
            case "n":
                tracker = new MedicationTracker();
                break;
            default:
                trackingChoice();
        }
    }

    // EFFECTS: Saves the current tracker to a JSON file. If an exception occurs it is caught.
    private void saveJsonData() {
        System.out.print("Name of save: ");
        try {
            String saveName = scanner.nextLine();
            jsonWriter = new JsonWriter(saveName);
            jsonWriter.open();
            jsonWriter.write(tracker);
            jsonWriter.close();
        } catch (Exception e) {
            System.out.println("An error occurred, exiting.");
        }
    }

    // MODIFIES: this
    // EFFECTS: Loads a tracker from a JSON file. If an IOException occurs, a new tracker is made.
    private void loadJsonData() {
        System.out.print("Name of save: ");
        try {
            String saveName = scanner.nextLine();
            JsonReader jsonReader = new JsonReader(saveName);
            tracker = jsonReader.read();
        } catch (IOException e) {
            System.out.println("An IO error occurred, starting new save.");
            tracker = new MedicationTracker();
        }
    }

    // EFFECTS: Prints this cool banner haha.
    private void printBanner() {
        ArrayList<String> banner = new ArrayList<>();
        banner.add("                   _  _        _____                     _                ");
        banner.add("  /\\/\\    ___   __| |(_)  ___ /__   \\ _ __   __ _   ___ | | __  ___  _ __ ");
        banner.add(" /    \\  / _ \\ / _` || | / __|  / /\\/| '__| / _` | / __|| |/ / / _ \\| '__|");
        banner.add("/ /\\/\\ \\|  __/| (_| || || (__  / /   | |   | (_| || (__ |   < |  __/| |   ");
        banner.add("\\/    \\/ \\___| \\__,_||_| \\___| \\/    |_|    \\__,_| \\___||_|\\_\\ \\___||_|   ");
        for (String row: banner) {
            System.out.println(row);
        }
        System.out.println("Welcome to the Medication Tracker!");
    }

    // EFFECTS: Prints out each string in an array.
    private void printStringArray(ArrayList<String> options) {
        for (String option: options) {
            System.out.println(option);
        }
    }

    // EFFECTS: Prints out each LocalTime in an array.
    private void printTimeArray(ArrayList<LocalTime> times) {
        for (LocalTime time: times) {
            System.out.println(time);
        }
    }

    // EFFECTS: Print banner and options. Takes a user's choice of action.
    //          If mainSwitches returns true, the program will quit the main loop.
    private void mainMenu() {
        printBanner();
        printStringArray(mainOptions);
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            choice = -1;
        }

        if (!mainSwitches(choice)) {
            mainMenu();
        }
        quitting();
    }

    // EFFECTS: Asks the user if they would like to save their tracker using JSON formatting.
    //          If not, program quits. If input is incorrect, recurse.
    private void quitting() {
        System.out.print("Save before quitting? (Y/N): ");
        switch (scanner.nextLine().toLowerCase()) {
            case "y":
                saveJsonData();
                break;
            case "n":
                break;
            default:
                quitting();
        }
    }

    // EFFECTS: Takes the user's choice and executes the action.
    //          If choice = 6, program will start to quit.
    private boolean mainSwitches(int choice) {
        switch (choice) {
            case 1:
                listMedication();
                break;
            case 2:
                nextMedication();
                break;
            case 3:
                addMedication();
                break;
            case 4:
                removeMedication();
                break;
            case 5:
                viewMedication();
                break;
            case 6:
                return true;
            default:
                System.out.println("Invalid option chosen!");
        }
        return false;
    }

    // EFFECTS: List all drugs inside the medication list. If no drugs have been added, say so.
    //          If the drug has less amount that dosage then alert the user.
    private void listMedication() {
        int indexPointer = 1;
        for (Drug drug : tracker.getMedicationList()) {

            String runOut = (drug.getAmountLeft() < drug.getDosage()) ? " - You have run out of this!" : "";

            System.out.println(indexPointer + " - " + drug.getName() + runOut);
            indexPointer++;
        }
        if (indexPointer == 1) {
            System.out.println("No drugs have been added!");
        }
    }

    // EFFECTS: Gets the next Drug schedules for today. If null, none are scheduled.
    private void nextMedication() {
        Drug nextDrug = tracker.getNextDrug();

        if (nextDrug != null) {
            drugMenu(nextDrug);
        } else {
            System.out.println("No upcoming scheduled drugs for the rest of today!");
        }


    }

    // MODIFIES: this
    // EFFECTS: Creates a new drug based on the user's input.
    //          Upon creation, user is taken to the menu for the new drug.
    private void addMedication() {
        System.out.print("Name of drug: ");
        String drugName = scanner.nextLine();
        System.out.print("Description of drug: ");
        String drugDesc = scanner.nextLine();
        System.out.print("Dose time of drug (HH:mm): ");
        LocalTime drugTime;
        try {
            drugTime = LocalTime.parse(scanner.nextLine());
        } catch (Exception e) {
            drugTime = LocalTime.now();
            System.out.println("Incorrect time format, set dosage time to now.");
        }

        System.out.print("Dosage of drug (ml): ");
        double drugDose = doubleCheckLiquids();
        System.out.print("Current quantity of drug (ml): ");
        double drugQuan = doubleCheckLiquids();
        Drug newDrug = new Drug(drugName, drugDesc, drugTime, drugDose, drugQuan);
        tracker.addDrug(newDrug);
        drugMenu(newDrug);
    }

    // EFFECTS: Checks to see if the input is a valid double, if not then return 0.
    private double doubleCheckLiquids() {
        double returnResponse;
        try {
            returnResponse = parseDouble(scanner.nextLine());
        } catch (Exception e) {
            System.out.print("Amount set to 0ml.");
            returnResponse = 0;
        }
        return returnResponse;
    }

    // MODIFIES: this
    // EFFECTS: Remove a drug based on a given index.
    private void removeMedication() {
        System.out.print("Index of drug to delete: ");
        int drugInd;
        try {
            drugInd = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            drugInd = -1;
        }

        if (tracker.removeDrug(drugInd - 1)) {
            System.out.print("Drug successfully removed.");
        } else {
            System.out.print("Incorrect index provided.");
        }

    }

    // MODIFIES: this
    // EFFECTS: Removed a given drug.
    private void removeMedication(Drug drug) {
        tracker.removeDrug(drug);
    }

    // REQUIRES: drugInd > 0 && drugInd <= tracker.getMedicationList().size()
    // EFFECTS: Asks the user which drug they would like to inspect.
    private void viewMedication() {
        System.out.print("Index of drug to inspect: ");
        int drugInd;
        try {
            drugInd = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            drugInd = -1;
        }

        if (drugInd > 0 && drugInd <= tracker.getMedicationList().size()) {
            drugMenu(tracker.getMedicationList().get(drugInd - 1));
        } else {
            System.out.print("Incorrect index provided.");
        }

    }

    // EFFECTS: Opens the menu for the given drug.
    private void drugMenu(Drug drug) {
        printDrug(drug);
        printStringArray(drugOptions);

        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            choice = -1;
        }

        if (!drugSwitches(choice, drug)) {
            drugMenu(drug);
        }
    }

    // EFFECTS: Takes the user's choice and executes the action.
    //          If choice = 7, program will go back to the main menu.
    private boolean drugSwitches(int choice, Drug drug) {
        switch (choice) {
            case 1:
                listIngredients(drug);
                break;
            case 2:
                setDrugFields(drug);
                break;
            case 3:
                removeMedication(drug);
                return true;
            case 4:
                dosageToggles(drug);
                break;
            case 5:
                changeAmount(drug);
                break;
            case 6:
                toggleIngredients(drug);
                break;
            case 7:
                return true;
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: Starts a new menu which allows the user to input an ingredient name and
    //          toggle whether that ingredient is in the drug or not.
    //          If another drug contains the same ingredient then a warning is displayed. Loops.
    private void toggleIngredients(Drug drug) {
        while (true) {
            printStringArray(drug.getIngredients());
            System.out.print("Insert ingredient to change [LEAVE EMPTY TO GO BACK]:");
            String ingredient = scanner.nextLine();
            if (ingredient.isEmpty()) {
                break;
            }
            if (drug.getIngredients().contains(ingredient)) {
                drug.removeIngredient(ingredient);
            } else {
                ArrayList<Drug> collisions = checkCollisions(ingredient);
                if (collisions.size() > 0) {
                    System.out.print("Other drugs have the same ingredient. Here is said list of drugs: ");
                    int counter = 1;
                    for (Drug collidingDrug: collisions) {
                        String suffix = counter != collisions.size() ? ", " : ".\n";
                        System.out.print(collidingDrug.getName() + suffix);
                        counter++;
                    }
                }
                drug.addIngredient(ingredient);
            }
        }
    }

    // EFFECTS: Goes through each drug and each of their ingredients.
    //          If the given string matches then return each drug that contains it.
    private ArrayList<Drug> checkCollisions(String ingredient) {

        ArrayList<Drug> conflictingDrugs = new ArrayList<>();

        for (Drug drug: tracker.getMedicationList()) {
            if (drug.getIngredients().contains(ingredient)) {
                conflictingDrugs.add(drug);
            }
        }
        return conflictingDrugs;
    }

    // MODIFIES: this
    // EFFECTS: Opens a new menu which allows the user to decide if they want to
    //          decrease or increase a given drug's amount and by how much. Loops.
    private void changeAmount(Drug drug) {
        printStringArray(amountOptions);
        int choice;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            choice = -1;
        }
        if (choice != 3) {
            System.out.println("How much would you like to change the amount by? (Currently "
                    + drug.getAmountLeft() + "ml): ");
            double amount;
            try {
                amount = parseDouble(scanner.nextLine());
            } catch (Exception e) {
                amount = -1;
            }
            amountSwitches(choice, drug, amount);
            changeAmount(drug);
        }
    }

    // EFFECTS: Increases or decreases the amount of a drug based on the choice.
    private void amountSwitches(int choice, Drug drug, double amount) {
        switch (choice) {
            case 1:
                drug.increaseAmountLeft(amount);
                break;
            case 2:
                drug.decreaseAmountLeft(amount);
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: Resets a given drug's values based on new inputted ones.
    private void setDrugFields(Drug drug) {
        System.out.print("Name of drug: ");
        drug.changeName(scanner.nextLine());
        System.out.print("Description of drug: ");
        drug.changeDesc(scanner.nextLine());
        System.out.print("Dosage of drug (ml): ");
        int dosage;
        try {
            dosage = Integer.parseInt(scanner.nextLine());
        } catch (Exception e) {
            dosage = 0;
            System.out.print("Amount set to 0ml.");
        }
        drug.changeDosage(dosage);
        scanner.nextLine();
    }


    // MODIFIES: this
    // EFFECTS: Starts a new menu which allows the user to input a time and
    //          toggle whether that time is a time to take a dose. Loops.
    private void dosageToggles(Drug drug) {
        while (true) {
            try {
                printTimeArray(drug.getDoseTimes());
                System.out.print("Insert time to toggle (HH:mm) [LEAVE EMPTY TO GO BACK]:");
                String drugTime = scanner.nextLine();
                if (drug.getDoseTimes().contains(LocalTime.parse(drugTime))) {
                    drug.getDoseTimes().remove(LocalTime.parse(drugTime));
                } else {
                    drug.addDoseTime(LocalTime.parse(drugTime));
                }
            } catch (Exception e) {
                break;
            }
        }

    }

    // EFFECTS: List all the ingredients of a given drug.
    private void listIngredients(Drug drug) {
        System.out.print(drug.getName() + " contains:");
        for (String ingredient: drug.getIngredients()) {
            System.out.print(ingredient + ", ");
        }
    }

    // EFFECTS: Prints all of a drug's information.
    private void printDrug(Drug drug) {
        System.out.println("Name of drug: " + drug.getName());
        System.out.println("Description of drug: " + drug.getDesc());
        System.out.println("Dose times of drug (HH:mm): " + drug.getDoseTimes());
        System.out.println("Dosage of drug (ml): " + drug.getDosage());
        System.out.println("Current quantity of drug (ml): " + drug.getAmountLeft());
    }


}
