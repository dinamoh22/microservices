package org.exampl;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AboutMeController {

    @GetMapping("/aboutme")
    public String aboutme() {

        return "hello";
    }

}
