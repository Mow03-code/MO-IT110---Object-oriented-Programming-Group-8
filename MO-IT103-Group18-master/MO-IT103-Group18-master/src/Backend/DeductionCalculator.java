package Backend;

public abstract class DeductionCalculator {
    protected double grossPay;

    public DeductionCalculator(double grossPay) {
        this.grossPay = grossPay;
    }

    public abstract double computeDeduction();
}