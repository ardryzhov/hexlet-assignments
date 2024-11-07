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


    // BEGIN
    @Test
    public void testGetTaskCorrect() throws Exception {
        Task expectedTask = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply((Select.field(Task::getTitle)), () -> faker.lorem().word())
                .supply((Select.field(Task::getDescription)), () -> faker.lorem().word())
                .create();

        taskRepository.save(expectedTask);

        var request = get("/tasks/" + expectedTask.getId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var returnedTask = taskRepository.findById(expectedTask.getId()).get();

        assertThat(expectedTask.getTitle()).isEqualTo(returnedTask.getTitle());
        assertThat(expectedTask.getDescription()).isEqualTo(returnedTask.getDescription());
    }

    @Test
    public void testCreateTaskCorrect() throws Exception {
        Task createdTask = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply((Select.field(Task::getTitle)), () -> faker.lorem().word())
                .supply((Select.field(Task::getDescription)), () -> faker.lorem().word())
                .create();

        var requst = post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(createdTask));

        mockMvc.perform(requst)
                .andExpect(status().isCreated());

        var result = taskRepository.save(createdTask);

        assertThat(createdTask.getTitle()).isEqualTo(result.getTitle());
        assertThat(createdTask.getDescription()).isEqualTo(result.getDescription());
    }

    @Test
    public void updateTask() throws Exception{
        Task task = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply((Select.field(Task::getTitle)), () -> faker.lorem().word())
                .supply((Select.field(Task::getDescription)), () -> faker.lorem().word())
                .create();

        taskRepository.save(task);

        task.setTitle("Hello");

        var request = put("/tasks/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(task));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var updatedTask = taskRepository.findById(task.getId());

        assertThat(task.getTitle()).isEqualTo(updatedTask.get().getTitle());
    }

    @Test
    public void deleteTask() throws Exception {
        Task task = Instancio.of(Task.class)
                .ignore(Select.field(Task::getId))
                .supply((Select.field(Task::getTitle)), () -> faker.lorem().word())
                .supply((Select.field(Task::getDescription)), () -> faker.lorem().word())
                .create();

        taskRepository.save(task);

        var request = delete("/tasks/" + task.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(task));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var result = taskRepository.findById(task.getId());

        assertThat(result.isEmpty()).isEqualTo(true);
    }
    // END
}
