package jpabook.jpashop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tools.jackson.datatype.hibernate7.Hibernate7Module;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpashopApplication.class, args);
	}

	// Hibernate7Module 빈 등록
	@Bean
    Hibernate7Module hibernate7Module() {
		Hibernate7Module hibernate7Module = new Hibernate7Module();
		// LAZY 프록시 강제 초기화 off
		hibernate7Module.configure(Hibernate7Module.Feature.FORCE_LAZY_LOADING, false);
		return hibernate7Module;
	}

}