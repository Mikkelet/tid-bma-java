package com.mikkelthygesen.android.tid_bma_java.Storage;

public class BlockMyAppSqlite {

    public static final class ItemTable {
        public static final String HEADER = "BlockedAppItem";

        public static final class Cols {
            public static final String UUID = "bundleId";
            public static final String NAME = "name";
            public static final String ISITBLOCKED = "isitblocked";
        }
    }

}
