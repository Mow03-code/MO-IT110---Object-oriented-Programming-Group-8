package Backend;

public abstract class PayrollProcessor {

    protected Employee employee;
    private double netPay;

    public PayrollProcessor(Employee employee) {
        this.employee = employee;
    }

    protected abstract double computePay();

    private double calculateNetPay(double grossPay, double deductions) {
        netPay = grossPay - deductions;
        return netPay;
    }

    public double getNetPay() {
        return netPay;
    }
}