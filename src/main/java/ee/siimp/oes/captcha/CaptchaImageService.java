package ee.siimp.oes.captcha;

import ee.siimp.oes.common.Config;
import ee.siimp.oes.common.utils.OpenCvUtil;
import lombok.AllArgsConstructor;
import org.bytedeco.javacpp.opencv_core;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

import static org.bytedeco.javacpp.opencv_core.Mat;


@Service
@AllArgsConstructor
public class CaptchaImageService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Config config;

    public CaptchaImage loadImage(String fileName) {
        LOG.info("loading image {}", fileName);
        Mat result = OpenCvUtil.readImage(fileName);
        return CaptchaImage.of(result, fileName);
    }

    public CaptchaImage cleanImage(CaptchaImage input) {
        LOG.info("cleaning image {}", input);
        Mat result = input.getImage();

        //TODO: try more times blur/thres but with smaller blur size
        result = OpenCvUtil.toGrayColorImage(result);
        result = OpenCvUtil.toBlurredImage(result, 3);
        result = OpenCvUtil.toThresholdImage(result, 100);
        result = OpenCvUtil.toBlurredImage(result, 4);
        result = OpenCvUtil.toThresholdImage(result, 200);
        result = OpenCvUtil.toMarginImage(result, 1);
        result = OpenCvUtil.removeArtifacts(result);

        //TODO: try mask approach - cleaned image would be mask for original (may draw black contours around letters to get better mask)
        saveDebugImage(input.getFileName(), "cleaned", result);
        return CaptchaImage.of(input, result);
    }

    public CaptchaImage findLetters(CaptchaImage input) {
        List<Mat> letterImages = OpenCvUtil.toLetterImages(input.getImage());
        debugSingleLetterImage(input, letterImages);
        return CaptchaImage.of(input, letterImages);
    }

    private void saveDebugImage(String originalImageName, String suffix, opencv_core.Mat image) {
        if (Boolean.TRUE.equals(config.getDebugEnabled())) {
            String cleanedImageName = getDebugFile(originalImageName, "." + suffix + ".png");
            LOG.debug("saving cleaned image {}", cleanedImageName);
            OpenCvUtil.writeImage(cleanedImageName, image);
        }
    }

    private String getDebugFile(String originalImageName, String suffix) {
        return config.getDebugPath() + "/" +
                originalImageName.substring(originalImageName.lastIndexOf('/') + 1) + suffix;
    }

    private void debugSingleLetterImage(CaptchaImage input, List<Mat> letters) {
        if (Boolean.TRUE.equals(config.getDebugEnabled())) {
            //Mat contoursImage = OpenCvUtil.drawDetectedContours(input.getImage(), letters);
            //saveDebugImage(input.getFileName(), "contours", contoursImage);
            for(int letterIndex = 0; letterIndex < letters.size(); letterIndex++) {
                String cleanedLetterImageName = getDebugFile(input.getFileName(), ".letter." + letterIndex + ".png");
                LOG.debug("saving letter image {}", cleanedLetterImageName);
                OpenCvUtil.writeImage(cleanedLetterImageName, letters.get(letterIndex));
            }
        }
    }

}
