package ui;

import model.Event;
import model.EventLog;

// Main class of the program.
public class Main {
    // EFFECTS: Entry point of the program. Creates a new JSON choosing GUI.
    public static void main(String[] args) {
        new JsonChooserGUI();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (Event e: EventLog.getInstance()) {
                System.out.println(e.toString());
            }
        }, "Shutdown-thread"));
    }
}
