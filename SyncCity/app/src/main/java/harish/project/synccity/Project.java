package harish.project.synccity;

public class Project {
    private String name;
    private String location;
    private String startDate;
    private String endDate;
    private String status; // "ongoing", "future", "finished"
    private String details;

    // Constructor
    public Project(String name, String location, String startDate, String endDate, String status, String details) {
        this.name = name;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.details = details;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }
}
