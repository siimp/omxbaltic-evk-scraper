package ee.siimp.oes.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = NONE)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles({"test"})
public class CaptchaServiceTests {

    private static final Logger LOG = LoggerFactory.getLogger(CaptchaServiceTests.class);

    @Autowired
    private CaptchaService captchaService;

    @Test
    public void should_solve_at_least_80_percent_of_images() throws IOException {
        Double toSolvePercentage = 80.0;

        Path dir = Paths.get("captchas");
        DirectoryStream.Filter<Path> filter = e -> !e.getFileName().toString().contains("simple") &&
                e.getFileName().toString().endsWith(".png");

        Double totalCount = 0.0;
        Double solvedCount = 0.0;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, filter)) {
            for (Path image : stream) {
                totalCount++;
                String expected = image.getFileName().toString().substring(0, 4);
                String result = captchaService.solveImage(image.toString()).toLowerCase();

                LOG.info("expecting {}, got {}", expected, result);
                if (expected.equals(result)) {
                    solvedCount++;
                }
            }
        }

        Double solvedPercentage = (solvedCount / totalCount) * 100.0;
        LOG.info("solved {}/{} -> {}%", solvedCount.intValue(), totalCount.intValue(), solvedPercentage);
        Assert.assertTrue(String.format("%.2f percent is less than %.2f percent", solvedPercentage, toSolvePercentage), solvedPercentage >= toSolvePercentage);
    }

    @Test
    public void test_simple_2VYK() {
        Assert.assertEquals("2VYK", captchaService.solveImage("captchas/simple_2VYK.png"));
    }

    @Test
    public void test_3977() {
        Assert.assertEquals("3977", captchaService.solveImage("captchas/3977.png").toLowerCase());
    }

    @Test
    public void test_3i9a() {
        Assert.assertEquals("3i9a", captchaService.solveImage("captchas/3i9a.png").toLowerCase());
    }

    @Test
    public void test_5u98() {
        Assert.assertEquals("5u98", captchaService.solveImage("captchas/5u98.png").toLowerCase());
    }

    @Test
    public void test_79zi() {
        Assert.assertEquals("79zi", captchaService.solveImage("captchas/79zi.png").toLowerCase());
    }

    @Test
    public void test_h2k6() {
        Assert.assertEquals("h2k6", captchaService.solveImage("captchas/h2k6.png").toLowerCase());
    }

    @Test
    public void test_hes8() {
        Assert.assertEquals("hes8", captchaService.solveImage("captchas/hes8.png").toLowerCase());
    }

    @Test
    public void test_joqe() {
        Assert.assertEquals("joqe", captchaService.solveImage("captchas/joqe.png").toLowerCase());
    }

    @Test
    public void test_maz3() {
        Assert.assertEquals("maz3", captchaService.solveImage("captchas/maz3.png").toLowerCase());
    }

    @Test
    public void test_sy35() {
        Assert.assertEquals("sy35", captchaService.solveImage("captchas/sy35.png").toLowerCase());
    }

}
