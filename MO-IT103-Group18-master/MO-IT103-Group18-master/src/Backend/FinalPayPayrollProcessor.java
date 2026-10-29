package Backend;

public class FinalPayPayrollProcessor extends PayrollProcessor {

    private static final int SICK_LEAVE_DAYS_PER_YEAR = 15; // MotorPH policy

    public FinalPayPayrollProcessor(Employee employee) {
        super(employee);
    }

    @Override
    protected double computePay() {
        // 1. Last salary
        double lastSalary = employee.getBasicSalary();

        // 2. Leave conversion: unused sick leave days x daily rate
        double dailyRate = employee.getHourlyRate() * 8;
        double leaveConversion = SICK_LEAVE_DAYS_PER_YEAR * dailyRate;

        // 3. Prorated 13th month pay (assumes full year worked)
        double proratedThirteenthMonth = employee.getBasicSalary() / 12.0;

        return lastSalary + leaveConversion + proratedThirteenthMonth;
    }

    public static void main(String[] args) {
        Employee emp = PayrollDataLoader.loadEmployee("10001");
        FinalPayPayrollProcessor processor = new FinalPayPayrollProcessor(emp);

        Payslip payslip = processor.processPayroll("10001", "Final Pay 2024");
        System.out.println(payslip);
    }
}