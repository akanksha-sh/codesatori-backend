package uk.co.codesatori.backend.model;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_1;

import org.junit.Test;
import uk.co.codesatori.backend.CodeSatoriTestUtils;
import uk.co.codesatori.backend.model.Compiler.Options;
import uk.co.codesatori.backend.model.Compiler.Options.Language;
import uk.co.codesatori.backend.model.Compiler.Output.Status;

public class CompilerTest {

  public static String VALID_JAVA_PROGRAM = CodeSatoriTestUtils.codify(
      "public class Solution {",
      "public static void main(String[] args) {",
      "System.out.print(\"Hello World!\");",
      "}",
      "}"
  );

  public static String INVALID_JAVA_PROGRAM = CodeSatoriTestUtils.codify(
      "public class Solution {",
      "public static void main(String[] args) {",
      "System.out.print(\"Whoops - looks like I forgot a semicolon!\")",
      "}",
      "}"
  );

  public static String FILESYSTEM_JAVA_PROGRAM = CodeSatoriTestUtils.codify(
      "import java.io.File;",
      "import java.io.IOException;",
      "public class Solution {",
      "public static void main(String[] args) {",
      "try {",
      "File.createTempFile(\"somePrefix\", \"someSuffix\");",
      "} catch (IOException e) {",
      "e.printStackTrace();",
      "}",
      "}",
      "}"
  );

  public static String TIMEOUT_JAVA_PROGRAM = CodeSatoriTestUtils.codify(
      "public class Solution {",
      "public static void main(String[] args) {",
      "while (true) {}",
      "}",
      "}"
  );

  @Test
  public void compilesAndRunsSuccessfullyWhenGivenValidJavaProgram() {
    Compiler.Options compilerOptions = new Options(
        VALID_JAVA_PROGRAM,
        Language.JAVA.value()
    );
    Compiler.Output compilerOutput = compilerOptions.compileAndRunCodeFor(UUID_1);
    assertThat(compilerOutput.getStatus()).isEqualTo(Status.SUCCESS);
    assertThat(compilerOutput.getMessage()).isEqualTo("Hello World!");
  }

  @Test
  public void compilationFailsWhenGivenInvalidJavaProgram() {
    Compiler.Options compilerOptions = new Options(
        INVALID_JAVA_PROGRAM,
        Language.JAVA.value()
    );
    Compiler.Output compilerOutput = compilerOptions.compileAndRunCodeFor(UUID_1);
    assertThat(compilerOutput.getStatus()).isEqualTo(Status.COMPILE_TIME_ERROR);
  }

  @Test
  public void throwsRuntimeErrorWhenJavaProgramTriesToAccessFileSystem() {
    Compiler.Options compilerOptions = new Options(
        FILESYSTEM_JAVA_PROGRAM,
        Language.JAVA.value()
    );
    Compiler.Output compilerOutput = compilerOptions.compileAndRunCodeFor(UUID_1);
    assertThat(compilerOutput.getStatus()).isEqualTo(Status.RUN_TIME_ERROR);
    assertThat(compilerOutput.getMessage())
        .contains("java.lang.SecurityException: Unable to create temporary file");
  }

  @Test
  public void compilationReturnsBadRequestAfterTimeout() {
    Compiler.Options compilerOptions = new Options(
        TIMEOUT_JAVA_PROGRAM,
        Language.JAVA.value()
    );
    Compiler.Output compilerOutput = compilerOptions.compileAndRunCodeFor(UUID_1);
    assertThat(compilerOutput.getStatus()).isEqualTo(Status.BAD_REQUEST);
    assertThat(compilerOutput.getMessage())
        .contains("timed out");
  }
}
