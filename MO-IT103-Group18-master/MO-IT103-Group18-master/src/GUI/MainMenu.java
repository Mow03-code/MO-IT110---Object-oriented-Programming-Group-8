/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import Backend.AttendanceRecord;
import Backend.Employee;
import Backend.PayrollDataLoader;
import Backend.RegularPayrollProcessor;
import Console.TACP2;
import static GUI.ViewEmployee.ATTENDANCE_RECORD_CSV;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;


public class MainMenu extends JFrame {
    private static String selectedEmployeeID = ""; // Stores selected Employee ID
    private static final String EMPLOYEE_CSV = PayrollDataLoader.EMPLOYEE_DETAILS_CSV; // Path is now defined in one place (PayrollDataLoader)

    //Here it will just launch the login screen.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> LogIn.showLoginScreen());
    }

    public MainMenu(String username) {
        setTitle("MotorPH Employee App");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 200, 50, 200));

        //We added a selection drop down for the selected employee ID (modification)
        JComboBox<String> employeeSelector = new JComboBox<>(getEmployeeIDs());
        employeeSelector.addActionListener(e -> selectedEmployeeID = (String) employeeSelector.getSelectedItem());
        
        //Buttons for the menu option, we added the view employee table button and removed admin access.
        JButton profileButton = new JButton("View Profile");
        JButton attendanceButton = new JButton("View Attendance");
        JButton payrollButton = new JButton("View Payroll");
        JButton viewEmployeeTableButton = new JButton("View Employee Table");
        JButton logoutButton = new JButton("Log Out");
        
        //Action listener for each button
        viewEmployeeTableButton.addActionListener(e -> EmployeeTable.displayEmployeeTable());
        profileButton.addActionListener(e -> showProfile());
        attendanceButton.addActionListener(e -> showAttendance());
        payrollButton.addActionListener(e -> showPayrollMenu());
        logoutButton.addActionListener(e -> logOut());

        panel.add(new JLabel("Select Employee:")); //We added a label for Employee Selector
        
        //Adding the buttons to the UI
        panel.add(employeeSelector); //And add a dropdown to UI
        panel.add(profileButton);
        panel.add(attendanceButton);
        panel.add(payrollButton);
        panel.add(viewEmployeeTableButton);
        panel.add(logoutButton);

        add(panel);
    }

    //This method is used to get the Id of the Employee for the drop down menu
    private String[] getEmployeeIDs() {
        return PayrollDataLoader.loadEmployeeIds().toArray(String[]::new);
    }

    public static String[] parseCSVLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); //Smart comma split
    }
    
    //Retrieves the profile of the selected employee
    private static String getUserProfile(String employeeID) {
        String line;
        
        //It will now try to read the csv file and find the user's info.
        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_CSV))) {
            while ((line = br.readLine()) != null) {
                String[] details = parseCSVLine(line);
                
                //now it checks if the data exists in the csv file and show it.
                if (details.length >= 5 && details[0].equals(employeeID)) {
                    return "Name: " + details[1] + ", " + details[2] + // Changed to Last Name, First Name
                           "\nEmployee Number: " + details[0] +
                           "\nBirthday: " + details[3] +
                           "\nAddress: " + details[4] +
                           "\nPhone Number: " + details[5] +
                           "\nStatus: " + details[10];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Profile not found.";
    }
    
    // This will read attendance CSV file and get the records of the current user.
    private static String getDailyAttendance(String employeeID) {
        String csvFile = ATTENDANCE_RECORD_CSV;
        String line;
        StringBuilder attendanceDetails = new StringBuilder();
        
        //It will try to read the csv file and show the records.
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                if (record.length >= 4 && record[0].equals(employeeID)) {
                    attendanceDetails.append("First name: ").append(record[2])
                        .append("   |   Employee #: ").append(record[0])
                        .append("   |   Date: ").append(record[3])
                        .append("   |   LogIn: ").append(record[4])
                        .append("   |   LogOut: ").append(record[5]).append("\n");
                }
            }
        } catch (IOException e) {
            
        }
        return attendanceDetails.length() > 0 ? attendanceDetails.toString() : "No attendance records found.";
    }

    
    //It calculates the weekly hours worked of the user from the Attendance csv file.
    // OOP: hours now come from AttendanceRecord.calculateHoursWorked(); the GUI only groups them by week.
    private static String getWeeklyHoursWorked(String employeeID) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        Map<Integer, Double> weeklyHoursMap = new LinkedHashMap<>();

        for (AttendanceRecord record : PayrollDataLoader.loadAttendance(employeeID)) {
            LocalDate date = LocalDate.parse(record.getDate(), dateFormatter);
            int weekNumber = (date.getDayOfYear() - 1) / 7 + 1; // This determine which week number it is in a year.
            weeklyHoursMap.merge(weekNumber, record.calculateHoursWorked(), Double::sum);
        }
        
        //This make the summary of weekly hours.
        StringBuilder weeklyHoursInfo = new StringBuilder("Weekly Hours Worked:\n");
        for (Map.Entry<Integer, Double> entry : weeklyHoursMap.entrySet()) {
            weeklyHoursInfo.append("Week ").append(entry.getKey()).append(": ").append(String.format("%.2f", entry.getValue())).append(" hours\n");
        }
        
        return weeklyHoursInfo.toString();
    }


    //Here, it retrieves attendance history of the selected employee
    private static List<String> getAttendanceHistory(String employeeID) {
        List<String> records = new ArrayList<>();
        for (AttendanceRecord record : PayrollDataLoader.loadAttendance(employeeID)) {
            records.add("Date: " + record.getDate() + " | Hours Worked: "
                    + String.format("%.2f", record.calculateHoursWorked()));
        }
        return records;
    }

    // Displays the profile of the selected employee
    private void showProfile() {
        if (selectedEmployeeID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an employee!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String profileInfo = getUserProfile(selectedEmployeeID);
        JOptionPane.showMessageDialog(this, profileInfo, "Employee Profile", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //This will display the options of attendance menu
    public void showAttendance() {
        String[] options = {"View Daily Attendance Record", "Calculate Hours Worked Per Week","View Attendance History"};
        int choice = JOptionPane.showOptionDialog(
            this, 
            "Select an option:", 
            "Attendance Menu", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.INFORMATION_MESSAGE, 
            null, 
            options, 
            options[0]
        );

        switch (choice) {
            case 0 -> showDailyAttendance();
            case 1 -> showWeeklyHoursWorked();
            case 2 -> showAttendanceHistory();
        }
    }
        //This will display the daily attendance of the user. We also make it scrollable. 
    private void showDailyAttendance() {
        String attendanceInfo = getDailyAttendance(selectedEmployeeID);

        JTextArea textArea = new JTextArea(attendanceInfo);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Daily Attendance", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //Here, it will display the total weekly hours worked by the user.
    private void showWeeklyHoursWorked() {
        String weeklyHoursInfo = getWeeklyHoursWorked(selectedEmployeeID);

        JTextArea textArea = new JTextArea(weeklyHoursInfo);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        //We also make it scrollable like what we did in viewing the attendance.
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(200, 300));

        //It displays the weekly hours worked in a message dialog
        JOptionPane.showMessageDialog(this, scrollPane, "Weekly Hours Worked", JOptionPane.INFORMATION_MESSAGE);
    }

    // Displays attendance history for the selected employee
    public void showAttendanceHistory() {
        if (selectedEmployeeID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an employee!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        List<String> attendanceHistory = getAttendanceHistory(selectedEmployeeID);

        StringBuilder historyInfo = new StringBuilder("Attendance History:\n");
        for (String record : attendanceHistory) {
            historyInfo.append(record).append("\n");
        }

        JTextArea textArea = new JTextArea(historyInfo.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(300, 300));

        JOptionPane.showMessageDialog(this, scrollPane, "Full Attendance History", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Compute total monthly hours worked for selected employee
    // Total monthly hours now come from the backend (AttendanceRecord via PayrollDataLoader)
    private double computeTotalMonthlyHours(String employeeID, String selectedMonth, String selectedYear) {
        Month month = Month.valueOf(selectedMonth.toUpperCase());
        int year = Integer.parseInt(selectedYear);
        return PayrollDataLoader.getTotalHoursWorked(employeeID, month, year);
    }

    //This will show the computed salary in a message dialog.
    //(The sample salary matrix from CP2 was removed: it showed hardcoded, unrelated figures.)
    private void showSalary(double totalHours) {
        String salaryInfo = computeSalary(selectedEmployeeID, totalHours);
        JOptionPane.showMessageDialog(this, salaryInfo + "\nHours Worked: " + String.format("%.2f", totalHours),
                "Salary Details", JOptionPane.INFORMATION_MESSAGE);
    }

    // Gross salary now comes from Employee.calculateSalary() instead of GUI-side math
    // Calculates gross salary using Employee.calculateSalary() with thousand separators
    public static String computeSalary(String employeeID, double totalHours) {
        Employee employee = PayrollDataLoader.loadEmployee(employeeID);
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(Locale.US);
        currencyFormat.setMinimumFr actionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);

        if (employee == null) {
            return "Employee Name: \nEmployee ID: " + employeeID + "\nGross Salary: 0.00";
        }
        double grossSalary = employee.calculateSalary(totalHours);
        return "Employee Name: " + employee.getName() + 
               "\nEmployee ID: " + employee.getEmployeeId() + 
               "\nGross Salary: ₱" + currencyFormat.format(grossSalary);
    }

    // Displays salary deductions dynamically using OOP RegularPayrollProcessor based on user's selected period
    private void showDeductions(String employeeID, double totalHours, String selectedMonth, String selectedYear) {
        Employee employee = PayrollDataLoader.loadEmployee(employeeID);
        if (employee == null) {
            JOptionPane.showMessageDialog(this, "Employee not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Setup currency formatter for thousand separators
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(Locale.US);
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);

        // Convert selected string month and year to proper types for the processor
        Month month = Month.valueOf(selectedMonth.toUpperCase());
        int year = Integer.parseInt(selectedYear);

        // Instantiate RegularPayrollProcessor using the selected period
        RegularPayrollProcessor processor = new RegularPayrollProcessor(employee, month, year);
        processor.processPayroll();

        // Retrieve computed values from backend getters
        double grossSalary = processor.getGrossPay();
        double sssDeduction = processor.getSssDeduction();
        double philHealthDeduction = processor.getPhilHealthDeduction();
        double pagIbigDeduction = processor.getPagIbigDeduction();
        double withholdingTax = processor.getWithholdingTax();
        double totalDeductions = sssDeduction + philHealthDeduction + pagIbigDeduction + withholdingTax;
        double netPay = processor.getNetPay();

        // Format the final breakdown with currency format for the UI dialog
        String deductionsInfo = "Gross Salary: ₱" + currencyFormat.format(grossSalary) +
                                "\nSSS Deduction: ₱" + currencyFormat.format(sssDeduction) +
                                "\nPhilHealth Deduction: ₱" + currencyFormat.format(philHealthDeduction) +
                                "\nPag-IBIG Deduction: ₱" + currencyFormat.format(pagIbigDeduction) +
                                "\nWithholding Tax: ₱" + currencyFormat.format(withholdingTax) +
                                "\nTotal Deductions: ₱" + currencyFormat.format(totalDeductions) +
                                "\nNet Pay: ₱" + currencyFormat.format(netPay);

        JOptionPane.showMessageDialog(this, deductionsInfo, "Salary Deductions Breakdown", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Displays generated payslip dynamically using OOP RegularPayrollProcessor based on user's selected period
    public static String showPayslip(String employeeID, double totalHours, String selectedMonth, String selectedYear) {
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(Locale.US);
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);

        double grossSalary = 0, sssDeduction = 0, philHealthDeduction = 0;
        double pagIbigDeduction = 0, withholdingTax = 0, totalDeductions = 0;
        double riceSubsidy = 0, phoneAllowance = 0, clothingAllowance = 0;
        double totalCompensation = 0, netSalary = 0;
        
        // Load the employee object once for both payroll and allowances
        Employee employee = PayrollDataLoader.loadEmployee(employeeID);

        // Compute compensation and deductions dynamically if hours exist and employee is found
        if (totalHours > 0 && employee != null) {
            Month month = Month.valueOf(selectedMonth.toUpperCase());
            int year = Integer.parseInt(selectedYear);

            RegularPayrollProcessor processor = new RegularPayrollProcessor(employee, month, year);
            processor.processPayroll();

            grossSalary = processor.getGrossPay();
            sssDeduction = processor.getSssDeduction();
            philHealthDeduction = processor.getPhilHealthDeduction();
            pagIbigDeduction = processor.getPagIbigDeduction();
            withholdingTax = processor.getWithholdingTax();
            totalDeductions = sssDeduction + philHealthDeduction + pagIbigDeduction + withholdingTax;
            netSalary = processor.getNetPay();
        }

        // Retrieve allowances directly from the Employee object
        if (employee != null) {
            riceSubsidy = employee.getRiceSubsidy();
            phoneAllowance = employee.getPhoneAllowance();
            clothingAllowance = employee.getClothingAllowance();
            totalCompensation = employee.getAllowances();
        }

        // Format payslip details
        String payslipInfo = """
                     **PAYSLIP**
                     Employee: """ + employeeID +
                     "\nGross Salary: ₱" + currencyFormat.format(grossSalary) +
                     "\n\n**Deductions**" +
                     "\nSSS Deduction: ₱" + currencyFormat.format(sssDeduction) +
                     "\nPhilHealth Deduction: ₱" + currencyFormat.format(philHealthDeduction) +
                     "\nPag-IBIG Deduction: ₱" + currencyFormat.format(pagIbigDeduction) +
                     "\nWithholding Tax: ₱" + currencyFormat.format(withholdingTax) +
                     "\nTotal Deductions: ₱" + currencyFormat.format(totalDeductions) +
                     "\n\n**Compensations**" +
                     "\nRice Subsidy: ₱" + currencyFormat.format(riceSubsidy) +
                     "\nPhone Allowance: ₱" + currencyFormat.format(phoneAllowance) +
                     "\nClothing Allowance: ₱" + currencyFormat.format(clothingAllowance) +
                     "\nTotal Compensation: ₱" + currencyFormat.format(totalCompensation) +
                     "\n\n**Net Salary**" +
                     "\nNet Pay: ₱" + currencyFormat.format(netSalary);

        JOptionPane.showMessageDialog(null, payslipInfo, "Generated Payslip", JOptionPane.INFORMATION_MESSAGE);
        return payslipInfo;
    }

    // Show payroll menu for the selected employee
    private void showPayrollMenu() {
        if (selectedEmployeeID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an employee!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] options = {"View Salary", "View Deductions", "View Payslip"};

        int choice = JOptionPane.showOptionDialog(
            this,
            "Select an option:",
            "Payroll Menu",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        //We want to close the selection if user close the dialog box
        if (choice == JOptionPane.CLOSED_OPTION) {
            return;
        }

        //We added a payroll coverage by month(feature 1)
        // Dropdowns for payroll selection
        JComboBox<String> monthSelector = new JComboBox<>(new String[]{
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        });
        
        //We also include the year in payroll coverage
        JComboBox<String> yearSelector = new JComboBox<>(new String[]{
            "2023", "2024", "2025", "2026", "2027"
        });
        
        //We added the selection pannel
        JPanel selectionPanel = new JPanel();
        selectionPanel.add(new JLabel("Select Month:"));
        selectionPanel.add(monthSelector);
        selectionPanel.add(new JLabel("Select Year:"));
        selectionPanel.add(yearSelector);

        int selection = JOptionPane.showConfirmDialog(
            null,
            selectionPanel,
            "Select Payroll Coverage",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (selection != JOptionPane.OK_OPTION) {
            return;
        }

        String selectedMonth = (String) monthSelector.getSelectedItem();
        String selectedYear = (String) yearSelector.getSelectedItem();
        double totalHours = computeTotalMonthlyHours(selectedEmployeeID, selectedMonth, selectedYear);

        switch (choice) {
            case 0 -> showSalary(totalHours);
            case 1 -> showDeductions(selectedEmployeeID, totalHours, selectedMonth, selectedYear);
            case 2 -> showPayslip(selectedEmployeeID, totalHours, selectedMonth, selectedYear);
        }
    }
    
    // Logs the user out and returns to the login screen
    private void logOut() {
        dispose(); // Close the main window
        SwingUtilities.invokeLater(() -> LogIn.showLoginScreen()); // Return to login screen
    }
    
}