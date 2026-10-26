package Backend;

public abstract class OvertimeRecord extends AttendanceRecord {

    private double overtimeHours;

    public OvertimeRecord(String employeeId, String date, String timeIn, String timeOut, double overtimeHours) {
        super(employeeId, date, timeIn, timeOut);
        this.overtimeHours = overtimeHours;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public abstract double computeOvertimePay(double hourlyRate);
}