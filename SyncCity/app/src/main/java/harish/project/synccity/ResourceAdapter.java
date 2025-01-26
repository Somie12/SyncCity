package harish.project.synccity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ResourceAdapter extends RecyclerView.Adapter<ResourceAdapter.ResourceViewHolder> {

    private final List<Resource> resourceList;
    private final OnResourceClickListener onResourceClickListener;

    public interface OnResourceClickListener {
        void onResourceClick(Resource resource);
    }

    public ResourceAdapter(List<Resource> resourceList, OnResourceClickListener onResourceClickListener) {
        this.resourceList = resourceList;
        this.onResourceClickListener = onResourceClickListener;
    }

    @NonNull
    @Override
    public ResourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resource, parent, false);
        return new ResourceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourceViewHolder holder, int position) {
        Resource resource = resourceList.get(position);
        holder.bind(resource, onResourceClickListener);
    }

    @Override
    public int getItemCount() {
        return resourceList.size();
    }

    static class ResourceViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvResourceName;
        private final TextView tvResourceType;
        private final TextView tvResourceQuantity;

        public ResourceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvResourceName = itemView.findViewById(R.id.tvResourceName);
            tvResourceType = itemView.findViewById(R.id.tvResourceType);
            tvResourceQuantity = itemView.findViewById(R.id.tvResourceQuantity);
        }

        public void bind(Resource resource, OnResourceClickListener listener) {
            tvResourceName.setText(resource.getName());
            tvResourceType.setText(resource.getType());
            tvResourceQuantity.setText(String.valueOf(resource.getQuantity()));

            itemView.setOnClickListener(v -> listener.onResourceClick(resource));
        }
    }
}

