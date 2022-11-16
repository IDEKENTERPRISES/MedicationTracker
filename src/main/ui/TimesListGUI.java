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

    //This is the method that is called when the JButton btn is clicked
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

    private void reopenTimes() {
        new TimesListGUI(tracker, drug);
        this.dispose();
    }


}