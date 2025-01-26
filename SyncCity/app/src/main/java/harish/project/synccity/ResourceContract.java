package harish.project.synccity;

import android.provider.BaseColumns;

public final class ResourceContract {

    private ResourceContract() {}

    public static final class ResourceEntry implements BaseColumns {
        public static final String TABLE_NAME = "resources";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_RESOURCE_NAME = "name";
        public static final String COLUMN_RESOURCE_TYPE = "type";
        public static final String COLUMN_RESOURCE_QUANTITY = "quantity";
    }
}
