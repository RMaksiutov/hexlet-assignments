package exercise.controller;

import org.junit.jupiter.api.Test;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import org.instancio.Instancio;
import org.instancio.Select;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.util.HashMap;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.datafaker.Faker;
import exercise.repository.TaskRepository;
import exercise.model.Task;

// BEGIN
@SpringBootTest
@AutoConfigureMockMvc
// END
class ApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;


    @Test
    public void testWelcomePage() throws Exception {
        var result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThat(body).contains("Welcome to Spring!");
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }


    private Task generateTask() {
        return Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply(Select.field(Task::getTitle), () -> faker.lorem().word())
                .supply(Select.field(Task::getDescription), () -> faker.lorem().paragraph())
                .create();
    }

    // BEGIN
    @Test
    public void testTask() throws Exception {
        var task1 = generateTask();
        var task2 = generateTask();
        taskRepository.save(task1);
        taskRepository.save(task2);

        var result1 = mockMvc.perform(get("/tasks/" + task1.getId()))
                .andExpect(status().isOk())
                .andReturn();
        var result2 = mockMvc.perform(get("/tasks/" + task2.getId()))
                .andExpect(status().isOk())
                .andReturn();

        var body1 = result1.getResponse().getContentAsString();
        var body2 = result2.getResponse().getContentAsString();

        taskRepository.deleteAll();

        assertThatJson(body1).and(
                a -> a.node("title").isEqualTo(task1.getTitle()),
                a -> a.node("description").isEqualTo(task1.getDescription())
        );
        assertThatJson(body2).and(
                a -> a.node("title").isEqualTo(task2.getTitle()),
                a -> a.node("description").isEqualTo(task2.getDescription())
        );
    }

    @Test
    public void testTaskCreate() throws Exception {
        var task = generateTask();

        var request = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(task));

        var result1 = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();

        var tasks = taskRepository.findAll();
        var taskToTest = tasks.get(tasks.size() - 1);
        taskRepository.deleteAll();
        assertThat(taskToTest.getTitle()).isEqualTo(task.getTitle());
        assertThat(taskToTest.getDescription()).isEqualTo(task.getDescription());
    }

    @Test
    public void testTaskUpdate() throws Exception {
        var task = generateTask();
        taskRepository.save(task);

        var data = new HashMap<>();
        data.put("title", "testTitle1");

        var request = put("/tasks/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                // ObjectMapper конвертирует Map в JSON
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        task = taskRepository.findById(task.getId()).get();
        assertThat(task.getTitle()).isEqualTo(("testTitle1"));
    }

    @Test
    public void testTaskDelete() throws Exception {
        var task = generateTask();
        taskRepository.save(task);
        var tasks = taskRepository.findAll();

        var request = delete("/tasks/" + tasks.get(tasks.size() - 1).getId());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var repSize = taskRepository.findAll().size();
        assertThat(repSize).isEqualTo(0);
    }
    // END
}
