/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import static GUI.ViewEmployee.EMPLOYEE_DETAILS_CSV;
import static GUI.ViewEmployee.parseCSVLine;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class HourlyRate {
    //This method retrives the hourly rate of an employee from the csv file base on the ID
    public static double getHourlyRate(String employeeID) {
        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_DETAILS_CSV))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                //Checking if the line has enough fields and the employee Id has a match
                if (record.length >= 19 && record[0].equals(employeeID)) {
                    return Double.parseDouble(record[18]); // Hourly rate is at index 19 of the csv file, starts with 1
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading employee salary data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return 0.0; //This is a default return value if the employee is not found
    }
}
