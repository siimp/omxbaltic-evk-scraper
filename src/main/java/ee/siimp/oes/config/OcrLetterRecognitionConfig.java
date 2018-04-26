package ee.siimp.oes.config;

import ee.siimp.oes.service.LetterRecognitionService;
import ee.siimp.oes.service.TesseractService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class OcrLetterRecognitionConfig {

    private TesseractService tesseractService;

    @Bean
    public LetterRecognitionService letterRecognitionService() {
        return tesseractService;
    }
}
