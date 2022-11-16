package ui;

import model.Drug;
import model.MedicationTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

// Create a new Frame to hold the menu for a drug
public class DrugMenuGUI extends JFrame implements ActionListener {

    private final MedicationTracker tracker;
    private final Drug drug;
    private JTextField nameField;
    private JTextField descField;
    private JTextField amountField;
    private JTextField dosageField;

    // EFFECTS: Open add all items to the frame and set up the buttons with listeners.
    //          It populates fields with the given drug
    public DrugMenuGUI(MedicationTracker tracker, Drug drug) {
        super(drug.getName() + " Menu");
        this.tracker = tracker;
        this.drug = drug;
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(500, 350));
        setLayout(new GridLayout(0,1));
        setUpFieldsUI();
        populateFields();
        setUpButtonsUI();
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    // EFFECTS: Open add all items to the frame and set up the buttons with listeners.
    //          No population takes place.
    public DrugMenuGUI(MedicationTracker tracker) {
        super("New Drug Menu");
        this.tracker = tracker;
        this.drug = new Drug();
        tracker.addDrug(this.drug);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(500, 350));
        setLayout(new GridLayout(0,1));
        setUpFieldsUI();
        setUpButtonsUI();
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    // MODIFIES: this
    // EFFECTS: Populates the fields with their assigned values.
    private void populateFields() {
        nameField.setText(drug.getName());
        descField.setText(drug.getDesc());
        amountField.setText(String.valueOf(drug.getAmountLeft()));
        dosageField.setText(String.valueOf(drug.getDosage()));
    }

    // MODIFIES:
    // EFFECTS: Adds all labels and fields to a new panel.
    private void setUpFieldsUI() {
        JPanel panel = new JPanel(new GridLayout(0,1));
        JLabel nameLabel = new JLabel("Name: ");
        JLabel descLabel = new JLabel("Description: ");
        JLabel amountLabel = new JLabel("Amount Left: ");
        JLabel dosageLabel = new JLabel("Dosage: ");
        nameField = new JTextField();
        descField = new JTextField();
        amountField = new JTextField();
        dosageField = new JTextField();
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(descLabel);
        panel.add(descField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(dosageLabel);
        panel.add(dosageField);
        add(panel);
    }

    // MODIFIES: this
    // EFFECTS: Creates and connects new buttons to this class' listener
    private void setUpButtonsUI() {
        JButton ingButt = new JButton("Ingredients Menu");
        JButton timesButt = new JButton("Dosage Times Menu");
        JButton saveButt = new JButton("Save Drug & Close");
        ingButt.setActionCommand("ingMenu");
        timesButt.setActionCommand("timesMenu");
        saveButt.setActionCommand("saveDrug");
        ingButt.addActionListener(this);
        timesButt.addActionListener(this);
        saveButt.addActionListener(this);
        JPanel panel = new JPanel(new GridLayout(1,0));
        panel.add(ingButt);
        panel.add(timesButt);
        panel.add(saveButt);
        add(panel);
    }

    // MODIFIES: drug, this
    // EFFECTS: Sets the new values to the drug and closes this Frame.
    private void saveDrug() {
        drug.changeName(nameField.getText());
        drug.changeDesc(descField.getText());
        drug.changeDosage(Double.parseDouble(dosageField.getText()));
        drug.setAmount(Double.parseDouble(amountField.getText()));
        this.dispose();
    }

    // MODIFIES: this
    // EFFECTS: Handles events fired by the buttons. Saving will close the frame.
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ingMenu")) {
            new IngredientListGUI(tracker, drug);
        }
        if (e.getActionCommand().equals("timesMenu")) {
            new TimesListGUI(tracker, drug);
        }
        if (e.getActionCommand().equals("saveDrug")) {
            saveDrug();
            new MainGUI(tracker);
            this.dispose();
        }
    }


}