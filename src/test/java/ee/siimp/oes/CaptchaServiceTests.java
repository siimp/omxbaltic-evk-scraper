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

    @Test
    public void test_3977() {
        Assert.assertEquals("3977", captchaService.solveImage("captchas/3977.png"));
    }

    @Test
    public void test_3i9a() {
        Assert.assertEquals("3i9a", captchaService.solveImage("captchas/3i9a.png"));
    }

    @Test
    public void test_5u98() {
        Assert.assertEquals("5u98", captchaService.solveImage("captchas/5u98.png"));
    }

    @Test
    public void test_79zi() {
        Assert.assertEquals("79zi", captchaService.solveImage("captchas/79zi.png"));
    }

    @Test
    public void test_h2k6() {
        Assert.assertEquals("h2k6", captchaService.solveImage("captchas/h2k6.png"));
    }

    @Test
    public void test_hes8() {
        Assert.assertEquals("hes8", captchaService.solveImage("captchas/hes8.png"));
    }

    @Test
    public void test_joqe() {
        Assert.assertEquals("joqe", captchaService.solveImage("captchas/joqe.png"));
    }

    @Test
    public void test_maz3() {
        Assert.assertEquals("maz3", captchaService.solveImage("captchas/maz3.png"));
    }

    @Test
    public void test_Sy35() {
        Assert.assertEquals("Sy35", captchaService.solveImage("captchas/Sy35.png"));
    }

}
