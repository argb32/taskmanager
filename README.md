*Dependencias del proyecto*
---
Spring initializer dependencies:
- Spring Web
- Lombok
- Spring Data JPA
- H2
- Validation

---
*JPA*
---
Al estar usando spring Data JPA no es necesario 
escribir código en el repositorio, esta dependencia lo hará
por nosotros. Se pueden aladir otras funciones pero usando
una sintaxis específicas.

https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html

Como por ejemplo:

```java
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    //SELECT * FROM tasks WHERE completed = :completed
    List<Task> findByCompleted(boolean completed);
    
    List<Task> findByTitleContainingIgnoreCase();
}
```

Por otro lado, para consultas complejas (aunque el ejemplo no lo es) tenemos
la siguiente manera de realizarlas.

```java
//Debemos usar Java Persistent Query language, el cual usa los nombres de las clases en vez de las tablas
    @Query("SELECT t FROM Task t WHERE t.completed = :completed")
    List<Task> findTasksByCompletedStatus(@Param("completed") boolean completed);
```

Esta consulta tendrá la misma respuesta que la anterior (findByCompleted).

-----
*Estructura*
---
Controlador -> Service -> Repositorio -> BBDD


- **Controlador**: peticiones HTTP, validar los inputs y devolver una respuesta. 
Se utilizan los DTO. Se reciben entidades desde la capa de servicio, aplicamos mapper
entidad -> DTO, se devuelven DTO. También se envian a la capa de servico entidades, 
por lo que se hará la transformación DTO -> Entity.
- **Servicio**: lógica de negocio. En esta capa se trabaja con las entidades.
- **Repositorio**: operaciones con la base de datos

Solo se expone las interfaces.
Podríamos trabajar con otro tipo de estructura en la cual el controlador se encarga solo de las 
peticiones HTTP y se deja el mapeo de las entidades a la capa de servicio, pero de esta manera 
se quedaría un servicio algo más grande
---
*ENTITIES*
---

Se utiliza @Entity para indicar que la clase es una entidad que Hibernate pueda utilizar y guardarla
en la base de datos. 
@Table es opcional, ya que Hibernate crea por defecto una tabla con el nombre
de la clase, pero es recomendable
@Id indica que prodpieda hará las funciones de clave primaria y
@GeneratedValue(strategy = GenerationType.IDENTITY) esto genera un ID
de manera aturomática en la base de datos al crear una nueva entidad.
También podemos añadir las restricciones que tendrá la base de datos con 
el decorador @Column
PrePersist hace que se ejecute este método en la creación del componente.
```java
@Entity //esta clase representa una entidad (pasa a ser una entidad que Hibernate puede mapear) y quiero guardarla en la base de datos
@Table(name= "tasks") // esto es opcional, ya que jpa genera una tabla y nombre por defecto que en este caso coincide con 'tasks' pero es recomendable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tasks {

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

    //PrePersist hace que se ejecute este método en la creación del componente
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }


}
```




---
*DTOS*
---
Con los DTOS controlamos la información que queremos mandar
y recibir en una petición. Para utilizarlos correctamente, 
necesitamos un mapper. Además, en el requestDto debemos manejar
las validaciones. 

Para controlar las validaciones automáticamente hay que hacer
uso de spring boot starter validation e indicar 
@Valid en el parámetro que queremos validar. 

Ej:

```java
@Data
public class TaskRequestDto {

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String description;

    @NotNull()
    private Boolean completed ;


}
```

```java

@PostMapping
public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto task) {
    Task savedTask = taskRepository.save(taskMapper.toEntity(task));

    TaskResponseDto responseDto = taskMapper.toResponseDto(savedTask);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
}


```

--- 
*MAPPERS*
---
En este proyecto se trabaja con mappers a mano

```java

@Component
public class TaskMapper {
public TaskResponseDto toResponseDto (Task task){
TaskResponseDto responseDto = new TaskResponseDto();

        responseDto.setId(task.getId());
        responseDto.setTitle(task.getTitle());
        responseDto.setDescription(task.getDescription());
        responseDto.setCompleted(task.getCompleted());
        responseDto.setCreatedAt(task.getCreatedAt());

        return responseDto;
    }

    public List<TaskResponseDto> toResponseListDto(List<Task> tasks){
        return tasks.stream().map(this::toResponseDto).toList();
    }
}

```

Pero podríamos utilizar la dependencia MapStruct
la cual lo gestiona automáticamente sin tener que escribir el código

---
*SWAGGER*
---
Necesitamos la siguiente dependencia:

```xml
<!-- Source: https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>3.1.0</version>
    <scope>compile</scope>
</dependency>
```

Y crear una clase de configuración (idealmente en un paquete de seguridad)
```java
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Task Manager")
                        .version("1.0")
                        .description("API RESTful for task managing")
                        .contact(new Contact()
                                .name("Alejandro Rojas")
                                .email("")
                                .url("placeholder.url")
                        )
                        .license( new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licesnses/LICENSE-2.0.html")
                        )
                );
    }

}
```

Después debemos habilitar las rutas si tenemos un SecurityFilterChain.

Url de swagger: http://localhost:8080/swagger-ui/index.html

Lo ideal aquí sería crear una clase para inicializar swagger y que aparezcan las peticiones en el 
orden CRUD correcto.

---

*CONFIGURACIÓN DEL H2 Y JPA*
---
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-h2console</artifactId>
</dependency>
```
```
#h2 configuration
spring.datasource.url=jdbc:h2:mem:taskdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

#jpa configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

#h2 console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```
--- 
http://localhost:8080/h2-console


**Exceptions**
---
Para manejar las excepciones, una vez creadas las clases 
debemos tener un exception handler. A este handler, debemos asignarle
el decorador @ControllerAdvice, es decir que esto se aplicará a todas las clases @Controller.

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
}
```
Dentro de esta clase se coloca el @ExceptionHandler() que recibirá como argumentos
la clase de la excepción que se quiera lanzar.
 ```java
@ExceptionHandler(TaskNotFoundException.class)
public ResponseEntity<Object> handleTaskNotFoundException(TaskNotFoundException exception){
//...
}
```
Y debemos crear el método que devolverá una respuesta con un Objeto, 
el cual debemos llenarlo con los parámetros convenientes utilizando
un Map. Este object será el body de la Response.
---
*CORS*
---
Para poder conectarnos a la api desde un navegador, necesitamos
configurar el cors. Necesitamos especificar un protocolo (http, https), el host
y el puerto de entrada. CORS no es un mecanismo de autenticación ni una regla que el servidor utilice para bloquear físicamente la conexión.

Es una política que implementa el navegador.

Para una solución sencilla, pero sin seguridad podemos utilizar algo como:
```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
```
Pero esto para una app real no tendría sentido, ya que no tiene ningún tupo de seguridad.

Para poder utilizar esto necesitamos añadirle una capa de seguridad con spring-security e incluso 
configurar el cors directamente dentro de spring security, por lo que dejar
mvc sería redundante e incluso una mala práctica.






