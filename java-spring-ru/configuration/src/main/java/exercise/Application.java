package exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import  org.springframework.beans.factory.annotation.Autowired;

import exercise.model.User;
import exercise.component.UserProperties;

@SpringBootApplication
@RestController
public class Application {

    // Все пользователи
    private List<User> users = Data.getUsers();

    // BEGIN
    @Autowired // Аннотация нужна для автоподстановки объекта
    private UserProperties adminInfo;
    @GetMapping("/admins")
    public List<String> adminIndex() {
        List<String> adminNames = new ArrayList<>();
        List<String> admins = adminInfo.getAdmins();
        for(User user : users) {
            for(String admin : admins) {
                if (admin.equals(user.getEmail())) adminNames.add(user.getName());
            }
        }
        return adminNames.stream().sorted().distinct().toList();
    }
    // END

    @GetMapping("/users")
    public List<User> index() {
        return users;
    }

    @GetMapping("/users/{id}")
    public Optional<User> show(@PathVariable Long id) {
        var user = users.stream()
            .filter(u -> u.getId() == id)
            .findFirst();
        return user;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
