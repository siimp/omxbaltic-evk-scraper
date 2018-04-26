package ee.siimp.oes.service;

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

    public String solveImage(String fileName) {
        LOG.info("solving image {}", fileName);

        Mat cleanedImage = getCleanedImage(fileName);
        List<Mat> letterImages = OpenCvUtil.toLetterImages(cleanedImage, 4);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < letterImages.size(); i++) {
            stringBuilder.append(letterRecognitionService.readSingleLetter(letterImages.get(i)));
        }

        String result = stringBuilder.toString();
        LOG.info("result was {}", result);
        return result;
    }

    private Mat getCleanedImage(String fileName) {
        Mat result = OpenCvUtil.readImage(fileName);
        result = OpenCvUtil.toGrayColorImage(result);
        result = OpenCvUtil.toBlurredImage(result, 4);
        result = OpenCvUtil.toThresholdImage(result, 100);
        result = OpenCvUtil.toBlurredImage(result, 3);
        result = OpenCvUtil.toThresholdImage(result, 200);
        return result;
    }

}
