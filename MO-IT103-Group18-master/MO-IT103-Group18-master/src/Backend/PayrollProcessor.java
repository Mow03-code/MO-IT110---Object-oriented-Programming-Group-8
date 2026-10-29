package Backend;

public abstract class PayrollProcessor {
    protected Employee employee;

    private double grossPay;
    private double sssDeduction;
    private double philHealthDeduction;
    private double pagIbigDeduction;
    private double withholdingTax;
    private double netPay;

    public PayrollProcessor(Employee employee) {
        this.employee = employee;
    }

    protected abstract double computePay();

    private double calculateNetPay(double grossPay, double deductions) {
        netPay = grossPay - deductions;
        return netPay;
    }

    // Runs the full payroll computation for this employee: gross pay,
    // each statutory deduction in the correct order, then net pay.
    public void processPayroll() {
        grossPay = computePay();

        // SSS, PhilHealth, and Pag-IBIG are each based directly on gross pay
        sssDeduction = new SSSDeduction().computeDeduction(grossPay);
        philHealthDeduction = new PhilHealthDeduction().computeDeduction(grossPay);
        pagIbigDeduction = new PagIbigDeduction().computeDeduction(grossPay);

        // Withholding tax is based on TAXABLE income (gross minus the three
        // deductions above), not on raw gross pay
        double taxableIncome = grossPay - sssDeduction - philHealthDeduction - pagIbigDeduction;
        withholdingTax = new WithholdingTaxDeduction().computeDeduction(taxableIncome);

        double totalDeductions = sssDeduction + philHealthDeduction + pagIbigDeduction + withholdingTax;
        calculateNetPay(grossPay, totalDeductions);
    }

    public double getGrossPay() { return grossPay; }
    public double getSssDeduction() { return sssDeduction; }
    public double getPhilHealthDeduction() { return philHealthDeduction; }
    public double getPagIbigDeduction() { return pagIbigDeduction; }
    public double getWithholdingTax() { return withholdingTax; }
    public double getNetPay() { return netPay; }
}
