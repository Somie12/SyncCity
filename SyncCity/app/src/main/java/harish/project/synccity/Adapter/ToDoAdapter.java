package harish.project.synccity.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import harish.project.synccity.AddNewTask;
import harish.project.synccity.TaskScheduling;
import harish.project.synccity.Model.ToDoModel;
import harish.project.synccity.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> todoList;
    private TaskScheduling activity;
    private FirebaseFirestore firestore;

    public ToDoAdapter(TaskScheduling mainActivity , List<ToDoModel> todoList){
        this.todoList = todoList;
        activity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.each_task , parent , false);
        firestore = FirebaseFirestore.getInstance();

        return new MyViewHolder(view);
    }

    public void deleteTask(int position){
        ToDoModel toDoModel = todoList.get(position);
        firestore.collection("task").document(toDoModel.TaskId).delete();
        todoList.remove(position);
        notifyItemRemoved(position);
    }
    public Context getContext(){
        return activity;
    }
    public void editTask(int position){
        ToDoModel toDoModel = todoList.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("task" , toDoModel.getTask());
        bundle.putString("start", toDoModel.getStart());
        bundle.putString("due" , toDoModel.getDue());
        bundle.putString("location" , toDoModel.getLocation());
        bundle.putString("dept" , toDoModel.getDept());
        bundle.putString("id" , toDoModel.TaskId);

        AddNewTask addNewTask = new AddNewTask();
        addNewTask.setArguments(bundle);
        addNewTask.show(activity.getSupportFragmentManager() , addNewTask.getTag());
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModel toDoModel = todoList.get(position);
        holder.mCheckBox.setText(toDoModel.getTask());

        holder.mStartDateTv.setText("Start: " + toDoModel.getStart());
        holder.mDueDateTv.setText("End: " + toDoModel.getDue());
        holder.mLocationTv.setText("Location: " + toDoModel.getLocation());
        holder.mDeptTv.setText("Department: " + toDoModel.getDept());

        holder.mCheckBox.setChecked(toBoolean(toDoModel.getStatus()));

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    firestore.collection("task").document(toDoModel.TaskId).update("status" , 1);
                }else{
                    firestore.collection("task").document(toDoModel.TaskId).update("status" , 0);
                }
            }
        });

    }

    private boolean toBoolean(int status){
        return status != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mStartDateTv;
        TextView mDueDateTv;
        TextView mLocationTv;
        TextView mDeptTv;
        CheckBox mCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mStartDateTv = itemView.findViewById(R.id.start_date_tv);
            mDueDateTv = itemView.findViewById(R.id.due_date_tv);
            mLocationTv = itemView.findViewById(R.id.location_tv);
            mDeptTv = itemView.findViewById(R.id.dept_tv);
            mCheckBox = itemView.findViewById(R.id.mcheckbox);

        }
    }
}