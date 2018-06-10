package ee.siimp.oes.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class Config {

    @Value("${oes.debug.enabled}")
    private Boolean debugEnabled;

    @Value("${oes.debug.path}")
    private String debugPath;
}
