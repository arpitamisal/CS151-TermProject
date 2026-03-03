package cs151.application;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Student {
    // Basic Information
    private String fullName;
    private AcademicStatus academicStatus;
    private boolean isEmployed;
    private String jobDetails;
    
    // Skills and Interests
    private List<String> programmingLanguages;
    private List<String> databases;
    private ProfessionalRole preferredRole;
    
    // Faculty Evaluation
    private List<String> comments;
    
    // Future Services Flags
    private boolean whitelist;
    private boolean blacklist;
    
    // Metadata
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    
    public enum AcademicStatus {
        FRESHMAN("Freshman"),
        SOPHOMORE("Sophomore"),
        JUNIOR("Junior"),
        SENIOR("Senior"),
        GRADUATE("Graduate");
        
        private final String displayName;
        
        AcademicStatus(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public enum ProfessionalRole {
        FRONT_END("Front-End"),
        BACK_END("Back-End"),
        FULL_STACK("Full-Stack"),
        DATA("Data"),
        OTHER("Other");
        
        private final String displayName;
        
        ProfessionalRole(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public Student() {
        this.programmingLanguages = new ArrayList<>();
        this.databases = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public Student(String fullName) {
        this();
        this.fullName = fullName;
    }
    
    // Getters and Setters
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public AcademicStatus getAcademicStatus() {
        return academicStatus;
    }
    
    public void setAcademicStatus(AcademicStatus academicStatus) {
        this.academicStatus = academicStatus;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public boolean isEmployed() {
        return isEmployed;
    }
    
    public void setEmployed(boolean employed) {
        this.isEmployed = employed;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public String getJobDetails() {
        return jobDetails;
    }
    
    public void setJobDetails(String jobDetails) {
        this.jobDetails = jobDetails;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public List<String> getProgrammingLanguages() {
        return programmingLanguages;
    }
    
    public void setProgrammingLanguages(List<String> programmingLanguages) {
        this.programmingLanguages = programmingLanguages;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public List<String> getDatabases() {
        return databases;
    }
    
    public void setDatabases(List<String> databases) {
        this.databases = databases;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public ProfessionalRole getPreferredRole() {
        return preferredRole;
    }
    
    public void setPreferredRole(ProfessionalRole preferredRole) {
        this.preferredRole = preferredRole;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public List<String> getComments() {
        return comments;
    }
    
    public void setComments(List<String> comments) {
        this.comments = comments;
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public void addComment(String comment) {
        if (comment != null && !comment.trim().isEmpty()) {
            this.comments.add(comment.trim());
            this.lastModifiedDate = LocalDateTime.now();
        }
    }
    
    public boolean isWhitelist() {
        return whitelist;
    }
    
    public void setWhitelist(boolean whitelist) {
        this.whitelist = whitelist;
        if (whitelist) {
            this.blacklist = false; // Mutually exclusive
        }
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public boolean isBlacklist() {
        return blacklist;
    }
    
    public void setBlacklist(boolean blacklist) {
        this.blacklist = blacklist;
        if (blacklist) {
            this.whitelist = false; // Mutually exclusive
        }
        this.lastModifiedDate = LocalDateTime.now();
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }
    
    // Utility methods
    public String getTrimmedFullName() {
        return fullName != null ? fullName.trim() : "";
    }
    
    public boolean isValid() {
        return fullName != null && !fullName.trim().isEmpty() &&
               academicStatus != null &&
               !programmingLanguages.isEmpty() &&
               !databases.isEmpty() &&
               preferredRole != null &&
               (!isEmployed || (jobDetails != null && !jobDetails.trim().isEmpty()));
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "fullName='" + fullName + '\'' +
                ", academicStatus=" + academicStatus +
                ", isEmployed=" + isEmployed +
                ", preferredRole=" + preferredRole +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return getTrimmedFullName().equalsIgnoreCase(student.getTrimmedFullName());
    }
    
    @Override
    public int hashCode() {
        return getTrimmedFullName().toLowerCase().hashCode();
    }
    
    // Serialization methods
    public String toSerializedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("STUDENT|").append(fullName).append("|")
          .append(academicStatus != null ? academicStatus.name() : "null").append("|")
          .append(isEmployed).append("|")
          .append(jobDetails != null ? jobDetails : "").append("|")
          .append(String.join(",", programmingLanguages)).append("|")
          .append(String.join(",", databases)).append("|")
          .append(preferredRole != null ? preferredRole.name() : "null").append("|")
          .append(String.join(";;", comments)).append("|")
          .append(whitelist).append("|")
          .append(blacklist).append("|")
          .append(createdDate.toString()).append("|")
          .append(lastModifiedDate.toString());
        return sb.toString();
    }
    
    public static Student fromString(String serialized) {
        try {
            String[] parts = serialized.split("\\|");
            if (parts.length < 12 || !"STUDENT".equals(parts[0])) {
                return null;
            }
            
            Student student = new Student();
            student.fullName = parts[1];
            student.academicStatus = "null".equals(parts[2]) ? null : AcademicStatus.valueOf(parts[2]);
            student.isEmployed = Boolean.parseBoolean(parts[3]);
            student.jobDetails = parts[4];
            
            if (!parts[5].isEmpty()) {
                student.programmingLanguages = Arrays.asList(parts[5].split(","));
            }
            
            if (!parts[6].isEmpty()) {
                student.databases = Arrays.asList(parts[6].split(","));
            }
            
            student.preferredRole = "null".equals(parts[7]) ? null : ProfessionalRole.valueOf(parts[7]);
            
            if (!parts[8].isEmpty()) {
                student.comments = Arrays.asList(parts[8].split(";;"));
            }
            
            student.whitelist = Boolean.parseBoolean(parts[9]);
            student.blacklist = Boolean.parseBoolean(parts[10]);
            student.createdDate = LocalDateTime.parse(parts[11]);
            student.lastModifiedDate = LocalDateTime.parse(parts[12]);
            
            return student;
        } catch (Exception e) {
            System.err.println("Error parsing student: " + e.getMessage());
            return null;
        }

    }

    public boolean nameContains(String q) {
        return q == null || q.isBlank()
                || (fullName != null && fullName.toLowerCase().contains(q.toLowerCase()));
    }

    public boolean statusEqualsIgnoreCase(String q) {
        if (q == null || q.isBlank()) return true;
        if (academicStatus == null) return false;
        return academicStatus.name().equalsIgnoreCase(q)
                || academicStatus.getDisplayName().equalsIgnoreCase(q);
    }

    public boolean roleEqualsIgnoreCase(String q) {
        if (q == null || q.isBlank()) return true;
        if (preferredRole == null) return false;
        return preferredRole.name().equalsIgnoreCase(q)
                || preferredRole.getDisplayName().equalsIgnoreCase(q);
    }

    public boolean hasLanguageContaining(String q) {
        if (q == null || q.isBlank()) return true;
        if (programmingLanguages == null) return false;
        final String needle = q.toLowerCase();
        return programmingLanguages.stream().anyMatch(s -> s != null && s.toLowerCase().contains(needle));
    }

    public boolean hasDatabaseContaining(String q) {
        if (q == null || q.isBlank()) return true;
        if (databases == null) return false;
        final String needle = q.toLowerCase();
        return databases.stream().anyMatch(s -> s != null && s.toLowerCase().contains(needle));
    }



}
