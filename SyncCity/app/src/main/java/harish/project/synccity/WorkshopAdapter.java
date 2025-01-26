package harish.project.synccity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class WorkshopAdapter extends RecyclerView.Adapter<WorkshopAdapter.ViewHolder> {
    private final List<Workshop> workshopList;

    public WorkshopAdapter(List<Workshop> workshopList) {
        this.workshopList = workshopList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.workshop_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Workshop workshop = workshopList.get(position);
        holder.title.setText(workshop.getTitle());
        holder.description.setText(workshop.getDescription());
        holder.date.setText(workshop.getDate());
    }

    @Override
    public int getItemCount() {
        return workshopList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description, date;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);
            date = view.findViewById(R.id.date);
        }
    }
}

