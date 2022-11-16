package ui;

import model.Drug;
import model.MedicationTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame implements ActionListener {

    private MedicationTracker tracker;

    public MainGUI(MedicationTracker tracker) {
        super("Medication Tracker");
        this.tracker = tracker;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(300, 240));
        setLayout(new BorderLayout());
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
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    //This is the method that is called when the JButton btn is clicked
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().startsWith("Drug#")) {

            int index = Integer.parseInt(e.getActionCommand().split("Drug#")[1]);

            new DrugMenuGUI(tracker, tracker.getMedicationList().get(index));
        }
    }
}