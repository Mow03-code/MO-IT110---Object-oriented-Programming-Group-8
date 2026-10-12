/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class TotalMonthlyHours {
   //This is where we compute the total hours worked in a selected month with the use ofd the attendance records.
    public static double computeTotalMonthlyHours(String employeeID, String selectedMonth, String selectedYear) {
        double totalHours = 0.0;
        try (BufferedReader br = new BufferedReader(new FileReader(ViewEmployee.ATTENDANCE_RECORD_CSV))) {
            br.readLine(); 
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = ViewEmployee.parseCSVLine(line);
                
                //This make sure the valid record format and match employee ID
                if (record.length >= 6 && record[0].equals(employeeID)) {
                    String date = record[3];
                    
                    //We convert here the csv date string into a localDate Object
                    LocalDate recordDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
                    
                    String recordMonth = recordDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
                    String recordYear = String.valueOf(recordDate.getYear());
                    
                    //Here it will check if the records is match to the selected month
                    if (recordMonth.equalsIgnoreCase(selectedMonth) && recordYear.equals(selectedYear)) {
                        LocalTime loginTime = LocalTime.parse(record[4]);
                        LocalTime logoutTime = LocalTime.parse(record[5]);

                        
                        //Now it calculates the hours worked base on the timestamp of login and logout in the csv file
                        double hoursWorked = logoutTime.getHour() - loginTime.getHour(); // Basic computation
                        totalHours += hoursWorked;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading attendance data!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return totalHours;
    } 
}
