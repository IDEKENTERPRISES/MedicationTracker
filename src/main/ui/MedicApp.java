package ui;

import model.Drug;
import model.MedicationTracker;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MedicApp {

    private Scanner scanner;
    private ArrayList<String> banner = null;
    private MedicationTracker tracker;

    private ArrayList<String> mainOptions = new ArrayList<String>(
            Arrays.asList("1 - List medication list",
                    "2 - Next drug reminder",
                    "3 - Add drug",
                    "4 - Remove drug",
                    "5 - View specific drug",
                    "6 - Quit")
    );

    public MedicApp() {
        scanner = new Scanner(System.in);
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

    private void mainMenu() {

        printBanner();
        for (String option: mainOptions) {
            System.out.println(option);
        }
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
        System.out.println("Name of drug: " + drug.getName());
        System.out.println("Description of drug: " + drug.getDesc());
        System.out.println("Dose times of drug (HH:mm): " + drug.getDoseTimes());
        System.out.println("Dosage of drug (ml): " + drug.getDosage());
        System.out.println("Current quantity of drug (ml): " + drug.getAmountLeft());
    }


}
