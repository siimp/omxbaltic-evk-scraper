package ee.siimp.oes.recognition;

import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class RecognitionMethodConfig {

    private OcrLetterRecognitionService ocrLetterRecognitionService;

    private AiLetterRecognitionService aiLetterRecognitionService;

    @Bean(name = "letterRecognitionService")
    @ConditionalOnProperty(name = "oes.recognition.method", havingValue = "ocr")
    public LetterRecognitionService ocrLetterRecognitionService() {
        return ocrLetterRecognitionService;
    }

    @Bean(name = "letterRecognitionService")
    @ConditionalOnProperty(name = "oes.recognition.method", havingValue = "ai")
    public LetterRecognitionService aiLetterRecognitionService() {
        return aiLetterRecognitionService;
    }
}
