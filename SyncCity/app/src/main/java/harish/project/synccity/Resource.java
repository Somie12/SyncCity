package harish.project.synccity;

public class Resource {
    private final int id;
    private final String name;
    private final String type;
    private final int quantity;

    public Resource(int id, String name, String type, int quantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }
}
