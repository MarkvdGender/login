package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("user")
@RestController
public class UserRestController {
    private static UserService service;

    public UserRestController(){
        service = UserService.getInstance();
    }

    @PostMapping("")
    public void save(@RequestBody User u){
        service.register(u);
    }

    @PostMapping("login")
    public String login(@RequestBody User u){
        if(service.login(u)){
            return service.createToken(u);
        }else{
            return "ongeldige combinatie";
        }

    }

    @PostMapping("{jwt}")
    public String auth(@PathVariable String jwt){
        return service.authenticate(jwt);
    }

    @GetMapping("leak")
    public List<User> leak(){
        return service.leak();
    }

}
