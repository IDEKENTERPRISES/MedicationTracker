package ui;

import model.Drug;
import model.MedicationTracker;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MedicApp {

    private Scanner scanner;
    private ArrayList<String> banner = null;
    private MedicationTracker tracker;

    private ArrayList<String> mainOptions = new ArrayList<String>(
            Arrays.asList("1 - List Medication List",
                    "2 - Next Drug reminder",
                    "3 - Add Drug",
                    "4 - Remove Drug",
                    "5 - View Specific Drug",
                    "6 - Quit")
    );

    private ArrayList<String> drugOptions = new ArrayList<String>(
            Arrays.asList("1 - List Ingredients",
                    "2 - Change Drug",
                    "3 - Remove Drug",
                    "4 - Add Dosage Time",
                    "5 - Increase/Decrease Amount",
                    "6 - Add/Remove Ingredient",
                    "7 - Back")
    );

    private ArrayList<String> amountOptions = new ArrayList<String>(
            Arrays.asList("1 - Increase Amount",
                    "2 - Decrease Amount",
                    "3 - Cancel")
    );

    public MedicApp() {
        scanner = new Scanner(System.in);
        tracker = new MedicationTracker();
        bannerSet();
        mainMenu();
    }

    private void bannerSet() {
        banner = new ArrayList<String>();
        banner.add("                   _  _        _____                     _                ");
        banner.add("  /\\/\\    ___   __| |(_)  ___ /__   \\ _ __   __ _   ___ | | __  ___  _ __ ");
        banner.add(" /    \\  / _ \\ / _` || | / __|  / /\\/| '__| / _` | / __|| |/ / / _ \\| '__|");
        banner.add("/ /\\/\\ \\|  __/| (_| || || (__  / /   | |   | (_| || (__ |   < |  __/| |   ");
        banner.add("\\/    \\/ \\___| \\__,_||_| \\___| \\/    |_|    \\__,_| \\___||_|\\_\\ \\___||_|   ");
    }

    private void printBanner() {
        for (String row: banner) {
            System.out.println(row);
        }
        System.out.println("Welcome to the Medication Tracker!");
    }

    private void printStringArray(ArrayList<String> options) {
        for (String option: options) {
            System.out.println(option);
        }
    }

    private void printTimeArray(ArrayList<LocalTime> times) {
        for (LocalTime time: times) {
            System.out.println(time);
        }
    }

    private void mainMenu() {

        printBanner();
        printStringArray(mainOptions);
        int choice = scanner.nextInt();
        scanner.nextLine();
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
                System.exit(6);
            default:
                System.out.println("Invalid option chosen!");
        }

        mainMenu();

    }

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

    private void nextMedication() {
        Drug nextDrug = tracker.getNextDrug();

        if (nextDrug != null) {
            drugMenu(nextDrug);
        } else {
            System.out.println("No upcoming scheduled drugs for the rest of today!");
        }


    }

    private void addMedication() {
        Drug newDrug;
        System.out.print("Name of drug: ");
        String drugName = scanner.nextLine();
        System.out.print("Description of drug: ");
        String drugDesc = scanner.nextLine();
        System.out.print("Dose time of drug (HH:mm): ");
        String drugTime = scanner.nextLine();
        System.out.print("Dosage of drug (ml): ");
        double drugDose = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Current quantity of drug (ml): ");
        double drugQuan = scanner.nextDouble();
        scanner.nextLine();
        newDrug = new Drug(drugName, drugDesc, LocalTime.parse(drugTime), drugDose, drugQuan);
        tracker.addDrug(newDrug);

        drugMenu(newDrug);
    }

    private void removeMedication() {
        System.out.print("Index of drug to inspect: ");
        int drugInd = scanner.nextInt();
        scanner.nextLine();

        if (drugInd > 0 && drugInd <= tracker.getMedicationList().size()) {
            tracker.removeDrug(drugInd - 1);
            System.out.print("Drug successfully removed.");
        } else {
            System.out.print("Incorrect index provided.");
        }
    }

    private void removeMedication(Drug drug) {
        tracker.removeDrug(drug);
    }

    private void viewMedication() {
        System.out.print("Index of drug to inspect: ");
        int drugInd = scanner.nextInt();
        scanner.nextLine();

        if (drugInd > 0 && drugInd <= tracker.getMedicationList().size()) {
            drugMenu(tracker.getMedicationList().get(drugInd - 1));
        } else {
            System.out.print("Incorrect index provided.");
        }

    }

    private void drugMenu(Drug drug) {
        printDrug(drug);
        printStringArray(drugOptions);

        int choice = scanner.nextInt();
        scanner.nextLine();
        boolean quitMenu = false;
        switch (choice) {
            case 1:
                listIngredients(drug);
                break;
            case 2:
                setDrugFields(drug);
                break;
            case 3:
                removeMedication(drug);
                quitMenu = true;
                break;
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
                quitMenu = true;
        }
        if (!quitMenu) {
            drugMenu(drug);
        }
    }

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

    private ArrayList<Drug> checkCollisions(String ingredient) {

        ArrayList<Drug> conflictingDrugs = new ArrayList<>();

        for (Drug drug: tracker.getMedicationList()) {
            if (drug.getIngredients().contains(ingredient)) {
                conflictingDrugs.add(drug);
            }
        }
        return conflictingDrugs;
    }

    private void changeAmount(Drug drug) {
        printStringArray(amountOptions);
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice != 3) {
            System.out.println("How much would you like to change the amount by? (Currently "
                    + drug.getAmountLeft() + "ml): ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    drug.increaseAmountLeft(amount);
                    break;
                case 2:
                    drug.decreaseAmountLeft(amount);
                    break;
            }
            changeAmount(drug);
        }
    }

    private void setDrugFields(Drug drug) {
        System.out.print("Name of drug: ");
        drug.changeName(scanner.nextLine());
        System.out.print("Description of drug: ");
        drug.changeDesc(scanner.nextLine());
        System.out.print("Dosage of drug (ml): ");
        drug.changeDosage(scanner.nextDouble());
        scanner.nextLine();
    }


    private void dosageToggles(Drug drug) {
        while (true) {
            try {
                printTimeArray(drug.getDoseTimes());
                System.out.print("Insert time to toggle (HH:mm) [LEAVE EMPTY TO GO BACK]:");
                String drugTime = scanner.nextLine();
                if (drug.getDoseTimes().contains(LocalTime.parse(drugTime))) {
                    drug.getDoseTimes().remove(LocalTime.parse(drugTime));
                } else {
                    drug.addDosageFreq(LocalTime.parse(drugTime));
                }
            } catch (Exception e) {
                break;
            }
        }

    }

    private void listIngredients(Drug drug) {
        System.out.print(drug.getName() + " contains:");
        for (String ingredient: drug.getIngredients()) {
            System.out.print(ingredient + ", ");
        }
    }

    private void printDrug(Drug drug) {
        System.out.println("Name of drug: " + drug.getName());
        System.out.println("Description of drug: " + drug.getDesc());
        System.out.println("Dose times of drug (HH:mm): " + drug.getDoseTimes());
        System.out.println("Dosage of drug (ml): " + drug.getDosage());
        System.out.println("Current quantity of drug (ml): " + drug.getAmountLeft());
    }


}
