package Backend;

public class HolidayOvertime extends OvertimeRecord {

    public HolidayOvertime(String employeeId, String date, String timeIn, String timeOut, double overtimeHours) {
        super(employeeId, date, timeIn, timeOut, overtimeHours);
    }

    @Override
    public double computeOvertimePay(double hourlyRate) {
        return getOvertimeHours() * hourlyRate * 2.0;
    }

    public static void main(String[] args) {
        HolidayOvertime ot = new HolidayOvertime("E001", "10-26-2026", "08:00", "19:00", 2.0);
        System.out.println("Holiday Overtime Pay: " + ot.computeOvertimePay(100));
    }
}