package ui;

import model.MedicationTracker;
import persistence.JsonReader;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class JsonChooserGUI extends JFrame implements ActionListener {

    private MedicationTracker tracker;
    private JFileChooser fileChooser;

    public JsonChooserGUI() {
        super("Medication Tracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(200, 140));
        ((JPanel) getContentPane()).setBorder(new EmptyBorder(13, 13, 13, 13));
        setLayout(new BorderLayout());
        JButton loadButton = new JButton("Load from save");
        loadButton.setActionCommand("loadButton");
        loadButton.addActionListener(this);
        JButton skipButton = new JButton("Skip...");
        skipButton.setActionCommand("skipButton");
        skipButton.addActionListener(this);
        add(loadButton, BorderLayout.NORTH);
        add(skipButton, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    //This is the method that is called when the JButton btn is clicked
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("loadButton")) {
            this.fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                loadJsonData(selectedFile);
                new MainGUI(tracker);
                this.dispose();
            }
        }
        if (e.getActionCommand().equals("skipButton")) {
            this.tracker = new MedicationTracker();
            System.out.println("New tracker created!");
            new MainGUI(tracker);
            this.dispose();
        }
    }

    // MODIFIES: this
    // EFFECTS: Loads a tracker from a JSON file. If an IOException occurs, a new tracker is made.
    private void loadJsonData(File file) {
        try {
            JsonReader jsonReader = new JsonReader(file);
            tracker = jsonReader.read();
            System.out.println("Loaded previous tracker!");
        } catch (IOException e) {
            System.out.println("An IO error occurred, starting new tracker.");
            tracker = new MedicationTracker();
        }
    }
}