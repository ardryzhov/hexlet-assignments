package exercise.controller;

import java.util.List;

import exercise.dto.TaskCreateDTO;
import exercise.dto.TaskDTO;
import exercise.dto.TaskUpdateDTO;
import exercise.mapper.TaskMapper;
import exercise.model.Task;
import exercise.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import exercise.exception.ResourceNotFoundException;
import exercise.repository.TaskRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    // BEGIN
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper mapper;

    @Autowired
    private UserRepository userRepository;


    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TaskDTO> findAll() {
        return taskRepository.findAll().stream()
                .map(mapper::modelToDto)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO findTask(@PathVariable Long id) {
        var entity = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        return mapper.modelToDto(entity);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@RequestBody TaskCreateDTO taskCreateDTO) {
        var entity = mapper.map(taskCreateDTO);
        taskRepository.save(entity);
        return mapper.modelToDto(entity);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO update(@PathVariable Long id, @RequestBody @Valid TaskUpdateDTO taskUpdateDTO) {
        var entity = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        var user = userRepository.findById(taskUpdateDTO.getAssigneeId())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        entity.setAssignee(user);

        mapper.update(taskUpdateDTO, entity);
        taskRepository.save(entity);

        return mapper.modelToDto(entity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskRepository.deleteById(id);
    }
    // END
}
