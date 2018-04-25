package ee.siimp.oes;

import ee.siimp.oes.service.CaptchaService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = NONE)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CaptchaServiceTests {
    
    @Autowired
    private CaptchaService captchaService;

    @Test
    public void test_simple_2VYK() {
        Assert.assertEquals("2VYK", captchaService.solveImage("captchas/simple_2VYK.png"));
    }

    @Test
    public void test_333o() {
        Assert.assertEquals("333o", captchaService.solveImage("captchas/333o.png"));
    }

}
