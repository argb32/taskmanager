package com.alergb.taskmanager.controller;

import com.alergb.taskmanager.dto.TaskRequestDto;
import com.alergb.taskmanager.dto.TaskResponseDto;
import com.alergb.taskmanager.entity.Task;
import com.alergb.taskmanager.mappers.TaskMapper;
import com.alergb.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(){
        return ResponseEntity.ok(taskMapper.toResponseListDto(taskService.getAllTasks()));
    }

    //Como en el service estamos trabajando con Optional debemos manejar el caso en el que no devuelve un task.
    //Para eso trabajamos con un map y orElse
    //Para enviar un atributo por la url añadimos path variable
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTaskById (@PathVariable Long id){
        return ResponseEntity.ok(taskMapper.toResponseDto(taskService.getTaskById(id)));

    }


    //Aunque aquí se devuelva una lista vacía, es bueno devolver un 200 OK, ya que simplemente no hay tareas completadas,
    //el 404 debe usarse para cosas concretas como una búsqueda por ID, por ejemplo.
    @GetMapping("/completed/{completed}")
    public ResponseEntity<List<TaskResponseDto>> findByCompleted(@PathVariable boolean completed){
        return ResponseEntity.ok(taskMapper.toResponseListDto(taskService.findByCompleted(completed)));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskResponseDto>> findByTitleContainingIgnoreCase(@RequestParam String title){

        return ResponseEntity.ok(taskMapper.toResponseListDto(taskService.findByTitleContainingIgnoreCase(title)));
    }

    //en los post necesitamos enviar algo, por lo tanto, es necesario un requestbody
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto task) {
        Task savedTask = taskService.createTask(taskMapper.toEntity(task));

        TaskResponseDto responseDto = taskMapper.toResponseDto(savedTask);
        //Estos dos return son equivalentes.
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
//        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask (@PathVariable Long id, @Valid @RequestBody TaskRequestDto updatedTaskDto) {
        Task updatedTask = taskMapper.toEntity(updatedTaskDto);

        TaskResponseDto responseTask=  taskMapper.toResponseDto(taskService.updateTask(id, updatedTask));

        return ResponseEntity.ok(responseTask);

    }

    //si hacemos un getById que puede devolver un optional hay que hacer el orElse para devolver el not found
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask (@PathVariable Long id){
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }



}
