package sk.flowy.msbiexport.repository;

import java.sql.Timestamp;
import java.util.Date;

public class DbQuery {

    public static String getListOfTables() {
        return "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='flowy'";
    }

    public static String checkIfTableExistsQuery(String tableName) {
        return "SELECT table_name FROM information_schema.tables WHERE table_schema = 'flowy' AND table_name = '" +
                tableName + "'";
    }

    public static String createGenerationCsvTable(String tableName) {
        return "CREATE TABLE " + tableName + " (id INT NOT NULL AUTO_INCREMENT, generated_at timestamp NOT NULL " +
                "DEFAULT '2000-01-01 00:00:00', client_id INT NOT NULL, FOREIGN KEY (client_id) REFERENCES klient(id)" +
                ", PRIMARY KEY (id))";
    }

    public static String updateTimestampQuery(Integer clientId) {
        return "UPDATE generovanie_csv SET generated_at = current_timestamp() WHERE client_id=" + clientId;
    }

    public static String getDateForClientQuery(Integer clientId) {
        return "SELECT generated_at FROM generovanie_csv WHERE client_id=" + clientId;
    }

    public static String populateGenerationCsvTableWithData() {
        return "INSERT into generovanie_csv (generated_at, client_id) SELECT '2000-01-01 00:00:00', id  from klient";
    }

