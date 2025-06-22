package cc.kertaskerja.manrisk_fraud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableJpaAuditing
public class ProjectManajemenRisikoKinerjaFraudApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectManajemenRisikoKinerjaFraudApplication.class, args);
	}

}
