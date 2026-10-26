package Backend;

public class LeaveRecord {
  private String employeeId;
private String leaveType;
private String startDate;
private String endDate;
private String approvalStatus;
private String paidStatus;  
public LeaveRecord(String employeeId, String leaveType, String startDate, String endDate, String approvalStatus, String paidStatus) {
    this.employeeId = employeeId;
    this.leaveType = leaveType;
    this.startDate = startDate;
    this.endDate = endDate;
    this.approvalStatus = approvalStatus;
    this.paidStatus = paidStatus;

}public String getEmployeeId() {
    return employeeId;
}

public String getLeaveType() {
    return leaveType;
}

public String getStartDate() {
    return startDate;
}

public String getEndDate() {
    return endDate;
}

public String getApprovalStatus() {
    return approvalStatus;
}

public String getPaidStatus() {
    return paidStatus;
}
public int getDuration() {
    String[] startParts = startDate.split("-");
    String[] endParts = endDate.split("-");
    int startDay = Integer.parseInt(startParts[1]);
    int endDay = Integer.parseInt(endParts[1]);
    int duration = (endDay - startDay) + 1;
    return duration;
}
public static void main(String[] args) {
    LeaveRecord leave1 = new LeaveRecord("10001", "Vacation", "10-26-2026", "10-28-2026", "Approved", "Paid");

    System.out.println("Employee ID: " + leave1.getEmployeeId());
    System.out.println("Leave Type: " + leave1.getLeaveType());
    System.out.println("Start Date: " + leave1.getStartDate());
    System.out.println("End Date: " + leave1.getEndDate());
    System.out.println("Approval Status: " + leave1.getApprovalStatus());
    System.out.println("Paid Status: " + leave1.getPaidStatus());
    System.out.println("Duration: " + leave1.getDuration() + " day(s)");
}
}
