package site.keydeuk.store.domain.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test1")
    public String test(){
        System.out.println("check");
        return "test!!!";
    }
}
