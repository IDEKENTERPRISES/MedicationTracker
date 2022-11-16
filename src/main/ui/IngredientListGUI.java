package ui;

import model.Drug;
import model.MedicationTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class IngredientListGUI extends JFrame implements ActionListener {

    private final MedicationTracker tracker;
    private final Drug drug;
    private JTextField ingField;

    public IngredientListGUI(MedicationTracker tracker, Drug drug) {
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
        for (String ing: drug.getIngredients()) {
            JLabel newLab = new JLabel(ing);
            panel.add(newLab);
        }
        JScrollPane scrollPane = new JScrollPane(panel);
        ingField = new JTextField();

        JPanel togglePanel = new JPanel(new GridLayout(1,2));
        JButton toggleIngButt = new JButton("Add/Remove");
        toggleIngButt.setActionCommand("toggleIng");
        toggleIngButt.addActionListener(this);
        togglePanel.add(toggleIngButt);
        add(scrollPane);
        add(ingField);
        add(togglePanel);
    }

    //This is the method that is called when the JButton btn is clicked
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("toggleIng")) {
            String ingredient = ingField.getText();
            if (!Objects.equals(ingredient, "")) {
                if (drug.getIngredients().contains(ingredient)) {
                    drug.removeIngredient(ingredient);
                } else {
                    drug.addIngredient(ingredient);
                }
            }
        }
        reopenIngredients();

    }

    private void reopenIngredients() {
        new IngredientListGUI(tracker, drug);
        this.dispose();
    }


}