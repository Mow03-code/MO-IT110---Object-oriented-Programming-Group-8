package Backend;

import java.time.Month;

public class RegularPayrollProcessor extends PayrollProcessor {

    private final Month month;
    private final int year;

    public RegularPayrollProcessor(Employee employee, Month month, int year) {
        super(employee);
        this.month = month;
        this.year = year;
    }

    @Override
    protected double computePay() {
        // Total hours worked this month, break already deducted
        // (no overtime premium applied here — overtime only gets
        // added once we have an actual approved OvertimeRecord for
        // this employee/month, which we don't yet have a data source for)
        double totalHours = PayrollDataLoader.getTotalHoursWorked(
                employee.getEmployeeId(), month, year);

        // NOTE: allowances are excluded on purpose — they go on the
        // payslip separately, not into taxable gross pay
        return employee.calculateSalary(totalHours);
    }

    public static void main(String[] args) {
        Employee emp = PayrollDataLoader.loadEmployee("10001");
        RegularPayrollProcessor processor = new RegularPayrollProcessor(emp, Month.JUNE, 2024);

        processor.processPayroll();

        System.out.println("Gross Pay: " + processor.getGrossPay());
        System.out.println("SSS: " + processor.getSssDeduction());
        System.out.println("PhilHealth: " + processor.getPhilHealthDeduction());
        System.out.println("Pag-IBIG: " + processor.getPagIbigDeduction());
        System.out.println("Withholding Tax: " + processor.getWithholdingTax());
        System.out.println("Net Pay: " + processor.getNetPay());
    }
}