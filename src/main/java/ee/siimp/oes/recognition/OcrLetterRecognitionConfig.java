package ee.siimp.oes.recognition;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class OcrLetterRecognitionConfig {

    private OcrLetterRecognitionService ocrLetterRecognitionService;

    @Bean
    public LetterRecognitionService letterRecognitionService() {
        return ocrLetterRecognitionService;
    }
}
