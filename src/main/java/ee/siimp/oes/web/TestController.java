package ee.siimp.oes.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ee.siimp.oes.service.CaptchaService;


@RestController
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private CaptchaService captchaService;
    
    @GetMapping
    public String test(@RequestParam String name) throws IOException {
        return captchaService.solveImage("captchas/"+name+".png", "captchas/debug");
    }

}
