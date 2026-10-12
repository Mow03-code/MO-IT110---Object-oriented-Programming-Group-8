/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import static GUI.ViewEmployee.EMPLOYEE_DETAILS_CSV;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import com.github.lgooddatepicker.components.DatePicker;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JComboBox;

/**
 *
 * @author USER
 */
public class NewEmployee {
    //Now, this will open a form for adding a new employee
    public static void displayNewEmployeeForm(JFrame parentFrame, DefaultTableModel model) {
        JFrame formFrame = new JFrame("Add New Employee");
        formFrame.setSize(500, 700);
        formFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        formFrame.setLocationRelativeTo(parentFrame);
        
        //We use the GridBagLayout to organize our input fields
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //This is are the labels for the employee input fields
        String[] labels = {
            "Employee Number*:", "Last Name*:", "First Name*:", "Birthday*:", "Address*:", "Phone Number*:",
            "SSS Number*:", "PhilHealth Number*:", "TIN Number*:", "Pag-IBIG Number*:", "Status*:",
            "Position*:", "Immediate Supervisor*:", "Basic Salary*:", "Rice Subsidy*:", "Phone Allowance*:",
            "Clothing Allowance*:", "Gross Semi-Monthly Rate*:", "Hourly Rate*:"
        };

        JTextField[] textFields = new JTextField[labels.length];
        final DatePicker[] birthdayPicker = new DatePicker[1];
        JComboBox<String> statusCombo = new JComboBox<>(new String[] {
            "Regular", "Probationary"
        });
        
        //Now it will generate the form fields with labels and we set the textfields to 20.
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;

            if (i == 3) { //Birthday field
                birthdayPicker[0] = new DatePicker();
                birthdayPicker[0].setDateToToday();
                panel.add(birthdayPicker[0], gbc);

            } else if (i == 10) { // Status field
                panel.add(statusCombo, gbc);
            }else {
                textFields[i] = new JTextField(20);

                if (i == 0) {
                    String generatedID = generateNextEmployeeID(EMPLOYEE_DETAILS_CSV);
                textFields[i].setText(generatedID);
                textFields[i].setEditable(false);
                textFields[i].setBackground(Color.LIGHT_GRAY);
                textFields[i].setToolTipText("Employee ID is generated automatically");
                } else {
                    final int index = i;
                    textFields[i].getDocument().addDocumentListener(new DocumentListener() {
                        @Override
                        public void insertUpdate(DocumentEvent e) { validateField(textFields[index]); }
                        @Override
                        public void removeUpdate(DocumentEvent e) { validateField(textFields[index]); }
                        @Override
                        public void changedUpdate(DocumentEvent e) { validateField(textFields[index]); }

                        private void validateField(JTextField field) {
                            String input = field.getText().trim();
                            field.setBackground(input.isEmpty() ? Color.PINK : Color.WHITE);
                        }
                    });
                }

                panel.add(textFields[i], gbc);
            }
        }
        
        //This is the submit button to add the employee to the record
        JButton submitButton = ButtonsStyle.ButtonStyle2("Submit");
            gbc.gridx = 0;
            gbc.gridy = labels.length;
            gbc.gridwidth = 2;
            panel.add(submitButton, gbc);

            //This is the action listener when adding a new emplpoyee data to the csv file and in the table
            submitButton.addActionListener(e -> {
            String[] newEmployee = new String[labels.length];

            for (int i = 0; i < labels.length; i++) {
                if (i == 3) {
                    LocalDate selectedDate = birthdayPicker[0].getDate();
                    if (selectedDate == null) {
                        JOptionPane.showMessageDialog(formFrame, "Please select a Birthday.", "Missing Field", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    String formatted = selectedDate.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                    newEmployee[i] = removeQuotes(formatted);
                } else if (i == 10) {
                    newEmployee[i] = removeQuotes((String) statusCombo.getSelectedItem());
                } else {
                    String input = textFields[i].getText().trim();
                    if (input.isEmpty()) {
                        JOptionPane.showMessageDialog(formFrame, "Please fill in the required field: " + labels[i].replace(":", ""), "Missing Field", JOptionPane.WARNING_MESSAGE);
                        textFields[i].setBackground(Color.PINK);
                        textFields[i].requestFocus();
                        return;
                    }
                    if (i == 5 || i == 6 || i == 7 || i == 8 || i == 9 || i == 13 || i == 14 || i == 15 || i == 16 || i == 17 || i == 18) {
                        if (!input.matches("\\d+(\\.\\d{1,2})?")) {
                            JOptionPane.showMessageDialog(formFrame, "Please enter a valid number for: " + labels[i], "Invalid Input", JOptionPane.WARNING_MESSAGE);
                            textFields[i].setBackground(Color.PINK);
                            textFields[i].requestFocus();
                            return;
                        }
                    }

                    newEmployee[i] = removeQuotes(input);
                }
            }

            System.out.println("Appending Employee Data: " + String.join(",", newEmployee));
            appendNewEmployeeToCSV(newEmployee);
            model.addRow(new Object[]{newEmployee[0], newEmployee[1], newEmployee[2], newEmployee[6], newEmployee[7], newEmployee[8], newEmployee[9]});
            formFrame.dispose();
        });

        formFrame.add(panel);
        formFrame.setVisible(true);
    }



//This method saves the new employee data into the csv file
    private static void appendNewEmployeeToCSV(String[] employeeData) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(EMPLOYEE_DETAILS_CSV, true))) {
            bw.write(String.join(",", employeeData)); //Append the new employee record
            bw.newLine();
            bw.flush(); //We want to make sure that the data is written in the csv file before closing
            JOptionPane.showMessageDialog(null, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving new employee!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private static String generateNextEmployeeID(String csvPath) {
        int maxID = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvPath))) {
            br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = ViewEmployee.parseCSVLine(line);
                if (fields.length > 0) {
                    try {
                        int currentID = Integer.parseInt(fields[0].replaceAll("\"", "").trim());
                        if (currentID > maxID) {
                            maxID = currentID;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping non-numeric ID: " + fields[0]);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error generating Employee ID!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return String.valueOf(maxID + 1);
    }
    
 
   private static String removeQuotes(String input) {
        input = input.replaceAll("^\"|\"$", ""); //it removes redundant quotes

        //This will check if the user's input has a comma then put it inside quotation
        if (input.contains(",") || input.contains("\"") || input.contains("'")) {
            input = "\"" + input.replace("\"", "\"\"") + "\"";
        }

        return input.trim(); //This trims the spaces
    }

}
