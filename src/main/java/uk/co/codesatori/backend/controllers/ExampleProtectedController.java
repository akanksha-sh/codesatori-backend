package uk.co.codesatori.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.codesatori.backend.security.SecurityService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/protected")
public class ExampleProtectedController {

  @Autowired
  SecurityService securityService;

  @GetMapping("/data")
  public Map<String, Object> getData() {
    String name=securityService.getUser().getName();
    Map<String, Object> response = new HashMap<String, Object>();
    response.put("msg", "Hello "+name+", you have accessed protected data from spring boot");
    return response;
  }
}