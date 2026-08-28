package com.alergb.taskmanager.service;

import com.alergb.taskmanager.entity.Task;
import com.alergb.taskmanager.entity.User;
import com.alergb.taskmanager.exeptions.TaskNotFoundException;
import com.alergb.taskmanager.repository.TaskRepository;
import com.alergb.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


//El decorador Transactional le indica a Spring que es una transacción y, por tanto, si algo
//no debe realizar la operación y realiza un rollback de los posibles cambios (que no se han realizado).
@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImp implements TaskService{

    private final UserService userService;
    private final TaskRepository taskRepository;

    public List<Task> getAllTasks(){
        return taskRepository.findAll();
    }

    public Task getTaskById (Long id){
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public List<Task> findByCompleted(boolean completed) {
        return taskRepository.findByCompleted(completed);
    }

    public Task createTask(Task task){
        return taskRepository.save(task);
    }

    public Task updateTask (Long id, Task updatedTask){

        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setCompleted(updatedTask.getCompleted());
                    task.setAssignedTo(updatedTask.getAssignedTo());

                    return taskRepository.save(task);

                }).orElseThrow(() -> new TaskNotFoundException(id));
    }

    public void deleteTask (Long id){
        taskRepository.delete(getTaskById(id));
    }

    public List<Task>  findByTitleContainingIgnoreCase (String title){
        return taskRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public void removeUser(Long userId, Task task) {
        User user = userService.findUserById(userId);

        task.getAssignedTo().remove(user);
        taskRepository.save(task);
    }

}
