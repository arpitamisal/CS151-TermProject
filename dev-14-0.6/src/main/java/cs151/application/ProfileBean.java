package cs151.application;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.List;

public class ProfileBean {
    private int id;
    private String fullName;
    private String academicStatus;
    private boolean isEmployed;
    private String jobDetails;
    private String preferredRole;
    private List<String> programmingLanguages = new ArrayList<>();
    private List<String> databases = new ArrayList<>();
    private TreeMap<Integer, String> comments = new TreeMap<>();
    private boolean isWhitelisted;
    private boolean isBlacklisted;

    public ProfileBean(int id, String fullName, String academicStatus, boolean isEmployed, String jobDetails,
            String preferredRole, List<String> programmingLanguages, List<String> databases,
            boolean isWhitelisted, boolean isBlacklisted) { 
        this.id = id;
        this.fullName = fullName;
        this.academicStatus = academicStatus;
        this.isEmployed = isEmployed;
        this.jobDetails = jobDetails;
        this.preferredRole = preferredRole;
        this.programmingLanguages = programmingLanguages;
        this.databases = databases;
        this.isWhitelisted = isWhitelisted;
        this.isBlacklisted = isBlacklisted;
        this.comments = new TreeMap<>();
    }

    public ProfileBean() {
        // Default constructor
    }

    // Getters and Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public List<String> getProgrammingLanguages() {
        return programmingLanguages;
    }

    public void setProgrammingLanguages(List<String> programmingLanguages) {
        this.programmingLanguages = programmingLanguages;
    }

    public List<String> getDatabases() {
        return databases;
    }

    public void setDatabases(List<String> databases) {
        this.databases = databases;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAcademicStatus() {
        return academicStatus;
    }

    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

    public boolean isEmployed() {
        return isEmployed;
    }

    public void setEmployed(boolean isEmployed) {
        this.isEmployed = isEmployed;
    }

    public String getJobDetails() {
        return jobDetails;
    }

    public void setJobDetails(String jobDetails) {
        this.jobDetails = jobDetails;
    }

    public String getPreferredRole() {
        return preferredRole;
    }

    public void setPreferredRole(String preferredRole) {
        this.preferredRole = preferredRole;
    }

    public boolean isWhitelisted() {
        return isWhitelisted;
    }

    public void setWhitelisted(boolean isWhitelisted) {
        this.isWhitelisted = isWhitelisted;
    }

    public boolean isBlacklisted() {
        return isBlacklisted;
    }

    public void setBlacklisted(boolean isBlacklisted) {
        this.isBlacklisted = isBlacklisted;
    }
    
    public TreeMap<Integer, String> getComments() {
        return comments;
    }

    public void addComments(int date, String comment) {
        this.comments.put(date, comment);

    }
    
}
