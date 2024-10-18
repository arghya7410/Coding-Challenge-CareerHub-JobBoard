package entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Company {
    private int companyID;
    private String companyName;
    private String location;
    private List<JobListing> jobs;

    public Company(int companyID, String companyName, String location) {
        this.companyID = companyID;
        this.companyName = companyName;
        this.location = location;
        this.jobs = new ArrayList<>();
    }

    public void postJob(String jobTitle, String jobDescription, String jobLocation, BigDecimal salary, String jobType) {
        JobListing job = new JobListing(jobs.size() + 1, this.companyID, jobTitle, jobDescription, jobLocation, salary, jobType);
        jobs.add(job);
    }

    public List<JobListing> getJobs() {
        return jobs;
    }

    // Getters and setters
    public int getCompanyID() {
        return companyID;
    }

    public void setCompanyID(int companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
