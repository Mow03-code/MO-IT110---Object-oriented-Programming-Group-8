package Backend;

public class RegularOvertime extends OvertimeRecord {

    public RegularOvertime(String employeeId, String date, String timeIn, String timeOut, double overtimeHours) {
        super(employeeId, date, timeIn, timeOut, overtimeHours);
    }

    @Override
    public double computeOvertimePay(double hourlyRate) {
        return getOvertimeHours() * hourlyRate * 1.25;
    }

    public static void main(String[] args) {
        RegularOvertime ot = new RegularOvertime("E001", "10-26-2026", "08:00", "19:00", 2.0);
        System.out.println("Regular Overtime Pay: " + ot.computeOvertimePay(100));
    }
}