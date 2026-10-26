package Backend;

public class Employee {
    private String name;
    private double basicSalary;
    private double hourlyRate;
    private String position;
    private double allowances;
    private String employmentStatus;

    public Employee(String name, double basicSalary, double hourlyRate, String position, double allowances, String employmentStatus) {
        this.name = name;
        this.basicSalary = basicSalary;
        this.hourlyRate = hourlyRate;
        this.position = position;
        this.allowances = allowances;
        this.employmentStatus = employmentStatus;
    }

    public String getName() {
        return name;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public String getPosition() {
        return position;
    }

    public double getAllowances() {
        return allowances;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public static void main(String[] args) {
        Employee emp1 = new Employee("Garcia", 25000.0, 500.0, "Payroll Clerk", 1500.0, "Regular");

        System.out.println("Employee Name: " + emp1.getName());
        System.out.println("Basic Salary: " + emp1.getBasicSalary());
        System.out.println("Hourly Rate: " + emp1.getHourlyRate());
        System.out.println("Position: " + emp1.getPosition());
        System.out.println("Allowances: " + emp1.getAllowances());
        System.out.println("Employment Status: " + emp1.getEmploymentStatus());
    }
}