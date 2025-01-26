package harish.project.synccity;

public class ProjectLinks {
    private String name;
    private String dept;
    private String location;
    private String startDate;
    private String endDate;
    private String status;
    private String details;

    public ProjectLinks(String name, String dept, String location, String startDate, String endDate, String status, String details) {
        this.name = name;
        this.dept= dept;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.details = details;
    }

    // Getter methods for each field
    public String getName() { return name; }
    public String getDept() { return dept; }
    public String getLocation() { return location; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getStatus() { return status; }
    public String getDetails() { return details; }
}
