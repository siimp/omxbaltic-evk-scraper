package ee.siimp.oes.captcha;

import lombok.Getter;

import java.util.List;

import static org.bytedeco.javacpp.opencv_core.Mat;

@Getter
public class CaptchaImage {

    private Mat image;
    private String fileName;
    private List<Mat> letterImages;

    @Override
    public String toString() {
        return fileName;
    }

    public static CaptchaImage of(Mat image, String fileName) {
        CaptchaImage captchaImage = new CaptchaImage();
        captchaImage.image = image;
        captchaImage.fileName = fileName;
        return captchaImage;
    }

    public static CaptchaImage of(CaptchaImage image, Mat manipulatedImage) {
        return of(manipulatedImage, image.getFileName());
    }

    public static CaptchaImage of(CaptchaImage image, List<Mat> letterImages) {
        CaptchaImage captchaImage = of(image.getImage(), image.getFileName());
        captchaImage.letterImages = letterImages;
        return captchaImage;
    }

    public Mat getImage() {
        return image;
    }

    public List<Mat> getLetterImages() {
        return letterImages;
    }
}
