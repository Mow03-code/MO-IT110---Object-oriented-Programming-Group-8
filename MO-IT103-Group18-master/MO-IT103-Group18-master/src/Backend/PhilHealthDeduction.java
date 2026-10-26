package Backend;

public class PhilHealthDeduction extends DeductionCalculator {

    public PhilHealthDeduction(double grossPay) {
        super(grossPay);
    }

    @Override
    public double computeDeduction() {
        if (grossPay >= 60000) {
            return 900.0;
        } else {
            return grossPay * 0.03 / 2;
        }
    }

    public static void main(String[] args) {
        PhilHealthDeduction philHealth1 = new PhilHealthDeduction(25000.0);
        System.out.println("Gross Pay: " + philHealth1.grossPay);
        System.out.println("PhilHealth Deduction: " + philHealth1.computeDeduction());
    }
}