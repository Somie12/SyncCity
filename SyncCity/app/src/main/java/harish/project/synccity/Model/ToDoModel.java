package harish.project.synccity.Model;

public class ToDoModel extends harish.project.synccity.Model.TaskId {

    private String task , due, start, location, dept;
    private int status;

    public String getTask() {
        return task;
    }

    public String getStart(){return start;}
    public String getDue() {return due;}
    public String getLocation(){return location;}
    public String getDept(){return dept;}

    public int getStatus() {
        return status;
    }
}