package Backend;

public class RegularPayrollProcessor extends PayrollProcessor {

    private AttendanceRecord attendanceRecord;
    private OvertimeRecord overtimeRecord;

    public RegularPayrollProcessor(Employee employee, AttendanceRecord attendanceRecord, OvertimeRecord overtimeRecord) {
        super(employee);
        this.attendanceRecord = attendanceRecord;
        this.overtimeRecord = overtimeRecord;
    }

    @Override
    protected double computePay() {
        double hoursWorked = attendanceRecord.calculateHoursWorked();
        double basicPay = hoursWorked * employee.getHourlyRate();
        double overtimePay = overtimeRecord.computeOvertimePay();
        return basicPay + overtimePay + employee.getAllowances();
    }

    public static void main(String[] args) {
        Employee emp = new Employee("10001", "Garcia", "Manuel III", "Chief Executive Officer", "Regular", 90000, 535.71, 1500, 2000, 1000);
        AttendanceRecord attendance = new AttendanceRecord("10001", "06-03-2024", "08:59", "18:31");
        RegularOvertime overtime = new RegularOvertime("10001", "06-03-2024", "08:59", "18:31", 535.71);
        overtime.setApprovalStatus("Approved");

        RegularPayrollProcessor processor = new RegularPayrollProcessor(emp, attendance, overtime);
        System.out.println("Computed Pay: " + processor.computePay());
    }
}