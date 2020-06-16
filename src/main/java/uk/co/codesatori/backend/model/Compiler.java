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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.Getter;
import lombok.Setter;
import uk.co.codesatori.backend.model.Compiler.Options.Language;
import uk.co.codesatori.backend.model.Compiler.Output.Status;

public class Compiler {

  private static final String JAVA_SECURITY_POLICY = "grant {\n"
      + "}";
  private static final long JAVA_TIMEOUT = 15;

  private static class CompileAndRun {

    public static Output java(File source, Path workingDirectory) {
      /* Start sub-process to compile java code. */
      String compileSourceCodeCommand = String.format("javac %s", source.getAbsolutePath());
      Process compiler = execute(compileSourceCodeCommand);
      if (compiler == null) {
        return null;
      }

      /* Read compiler output into memory and wait for exit code before continuing. */
      InputStream errorStream = compiler.getErrorStream();
      String errorOutput;
      int exitCode;
      try {
        errorOutput = new String(errorStream.readAllBytes());
        exitCode = compiler.waitFor();
      } catch (IOException | InterruptedException e) {
        compiler.destroy();
        return null;
      }

      /* If compilation has failed, return an appropriate output. */
      if (exitCode != 0) {
        return new Output(Status.COMPILE_TIME_ERROR.value(), errorOutput);
      }

      /* Otherwise, setup environment required to run the compiled program.
       * Firstly, create a policy file to prevent malicious code from being run. */
      File javaSecurityPolicy = new File(workingDirectory.resolve("java.policy").toString());
      try {
        javaSecurityPolicy.createNewFile();
        /* Write the contents of the policy to the new file. */
        BufferedWriter writer = new BufferedWriter(new FileWriter(javaSecurityPolicy));
        writer.write(JAVA_SECURITY_POLICY);
        writer.flush();
        writer.close();
      } catch (IOException e) {
        return null;
      }

      /* Get the path to the binary file generated during compilation.
       * Then generate the command required to run the binary file as a program. */
      String binaryPath = workingDirectory.resolve(Language.JAVA.binary()).toString();
      String runSourceCodeCommand = String
          .format("java -Djava.security.manager -Djava.security.policy==%s -classpath %s %s",
              javaSecurityPolicy.getAbsolutePath(), workingDirectory.toString(),
              Language.JAVA.binary());

      /* Finally, run the binary file as part of a JVM secured by the security policy. */
      ExecutorService executor = Executors.newFixedThreadPool(1);
      /* This needs to be done in a separate execution thread so that non-terminal
       * programs can be timed-out. */
      Future<Output> output = executor.submit(new Callable<Output>() {
        @Override
        public Output call() throws Exception {
          Process secureJVM = execute(runSourceCodeCommand);
          if (secureJVM == null) {
            return null;
          }

          /* Read program outputs into memory and wait for exit code before continuing. */
          String consoleOutput;
          InputStream consoleStream = secureJVM.getInputStream();
          String errorOutput;
          InputStream errorStream = secureJVM.getErrorStream();
          int exitCode;
          try {
            consoleOutput = new String(consoleStream.readAllBytes());
            errorOutput = new String(errorStream.readAllBytes());
            exitCode = secureJVM.waitFor();
          } catch (IOException | InterruptedException e) {
            secureJVM.destroy();
            return null;
          }

          /* If program execution has failed, return an appropriate output. */
          if (exitCode != 0) {
            return new Output(Status.RUN_TIME_ERROR.value(), errorOutput);
          }

          /* Otherwise, return the program output. */
          return new Output(Status.SUCCESS.value(), consoleOutput);
        }
      });

      try {
        return output.get(JAVA_TIMEOUT, TimeUnit.SECONDS);
      } catch (InterruptedException | ExecutionException e) {
        return null;
      } catch (TimeoutException e) {
        return new Output(Status.BAD_REQUEST.value(),
            "Your program timed out. Are you sure it terminates?");
      }
    }

    private static Process execute(String command) {
      Process process = null;
      try {
        process = Runtime.getRuntime().exec(command);
      } catch (IOException e) {
        if (process != null) {
          process.destroy();
        }
        return null;
      }
      return process;
    }
  }

  @Getter
  @Setter
  public static class Options {

    private String code;
    private Integer languageIndex;

    public Options(String code, int languageIndex) {
      this.code = code;
      this.languageIndex = languageIndex;
    }

    public Options() {
    }

    private Options.Language language() {
      return Options.Language.of(languageIndex);
    }

    public Output compileAndRunCodeFor(UUID uuid) {
      File source;
      Path workingDirectory;
      try {
        /* Create a temporary source source for the code to be compiled. */
        workingDirectory = Files
            .createTempDirectory(String.format("Lang=%s_User=%s_Temp", language(), uuid));
        String sourcePath = workingDirectory.resolve(language().source()).toString();
        source = new File(sourcePath);

        /* Write the actual code into the temp source. */
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
          return CompileAndRun.java(source, workingDirectory);
      }

      /* Send out an error message if the language isn't supported. */
      return Output.languageUnsupported();
    }


    public enum Language {
      JAVA;

      public static Language of(int value) {
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


      public String source() {
        switch (this) {
          case JAVA:
            return "Solution.java";
        }
        throw new UnsupportedOperationException(
            "There is no source file name corresponding to " + this + ".");
      }


      public String binary() {
        switch (this) {
          case JAVA:
            return "Solution";
        }
        throw new UnsupportedOperationException(
            "There is no binary file name corresponding to " + this + ".");
      }

    }
  }

  public static class Output {

    private Integer statusCode;
    private String message;

    public Output(Integer statusCode, String message) {
      this.statusCode = statusCode;
      this.message = message;
    }

    public Output() {
    }

    public Integer getStatusCode() {
      return statusCode;
    }

    public Status getStatus() {
      return Status.of(statusCode);
    }

    public String getMessage() {
      return message;
    }

    public static Output languageUnsupported() {
      return new Output(Status.BAD_REQUEST.value(),
          "Whoops - looks like that language isn't supported!");
    }

    public enum Status {
      BAD_REQUEST,
      COMPILE_TIME_ERROR,
      RUN_TIME_ERROR,
      SUCCESS;

      public static Status of(int value) {
        List<Status> statuses = Arrays.asList(Status.values());
        for (Status status : statuses) {
          if (status.value() == value) {
            return status;
          }
        }
        throw new UnsupportedOperationException(
            "There is no status corresponding to " + value + ".");
      }

      public int value() {
        switch (this) {
          case SUCCESS:
            return 0;
          case RUN_TIME_ERROR:
            return 1;
          case COMPILE_TIME_ERROR:
            return 2;
          case BAD_REQUEST:
            return 3;
        }
        throw new UnsupportedOperationException("There is no value corresponding to " + this + ".");
      }
    }
  }
}
