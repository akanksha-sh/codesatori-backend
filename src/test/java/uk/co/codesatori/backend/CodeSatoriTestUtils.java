package uk.co.codesatori.backend;

import java.util.Arrays;
import java.util.UUID;
import uk.co.codesatori.backend.model.User;
import uk.co.codesatori.backend.model.User.Role;

public class CodeSatoriTestUtils {

  public static String FIREBASE_UUID_1 = "7dh28wud8b9s02DJWus9f7Wbd8S7";
  public static String FIREBASE_UUID_2 = "9sW7gb2hSkfWJD8uj3n583Shwb6G";

  public static UUID UUID_1 = UUID.nameUUIDFromBytes(FIREBASE_UUID_1.getBytes());
  public static UUID UUID_2 = UUID.nameUUIDFromBytes(FIREBASE_UUID_2.getBytes());

  public static UUID UUID_3 = UUID.fromString("031e376f-c89a-47b6-a380-8fc854d2b805");
  public static UUID UUID_4 = UUID.fromString("850426ff-f01c-425e-b9e9-d0dd77bd8a1f");

  public static String EMAIL_1 = "williamseliot@edu.uk";
  public static String EMAIL_2 = "grahammacleod@grh.com";

  public static User MR_WILLIAMS = new User(
      UUID_1,
      "Eliot",
      "Williams",
      Role.TEACHER.value(),
      EMAIL_1
  );

  public static User MR_MACLEOD = new User(
      UUID_2,
      "Graham",
      "Macleod",
      Role.TEACHER.value(),
      EMAIL_2
  );

  public static String codify(String... linesOfCode) {
    return Arrays.stream(linesOfCode)
        .reduce((s1, s2) -> s1 + "\n" + s2)
        .orElse("");
  }
}
