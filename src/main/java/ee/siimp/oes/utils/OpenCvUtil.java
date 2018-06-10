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

import static org.bytedeco.javacpp.helper.opencv_core.CV_RGB;
import static org.bytedeco.javacpp.opencv_core.Mat;
import static org.bytedeco.javacpp.opencv_core.Rect;


public class OpenCvUtil {

    private static final Logger LOG = LoggerFactory.getLogger(OpenCvUtil.class);

    //private static final opencv_core.Scalar RED = new opencv_core.Scalar(CV_RGB(255, 0, 0));
    private static final opencv_core.Scalar WHITE = new opencv_core.Scalar(CV_RGB(255, 255, 255));
    private static final int MIN_LETTER_WIDTH = 10;
    private static final int MAX_LETTER_WIDTH = 26;
    private static final int MIN_LETTER_HEIGHT = 20;
    //for i and j dots
    private static final int LETTER_TOP_PADDING = 6;

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

    public static List<Mat> toLetterImages(Mat image) {
        List<MatWithLocation> letters = new ArrayList<>();
        try (opencv_core.MatVector contours = new opencv_core.MatVector(); Mat hierarchy = new Mat();) {
            opencv_imgproc.findContours(image, contours, hierarchy, opencv_imgproc.CV_RETR_TREE, opencv_imgproc.CV_CHAIN_APPROX_SIMPLE);

            for (int i = 0; i < contours.size(); i++) {
                try (opencv_core.Rect r = opencv_imgproc.boundingRect(contours.get(i))) {

                    if (isFullImageContour(r, image)) {
                        continue;
                    }

                    LOG.debug("contour w={}, h={}, isPotensialLetter={}, isDoubleLetter={}", r.width(), r.height(),
                            isPotensialLetter(r), isDoubleLetter(r));


                    if (isPotensialLetter(r) && isDoubleLetter(r)) {
                        addDoubleLetter(image, letters, r);
                    } else if (isPotensialLetter(r)) {
                        addSingleLetter(image, letters, r);
                    }
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return letters.stream()
                .sorted(Comparator.comparingInt(MatWithLocation::getX))
                //.limit(lettersOnImage)
                .map(MatWithLocation::getMat)
                .collect(Collectors.toList());
    }

    private static void addSingleLetter(Mat image, List<MatWithLocation> letters, Rect r) {
        Mat letterImage = image.apply(r).adjustROI(LETTER_TOP_PADDING, 0, 0, 0);
        letters.add(new MatWithLocation(letterImage, r.x()));
    }

    private static void addDoubleLetter(Mat image, List<MatWithLocation> letters, Rect r) {
        int singleLetterWidth = r.width() / 2;
        Mat firstLetterImage = image.apply(r).adjustROI(LETTER_TOP_PADDING, 0, 0, -singleLetterWidth);
        letters.add(new MatWithLocation(firstLetterImage, r.x()));
        Mat secondLetterImage = image.apply(r).adjustROI(LETTER_TOP_PADDING, 0, -singleLetterWidth, 0);
        letters.add(new MatWithLocation(secondLetterImage, r.x() + singleLetterWidth));
    }

    private static boolean isDoubleLetter(Rect r) {
        return r.width() > MAX_LETTER_WIDTH;
    }

    private static boolean isPotensialLetter(Rect r) {
        return r.width() >= MIN_LETTER_WIDTH && r.height() >= MIN_LETTER_HEIGHT;
    }

    private static boolean isFullImageContour(opencv_core.Rect rect, Mat image) {
        return rect.size().width() == image.size().width();
    }

    public static Mat removeArtifacts(Mat image) {
        try (opencv_core.MatVector contours = new opencv_core.MatVector(); Mat hierarchy = new Mat();) {
            opencv_imgproc.findContours(image, contours, hierarchy, opencv_imgproc.CV_RETR_TREE, opencv_imgproc.CV_CHAIN_APPROX_SIMPLE);

            for (int i = 0; i < contours.size(); i++) {
                try (opencv_core.Rect r = opencv_imgproc.boundingRect(contours.get(i))) {
                    if (isFullImageContour(r, image)) {
                        continue;
                    }

                    if (isArtifact(r)) {
                        opencv_imgproc.drawContours(image, contours, i, WHITE,
                                opencv_imgproc.CV_FILLED, opencv_core.LINE_8, null, Integer.MAX_VALUE, null);
                    }
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return image;
    }

    private static boolean isArtifact(Rect r) {
        return r.width() < MIN_LETTER_WIDTH || r.height() < MIN_LETTER_HEIGHT ||
                //r.width() > 2 * MAX_LETTER_WIDTH ||
                (r.width() > MAX_LETTER_WIDTH && r.height() < MIN_LETTER_HEIGHT);
    }
}

@Getter
@AllArgsConstructor
class MatWithLocation {
    private Mat mat;
    private int x;
}
