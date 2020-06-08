package uk.co.codesatori.backend;

import java.util.UUID;
import uk.co.codesatori.backend.model.Teacher;
import uk.co.codesatori.backend.model.User;

public class CodeSatoriTestUtils {

  public static UUID UUID_1 = UUID.fromString("449cefa5-47cd-4777-adbd-5653b051ef5a");
  public static UUID UUID_2 = UUID.fromString("33123be4-1423-483b-80ae-b558d04d6008");
  public static UUID UUID_3 = UUID.fromString("031e376f-c89a-47b6-a380-8fc854d2b805");
  public static UUID UUID_4 = UUID.fromString("850426ff-f01c-425e-b9e9-d0dd77bd8a1f");

  public static User MR_WILLIAMS = new Teacher(
      UUID_1,
      "Eliot"
  );

  public static User MR_MACLEOD = new Teacher(
      UUID_2,
      "Graham"
  );

}
