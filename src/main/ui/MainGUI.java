package ui;

import model.Drug;
import model.MedicationTracker;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalTime;

public class MainGUI extends JFrame implements ActionListener {

    private final MedicationTracker tracker;

    // EFFECTS: Creates a new Frame which will list all drugs as buttons.
    //          When clicking a drug button the drug menu will open.
    //          The user is also able to save the tracker using a file dialog.
    public MainGUI(MedicationTracker tracker) {
        super("Medication Tracker");
        this.tracker = tracker;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(300, 240));
        setLayout(new GridLayout(0,1));
        setUpUI();
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    // MODIFIES: this
    // EFFECTS: Adds all the drugs as buttons to a scroll pane inside a panel.
    //          The panel is then added to the frame.
    private void setUpUI() {
        JPanel panel = new JPanel(new GridLayout(0,1));
        int ind = 0;
        for (Drug drug: tracker.getMedicationList()) {
            JButton newButt = new JButton(drug.getName());
            newButt.setActionCommand("Drug#" + ind);
            newButt.addActionListener(this);
            panel.add(newButt);
            ind++;
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);
        JPanel buttonsPanel = new JPanel(new GridLayout(1,2));
        JButton saveButton = new JButton("Save Tracker");
        JButton newDrugButton = new JButton("Create New Drug");
        saveButton.setActionCommand("save");
        newDrugButton.setActionCommand("newDrug");
        saveButton.addActionListener(this);
        newDrugButton.addActionListener(this);
        buttonsPanel.add(newDrugButton);
        buttonsPanel.add(saveButton);
        add(buttonsPanel);
    }

    // MODIFIES: this
    // EFFECTS: Pressing a drug will send the chosen drug to the drug menu. This will close this frame.
    //          Choosing to save the tracker will open a file dialog,
    //          allowing the user to choose where to save their file.
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().startsWith("Drug#")) {

            int index = Integer.parseInt(e.getActionCommand().split("Drug#")[1]);

            new DrugMenuGUI(tracker, tracker.getMedicationList().get(index));
            this.dispose();
        }

        if (e.getActionCommand().equals("save")) {
            saveTracker();
        }

        if (e.getActionCommand().equals("newDrug")) {
            new DrugMenuGUI(tracker);
            this.dispose();
        }
    }

    // EFFECTS: Save the tracker in JSON with the extension .json using the path the user chooses from the file dialog.
    private void saveTracker() {
        try {
            JFileChooser chooser = new JFileChooser();
            int userSelection = chooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = chooser.getSelectedFile();
                JsonWriter jsonWriter = new JsonWriter(fileToSave);
                jsonWriter.open();
                jsonWriter.write(tracker);
                jsonWriter.close();
            }
        } catch (Exception e) {
            System.out.println("An error occurred, exiting. Error: " + e.getMessage());
        }
    }
}