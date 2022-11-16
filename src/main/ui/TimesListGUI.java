package ui;

import model.Drug;
import model.MedicationTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;

public class TimesListGUI extends JFrame implements ActionListener {

    private final MedicationTracker tracker;
    private final Drug drug;
    private JTextField timeField;

    // EFFECTS: Creates a new Frame that contains all dosage times
    //          in a list with a text field and button to add/remove the time
    //          for a given drug.
    public TimesListGUI(MedicationTracker tracker, Drug drug) {
        super("Medication Tracker");
        this.tracker = tracker;
        this.drug = drug;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(300, 240));
        setLayout(new GridLayout(0,1));
        setUpUI();
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    // MODIFIES: this
    // EFFECTS: Creates a new panel that gets populated by a Scroll Pane
    // and the dosage times of the drug. This then gets added to the frame.
    private void setUpUI() {
        JPanel panel = new JPanel(new GridLayout(0,1));
        for (LocalTime time: drug.getDoseTimes()) {
            JLabel newLab = new JLabel(time.toString());
            panel.add(newLab);
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        timeField = new JTextField();

        JPanel togglePanel = new JPanel(new GridLayout(1,2));
        JButton toggleIngButt = new JButton("Add/Remove");
        toggleIngButt.setActionCommand("toggleTime");
        toggleIngButt.addActionListener(this);
        togglePanel.add(toggleIngButt);
        add(scrollPane);
        add(timeField);
        add(togglePanel);
    }

    // MODIFIES: drug
    // EFFECTS: Toggles a given time when the button is clicked.
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("toggleTime")) {
            String drugTime = timeField.getText();
            try {
                if (drug.getDoseTimes().contains(LocalTime.parse(drugTime))) {
                    drug.getDoseTimes().remove(LocalTime.parse(drugTime));
                } else {
                    drug.addDoseTime(LocalTime.parse(drugTime));
                }
                reopenTimes();
            } catch (Exception ex) {
                System.out.println("Error occurred, improper time format given.");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Opens a new instance of TimesListGUI and closes this instance.
    private void reopenTimes() {
        new TimesListGUI(tracker, drug);
        this.dispose();
    }


}