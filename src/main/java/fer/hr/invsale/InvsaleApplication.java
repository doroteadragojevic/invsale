package fer.hr.invsale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InvsaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvsaleApplication.class, args);
	}

}
