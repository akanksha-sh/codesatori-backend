package uk.co.codesatori.backend.model;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static uk.co.codesatori.backend.CodeSatoriTestUtils.UUID_1;

import org.junit.Test;
import uk.co.codesatori.backend.CodeSatoriTestUtils;
import uk.co.codesatori.backend.model.Compiler.Options;
import uk.co.codesatori.backend.model.Compiler.Options.Language;

public class CompilerTest {

  public static String VALID_JAVA_PROGRAM = CodeSatoriTestUtils.codify(
      "public class Solution {",
      "public static void main(String[] args) {",
      "System.out.println(\"Hello World!\");",
      "}",
      "}"
  );

  public static String INVALID_JAVA_PROGRAM = CodeSatoriTestUtils.codify(
      "public class Solution {",
      "public static void main(String[] args) {",
      "System.out.println(\"Whoops - looks like I forgot a semicolon!\")",
      "}",
      "}"
  );

  @Test
  public void compilationSucceedsWhenGivenValidJavaProgram() {
    Compiler.Options compilerOptions = new Options(
        VALID_JAVA_PROGRAM,
        Language.JAVA.value()
    );
    Compiler.Output compilerOutput = compilerOptions.getCompilerOutputFor(UUID_1);
    assertThat(compilerOutput.isSuccess()).isTrue();
  }

  @Test
  public void compilationFailsWhenGivenInvalidJavaProgram() {
    Compiler.Options compilerOptions = new Options(
        INVALID_JAVA_PROGRAM,
        Language.JAVA.value()
    );
    Compiler.Output compilerOutput = compilerOptions.getCompilerOutputFor(UUID_1);
    assertThat(compilerOutput.isSuccess()).isFalse();
  }
}
