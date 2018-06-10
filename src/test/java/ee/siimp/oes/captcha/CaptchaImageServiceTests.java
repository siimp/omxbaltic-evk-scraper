package ee.siimp.oes.captcha;

import ee.siimp.oes.common.Config;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CaptchaImageServiceTests {

    private static final Logger LOG = LoggerFactory.getLogger(CaptchaImageServiceTests.class);

    @Mock
    private Config config;

    @InjectMocks
    private CaptchaImageService captchaImageService;

    @Before
    public void setUp() {
        when(config.getDebugEnabled()).thenReturn(true);
        when(config.getDebugPath()).thenReturn("debug");
    }

    @Test
    public void should_always_detect_4_letter_contours() throws IOException {
        Path dir = Paths.get("captchas");
        DirectoryStream.Filter<Path> filter = e -> !e.getFileName().toString().contains("simple") &&
                e.getFileName().toString().endsWith(".png");

        Double totalCount = 0.0;
        Double solvedCount = 0.0;
        Integer expected = 4;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, filter)) {
            for (Path imagePath : stream) {
                totalCount++;
                CaptchaImage image = captchaImageService.loadImage(imagePath.toString());
                CaptchaImage cleanedImage = captchaImageService.cleanImage(image);
                CaptchaImage imageWithLetters = captchaImageService.findLetters(cleanedImage);

                Integer result = imageWithLetters.getLetterImages().size();
                LOG.info("expecting {}, got {}", expected, result);
                if (expected.equals(result)) {
                    solvedCount++;
                }
            }
        }

        LOG.info("solved {}/{} -> {}%", solvedCount.intValue(), totalCount.intValue(), (solvedCount / totalCount) * 100.0);
        Assert.assertTrue(String.format("%.2f percent is less than 100 percent", (solvedCount / totalCount) * 100.0), solvedCount / totalCount >= 1.0);

    }

    @Test
    public void test_maz3() {
        CaptchaImage image = captchaImageService.loadImage("captchas/maz3.png");
        CaptchaImage cleanedImage = captchaImageService.cleanImage(image);

        CaptchaImage result = captchaImageService.findLetters(cleanedImage);
        assertThat(result.getLetterImages().size()).isEqualTo(4);
    }

    @Test
    public void test_joqe() {
        CaptchaImage image = captchaImageService.loadImage("captchas/joqe.png");
        CaptchaImage cleanedImage = captchaImageService.cleanImage(image);

        CaptchaImage result = captchaImageService.findLetters(cleanedImage);
        assertThat(result.getLetterImages().size()).isEqualTo(4);
    }

    @Test
    public void test_higu() {
        CaptchaImage image = captchaImageService.loadImage("captchas/higu.png");
        CaptchaImage cleanedImage = captchaImageService.cleanImage(image);

        CaptchaImage result = captchaImageService.findLetters(cleanedImage);
        assertThat(result.getLetterImages().size()).isEqualTo(4);
    }

    @Test
    public void test_p922() {
        CaptchaImage image = captchaImageService.loadImage("captchas/p922.png");
        CaptchaImage cleanedImage = captchaImageService.cleanImage(image);

        CaptchaImage result = captchaImageService.findLetters(cleanedImage);
        assertThat(result.getLetterImages().size()).isEqualTo(4);
    }

    @Test
    public void test_sy35() {
        CaptchaImage image = captchaImageService.loadImage("captchas/sy35.png");
        CaptchaImage cleanedImage = captchaImageService.cleanImage(image);

        CaptchaImage result = captchaImageService.findLetters(cleanedImage);
        assertThat(result.getLetterImages().size()).isEqualTo(4);
    }

    @Test
    public void test_h2k6() {
        CaptchaImage image = captchaImageService.loadImage("captchas/h2k6.png");
        CaptchaImage cleanedImage = captchaImageService.cleanImage(image);

        CaptchaImage result = captchaImageService.findLetters(cleanedImage);
        assertThat(result.getLetterImages().size()).isEqualTo(4);
    }

}
