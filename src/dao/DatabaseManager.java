package dao;

import entity.*;
import exception.*;
import util.DBConnUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private Connection connection;

    public DatabaseManager() {
        try {
            this.connection = DBConnUtil.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initializeDatabase() throws SQLException {
        String createCompaniesTable = "CREATE TABLE IF NOT EXISTS Companies (" +
                "CompanyID INT PRIMARY KEY AUTO_INCREMENT, " +
                "CompanyName VARCHAR(255), " +
                "Location VARCHAR(255))";

        String createJobsTable = "CREATE TABLE IF NOT EXISTS Jobs (" +
                "JobID INT PRIMARY KEY AUTO_INCREMENT, " +
                "CompanyID INT, " +
                "JobTitle VARCHAR(255), " +
                "JobDescription TEXT, " +
                "JobLocation VARCHAR(255), " +
                "Salary DECIMAL(10, 2), " +
                "JobType VARCHAR(50), " +
                "PostedDate DATETIME, " +
                "FOREIGN KEY (CompanyID) REFERENCES Companies(CompanyID))";

        String createApplicantsTable = "CREATE TABLE IF NOT EXISTS Applicants (" +
                "ApplicantID INT PRIMARY KEY AUTO_INCREMENT, " +
                "FirstName VARCHAR(255), " +
                "LastName VARCHAR(255), " +
                "Email VARCHAR(255), " +
                "Phone VARCHAR(20), " +
                "Resume TEXT)";

        String createApplicationsTable = "CREATE TABLE IF NOT EXISTS Applications (" +
                "ApplicationID INT PRIMARY KEY AUTO_INCREMENT, " +
                "JobID INT, " +
                "ApplicantID INT, " +
                "ApplicationDate DATETIME, " +
                "CoverLetter TEXT, " +
                "FOREIGN KEY (JobID) REFERENCES Jobs(JobID), " +
                "FOREIGN KEY (ApplicantID) REFERENCES Applicants(ApplicantID))";

        try (Statement statement = connection.createStatement()) {
            statement.execute(createCompaniesTable);
            statement.execute(createJobsTable);
            statement.execute(createApplicantsTable);
            statement.execute(createApplicationsTable);
        }
    }

    public void insertJobListing(JobListing job) throws SQLException {
        String sql = "INSERT INTO Jobs (CompanyID, JobTitle, JobDescription, JobLocation, Salary, JobType, PostedDate) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, job.getCompanyID());
            pstmt.setString(2, job.getJobTitle());
            pstmt.setString(3, job.getJobDescription());
            pstmt.setString(4, job.getJobLocation());
            pstmt.setBigDecimal(5, job.getSalary());
            pstmt.setString(6, job.getJobType());
            pstmt.setTimestamp(7, Timestamp.valueOf(job.getPostedDate()));
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    job.setJobID(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void insertCompany(Company company) throws SQLException {
        String sql = "INSERT INTO Companies (CompanyName, Location) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, company.getCompanyName());
            pstmt.setString(2, company.getLocation());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    company.setCompanyID(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void insertApplicant(Applicant applicant) throws SQLException {
        String sql = "INSERT INTO Applicants (FirstName, LastName, Email, Phone, Resume) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, applicant.getFirstName());
            pstmt.setString(2, applicant.getLastName());
            pstmt.setString(3, applicant.getEmail());
            pstmt.setString(4, applicant.getPhone());
            pstmt.setString(5, applicant.getResume());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    applicant.setApplicantID(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void insertJobApplication(JobApplication application) throws SQLException {
        String sql = "INSERT INTO Applications (JobID, ApplicantID, ApplicationDate, CoverLetter) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, application.getJobID());
            pstmt.setInt(2, application.getApplicantID());
            pstmt.setTimestamp(3, Timestamp.valueOf(application.getApplicationDate()));
            pstmt.setString(4, application.getCoverLetter());
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    application.setApplicationID(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<JobListing> getJobListings() throws SQLException {
        List<JobListing> jobListings = new ArrayList<>();
        String sql = "SELECT * FROM Jobs";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                JobListing job = new JobListing(
                        rs.getInt("JobID"),
                        rs.getInt("CompanyID"),
                        rs.getString("JobTitle"),
                        rs.getString("JobDescription"),
                        rs.getString("JobLocation"),
                        rs.getBigDecimal("Salary"),
                        rs.getString("JobType")
                );
                job.setPostedDate(rs.getTimestamp("PostedDate").toLocalDateTime());
                jobListings.add(job);
            }
        }
        return jobListings;
    }

    public List<Company> getCompanies() throws SQLException {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT * FROM Companies";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Company company = new Company(
                        rs.getInt("CompanyID"),
                        rs.getString("CompanyName"),
                        rs.getString("Location")
                );
                companies.add(company);
            }
        }
        return companies;
    }

    public List<Applicant> getApplicants() throws SQLException {
        List<Applicant> applicants = new ArrayList<>();
        String sql = "SELECT * FROM Applicants";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                
                        Applicant applicant = new Applicant(
                                rs.getInt("ApplicantID"),
                                rs.getString("FirstName"),
                                rs.getString("LastName"),
                                rs.getString("Email"),
                                rs.getString("Phone")
                        );
                        applicant.setResume(rs.getString("Resume"));
                        applicants.add(applicant);
                    }
                }
                return applicants;
            }

            public List<JobApplication> getApplicationsForJob(int jobID) throws SQLException {
                List<JobApplication> applications = new ArrayList<>();
                String sql = "SELECT * FROM Applications WHERE JobID = ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setInt(1, jobID);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            JobApplication application = new JobApplication(
                                    rs.getInt("ApplicationID"),
                                    rs.getInt("JobID"),
                                    rs.getInt("ApplicantID"),
                                    rs.getString("CoverLetter")
                            );
                            application.setApplicationDate(rs.getTimestamp("ApplicationDate").toLocalDateTime());
                            applications.add(application);
                        }
                    }
                }
                return applications;
            }

            public List<JobListing> getJobListingsInSalaryRange(BigDecimal minSalary, BigDecimal maxSalary) throws SQLException {
                List<JobListing> jobListings = new ArrayList<>();
                String sql = "SELECT j.*, c.CompanyName FROM Jobs j JOIN Companies c ON j.CompanyID = c.CompanyID WHERE j.Salary BETWEEN ? AND ?";
                try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setBigDecimal(1, minSalary);
                    pstmt.setBigDecimal(2, maxSalary);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            JobListing job = new JobListing(
                                    rs.getInt("JobID"),
                                    rs.getInt("CompanyID"),
                                    rs.getString("JobTitle"),
                                    rs.getString("JobDescription"),
                                    rs.getString("JobLocation"),
                                    rs.getBigDecimal("Salary"),
                                    rs.getString("JobType")
                            );
                            job.setPostedDate(rs.getTimestamp("PostedDate").toLocalDateTime());
                            jobListings.add(job);
                        }
                    }
                }
                return jobListings;
            }

            public void close() {
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }