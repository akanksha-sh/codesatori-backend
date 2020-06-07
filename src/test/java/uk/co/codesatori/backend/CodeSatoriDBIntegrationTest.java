package uk.co.codesatori.backend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.codesatori.backend.model.Teacher;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.repositories.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes= CodeSatoriBackEndApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CodeSatoriDBIntegrationTest {

  private static UUID UUID_1 = UUID.fromString("449cefa5-47cd-4777-adbd-5653b051ef5a");
  private static UUID UUID_2 = UUID.fromString("33123be4-1423-483b-80ae-b558d04d6008");

  private static User MR_WILLIAMS = new Teacher(
      UUID_1,
      "mrwilliams",
      "password1"
  );

  private static User MR_MACLEOD = new Teacher(
      UUID_2,
      "mrmacleod",
      "password2"
  );

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void testFindAll() {
    Iterable<User> users = userRepository.findAll();
    assertFalse(users.iterator().hasNext());
  }

//  @Test
//  public void addUser() throws Exception {
//    mockMvc.perform(post("/user")
//        .contentType("application/json")
//        .content(objectMapper.writeValue(
//            MR_WILLIAMS);))
//        .andExpect(status().isOk());
//
//    Optional<User> user = userRepository.findByUsername("mrwilliams");
//   // assertThat(user.isEmpty());
//  }
}


