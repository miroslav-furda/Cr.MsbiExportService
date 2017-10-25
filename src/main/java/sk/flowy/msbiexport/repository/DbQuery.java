package sk.flowy.msbiexport.repository;

public class DbQuery {

    public static String getListOfTables() {
        return "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='flowy'";
    }

    public static String getAllDataForClient(String tableName, Integer clientId) {

        switch(tableName) {
            case "klient" : return "SELECT * FROM " + tableName + " WHERE id = " + clientId;
            case "dodavatel_klient" : return "SELECT * FROM " + tableName + " WHERE id_klient = " + clientId;
            case "dodavatel" : return "SELECT dodavatel.* FROM dodavatel " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + clientId;
            case "dodavatel_produkt" : return "SELECT DISTINCT dodavatel_produkt.* FROM dodavatel_produkt " +
                    "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + clientId;
            case "produkt" : return "SELECT DISTINCT produkt.* FROM produkt " +
                    "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                    "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + clientId;
            case "ean" : return "SELECT DISTINCT ean.* FROM ean INNER JOIN produkt ON ean.id_produkt = produkt.id " +
                    "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                    "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + clientId;
            case "cenotvorba" : return "SELECT cenotvorba.* FROM cenotvorba WHERE cenotvorba.id_klient = " + clientId;
            case "tags" : return "SELECT tags.* FROM tags " +
                    "INNER JOIN cenotvorba ON tags.id = cenotvorba.id_tag AND cenotvorba.id_klient = " + clientId;
            case "tag_produkt" : return "SELECT tag_produkt.* FROM tag_produkt INNER JOIN tags ON tags.id = tag_produkt.id_tag " +
                    "INNER JOIN cenotvorba ON tags.id = cenotvorba.id_tag AND cenotvorba.id_klient = " + clientId;
            case "uzivatel" : return "SELECT uzivatel.* FROM uzivatel WHERE uzivatel.id_klient = " + clientId;
            case "log_prihlasenie" : return "SELECT log_prihlasenie.* FROM log_prihlasenie " +
                    "INNER JOIN uzivatel ON log_prihlasenie.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + clientId;
            case "prijemka" : return "SELECT prijemka.* FROM prijemka " +
                    "INNER JOIN uzivatel ON uzivatel.id = prijemka.id_uzivatel AND uzivatel.id_klient = " + clientId;
            //no real reference from table uzivatel, only some column id_uzivatel in blocek table.
            case "blocek" : return "SELECT blocek.* FROM blocek " +
                    "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + clientId;
            // in some logs is possible to get only through uzivatel.
            case "log_cart_bill" : return"SELECT log_cart_bill.* FROM log_cart_bill INNER JOIN  blocek ON log_cart_bill.id_bill = blocek.id " +
                    "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + clientId;
            case "log_cart_storno" : return " SELECT DISTINCT log_cart_storno.* FROM log_cart_storno " +
                    "INNER JOIN  blocek ON log_cart_storno.id_cart_bill = blocek.id " +
                    "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + clientId;
            case "log_cart_item" : return " SELECT DISTINCT log_cart_item.* FROM log_cart_item " +
                    "INNER JOIN  blocek ON log_cart_item.id_cart_bill = blocek.id " +
                    "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + clientId;
            case "log_cart_item_storno" : return " SELECT DISTINCT log_cart_item_storno.* FROM log_cart_item_storno " +
                    "INNER JOIN  blocek ON log_cart_item_storno.id_cart_bill = blocek.id " +
                    "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + clientId;
            case "blocek_polozky" : return "SELECT blocek_polozky.* FROM blocek_polozky INNER JOIN  blocek ON blocek_polozky.id_blocek = blocek.id " +
                    "INNER JOIN uzivatel ON blocek.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + clientId;
            case "obrazky" : return "SELECT DISTINCT obrazky.* FROM obrazky INNER JOIN produkt ON obrazky.product_id = produkt.id " +
                    "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                    "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + clientId;
            case "stavy" : return "SELECT DISTINCT stavy.* FROM stavy INNER JOIN produkt ON stavy.id_produkt = produkt.id " +
                    "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                    "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + clientId;
            case "druh" : return "SELECT DISTINCT druh.* FROM druh INNER JOIN produkt ON druh.id = produkt.nonBlockable " +
                    "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                    "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + clientId;
            case "cenotvorba_ceny" : return "SELECT * FROM cenotvorba_ceny WHERE id_klient = " + clientId;
            case "objednavky" : return "SELECT * FROM objednavky WHERE id_klient = " + clientId;
            case "objednavky_produkt" : return "SELECT objednavky_produkt.* FROM objednavky_produkt " +
                    "INNER JOIN objednavky ON objednavky_produkt.objednavka_id = objednavky.id AND objednavky.id_klient = " + clientId;
            case "inventura" : return "SELECT inventura.* FROM inventura " +
                    "INNER JOIN uzivatel ON inventura.id_uzivatel = uzivatel.id AND uzivatel.id_klient = " + clientId;
            case "atributy_produkt" : return "SELECT DISTINCT atributy_produkt.* FROM atributy_produkt INNER JOIN produkt ON atributy_produkt.id_produkt = produkt.id " +
                    "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                    "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + clientId;
            case "metrika" : return "SELECT DISTINCT metrika.* FROM metrika INNER JOIN atributy ON metrika.id = atributy.id_metrika " +
                    "INNER JOIN atributy_produkt ON atributy.id = atributy_produkt.id_atribut " +
                    "INNER JOIN produkt ON atributy_produkt.id_produkt = produkt.id " +
                    "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                    "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + clientId;
            case "dph_hladina" : return "SELECT DISTINCT dph_hladina.* FROM dph_hladina " +
                    "INNER JOIN produkt ON dph_hladina.id_produkt = produkt.id " +
                    "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                    "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + clientId;
            case "dph" : return "SELECT DISTINCT dph.* FROM dph " +
                    "INNER JOIN dph_hladina ON dph.id = dph_hladina.id_dph" +
                    "INNER JOIN produkt ON dph_hladina.id_produkt = produkt.id " +
                    "INNER JOIN dodavatel_produkt ON produkt.id = dodavatel_produkt.id_produkt " +
                    "INNER JOIN dodavatel ON dodavatel_produkt.id_dodavatel = dodavatel.id " +
                    "INNER JOIN dodavatel_klient ON dodavatel.id = dodavatel_klient.id_dodavatel AND dodavatel_klient.id_klient = " + clientId;
            case "log_log" : return "SELECT log_log.* FROM log_log " +
                    "INNER JOIN uzivatel ON log_log.id_user = uzivatel.id AND uzivatel.id_klient = " + clientId;
            default: return "SELECT * FROM " + tableName;
        }
    }
}

