package ee.siimp.oes.captcha;

import ee.siimp.oes.recognition.LetterRecognitionService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

@Service
@AllArgsConstructor
public class CaptchaService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private LetterRecognitionService letterRecognitionService;

    private CaptchaImageService captchaImageService;

    public String solveImage(String fileName) {
        LOG.info("solving image {}", fileName);

        CaptchaImage image = captchaImageService.loadImage(fileName);
        CaptchaImage cleanedImage = captchaImageService.cleanImage(image);

        CaptchaImage withLetters = captchaImageService.findLetters(cleanedImage);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < withLetters.getLetterImages().size(); i++) {
            String letter = letterRecognitionService.read(withLetters.getLetterImages().get(i));
            LOG.debug("letter {} -> {}", i, letter);
            stringBuilder.append(letter);
        }

        String result = stringBuilder.toString();
        LOG.info("result was {}", result);

        return result;
    }

}
