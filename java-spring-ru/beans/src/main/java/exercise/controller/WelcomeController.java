package exercise.controller;

import exercise.daytime.Day;
import exercise.daytime.Night;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

// BEGIN
@RestController
public class WelcomeController {
    @Autowired
    private Day day;

    @Autowired
    private Night night;

    @GetMapping("/welcome") // Список страниц
    public String index() {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        if (hour > 6 && hour <= 22) return "It is " + day.getName() + " now! Welcome to Spring!";
        return "It is " + night.getName() + " now! Welcome to Spring!";
    }
}
// END
