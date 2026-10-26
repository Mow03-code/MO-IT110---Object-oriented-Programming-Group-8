package Backend;

public class PagIbigDeduction extends DeductionCalculator {

    public PagIbigDeduction(double grossPay) {
        super(grossPay);
    }

    @Override
    public double computeDeduction() {
        return Math.min(grossPay * 0.02, 100);
    }

    public static void main(String[] args) {
        PagIbigDeduction pagIbig1 = new PagIbigDeduction(25000.0);
        System.out.println("Gross Pay: " + pagIbig1.grossPay);
        System.out.println("Pag-IBIG Deduction: " + pagIbig1.computeDeduction());
    }
}