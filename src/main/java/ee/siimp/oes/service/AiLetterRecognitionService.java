package ee.siimp.oes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static org.bytedeco.javacpp.opencv_core.Mat;

@Service
public class AiLetterRecognitionService implements LetterRecognitionService {

    private static final Logger LOG = LoggerFactory.getLogger(AiLetterRecognitionService.class);

    @Override
    public String read(Mat mat) {
        return null;
    }
}
