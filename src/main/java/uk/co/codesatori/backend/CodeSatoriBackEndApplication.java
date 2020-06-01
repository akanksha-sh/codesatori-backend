package uk.co.codesatori.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import uk.co.codesatori.backend.repositories.ClassOfStudentsRepository;
import uk.co.codesatori.backend.repositories.UserRepository;


@SpringBootApplication
public class CodeSatoriBackEndApplication {

  @Autowired
  private ClassOfStudentsRepository classRepository;

  @Autowired
  private UserRepository userRepository;

  public static void main(String[] args) {
    SpringApplication.run(CodeSatoriBackEndApplication.class, args);
  }

  @Bean
  CommandLineRunner runner() {
    return args -> {
      /* TODO: put something here! */
    };
  }

}
