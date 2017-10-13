package sk.flowy.msbiexport.db;

import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class DbQuery {

    public String getAllRecordFromAtributy() {
        String query =
                String.format("SELECT * FROM atributy");
        return query;
    }

    public String getAdminsTableColumnCount() {
        String query =
                String.format("SELECT COUNT(*) FROM INFORMATION_SCHEMA.COLUMNS WHERE table_catalog = 'flowyDB' AND table_name = 'admins'");
        return query;
    }
}
