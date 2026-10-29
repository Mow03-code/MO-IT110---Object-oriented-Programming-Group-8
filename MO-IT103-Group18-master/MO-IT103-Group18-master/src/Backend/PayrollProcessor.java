package Backend;

public abstract class PayrollProcessor {
    protected final Employee employee;

    private double netPay;

    public PayrollProcessor(Employee employee) {
        this.employee = employee;
    }

    protected abstract double computePay();

    // Subclasses override these to turn off deductions/allowances
    // when they don't apply (e.g. 13th month pay)
    protected boolean appliesStatutoryDeductions() {
        return true;
    }

    protected boolean appliesAllowances() {
        return true;
    }

    private double calculateNetPay(double grossPay, double deductions) {
        netPay = grossPay - deductions;
        return netPay;
    }

    public double getNetPay() { return netPay; }

    public Payslip processPayroll(String employeeId, String payPeriod) {
        double gross = computePay();

        double sss = 0;
        double philHealth = 0;
        double pagIbig = 0;
        double tax = 0;

        if (appliesStatutoryDeductions()) {
            sss = new SSSDeduction().computeDeduction(gross);
            philHealth = new PhilHealthDeduction().computeDeduction(gross);
            pagIbig = new PagIbigDeduction().computeDeduction(gross);
            double taxable = gross - sss - philHealth - pagIbig;
            tax = new WithholdingTaxDeduction().computeDeduction(taxable);
        }

        double net = calculateNetPay(gross, sss + philHealth + pagIbig + tax);

        double riceSubsidy = appliesAllowances() ? employee.getRiceSubsidy() : 0;
        double phoneAllowance = appliesAllowances() ? employee.getPhoneAllowance() : 0;
        double clothingAllowance = appliesAllowances() ? employee.getClothingAllowance() : 0;

        return new Payslip(employeeId, payPeriod, gross, sss, philHealth, pagIbig, tax, net,
                riceSubsidy, phoneAllowance, clothingAllowance);
    }
}