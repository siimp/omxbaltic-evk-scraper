package ee.siimp.oes.service;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.tesseract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import static org.bytedeco.javacpp.opencv_core.Mat;

@Service
public class OcrLetterRecognitionService implements LetterRecognitionService {

    private static final Logger LOG = LoggerFactory.getLogger(OcrLetterRecognitionService.class);

    private static final String CHAR_WHITELIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUWXYZ0123456789";

    @Value("classpath:tesseract")
    private Resource tesseractConfigPath;

    public String read(Mat image) {
        String result = null;
        try (tesseract.TessBaseAPI api = new tesseract.TessBaseAPI();) {
            if (api.Init(tesseractConfigPath.getFile().getAbsolutePath(), "eng") != 0) {
                LOG.error("Tesseract config not found in {}", tesseractConfigPath.getFile().getAbsolutePath());
                return null;
            }
            api.SetVariable("tessedit_char_whitelist", CHAR_WHITELIST);
            //api.SetVariable("language_model_penalty_non_dict_word", "0");
            //api.SetVariable("language_model_penalty_non_freq_dict_word", "0");

            api.SetImage(image.arrayData(), image.size().width(), image.size().height(), image.channels(), (int) image.step1());

            result = getText(api);
            api.End();
            return result;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return result;
    }

    private String getText(tesseract.TessBaseAPI api) {
        try (BytePointer outText = api.GetUTF8Text()) {
            return outText.getString().replaceAll(" ", "").trim();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return null;
    }
}
