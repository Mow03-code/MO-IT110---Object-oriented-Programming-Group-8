# MotorPH Employee App — Testing Guide

**MO-IT110 Object-Oriented Programming — Group 8 (S2101)**

Group Members:
- Monette Pagdanganan
- Katrin Ibe
- Noel Baluso
- Czarina Ravin Acusar
- Maricel Abella

## Login

Log in using an employee's **Employee ID** and **Last Name** (not case-sensitive). Credentials are checked against the Employee Details CSV, not a separate login file.

Example: Employee ID `10001`, Last Name `Garcia`

## Setup

1. Clone the repository.
2. Open the project folder in NetBeans (or VS Code with the Java extension).
3. Make sure the `lib/LGoodDatePicker-11.2.1.jar` file is present — it's required for the New/Update Employee date pickers.
4. Build the project ("Clean and Build" in NetBeans, or just Run in VS Code).

## Running the app

Run `src/GUI/MainMenu.java` — it has the main entry point and opens the MotorPH Employee App window directly to the Main Menu.

## Sample login / data notes

- Employee 10001 (Garcia, Manuel III) has real attendance data for June and October 2024 — use these months when testing payroll features.
- January 2024 has no attendance data on purpose, to test how the app handles an empty month.
- Employee records run from 10001 to 10034.

## Features to test

1. **View Profile** — shows employee details by ID.
2. **View Attendance** — daily records, weekly hours, attendance history, and leave records.
3. **View Payroll** — select Regular, 13th Month, or Final Pay, then view Salary, Deductions, or Payslip.
4. **My Payslip Disputes** — file a dispute tied to a specific payslip period, and see your filed disputes.
5. **View Employee Table** — view/search all employees.
6. **New Employee** — add a new employee record (test that invalid salary/rate values are rejected).
7. **Manage Employees → Update** — edit an existing employee's details.
8. **Manage Employees → Delete** — remove an employee record (confirmation required).
