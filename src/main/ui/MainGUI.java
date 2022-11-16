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

    //This is the method that is called when the JButton btn is clicked
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().startsWith("Drug#")) {

            int index = Integer.parseInt(e.getActionCommand().split("Drug#")[1]);

            new DrugMenuGUI(tracker, tracker.getMedicationList().get(index));
        }

        if (e.getActionCommand().equals("save")) {
            saveTracker();
        }

        if (e.getActionCommand().equals("newDrug")) {
            new DrugMenuGUI(tracker);
            this.dispose();
        }
    }

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