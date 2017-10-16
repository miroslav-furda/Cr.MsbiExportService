package sk.flowy.msbiexport.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class SecurityFilterTest {

    private SecurityFilter securityFilter;

    @Before
    public void setup() {
        securityFilter = new SecurityFilter();
    }

    @Test
    public void test() {

    }

}