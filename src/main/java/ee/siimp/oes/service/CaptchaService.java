package ee.siimp.oes.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;
import org.bytedeco.javacpp.opencv_imgproc;
import org.bytedeco.javacpp.tesseract.TessBaseAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ee.siimp.oes.utils.OpenCvUtil;

@Service
public class CaptchaService {
    
    private static final Logger log = LoggerFactory.getLogger(CaptchaService.class);
    
    @Value("classpath:tesseract")
    private Resource tesseractConfigPath;
    
    private static final int MAX_THRESHOLD = 180;
    private static final int MIN_THRESHOLD = 90;
    private static final String CHAR_WHITELIST = "abcdefghijklmnopqrstuvwxyz0123456789";

    public String solveImage(String fileName) {
        return solveImage(fileName, null);
    }
    
    @SuppressWarnings("resource")
    public String solveImage(String fileName, String  debugDirectory) {    
        boolean debug = isDebug(debugDirectory);
        
        
        Mat srcImage = OpenCvUtil.readImageFromFile(fileName);
        Mat grayImage = OpenCvUtil.toGrayImage(srcImage);
        
        String result = "";
        for (int threshold = MAX_THRESHOLD; threshold > MIN_THRESHOLD && result.length() != 4; threshold-=10) {
            MatVector contours = new MatVector();
            Mat hierarhy = new Mat();
            
            Mat resultImage = clearNoise(grayImage, threshold);
            opencv_imgproc.findContours(resultImage, contours, hierarhy, opencv_imgproc.CV_RETR_TREE, opencv_imgproc.CV_CHAIN_APPROX_SIMPLE);
            
            Mat cleanedImage = srcImage.clone();
            cleanedImage.setTo(new Mat(OpenCvUtil.WHITE));
            
            Mat maskImage = null;
            if (debug) {
                Mat detectedLetters = srcImage.clone();
                maskImage = OpenCvUtil.createContourMask(srcImage, contours, detectedLetters);
                OpenCvUtil.writeImage(getDebugImageFile(fileName, debugDirectory, "detected"), detectedLetters);
            } else {
                maskImage = OpenCvUtil.createContourMask(srcImage, contours);
            }
            
            OpenCvUtil.applyMask(srcImage, cleanedImage, maskImage);
            //cleanedImage = clearNoise(cleanedImage, threshold);
            if (debug) {
                OpenCvUtil.writeImage(getDebugImageFile(fileName, debugDirectory, "cleaned"), cleanedImage);
            }
            
            try {
                result = extractTextWithTesseract(cleanedImage);
                log.info(String.format("trying  %s [thres=%d] -> %s", fileName, threshold, result));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (result.length() == 4) {
                if (debug) {
                    OpenCvUtil.writeImage(getDebugImageFile(fileName, debugDirectory, "result"), cleanedImage);
                }
                log.info(String.format("ocr result is %s", result));
            }
        }

        return result;
    }

    private static String getDebugImageFile(String fileName, String debugDirectory, String fileNameSuffix) {
        String file = fileName.substring(fileName.lastIndexOf('/'), fileName.lastIndexOf('.'));
        String extension = fileName.substring(fileName.lastIndexOf('.'));
        Path path = Paths.get(debugDirectory + "/" + file + "_" + fileNameSuffix + extension);
        return path.toAbsolutePath().toString();
    }

    private static boolean isDebug(String debugDirectory) {
        boolean debug = false;
        if (!StringUtils.isEmpty(debugDirectory)) {
            Path debugPath = Paths.get(debugDirectory);
            try {
                debugPath = Files.createDirectories(debugPath);
                debug = Files.isWritable(debugPath);
                if (debug) {
                    log.info(String.format("debug path is %s", debugPath.toAbsolutePath().toString()));
                }
            } catch (IOException e) {
                debug = false;
                log.error(e.getMessage());
            }
        }
        return debug;
    }


    
    private String extractTextWithTesseract(Mat imageMat) throws IOException {
        String result = null;
        
        TessBaseAPI api = new TessBaseAPI();
        if (api.Init(tesseractConfigPath.getURI().getPath(), "eng") != 0) {
            throw new RuntimeException("Tesseract config not found");
        }
        api.SetVariable("tessedit_char_whitelist", CHAR_WHITELIST);
        api.SetVariable("language_model_penalty_non_dict_word", "0");
        api.SetVariable("language_model_penalty_non_freq_dict_word", "0");
        

        api.SetImage(imageMat.arrayData(), imageMat.size().width(), imageMat.size().height(), imageMat.channels(), (int) imageMat.step1());
       
        try (BytePointer outText = api.GetUTF8Text()){
            result = outText.getString().replaceAll(" ", "").trim();
            api.End();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            api.End();
        } 
        return null;

    }
    

    @SuppressWarnings("resource")
    private static Mat clearNoise(Mat cleanedImage, int threshold) {
        Mat result =  OpenCvUtil.toBlurredImage(cleanedImage, 3);
        result = OpenCvUtil.toThresholdImage(result, threshold, opencv_imgproc.CV_THRESH_BINARY);
        return result;
    }

}
