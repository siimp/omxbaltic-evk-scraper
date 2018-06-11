package ee.siimp.oes.recognition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;

import static org.bytedeco.javacpp.opencv_core.Mat;

@Service
public class AiLetterRecognitionService implements LetterRecognitionService {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public String read(Mat mat) {
        return null;
    }
}
