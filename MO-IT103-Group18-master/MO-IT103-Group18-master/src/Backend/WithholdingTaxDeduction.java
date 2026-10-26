package Backend;

public class WithholdingTaxDeduction extends DeductionCalculator {

    public WithholdingTaxDeduction(double grossPay) {
        super(grossPay);
    }

    @Override
    public double computeDeduction() {
        if (grossPay <= 20833) {
            return 0;
        } else if (grossPay <= 33332) {
            return (grossPay - 20833) * 0.20;
        } else if (grossPay <= 66666) {
            return 2500 + (grossPay - 33332) * 0.25;
        } else if (grossPay <= 166666) {
            return 10833.33 + (grossPay - 66666) * 0.30;
        } else if (grossPay <= 666666) {
            return 40833.33 + (grossPay - 166666) * 0.32;
        } else {
            return 200833.33 + (grossPay - 666666) * 0.35;
        }
    }

    public static void main(String[] args) {
        WithholdingTaxDeduction tax = new WithholdingTaxDeduction(25000);
        System.out.println("Withholding Tax: " + tax.computeDeduction());
    }
}