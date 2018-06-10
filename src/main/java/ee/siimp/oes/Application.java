package ee.siimp.oes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	//TODO: refactor to domain layer project structure
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
