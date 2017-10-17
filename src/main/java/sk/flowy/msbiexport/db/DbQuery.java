package sk.flowy.msbiexport.db;

import org.springframework.stereotype.Component;


@Component
public class DbQuery {

    public String getListOfTables() {
        String query =
                String.format("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='flowy'");
        return query;
    }

    public String getAlldata(String tableName) {
        String query =
                new StringBuilder("SELECT * FROM ")
                .append(tableName).toString();
        return query;
    }
}

