package main;

import dao.DatabaseManager;
import entity.*;
import exception.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class MainModule {
    private static DatabaseManager dbManager;
    private static Scanner scanner;

    public static void main(String[] args) {
        dbManager = new DatabaseManager();
        scanner = new Scanner(System.in);

        try {
            dbManager.initializeDatabase();
            showMenu();
        } catch (SQLException e) {
            System.out.println("Error initializing database: " + e.getMessage());
        } finally {
            dbManager.close();
            scanner.close();
        }
    }

    private static void showMenu() {
        while (true) {
            System.out.println("\nCareerHub Job Board");
            System.out.println("1. Post a job");
            System.out.println("2. View all job listings");
            System.out.println("3. Apply for a job");
            System.out.println("4. Create applicant profile");
            System.out.println("5. Search jobs by salary range");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    postJob();
                    break;
                case 2:
                    viewAllJobListings();
                    break;
                case 3:
                    applyForJob();
                    break;
                case 4:
                    createApplicantProfile();
                    break;
                case 5:
                    searchJobsBySalaryRange();
                    break;
                case 6:
                    System.out.println("Thank you for using CareerHub Job Board. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void postJob() {
        try {
            System.out.print("Enter company name: ");
            String companyName = scanner.nextLine();
            System.out.print("Enter company location: ");
            String companyLocation = scanner.nextLine();

            Company company = new Company(0, companyName, companyLocation);
            dbManager.insertCompany(company);

            System.out.print("Enter job title: ");
            String jobTitle = scanner.nextLine();
            System.out.print("Enter job description: ");
            String jobDescription = scanner.nextLine();
            System.out.print("Enter job location: ");
            String jobLocation = scanner.nextLine();
            System.out.print("Enter salary: ");
            BigDecimal salary = scanner.nextBigDecimal();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter job type: ");
            String jobType = scanner.nextLine();

            JobListing job = new JobListing(0, company.getCompanyID(), jobTitle, jobDescription, jobLocation, salary, jobType);
            dbManager.insertJobListing(job);

            System.out.println("Job posted successfully!");
        } catch (SQLException e) {
            System.out.println("Error posting job: " + e.getMessage());
        }
    }

    private static void viewAllJobListings() {
        try {
            List<JobListing> jobs = dbManager.getJobListings();
            for (JobListing job : jobs) {
                System.out.println("Job ID: " + job.getJobID());
                System.out.println("Title: " + job.getJobTitle());
                System.out.println("Company ID: " + job.getCompanyID());
                System.out.println("Location: " + job.getJobLocation());
                System.out.println("Salary: " + job.getSalary());
                System.out.println("Type: " + job.getJobType());
                System.out.println("Posted Date: " + job.getPostedDate());
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving job listings: " + e.getMessage());
        }
    }

    private static void applyForJob() {
        try {
            System.out.print("Enter your applicant ID: ");
            int applicantID = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter the job ID you want to apply for: ");
            int jobID = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter your cover letter: ");
            String coverLetter = scanner.nextLine();

            JobApplication application = new JobApplication(0, jobID, applicantID, coverLetter);
            dbManager.insertJobApplication(application);

            System.out.println("Application submitted successfully!");
        } catch (SQLException e) {
            System.out.println("Error submitting application: " + e.getMessage());
        }
    }

    private static void createApplicantProfile() {
        try {
            System.out.print("Enter first name: ");
            String firstName = scanner.nextLine();
            System.out.print("Enter last name: ");
            String lastName = scanner.nextLine();
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            System.out.print("Enter phone number: ");
            String phone = scanner.nextLine();

            Applicant applicant = new Applicant(0, firstName, lastName, email, phone);
            applicant.createProfile(email, firstName, lastName, phone);
            dbManager.insertApplicant(applicant);

            System.out.println("Applicant profile created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating applicant profile: " + e.getMessage());
        } catch (InvalidEmailFormatException e) {
            System.out.println("Invalid email format: " + e.getMessage());
        }
    }

    private static void searchJobsBySalaryRange() {
        try {
            System.out.print("Enter minimum salary: ");
            BigDecimal minSalary = scanner.nextBigDecimal();
            System.out.print("Enter maximum salary: ");
            BigDecimal maxSalary = scanner.nextBigDecimal();

            List<JobListing> jobs = dbManager.getJobListingsInSalaryRange(minSalary, maxSalary);
            for (JobListing job : jobs) {
                System.out.println("Job ID: " + job.getJobID());
                System.out.println("Title: " + job.getJobTitle());
                System.out.println("Company ID: " + job.getCompanyID());
                System.out.println("Location: " + job.getJobLocation());
                System.out.println("Salary: " + job.getSalary());
                System.out.println("Type: " + job.getJobType());
                System.out.println("Posted Date: " + job.getPostedDate());
                System.out.println("-----------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error searching jobs: " + e.getMessage());
        }
    }
}
