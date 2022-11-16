package ui;

import model.Drug;
import model.MedicationTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class DrugMenuGUI extends JFrame implements ActionListener {

    private final MedicationTracker tracker;
    private final Drug drug;
    private JTextField nameField;
    private JTextField descField;
    private JTextField amountField;
    private JTextField dosageField;

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

    private void populateFields() {
        nameField.setText(drug.getName());
        descField.setText(drug.getDesc());
        amountField.setText(String.valueOf(drug.getAmountLeft()));
        dosageField.setText(String.valueOf(drug.getDosage()));
    }

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

    private void saveDrug() {
        drug.changeName(nameField.getText());
        drug.changeDesc(descField.getText());
        drug.changeDosage(Double.parseDouble(dosageField.getText()));
        drug.setAmount(Double.parseDouble(amountField.getText()));
        this.dispose();
    }

    //This is the method that is called when the JButton btn is clicked
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