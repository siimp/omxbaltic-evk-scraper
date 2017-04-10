package ee.siimp.oes.utils;

import org.bytedeco.javacpp.opencv_core;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_core.Rect;
import org.bytedeco.javacpp.opencv_core.Scalar;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_imgcodecs;
import org.bytedeco.javacpp.opencv_imgproc;

public class OpenCvUtil {
    
    public static final Scalar WHITE = new Scalar(org.bytedeco.javacpp.helper.opencv_core.CV_RGB(255, 255, 255));
    public static final Scalar RED = new Scalar(org.bytedeco.javacpp.helper.opencv_core.CV_RGB(255, 0, 0));
    public static final Scalar GREEN = new Scalar(org.bytedeco.javacpp.helper.opencv_core.CV_RGB(0, 255, 0));

    private static final int MIN_LETTER_WIDTH = 4;
    private static final int MAX_LETTER_WIDTH = 40;
    private static final int MIN_LETTER_HEIGHT = 10;
    private static final int MAX_LETTER_HEIGHT = 50;
    private static final int MIN_POINT_SIZE = 3;
    private static final int MAX_POINT_SIZE = 6;
    
    public static Mat readImageFromFile(String fileName) {
        return opencv_imgcodecs.imread(fileName);
    }

    public static Mat toGrayImage(Mat image) {
        Mat grayImage = new Mat();
        opencv_imgproc.cvtColor(image, grayImage, opencv_imgproc.CV_BGR2GRAY);
        return grayImage;
    }

    public static Mat toThresholdImage(Mat image, int threshold, int algorithm) {
        Mat thresholdImage = new Mat();
        opencv_imgproc.threshold(image, thresholdImage, threshold, 255, algorithm);
        return thresholdImage;
    }



    public static Mat createContourMask(Mat srcImage, MatVector contours, Mat debugImage) {
        Mat maskImage = Mat.zeros(srcImage.size(), opencv_core.CV_8UC1).asMat();

        for (int i = 0; i < contours.size(); i++) {
            try(Rect r = opencv_imgproc.boundingRect(contours.get(i))) {
                if (isLetterContour(r) || isDot(r)) { 
                    opencv_imgproc.drawContours(maskImage, contours, i, WHITE, 
                            opencv_imgproc.CV_FILLED, opencv_core.LINE_8, null, Integer.MAX_VALUE, null);
                    if (debugImage != null) {
                        opencv_imgproc.drawContours(debugImage, contours, i, RED);
                    }
                } else if (debugImage != null) {
                    opencv_imgproc.drawContours(debugImage, contours, i, GREEN);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        return maskImage;
    }

    private static boolean isDot(Rect r) {
        return r.height() < MAX_POINT_SIZE && r.height() > MIN_POINT_SIZE && r.width() < MAX_POINT_SIZE && r.width() > MIN_POINT_SIZE;
    }

    private static boolean isLetterContour(Rect r) {
        return r.height() < MAX_LETTER_HEIGHT && r.height() > MIN_LETTER_HEIGHT && r.width() < MAX_LETTER_WIDTH && r.width() > MIN_LETTER_WIDTH;
    }

    public static Mat createContourMask(Mat image, MatVector contours) {
        return createContourMask(image, contours, null);
    }
    

    public static void applyMask(Mat image, Mat cleanedImage, Mat maskImage) {
        opencv_core.bitwise_and(image, image, cleanedImage, maskImage);
    }

    public static void writeImage(String fileName, Mat image) {
        opencv_imgcodecs.imwrite(fileName, image);
    }

    @SuppressWarnings("resource")
    public static Mat toBlurredImage(Mat image, int size) {
        Mat blurredImage = new Mat();
        opencv_imgproc.blur(image, blurredImage, new Size(size, size));
        return blurredImage;
    }
    
    @SuppressWarnings("resource")
    public static Mat toGaussianBlurredImage(Mat image, int size, double sigmaX) {
        Mat blurredImage = new Mat();
        opencv_imgproc.GaussianBlur(image, blurredImage, new Size(size, size), sigmaX);
        return blurredImage;
    }

    public static Mat toMedianBlurredImage(Mat image, int size) {
        Mat blurredImage = new Mat();
        opencv_imgproc.medianBlur(image, blurredImage, size);
        return blurredImage;
    }

}
