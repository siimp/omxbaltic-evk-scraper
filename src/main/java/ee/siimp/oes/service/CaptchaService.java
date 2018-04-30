package ee.siimp.oes.service;

import ee.siimp.oes.utils.OpenCvUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.bytedeco.javacpp.opencv_core.Mat;

@Service
public class CaptchaService {

    private static final Logger LOG = LoggerFactory.getLogger(CaptchaService.class);

    @Autowired
    private LetterRecognitionService letterRecognitionService;

    @Value("${oes.debug.enabled}")
    private Boolean debugEnabled;

    @Value("${oes.debug.path}")
    private String debugPath;

    public String solveImage(String fileName) {
        LOG.info("solving image {}", fileName);

        Mat cleanedImage = getCleanedImage(fileName);
        debugCleanedImage(fileName, cleanedImage);


        List<Mat> letterImages = OpenCvUtil.toLetterImages(cleanedImage, 4);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < letterImages.size(); i++) {
            stringBuilder.append(letterRecognitionService.read(letterImages.get(i)));
            debugSingleLetterImage(fileName, letterImages, i);
        }

        String result = stringBuilder.toString();
        LOG.info("result was {}", result);
        return result;
    }

    private void debugSingleLetterImage(String originalImageName, List<Mat> letterImages, int letterIndex) {
        if (Boolean.TRUE.equals(debugEnabled)) {
            String cleanedLetterImageName = getDebugFile(originalImageName, ".letter." + letterIndex + ".png");
            LOG.debug("saving letter image {}", cleanedLetterImageName);
            OpenCvUtil.writeImage(cleanedLetterImageName, letterImages.get(letterIndex));
        }
    }

    private void debugCleanedImage(String originalImageName, Mat image) {
        if (Boolean.TRUE.equals(debugEnabled)) {
            String cleanedImageName = getDebugFile(originalImageName, ".cleaned.png");
            LOG.debug("saving cleaned image {}", cleanedImageName);
            OpenCvUtil.writeImage(cleanedImageName, image);
        }
    }

    private String getDebugFile(String originalImageName, String suffix) {
        return debugPath + "/" +
                        originalImageName.substring(originalImageName.lastIndexOf('/') + 1) + suffix;
    }

    private Mat getCleanedImage(String fileName) {
        Mat result = OpenCvUtil.readImage(fileName);
        result = OpenCvUtil.toGrayColorImage(result);
        result = OpenCvUtil.toBlurredImage(result, 3);
        result = OpenCvUtil.toThresholdImage(result, 100);
        result = OpenCvUtil.toBlurredImage(result, 4);
        result = OpenCvUtil.toThresholdImage(result, 200);
        return result;
    }

}
