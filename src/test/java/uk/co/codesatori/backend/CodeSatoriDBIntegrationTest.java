package uk.co.codesatori.backend;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.MR_WILLIAMS;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import java.util.UUID;
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


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CodeSatoriBackEndApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CodeSatoriDBIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Test
  public void postingUserToDBCreatesNewEntry() throws Exception {
    mockMvc.perform(post("/user_details")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(MR_WILLIAMS)))
        .andExpect(status().isOk());

    Optional<User> user = userRepository.findByEmail(MR_WILLIAMS.getEmail());
    assertThat(user.isPresent());
  }
}


