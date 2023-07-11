package com.example.demo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.concurrent.TimeUnit;

@Controller
public class CalculatingController {

    @GetMapping("/calculating")
    public RedirectView redirect() throws InterruptedException {
        // Delay for 3 seconds
        TimeUnit.SECONDS.sleep(3);
        return new RedirectView("/options");
    }

    @GetMapping("/options")
    public String showOptionsPage() {
        return "options";
    }
}


