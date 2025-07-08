package com.example.springboot_tutorial.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // APIのコントローラーであることを宣言している
public class HelloWorldController {

    @GetMapping("/hello")  // GETリクエストのURLパスを指定。このパスが来たらここのメソッドが実行される
    public String sayHello() {
        return "Hello, World!";
    }
}