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
            //no real reference from table uzivatel, only some column id_uzivatel in blocek table.
            case "blocek" : return "SELECT blocek.* FROM blocek INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + klientID;
            // in some logs is possible to get only through uzivatel.
            case "log_cart_bill" : return"SELECT log_cart_bill.* FROM log_cart_bill INNER JOIN  blocek ON log_cart_bill.id_bill = blocek.id " +
                    "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + klientID;
            case "log_cart_storno" : return " SELECT log_cart_storno.* FROM log_cart_storno " +
                    "INNER JOIN  blocek ON log_cart_storno.id_cart_bill = blocek.id " +
                    "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + klientID;
            case "log_cart_item" : return " SELECT log_cart_item.* FROM log_cart_item " +
                    "INNER JOIN  blocek ON log_cart_item.id_cart_bill = blocek.id " +
                    "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + klientID;
            case "log_cart_item_storno" : return " SELECT log_cart_item_storno.* FROM log_cart_item_storno " +
                    "INNER JOIN  blocek ON log_cart_item_storno.id_cart_bill = blocek.id " +
                    "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + klientID;
            case "blocek_polozky" : return "SELECT blocek_polozky.* FROM blocek_polozky INNER JOIN  blocek ON blocek_polozky.id_blocek = blocek.id " +
                    "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + klientID;
            case "obrazky" : return "SELECT obrazky.* FROM obrazky INNER JOIN produkt ON obrazky.product_id = produkt.id " +
                    "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                    "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + klientID;
            case "stavy" : return "SELECT stavy.* FROM stavy INNER JOIN produkt ON stavy.id_produkt = produkt.id " +
                    "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                    "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + klientID;
            case "druh" : return "SELECT druh.* FROM druh INNER JOIN produkt ON druh.id = produkt.nonBlockable " +
                    "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                    "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + klientID;
            case "cenotvorba_ceny" : return "SELECT * FROM cenotvorba_ceny WHERE id_klient = " + klientID;
            case "objednavky" : return "SELECT * FROM objednavky WHERE id_klient = " + klientID;
            case "objednavky_produkt" : return "SELECT objednavky_produkt.* FROM objednavky_produkt " +
                    "INNER JOIN objednavky ON objednavky_produkt.objednavka_id = objednavky.id AND objednavky.id_klient = " + klientID;
            case "inventura" : return "SELECT inventura.* FROM inventura " +
                    "INNER JOIN uzivatel ON inventura.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + klientID;
            case "atributy_produkt" : return "SELECT atributy_produkt.* FROM atributy_produkt INNER JOIN produkt ON atributy_produkt.id_produkt = produkt.id " +
                    "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                    "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + klientID;
            default: return "SELECT * FROM " + tableName;
        }
    }
}

