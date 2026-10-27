package Backend;

public class AttendanceRecord {
    private String employeeId;
    private String date;
    private String timeIn;
    private String timeOut;

    public AttendanceRecord(String employeeId, String date, String timeIn, String timeOut) {
        this.employeeId = employeeId;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
    } 
    public String getEmployeeId() {
    return employeeId;
}

public String getDate() {
    return date;
}

public String getTimeIn() {
    return timeIn;
}

public String getTimeOut() {
    return timeOut;
}
public double calculateHoursWorked() {
    String[] inParts = timeIn.split(":");
    String[] outParts = timeOut.split(":");
    int inMinutes = Integer.parseInt(inParts[0]) * 60 + Integer.parseInt(inParts[1]);
    int outMinutes = Integer.parseInt(outParts[0]) * 60 + Integer.parseInt(outParts[1]);
    double hoursWorked = (outMinutes - inMinutes) / 60.0;
    return hoursWorked;
}
public static void main(String[] args) {
    AttendanceRecord record1 = new AttendanceRecord("10001", "06-03-2024", "08:59", "18:31");

    System.out.println("Employee ID: " + record1.getEmployeeId());
    System.out.println("Date: " + record1.getDate());
    System.out.println("Time In: " + record1.getTimeIn());
    System.out.println("Time Out: " + record1.getTimeOut());
    System.out.println("Hours Worked: " + record1.calculateHoursWorked());
}
   } 
    
