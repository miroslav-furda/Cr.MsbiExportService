package sk.flowy.msbiexport.service;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import sk.flowy.msbiexport.repository.DbRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CsvCreationServiceImplTest {

    @MockBean
    DbRepository dbRepository;

    List<String> listOfTables = new ArrayList<>();

    private CsvCreationService csvCreationService;
    private String table_user = "table_user";
    private String table_product = "table_product";
    private String[] data_user_1 = {
            "id", "name", "created_at",
            "1", "John", "1509353990"};  //30.10.17-8:59:50
    private String[] data_product_user_1 = {
            "id", "id_user", "name", "created_at",
            "1", "1", "milk", "1509364790", //30. October 2017 11:59:50
            "2", "1", "chocolate", "1509368390",    //Monday 30. October 2017 12:59:50
            "3", "1", "milk", "1509454790", //31. October 2017 12:59:50
            "4", "1", "watter", "1509451790",   //31. October 2017 12:09:50
            "5", "1", "pepsi", "1509458390",    //31. October 2017 13:59:50
            "6", "1", "ice cream", "1509541190",    //1. November 2017 12:59:50
            "7", "1", "watter", "1509541390",   //1. November 2017 13:03:10
            "8", "1", "milk", "1509544790", //1. November 2017 13:59:50
            "9", "1", "vodka", "1509543990",};  //1. November 2017 13:46:30

    private List<String[]> data_user = new ArrayList<>();
    private List<String[]> data_product = new ArrayList<>();
    private Date lastGenerationDate;


    @Before
    public void setup() {
        listOfTables.add(table_user);
        listOfTables.add(table_product);
        data_user.add(data_user_1);
        data_product.add(data_product_user_1);
        lastGenerationDate = new Date();
        csvCreationService = new CsvCreationServiceImpl(dbRepository);
    }

    @Test
    public void if_first_export_get_csv_for_all_data() {
        when(dbRepository.getListOfTables()).thenReturn(listOfTables);
        when(dbRepository.getTimestampForClient(1)).thenReturn(lastGenerationDate);
        when(dbRepository.getAllFromTableForClient(table_user,1, lastGenerationDate)).thenReturn(data_user);
        when(dbRepository.getAllFromTableForClient(table_product,1, lastGenerationDate)).thenReturn(data_product);

        Stream<Path> files = csvCreationService.exportDataForMSBI(1);

        Assertions.assertThat(files.count()).isEqualTo(2L);
    }

}
