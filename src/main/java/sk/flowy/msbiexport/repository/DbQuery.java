package sk.flowy.msbiexport.repository;

public class DbQuery {

    public static String getListOfTables() {
        return "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='flowy'";
    }

    public static String getAlldata(String tableName) {
        return "SELECT * FROM " + tableName;
    }
}

