/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Nichie
 */
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
    private static final String EMPLOYEE_CSV = "MO-IT103-Group18-master/src/payroll/hub/databases/MotorPH Employee Data - Employee Details.csv"; // Path to employee details CSV

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
        List<String> ids = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_CSV))) {
            br.readLine(); //Skiping the header
            String line;
            while ((line = br.readLine()) != null) {
                String[] details = parseCSVLine(line);
                if (details.length > 0) {
                    ids.add(details[0]); //Employee ID is first column
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ids.toArray(String[]::new);
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
        String csvFile = "MO-IT103-Group18-master/src/payroll/hub/databases/Copy of MotorPH Employee Data - Attendance Record.csv";
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

    
    //It calculates the weekly hours wroked of the user from the Attendance csv file.
    private static String getWeeklyHoursWorked(String employeeID) {
        String csvFile = "MO-IT103-Group18-master/src/payroll/hub/databases/Copy of MotorPH Employee Data - Attendance Record.csv";
        String line;
        
        //We define here the format of date and time.
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime loginTime = LocalTime.parse(record[4], timeFormatter);
        LocalTime logoutTime = LocalTime.parse(record[5], timeFormatter);

        Map<Integer, Double> weeklyHoursMap = new LinkedHashMap<>();
        
        //This will attempt to read the csv file
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                
                //This ensure that the records has has enough fields and can able to determine the username.
                if (record.length >= 5 && record[0].equals(employeeID)) {
                    LocalDate date = LocalDate.parse(record[3], dateFormatter);
                    int weekNumber = (date.getDayOfYear() - 1) / 7 + 1; // This determine which week number it is in a year.

                    LocalTime loginTime = LocalTime.parse(record[4], timeFormatter);
                    LocalTime logoutTime = LocalTime.parse(record[5], timeFormatter);
                    
                    //It calculates the difference in hours between the log in and log out. 
                    double dailyHours = ChronoUnit.MINUTES.between(loginTime, logoutTime) / 60.0;
                    //It gathers the accumulated hours work per week.
                    weeklyHoursMap.put(weekNumber, weeklyHoursMap.getOrDefault(weekNumber, 0.0) + dailyHours);
                }
            }
        } catch (IOException | RuntimeException e) {
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
        String line;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDANCE_RECORD_CSV))) {
            while ((line = br.readLine()) != null) { //Reads ecah line in the csv file until no lines are left
                String[] record = parseCSVLine(line);
                if (record.length >= 6 && record[0].equals(employeeID)) {
                    //We converted the login and logout time strings into localtime object using our predefined format
                    LocalTime loginTime = LocalTime.parse(record[4], timeFormatter);
                    LocalTime logoutTime = LocalTime.parse(record[5], timeFormatter);
                    double hoursWorked = (logoutTime.toSecondOfDay() - loginTime.toSecondOfDay()) / 3600.0;
                    records.add("Date: " + record[3] + " | Hours Worked: " + String.format("%.2f", hoursWorked));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        //This will display the daily attendance of the user. We also amake it scrollable. 
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
    private double computeTotalMonthlyHours(String employeeID, String selectedMonth, String selectedYear) {
        double totalHours = 0.0;

        try (BufferedReader br = new BufferedReader(new FileReader(ATTENDANCE_RECORD_CSV))) {
            br.readLine(); // Skip header
            String line;

            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                if (record.length >= 6 && record[0].equals(employeeID)) {
                    try {
                        LocalDate recordDate = LocalDate.parse(record[3], DateTimeFormatter.ofPattern("MM-dd-yyyy"));

                        Month selectedMonthEnum = Month.valueOf(selectedMonth.toUpperCase());
                        int selectedYearInt = Integer.parseInt(selectedYear);

                        if (recordDate.getMonth() == selectedMonthEnum && recordDate.getYear() == selectedYearInt) {
                            LocalTime loginTime = LocalTime.parse(record[4]);
                            LocalTime logoutTime = LocalTime.parse(record[5]);

                            double hoursWorked = java.time.Duration.between(loginTime, logoutTime).toMinutes() / 60.0;
                            totalHours += Math.max(hoursWorked, 0);
                        }
                    } catch (NumberFormatException ex) {
                        System.err.println("Skipping malformed record: " + Arrays.toString(record));
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading attendance data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return totalHours;
    }
    
    //This will show the computed salary in a message dialog.
    //We add a sample matrix of the salary tier.
    private void showSalary(double totalHours) { 
        String salaryInfo = computeSalary(selectedEmployeeID, totalHours);
        int[][] salaryMatrix = {
            {35000, 5000, 30000},
            {42000, 6000, 36000},
            {50000, 7500, 42500}
        };

        StringBuilder salaryDetails = new StringBuilder();
        salaryDetails.append(salaryInfo).append("\n\nSample Salary Matrix:\n");
    
    for (int i = 0; i < salaryMatrix.length; i++) {
        salaryDetails.append("Tier ").append(i + 1).append(": Base = ").append(salaryMatrix[i][0])
                     .append(", Tax = ").append(salaryMatrix[i][1])
                     .append(", Net Pay = ").append(salaryMatrix[i][2]).append("\n");
    }

    JOptionPane.showMessageDialog(this, salaryDetails.toString(), "Salary Details", JOptionPane.INFORMATION_MESSAGE);
}


    // Compute salary based on total hours worked
    public static String computeSalary(String employeeID, double totalHours) {
        String line;
        double hourlyRate = 0;
        String employeeName = "";
        String employeeIDFound = "";

        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEE_CSV))) {
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                if (record.length >= 19 && record[0].equals(employeeID)) {
                    employeeName = record[2] + " " + record[1];
                    employeeIDFound = record[0];
                    hourlyRate = Double.parseDouble(record[18]);
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        double grossSalary = totalHours * hourlyRate;
        return "Employee Name: " + employeeName + "\nEmployee ID: " + employeeIDFound + "\nGross Salary: " + String.format("%.2f", grossSalary);
    }

    // Show deductions for selected employee
    private void showDeductions(String employeeID, double totalHours) {
        String salaryInfo = computeSalary(employeeID, totalHours);
        String[] salaryDetails = salaryInfo.split("\n");
        double grossSalary = Double.parseDouble(salaryDetails[2].replaceAll("Gross Salary: ", "").trim());

        List<Double> deductionsList = new ArrayList<>();
        deductionsList.add(TACP2.computeSSS(grossSalary));
        deductionsList.add(TACP2.computePhilHealth(grossSalary));
        deductionsList.add(TACP2.computePagIbig(grossSalary));
        deductionsList.add(TACP2.computeWithholdingTax(grossSalary));

        double totalDeductions = deductionsList.stream().mapToDouble(Double::doubleValue).sum();
        double netPay = grossSalary - totalDeductions;

        String deductionsInfo = "Gross Salary: " + String.format("%.2f", grossSalary) +
                                "\nSSS Deduction: " + String.format("%.2f", deductionsList.get(0)) +
                                "\nPhilHealth Deduction: " + String.format("%.2f", deductionsList.get(1)) +
                                "\nPag-IBIG Deduction: " + String.format("%.2f", deductionsList.get(2)) +
                                "\nWithholding Tax: " + String.format("%.2f", deductionsList.get(3)) +
                                "\nTotal Deductions: " + String.format("%.2f", totalDeductions) +
                                "\nNet Pay: " + String.format("%.2f", netPay);

        JOptionPane.showMessageDialog(null, deductionsInfo, "Salary Deductions Breakdown", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // Display generated payslip for selected employee
    public static String showPayslip(String employeeID, double totalHours) {
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(Locale.US);
        currencyFormat.setMinimumFractionDigits(2);
        currencyFormat.setMaximumFractionDigits(2);

        double grossSalary = 0, sssDeduction = 0, philHealthDeduction = 0;
        double pagIbigDeduction = 0, withholdingTax = 0, totalDeductions = 0;
        double riceSubsidy = 0, phoneAllowance = 0, clothingAllowance = 0;
        double totalCompensation = 0, netSalary = 0;
        
        //Makes the compesnsation zero if no records is found or if total hours is zero.
        if (totalHours > 0) {
            String salaryInfo = computeSalary(employeeID, totalHours);
            String[] salaryDetails = salaryInfo.split("\n");
            grossSalary = Double.parseDouble(salaryDetails[2].replaceAll("Gross Salary: ", "").trim());
            
            //Compute deductions using TACP2 methods
            sssDeduction = TACP2.computeSSS(grossSalary);
            philHealthDeduction = TACP2.computePhilHealth(grossSalary);
            pagIbigDeduction = TACP2.computePagIbig(grossSalary);
            withholdingTax = TACP2.computeWithholdingTax(grossSalary);
            totalDeductions = sssDeduction + philHealthDeduction + pagIbigDeduction + withholdingTax;

            //Load CSV allowances
            String csvFile = "MO-IT103-Group18-master/src/payroll/hub/databases/MotorPH Employee Data - Employee Details.csv";
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] record = parseCSVLine(line);
                    if (record.length >= 19 && record[0].equals(employeeID)) {
                        riceSubsidy = parseQuotedNumber(record[14]);
                        phoneAllowance = parseQuotedNumber(record[15]);
                        clothingAllowance = parseQuotedNumber(record[16]);
                        break;
                    }
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }

            totalCompensation = riceSubsidy + phoneAllowance + clothingAllowance;
            netSalary = grossSalary - totalDeductions;
        }
        // Retrieve allowances from CSV file
        String csvFile = "MO-IT103-Group18-master/src/payroll/hub/databases/MotorPH Employee Data - Employee Details.csv";
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] record = parseCSVLine(line);
                if (record.length >= 19 && record[0].equals(employeeID)) {  
                    riceSubsidy = parseQuotedNumber(record[14]);
                    phoneAllowance = parseQuotedNumber(record[15]);
                    clothingAllowance = parseQuotedNumber(record[16]);
                    break;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
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
    
    private static double parseQuotedNumber(String value) {
        if (value == null || value.trim().isEmpty()) return 0;

        value = value.trim();

        // Remove outer quotes only
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }

        // Remove inner commas (thousand separators)
        value = value.replace(",", "");

        return Double.parseDouble(value);
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
            case 1 -> showDeductions(selectedEmployeeID, totalHours);
            case 2 -> showPayslip(selectedEmployeeID, totalHours);
        }
    }
    
    // Logs the user out and returns to the login screen
    private void logOut() {
        dispose(); // Close the main window
        SwingUtilities.invokeLater(() -> LogIn.showLoginScreen()); // Return to login screen
    }
    
}