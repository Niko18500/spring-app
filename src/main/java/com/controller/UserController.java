package com.controller;

import com.model.*;
import com.repository.UserRepository;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final UserRepository userRepo = UserRepository.get();

    @PostMapping("/register")
    public Response register(@RequestBody RegisterRequest data) {
        log.info(data.toString());

        var alreadyExists = userRepo.findByUsername(data.getUsername()).isPresent();

        if (alreadyExists) {
            return Response.bad("Username already in use!");
        } else {
            userRepo.addUser(data.getUsername(), data.getPassword());
            return Response.good();
        }
    }

    @PostMapping("/updateUsername")
    public Response updateUsername(@RequestBody UpdateUsernameRequest data) {
        log.info(data.toString());

        var maybeUser = userRepo.findByID(data.getId());

        if (maybeUser.isPresent()) {
            // NOTE: update username here
            return Response.good();
        } else {
            return Response.bad("Wrong ID!");
        }
    }

    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest data) {
        log.info(data.toString());

        var maybeUser = userRepo.findByUsername(data.getUsername());

        if (maybeUser.isPresent()) {
            var user = maybeUser.get();

            if (user.getPassword().equals(data.getPassword())) {
                return new LoginResponse(user.getId());
            } else {
                return Response.bad("Wrong password!");
            }

        } else {
            return Response.bad("Wrong username!");
        }
    }

    @GetMapping("/count")
    public long count() {
        return userRepo.count();
    }
}