    //TODO upravit metrika case, dph case a log_log case
    //TODO test s novymi datmi + negenerovat prazdne files
    public static String getAllDataForKlient(String tableName, Integer klientID, Date date) {
        String query;

        String whereClause = " WHERE " + tableName + ".updated_at>'" + new Timestamp(date.getTime()) + "'";
        String additionalClauseToWhere = " AND " + tableName + ".updated_at>'" + new Timestamp(date.getTime()) + "'";

        switch (tableName) {
            case "klient":
                query = "SELECT * FROM " + tableName + " WHERE id = " + klientID + additionalClauseToWhere;
                break;
            case "dodavatel_klient":
                query = "SELECT * FROM " + tableName + " WHERE id_klient = " + klientID + additionalClauseToWhere;
                break;
            case "dodavatel":
                query = "SELECT dodavatel.* FROM dodavatel INNER JOIN dodavatel_klient ON dodavatel.id = " +
                        "dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + klientID + whereClause;
                break;
            case "dodavatel_produkt":
                query = "SELECT dodavatel_produkt.* FROM dodavatel_produkt INNER JOIN dodavatel ON dodavatel_produkt" +
                        ".id_dodavatel = dodavatel.id " +
                        "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND " +
                        "dodavatel_klient.id_klient = " + klientID + " WHERE dodavatel.updated_at>'" + new Timestamp(date.getTime()) + "'";;
                break;
            case "produkt":
                query = "SELECT produkt.* FROM produkt INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt" +
                        ".id_produkt INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                        "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND " +
                        "dodavatel_klient.id_klient = " + klientID + whereClause;
                break;
            case "ean":
                query = "SELECT ean.* FROM ean INNER JOIN produkt ON ean.id_produkt = produkt.id " +
                        " INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt INNER JOIN " +
                        "dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id" +
                        " INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND " +
                        "dodavatel_klient.id_klient = " + klientID + whereClause;
                break;
            case "cenotvorba":
                query = "SELECT cenotvorba.* FROM cenotvorba WHERE cenotvorba.id_klient = " + klientID +
                        additionalClauseToWhere;
                break;
            case "tags":
                query = "SELECT tags.* FROM tags INNER JOIN cenotvorba ON tags.id = cenotvorba.id_tag AND cenotvorba" +
                        ".id_klient = " + klientID + whereClause;
                break;
            case "tag_produkt":
                query = "SELECT tag_produkt.* FROM tag_produkt INNER JOIN tags ON tags.id = tag_produkt.id_tag " +
                        "INNER JOIN cenotvorba ON tags.id = cenotvorba.id_tag AND cenotvorba.id_klient = " + klientID
                        + " WHERE tags.updated_at>'" + new Timestamp(date.getTime()) + "'";;
                break;
            case "uzivatel":
                query = "SELECT uzivatel.* FROM uzivatel WHERE uzivatel.id_klient = " + klientID + additionalClauseToWhere;
                break;
            case "log_prihlasenie":
                query = "SELECT log_prihlasenie.* FROM log_prihlasenie INNER JOIN uzivatel ON log_prihlasenie" +
                        ".id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + klientID + " WHERE uzivatel.updated_at>'" + new Timestamp(date.getTime()) + "'";
                break;
            case "prijemka":
                query = "SELECT prijemka.* FROM prijemka INNER JOIN uzivatel ON uzivatel.id = prijemka.id_uzivatel " +
                        "AND uzivatel.id_klient = " + klientID + whereClause;
                break;
            //no real reference from table uzivatel, only some column id_uzivatel in blocek table.
            case "blocek":
                query = "SELECT blocek.* FROM blocek INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND " +
                        "uzivatel.id_klient = " + klientID + whereClause;
                break;
            // in some logs is possible to get only through uzivatel.
            case "log_cart_bill":
                query = "SELECT log_cart_bill.* FROM log_cart_bill INNER JOIN  blocek ON log_cart_bill.id_bill = " +
                        "blocek.id " +
                        "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + klientID + whereClause;
                break;
            case "log_cart_storno":
                query = " SELECT log_cart_storno.* FROM log_cart_storno " +
                        "INNER JOIN  blocek ON log_cart_storno.id_cart_bill = blocek.id " +
                        "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + klientID + whereClause;
                break;
            case "log_cart_item":
                query = " SELECT log_cart_item.* FROM log_cart_item " +
                        "INNER JOIN  blocek ON log_cart_item.id_cart_bill = blocek.id " +
                        "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + klientID + whereClause;
                break;
            case "log_cart_item_storno":
                query = " SELECT log_cart_item_storno.* FROM log_cart_item_storno " +
                        "INNER JOIN  blocek ON log_cart_item_storno.id_cart_bill = blocek.id " +
                        "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + klientID + whereClause;
                break;
            case "blocek_polozky":
                query = "SELECT blocek_polozky.* FROM blocek_polozky INNER JOIN  blocek ON blocek_polozky.id_blocek =" +
                        " " +
                        "blocek.id " +
                        "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + klientID + whereClause;
                break;
            case "obrazky":
                query = "SELECT obrazky.* FROM obrazky INNER JOIN produkt ON obrazky.product_id = produkt.id " +
                        "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                        "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                        "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND " +
                        "dodavatel_klient.id_klient = " + klientID + whereClause;
                break;
            case "stavy":
                query = "SELECT stavy.* FROM stavy INNER JOIN produkt ON stavy.id_produkt = produkt.id " +
                        "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                        "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                        "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND " +
                        "dodavatel_klient.id_klient = " + klientID + whereClause;
                break;
            case "druh":
                query = "SELECT druh.* FROM druh INNER JOIN produkt ON druh.id = produkt.nonBlockable " +
                        "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                        "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                        "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND " +
                        "dodavatel_klient.id_klient = " + klientID + " WHERE produkt.updated_at>'" + new Timestamp(date.getTime()) + "'";
                break;
            case "cenotvorba_ceny":
                query = "SELECT * FROM cenotvorba_ceny WHERE id_klient = " + klientID + additionalClauseToWhere;
                break;
            case "objednavky":
                query = "SELECT * FROM objednavky WHERE id_klient = " + klientID + additionalClauseToWhere;
                break;
            case "objednavky_produkt":
                query = "SELECT objednavky_produkt.* FROM objednavky_produkt " +
                        "INNER JOIN objednavky ON objednavky_produkt.objednavka_id = objednavky.id AND objednavky" +
                        ".id_klient = " + klientID + whereClause;
                break;
            case "inventura":
                query = "SELECT inventura.* FROM inventura " +
                        "INNER JOIN uzivatel ON inventura.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " +
                        klientID + whereClause;
                break;
            case "atributy_produkt":
                query = "SELECT atributy_produkt.* FROM atributy_produkt INNER JOIN produkt ON atributy_produkt" +
                        ".id_produkt = produkt.id " +
                        "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                        "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                        "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND " +
                        "dodavatel_klient.id_klient = " + klientID + whereClause;
                break;
            default:
                return "SELECT * FROM " + tableName;
        }

        return query;
    }
}

