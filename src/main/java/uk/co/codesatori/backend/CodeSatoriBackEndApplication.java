package uk.co.codesatori.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import uk.co.codesatori.backend.domain.Course;
import uk.co.codesatori.backend.domain.CourseRepository;
import uk.co.codesatori.backend.domain.User;
import uk.co.codesatori.backend.domain.UserRepository;


@SpringBootApplication
public class CodeSatoriBackEndApplication {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private UserRepository userRepository;

  public static void main(String[] args) {
    SpringApplication.run(CodeSatoriBackEndApplication.class, args);
  }

  // Save demo data after start -- TODO: remove after setting up PostgreSQL
  @Bean
  CommandLineRunner runner() {
    return args -> {
      //demo user: admin, pass: admin
      userRepository.save(new User("admin", new BCryptPasswordEncoder().encode("admin")));

      //demo classes
      courseRepository.save(new Course("Java for Beginners", "CO101", 1L));
      courseRepository.save(new Course("Advanced Haskell", "CO201", 2L));
    };
  }

}
