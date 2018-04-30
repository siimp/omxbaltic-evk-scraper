package ee.siimp.oes.config;

import ee.siimp.oes.service.LetterRecognitionService;
import ee.siimp.oes.service.OcrLetterRecognitionService;
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
