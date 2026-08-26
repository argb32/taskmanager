package com.alergb.taskmanager.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity //esta clase representa una entidad (pasa a ser una entidad que Hibernate puede mapear) y quiero guardarla en la base de datos
@Table(name= "tasks") // esto es opcional, ya que jpa genera una tabla y nombre por defecto que en este caso coincide con 'tasks' pero es recomendable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Boolean completed = false;

    //Al tener un nombre con dos palabras lo definimos manualmente para seguir la estrategia correcta.
    //Además, no queremos que se pueda modificar una vez creado
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "assigned_to",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id")
    )
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonIgnore
    private Set<User> assignedTo = new HashSet<>();

    //PrePersist hace que se ejecute este método en la creación del componente
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
