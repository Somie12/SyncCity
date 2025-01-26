package harish.project.synccity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
    private List<Project> projects;
    private List<Project> filteredProjects;

    public ProjectAdapter(List<Project> projects) {
        this.projects = projects;
        this.filteredProjects = new ArrayList<>(projects);
    }

    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project, parent, false);
        return new ProjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = filteredProjects.get(position);
        holder.nameTextView.setText(project.getName());
        holder.locationTextView.setText(project.getLocation());
        holder.startDateTextView.setText(project.getStartDate());
        holder.endDateTextView.setText(project.getEndDate());
        holder.statusTextView.setText(project.getStatus());
        holder.detailsTextView.setText(project.getDetails());
    }

    @Override
    public int getItemCount() {
        return filteredProjects.size();
    }

    public void filter(String query) {
        filteredProjects.clear();
        if (query.isEmpty()) {
            filteredProjects.addAll(projects);
        } else {
            for (Project project : projects) {
                if (project.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredProjects.add(project);
                }
            }
        }
        notifyDataSetChanged();
    }

    public void filterByStatus(String status) {
        filteredProjects.clear();
        for (Project project : projects) {
            if (project.getStatus().equalsIgnoreCase(status)) {
                filteredProjects.add(project);
            }
        }
        notifyDataSetChanged();
    }

    public void sortProjectsByStatus() {
        Collections.sort(filteredProjects, new Comparator<Project>() {
            @Override
            public int compare(Project p1, Project p2) {
                return p1.getStatus().compareTo(p2.getStatus());
            }
        });
        notifyDataSetChanged();
    }

    public static class ProjectViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView locationTextView;
        TextView startDateTextView;
        TextView endDateTextView;
        TextView statusTextView;
        TextView detailsTextView;

        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.projectName);
            locationTextView = itemView.findViewById(R.id.projectLocation);
            startDateTextView = itemView.findViewById(R.id.projectStartDate);
            endDateTextView = itemView.findViewById(R.id.projectEndDate);
            statusTextView = itemView.findViewById(R.id.projectStatus);
            detailsTextView = itemView.findViewById(R.id.projectDetails);
        }
    }
}
