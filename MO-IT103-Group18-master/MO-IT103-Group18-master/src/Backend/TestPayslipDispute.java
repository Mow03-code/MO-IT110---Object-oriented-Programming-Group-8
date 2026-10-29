package Backend;

import Backend.PayslipDispute;

public class TestPayslipDispute {

    public static void main(String[] args) {

        PayslipDispute dispute = new PayslipDispute(
            "DSP-001",
            "10001",
            "2026-10",
            "Incorrect deduction",
            "The employee believes the deduction is incorrect."
        );

        System.out.println("Dispute ID: " + dispute.getDisputeId());
        System.out.println("Status: " + dispute.getStatus());

        dispute.markUnderReview();

        System.out.println("Updated Status: " + dispute.getStatus());

        dispute.resolve(
            "PAYROLL-01",
            "Deduction checked and corrected."
        );

        System.out.println("Final Status: " + dispute.getStatus());
        System.out.println("Resolution: " + dispute.getResolution());
    }
    
}
