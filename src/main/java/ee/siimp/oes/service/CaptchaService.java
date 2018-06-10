package ee.siimp.oes.service;

import ee.siimp.oes.model.CaptchaImage;
import ee.siimp.oes.utils.OpenCvUtil;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.bytedeco.javacpp.opencv_core.Mat;

@Service
@AllArgsConstructor
public class CaptchaService {

    private static final Logger LOG = LoggerFactory.getLogger(CaptchaService.class);

    private LetterRecognitionService letterRecognitionService;

    private CaptchaImageService captchaImageService;

    private Config config;

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
            debugSingleLetterImage(fileName, withLetters.getLetterImages(), i);
        }

        String result = stringBuilder.toString();
        LOG.info("result was {}", result);

        return result;
    }

    //TODO: refactor debug methods out
    private void debugSingleLetterImage(String originalImageName, List<Mat> letterImages, int letterIndex) {
        if (Boolean.TRUE.equals(config.getDebugEnabled())) {
            String cleanedLetterImageName = getDebugFile(originalImageName, ".letter." + letterIndex + ".png");
            LOG.debug("saving letter image {}", cleanedLetterImageName);
            OpenCvUtil.writeImage(cleanedLetterImageName, letterImages.get(letterIndex));
        }
    }

    private String getDebugFile(String originalImageName, String suffix) {
        return config.getDebugPath() + "/" +
                originalImageName.substring(originalImageName.lastIndexOf('/') + 1) + suffix;
    }

}
