package com.example.controller;

import com.example.dto.UserSignupRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/user")
@RestController
public class UserController {
  public UserController() {
  }

  @PostMapping("")
  public void signUp(@Valid @RequestBody UserSignupRequest userSignupRequest) throws Exception {
      System.out.println(userSignupRequest.getUserEmail());

  }


}
