package harish.project.synccity;

import java.util.ArrayList;
import java.util.List;

public class ProjectRepository {
    public List<ProjectLinks> getProjects() {
        List<ProjectLinks> projects = new ArrayList<>();
        projects.add(new ProjectLinks("Road Construction ", "Civil Department", "ITER Road, Jagmohan Nagar, Bhubaneshwar", "01/01/2024", "23/04/2024", "Completed", "Widening the Road"));
        projects.add(new ProjectLinks("Underground Electricity Distribution", "Electrical Department", "ITER Road, Jagmohan Nagar, Bhubaneshwar", "02/01/2025", "02/02/2025", "Upcoming", "Installation of Underground Electrical Transmission Lines"));
        projects.add(new ProjectLinks("Sewarage Project", "Department of Water and Resources", "ITER Road, Jagmohan Nagar, Bhubaneshwar", "03/07/2024", "13/09/2024", "In Progress", "Construction of Underground Sewerage System"));
        projects.add(new ProjectLinks("Road Restoration", "Civil Department", "ITER Road, Jagmohan Nagar, Bhubaneshwar", "15/09/2024", "04/11/2024", "Upcoming", "Reconstruction of the Deconstructed Road"));
        // Add more projects here
        return projects;
    }
}