package ui;

import com.google.zxing.WriterException;
import model.Drug;
import model.EventLog;
import model.MedicationTracker;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static ui.GenerateQR.createQRImage;

// The main GUI for the program. Shows a list of drugs and allows the selection of a drug to access its menu.
public class MainGUI extends JFrame implements ActionListener {

    private final MedicationTracker tracker;

    // EFFECTS: Creates a new Frame which will list all drugs as buttons.
    //          When clicking a drug button the drug menu will open.
    //          The user is also able to save the tracker using a file dialog.
    public MainGUI(MedicationTracker tracker) {
        super("Medication Tracker");
        this.tracker = tracker;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(450, 240));
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
        drugButtons(panel);

        JScrollPane scrollPane = new JScrollPane(panel);
        add(scrollPane);

        JPanel buttonsPanel = new JPanel(new GridLayout(1,3));

        JButton saveButton = new JButton("Save Tracker");
        JButton newDrugButton = new JButton("Create New Drug");
        JButton clearLogButton = new JButton("Clear Log");

        saveButton.setActionCommand("save");
        newDrugButton.setActionCommand("newDrug");
        clearLogButton.setActionCommand("clearLog");

        saveButton.addActionListener(this);
        newDrugButton.addActionListener(this);
        clearLogButton.addActionListener(this);

        buttonsPanel.add(newDrugButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(clearLogButton);

        add(buttonsPanel);
    }

    private void drugButtons(JPanel panel) {
        int ind = 0;
        for (Drug drug: tracker.getMedicationList()) {
            JButton newButt = new JButton(drug.getName());
            newButt.setActionCommand("Drug#" + ind);
            newButt.addActionListener(this);
            panel.add(newButt);
            ind++;
        }
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

        if (e.getActionCommand().equals("clearLog")) {
            EventLog.getInstance().clear();
        }

        if (e.getActionCommand().equals("newDrug")) {
            new DrugMenuGUI(tracker);
            this.dispose();
        }
    }

    // MODIFIES: this, newFrame
    // EFFECTS: Creates a new frame containing the QR equivalent of the tracker in JSON.
    // Clicking the QR will go back to the main screen.
    private void qrPage(BufferedImage code) {
        JFrame newFrame = new JFrame();
        newFrame.setVisible(true);
        newFrame.setSize(new Dimension(400,400));
        newFrame.setResizable(false);
        newFrame.setLocationRelativeTo(null);
        JLabel picLabel = new JLabel(new ImageIcon(
                code.getScaledInstance(200, 200, Image.SCALE_FAST)));

        newFrame.add(picLabel);
        newFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        picLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                newFrame.dispose();
                setVisible(true);
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: Generates a QR of the JSON of the tracker and prints it to screen and to the save location.
    private void generateTheCode(File chooser) {
        try {
            createQRImage(new File(chooser.getAbsolutePath() + ".png"),
                    tracker.toJson().toString(4), 4, "png");

            BufferedImage code = ImageIO.read(new File(chooser.getAbsolutePath() + ".png"));
            qrPage(code);
            this.setVisible(false);
        } catch (IOException e) {
            System.out.println("Error when generating the file.");
        } catch (WriterException e) {
            System.out.println("Error when writing file.");
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

                generateTheCode(fileToSave);
            }
        } catch (Exception e) {
            System.out.println("An error occurred, exiting. Error: " + e.getMessage());
        }
    }
}