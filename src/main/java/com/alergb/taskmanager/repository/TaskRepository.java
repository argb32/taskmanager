package com.alergb.taskmanager.repository;

import com.alergb.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    //SELECT * FROM tasks WHERE completed =:completed
    List<Task> findByCompleted(boolean completed);

    List<Task> findByTitleContainingIgnoreCase(String title);

    //Debemos usar Java Persistent Query language, el cual usa los nombres de las clases en vez de las tablas
    @Query("SELECT t FROM Task t WHERE t.completed = :completed")
    List<Task> findTasksByCompletedStatus(@Param("completed") boolean completed);

}
