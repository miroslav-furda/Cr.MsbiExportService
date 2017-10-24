package sk.flowy.msbiexport.repository;

public class DbQuery {

    public static String getListOfTables() {
        return "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='flowy'";
    }

    public static String getAlldata(String tableName) {
        return "SELECT * FROM " + tableName;
    }
    public static String getAllDataForKlient(String tableName, Integer klientID) {
        switch(tableName) {
            case "klient" : return "SELECT * FROM " + tableName + " WHERE id = " + klientID;
            case "dodavatel_klient" : return "SELECT * FROM " + tableName + " WHERE id_klient = " + klientID;
            case "dodavatel" : return "SELECT dodavatel.* FROM dodavatel INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + klientID;
            case "dodavatel_produkt" : return "SELECT dodavatel_produkt.* FROM dodavatel_produkt INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + klientID;
            case "produkt" : return "SELECT produkt.* FROM produkt INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + klientID;
            case "ean" : return "SELECT ean.* FROM ean INNER JOIN produkt ON ean.id_produkt = produkt.id " +
            " INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id" +
            " INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + klientID;
            case "cenotvorba" : return "SELECT cenotvorba.* FROM cenotvorba WHERE cenotvorba.id_klient = " + klientID;
            case "tags" : return "SELECT tags.* FROM tags INNER JOIN cenotvorba ON tags.id = cenotvorba.id_tag AND cenotvorba.id_klient = " + klientID;
            case "tag_produkt" : return "SELECT tag_produkt.* FROM tag_produkt INNER JOIN tags ON tags.id = tag_produkt.id_tag " +
                    "INNER JOIN cenotvorba ON tags.id = cenotvorba.id_tag AND cenotvorba.id_klient = " + klientID;
            case "uzivatel" : return "SELECT uzivatel.* FROM uzivatel WHERE uzivatel.id_klient = " + klientID;
            case "log_prihlasenie" : return "SELECT log_prihlasenie.* FROM log_prihlasenie INNER JOIN uzivatel ON log_prihlasenie.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + klientID;
            case "prijemka" : return "SELECT prijemka.* FROM prijemka INNER JOIN uzivatel ON uzivatel.id = prijemka.id_uzivatel AND uzivatel.id_klient = " + klientID;
            case "log_cart_bill" : return "SELECT log_cart_bill.* FROM log_cart_bill INNER JOIN uzivatel ON log_cart_bill.id_user = uzivatel.id AND uzivatel.id_klient = " + klientID;
            default: return "SELECT * FROM " + tableName;
        }
    }
}

