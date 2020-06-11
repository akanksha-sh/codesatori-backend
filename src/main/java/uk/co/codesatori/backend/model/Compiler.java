package uk.co.codesatori.backend.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Compiler {

  private static class Compile {

    public static Output java(File source) {
      /* Start sub-process to compile java code. */
      Process compiler = null;
      try {
        compiler = Runtime.getRuntime().exec(
            String.format("javac %s", source.getAbsolutePath()));
      } catch (IOException e) {
        return null;
      }

      /* Read compiler output into memory and wait for exit code before continuing. */
      InputStream consoleStream = compiler.getInputStream();
      InputStream errorStream = compiler.getErrorStream();
      String consoleOutput;
      String errorOutput;
      int exitCode;
      try {
        consoleOutput = new String(consoleStream.readAllBytes());
        errorOutput = new String(errorStream.readAllBytes());
        exitCode = compiler.waitFor();
      } catch (IOException | InterruptedException e) {
        compiler.destroy();
        return null;
      }

      /* Extract the necessary information from the process and return. */
      if (exitCode == 0) {
        return new Output(true, consoleOutput);
      } else {
        return new Output(false, errorOutput);
      }
    }
  }

  public static class Options {

    private String code;
    private int languageValue;

    public Options(String code, int languageValue) {
      this.code = code;
      this.languageValue = languageValue;
    }

    public Options() {
    }

    private Options.Language language() {
      return Options.Language.of(languageValue);
    }

    public Output getCompilerOutputFor(UUID uuid) {
      File source;
      try {
        /* Create a temporary source file for the code to be compiled. */
        Path temporaryDirectory = Files.createTempDirectory(String.format("Lang=%s_User=%s", language(), uuid));
        String sourcePath = temporaryDirectory.resolve(language().file()).toString();
        source = new File(sourcePath);

        /* Write the actual code into the temp file. */
        BufferedWriter writer = new BufferedWriter(new FileWriter(source));
        writer.write(code);
        writer.flush();
        writer.close();
      } catch (IOException e) {
        return null;
      }

      /* Compile source code based on the language. */
      switch (language()) {
        case JAVA:
          return Compile.java(source);
      }

      /* Send out an error message if the language isn't supported. */
      return Output.languageUnsupported();
    }


    public enum Language {
      JAVA;

      public static Options.Language of(int value) {
        List<Language> languages = Arrays.asList(Language.values());
        for (Language language : languages) {
          if (language.value() == value) {
            return language;
          }
        }
        throw new UnsupportedOperationException(
            "There is no language corresponding to " + value + ".");
      }

      public int value() {
        switch (this) {
          case JAVA:
            return 0;
        }
        throw new UnsupportedOperationException("There is no value corresponding to " + this + ".");
      }


      public String file() {
        switch (this) {
          case JAVA:
            return "Solution.java";
        }
        throw new UnsupportedOperationException(
            "There is no file extension corresponding to " + this + ".");
      }

    }
  }

  public static class Output {

    private boolean success;
    private String message;

    public Output(boolean success, String message) {
      this.success = success;
      this.message = message;
    }

    public Output() {
    }

    public boolean isSuccess() {
      return success;
    }

    public String getMessage() {
      return message;
    }

    public static Output languageUnsupported() {
      return new Output(false, "Whoops - looks like that language isn't supported yet!");
    }
  }
}
