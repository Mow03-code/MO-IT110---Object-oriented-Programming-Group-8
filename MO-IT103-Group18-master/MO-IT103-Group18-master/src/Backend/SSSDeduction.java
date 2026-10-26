package Backend;

public class SSSDeduction extends DeductionCalculator {

    public SSSDeduction(double grossPay) {
        super(grossPay);
    }
@Override
public double computeDeduction() {
    if (grossPay <= 3250) {
        return 135.0;
    } else if (grossPay > 99750) {
        return 4500.0;
    } else {
        int steps = (int) Math.ceil((grossPay - 3250) / 500.0);
        return 135.0 + (steps * 22.50);
    }
}
  public static void main(String[] args) {
    SSSDeduction sss1 = new SSSDeduction(25000.0);
    System.out.println("Gross Pay: " + sss1.grossPay);
    System.out.println("SSS Deduction: " + sss1.computeDeduction());
}
    }
