package ee.siimp.oes.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_core.Rect;


public class OpenCvUtil {

    private static final Logger LOG = LoggerFactory.getLogger(OpenCvUtil.class);
    private static final int LETTER_WIDTH = 10;

    private OpenCvUtil() {
    }

    public static Mat readImage(String fileName) {
        return opencv_imgcodecs.imread(fileName);
    }

    public static void writeImage(String fileName, Mat image) {
        opencv_imgcodecs.imwrite(fileName, image);
    }

    public static Mat toGrayColorImage(Mat image) {
        Mat result = new Mat();
        opencv_imgproc.cvtColor(image, result, opencv_imgproc.CV_BGR2GRAY);
        return result;
    }

    public static Mat toBlurredImage(Mat image, int size) {
        Mat result = new Mat();
        opencv_imgproc.blur(image, result, new opencv_core.Size(size, size));
        return result;
    }

    public static Mat toThresholdImage(Mat image, int threshold) {
        Mat result = new Mat();
        opencv_imgproc.threshold(image, result, threshold, 255, opencv_imgproc.CV_THRESH_BINARY);
        return result;
    }

    public static List<Mat> toLetterImages(Mat image, int lettersOnImage) {
        List<MatWithLocation> letters = new ArrayList<>();
        try (opencv_core.MatVector contours = new opencv_core.MatVector(); Mat hierarhy = new Mat();) {
            opencv_imgproc.findContours(image, contours, hierarhy, opencv_imgproc.CV_RETR_TREE, opencv_imgproc.CV_CHAIN_APPROX_SIMPLE);

            for (int i = 0; i < contours.size(); i++) {
                try (opencv_core.Rect r = opencv_imgproc.boundingRect(contours.get(i))) {
                    if (isFullImageContour(r, image) || !isPotensialLetter(r)) {
                        continue;
                    }
                    Mat letterImage = image.apply(r).adjustROI(5, 0, 0, 0);
                    letters.add(new MatWithLocation(letterImage, r.x()));
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return letters.stream()
                .sorted(Comparator.comparingInt(MatWithLocation::getX))
                .limit(lettersOnImage)
                .map(MatWithLocation::getMat)
                .collect(Collectors.toList());
    }

    private static boolean isPotensialLetter(Rect r) {
        return r.width() >= LETTER_WIDTH;
    }

    private static boolean isFullImageContour(opencv_core.Rect rect, Mat image) {
        return rect.size().width() == image.size().width();
    }


}

@Getter
@AllArgsConstructor
class MatWithLocation {
    private Mat mat;
    private int x;
}
