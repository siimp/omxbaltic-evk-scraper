package ee.siimp.oes;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ee.siimp.oes.service.CaptchaService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CaptchaServiceTests {
    
    private static final String DEBUG_DIRECTORY = "src/test/java/ee/siimp/oes/debug";
    
    @Autowired
    private CaptchaService captchaService;
    
    @Test
    public void test_333o() {
        Assert.assertEquals("333o", captchaService.solveImage("captchas/333o.png", DEBUG_DIRECTORY));
    }
    
    @Test
    public void test_3977() {
        Assert.assertEquals("3977", captchaService.solveImage("captchas/3977.png", DEBUG_DIRECTORY));
    }
    
    @Test
    public void test_3i9a() {
        Assert.assertEquals("3i9a", captchaService.solveImage("captchas/3i9a.png", DEBUG_DIRECTORY));
    }
    
    @Test
    public void test_5u98() {
        Assert.assertEquals("5u98", captchaService.solveImage("captchas/5u98.png", DEBUG_DIRECTORY));
    }
    
    @Test
    public void test_79zi() {
        Assert.assertEquals("79zi", captchaService.solveImage("captchas/79zi.png", DEBUG_DIRECTORY));
    }
    
    @Test
    public void test_h2k6() {
        Assert.assertEquals("h2k6", captchaService.solveImage("captchas/h2k6.png", DEBUG_DIRECTORY));
    }
    
    @Test
    public void test_hes8() {
        Assert.assertEquals("hes8", captchaService.solveImage("captchas/hes8.png", DEBUG_DIRECTORY));
    }
    
    @Test
    public void test_joqe() {
        Assert.assertEquals("joqe", captchaService.solveImage("captchas/joqe.png", DEBUG_DIRECTORY));
    }
    
    @Test
    public void test_maz3() {
        Assert.assertEquals("maz3", captchaService.solveImage("captchas/maz3.png", DEBUG_DIRECTORY));
    }
    
    @Test
    public void test_Sy35() {
        Assert.assertEquals("Sy35", captchaService.solveImage("captchas/Sy35.png", DEBUG_DIRECTORY));
    }

}
