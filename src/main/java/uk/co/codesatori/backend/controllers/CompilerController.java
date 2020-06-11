package uk.co.codesatori.backend.controllers;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import uk.co.codesatori.backend.model.Compiler;
import uk.co.codesatori.backend.security.SecurityService;

@RestController
public class CompilerController {

  @Autowired
  private SecurityService securityService;

  @GetMapping("/code/compile")
  public Compiler.Output compile(@RequestBody Compiler.Options compilerOptions) {
    /* Makes an initial check to see that a valid user is making the request.
     * If not, throw an error.*/
    UUID uuid = securityService.getCurrentUUID();
    if (uuid == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No such user.");
    }

    /* Now, attempt to compile the code.
     * If unsuccessful, throw an error.*/
    Compiler.Output compilerOutput = compilerOptions.getCompilerOutputFor(uuid);
    if (compilerOutput == null) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
          "Sorry, something went wrong on our end!");
    }

    /* Otherwise - hooray! Return the results of compilation. */
    return compilerOutput;
  }
}
