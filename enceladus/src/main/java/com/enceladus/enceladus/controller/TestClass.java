package com.enceladus.enceladus.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestClass {

    @GetMapping("/")
    public String greet(){
        return("Hello Enceladus");
    }

}
