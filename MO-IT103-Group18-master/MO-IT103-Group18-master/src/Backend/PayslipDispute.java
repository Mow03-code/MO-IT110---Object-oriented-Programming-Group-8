package Backend;

import java.time.LocalDateTime;

/**
 * Represents an employee's complaint about a payslip.
 * This class stores dispute details and tracks review and resolution.
 * Database persistence and GUI handling belong in separate classes.
 */
public class PayslipDispute {

     public enum Status {
        OPEN,
        UNDER_REVIEW,
        RESOLVED,
        REJECTED
    }

    private final String disputeId;
    private final String employeeId;
    private final String payslipPeriod;
    private final String reason;
    private final String description;
    private final LocalDateTime submittedAt;

    private Status status;
    private String resolution;
    private String resolvedBy;
    private LocalDateTime resolvedAt;

    public PayslipDispute(String disputeId, String employeeId, String payslipPeriod,
                          String reason, String description) {
        this.disputeId = requireText(disputeId, "Dispute ID");
        this.employeeId = requireText(employeeId, "Employee ID");
        this.payslipPeriod = requireText(payslipPeriod, "Payslip period");
        this.reason = requireText(reason, "Dispute reason");
        this.description = requireText(description, "Description");

        this.status = Status.OPEN;
        this.submittedAt = LocalDateTime.now();
    }

    public String getDisputeId() {
        return disputeId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getPayslipPeriod() {
        return payslipPeriod;
    }

    public String getReason() {
        return reason;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public String getResolution() {
        return resolution;
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }

    /** Moves an open dispute to the payroll review stage. */
    public void markUnderReview() {
        if (status != Status.OPEN) {
            throw new IllegalStateException(
                    "Only an open dispute can be moved to review.");
        }
        status = Status.UNDER_REVIEW;
    }

    /** Records the outcome when payroll resolves a dispute. */
    public void resolve(String payrollUserId, String resolutionNotes) {
        closeDispute(Status.RESOLVED, payrollUserId, resolutionNotes);
    }

    /** Records why payroll rejected a dispute. */
    public void reject(String payrollUserId, String rejectionReason) {
        closeDispute(Status.REJECTED, payrollUserId, rejectionReason);
    }

    private void closeDispute(Status finalStatus, String payrollUserId,
                              String resolutionNotes) {
        if (status != Status.OPEN && status != Status.UNDER_REVIEW) {
            throw new IllegalStateException(
                    "This dispute has already been closed.");
        }

        this.resolvedBy = requireText(payrollUserId, "Payroll user ID");
        this.resolution = requireText(resolutionNotes, "Resolution notes");
        this.status = finalStatus;
        this.resolvedAt = LocalDateTime.now();
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required.");
        }
        return value.trim();
    }

    @Override
    public String toString() {
        return "PayslipDispute{" +
                "disputeId='" + disputeId + '\'' +
                ", employeeId='" + employeeId + '\'' +
                ", payslipPeriod='" + payslipPeriod + '\'' +
                ", reason='" + reason + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", submittedAt=" + submittedAt +
                ", resolution='" + resolution + '\'' +
                ", resolvedBy='" + resolvedBy + '\'' +
                ", resolvedAt=" + resolvedAt +
                '}';
    }


}

