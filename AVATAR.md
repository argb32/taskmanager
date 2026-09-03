Avatar

Sí, puedes diseñarlo con una entidad Avatar separada como has hecho. De hecho, para tu proyecto me parece una estructura bastante razonable. El problema no es tu AvatarRequestDto como concepto, sino cómo estás intentando transportar una imagen HTTP y algunos detalles de las anotaciones de validación.

Vamos por partes.

1. ¿Qué es exactamente MultipartFile?
   MultipartFile es una abstracción de Spring para representar un archivo que ha llegado en una petición HTTP.

Cuando desde Postman o un frontend seleccionas:

avatar.jpg

no estás enviando un JSON como:

{
"avatar": "...",
"avatarContentType": "image/jpeg"
}

Normalmente estás enviando una petición:

POST /users/15/avatar
Content-Type: multipart/form-data

El cuerpo contiene distintas "partes" (parts):

multipart/form-data
│
├── avatar
│     ├── filename: avatar.jpg
│     ├── Content-Type: image/jpeg
│     └── contenido binario
│
└── otros campos...

Spring toma esa parte que representa el archivo y te la entrega como:

MultipartFile avatar

Por eso puedes hacer:

@PostMapping("/{id}/avatar")
public ResponseEntity<?> uploadAvatar(
@PathVariable Long id,
@RequestParam("avatar") MultipartFile avatar) {

    byte[] bytes = avatar.getBytes();

    String contentType = avatar.getContentType();

    String filename = avatar.getOriginalFilename();

    long size = avatar.getSize();

    // ...
}

MultipartFile no es tu entidad de base de datos.

Es simplemente el objeto que Spring utiliza para representar temporalmente el archivo que acaba de llegar.

2. ¿Por qué no puedes hacer esto?
   Tú tienes:

@PostMapping("/{id}/avatar")
public ResponseEntity<AvatarResponseDto> uploadAvatar(
@PathVariable Long id,
@Valid @RequestBody AvatarRequestDto avatar) {

}

y:

@Data
public class AvatarRequestDto {

    @NotBlank
    private byte[] avatar;

    @NotBlank
    private String avatarContentType;
}

El problema fundamental es:

@RequestBody está pensado principalmente para deserializar el cuerpo completo de la petición como un formato estructurado como JSON.

Por ejemplo, esto funciona perfectamente:

POST /users/15
Content-Type: application/json

con:

{
"name": "Alejandro",
"email": "test@test.com"
}

Spring hace:

JSON
↓
Jackson
↓
UserRequestDto

Pero una imagen normalmente llega como:

Content-Type: multipart/form-data

y no como JSON.

El flujo es diferente:

multipart/form-data
↓
Spring Multipart handling
↓
MultipartFile

3. Pero... ¿podría mandar el byte[] dentro de JSON?
   Sí. Aquí está la parte interesante.

Tu idea no es técnicamente imposible.

Podrías hacer algo como:

{
"avatar": "iVBORw0KGgoAAAANSUhEUgAA...",
"avatarContentType": "image/png"
}

y que Jackson convierta ese valor a:

byte[] avatar;

Esto es posible porque Jackson puede representar un byte[] en JSON mediante Base64.

El flujo sería:

imagen.png
↓
bytes
↓
Base64
↓
JSON
↓
HTTP
↓
Jackson
↓
byte[]

Por tanto, tu endpoint:

@RequestBody AvatarRequestDto avatar

sí podría funcionar si el cliente enviase el byte[] codificado correctamente en JSON.

El problema es que no suele ser la mejor manera de subir archivos.

4. ¿Por qué multipart/form-data es mejor para imágenes?
   Porque estás enviando un archivo, no simplemente datos JSON.

Imagina una imagen de 2 MB.

Con multipart:

imagen
↓
~2 MB
↓
HTTP multipart

Con JSON + Base64:

imagen
↓
bytes
↓
Base64
↓
JSON

Base64 aumenta el tamaño de los datos aproximadamente un 33%.

Por ejemplo:

Imagen original:       2 MB
Base64:               ~2.67 MB
JSON:                 ~2.67 MB

Para un avatar pequeño no es una tragedia, pero es una razón para preferir multipart/form-data cuando estás transmitiendo archivos.

Además, MultipartFile te da directamente información útil del archivo:

avatar.getOriginalFilename();
avatar.getContentType();
avatar.getSize();
avatar.getBytes();
avatar.isEmpty();
avatar.getInputStream();

Con tu DTO tendrías que encargarte tú de representar la imagen dentro del JSON.

5. Hay otro problema en tu DTO
   Tienes:

@NotBlank
private byte[] avatar;

@NotBlank no es apropiado para byte[].

@NotBlank está pensado para String:

@NotBlank
private String name;

Para arrays puedes utilizar:

@NotNull
@Size(min = 1)
private byte[] avatar;

aunque, sinceramente, si utilizas MultipartFile, las validaciones las haría directamente sobre el MultipartFile o en el service.

Por ejemplo:

if (avatar.isEmpty()) {
throw new IllegalArgumentException("Avatar cannot be empty");
}

Y también:

if (avatar.getSize() > 2 * 1024 * 1024) {
throw new IllegalArgumentException("Avatar cannot exceed 2MB");
}

6. Tu arquitectura con Avatar separado está bien
   Esta parte:

@OneToOne(fetch = FetchType.LAZY)
@JoinTable(
name = "user_avatar",
joinColumns = @JoinColumn(name = "user_id"),
inverseJoinColumns = @JoinColumn(name = "avatar_id")
)
private Avatar avatar;

es perfectamente válida.

Estás modelando:

User
│
│ 1 ───── 1
│
▼
Avatar

Y tienes:

@Entity
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] avatar;

    private String avatarContentType;
}

La BD conceptualmente tendría:

users
----------------
id
name
email
password
...


user_avatar
----------------
user_id
avatar_id


avatar
----------------
avatar_id
avatar
avatar_content_type

Es decir:

User #15
│
│
▼
Avatar #8
│
├── avatar = [bytes]
└── contentType = image/jpeg

Para un proyecto como el tuyo, esta separación tiene sentido.

7. Entonces, ¿cómo haría yo tu endpoint?
   Con tu arquitectura actual:

@PostMapping("/{id}/avatar")
public ResponseEntity<AvatarResponseDto> uploadAvatar(
@PathVariable Long id,
@RequestParam("avatar") MultipartFile avatar) {

    // service...

}

La petición sería:

POST /users/15/avatar

Content-Type: multipart/form-data

con:

avatar → File → avatar.jpg

Spring crea:

MultipartFile avatar

y tú puedes hacer:

byte[] bytes = avatar.getBytes();

Después construyes tu entidad:

Avatar newAvatar = new Avatar();

newAvatar.setAvatar(avatar.getBytes());
newAvatar.setAvatarContentType(avatar.getContentType());

La guardas:

Avatar savedAvatar = avatarRepository.save(newAvatar);

y posteriormente se la asignas al usuario:

user.setAvatar(savedAvatar);

userRepository.save(user);

El flujo completo sería:

                POST /users/15/avatar
                         │
                         │ multipart/form-data
                         ▼
                  MultipartFile
                         │
               ┌─────────┴─────────┐
               │                   │
        getBytes()          getContentType()
               │                   │
               └─────────┬─────────┘
                         ▼
                      Avatar
                         │
                  avatarRepository
                         │
                         ▼
                     Database
                         │
                         ▼
                     User.avatar

8. ¿Y qué papel tendría AvatarRequestDto?
   Aquí yo no lo utilizaría.

Esto:

@Data
public class AvatarRequestDto {

    private byte[] avatar;
    private String avatarContentType;
}

representa una petición basada en JSON.

Pero estás haciendo una operación de subida de archivo.

Por tanto:

MultipartFile

es el objeto de entrada de la petición.

Tu DTO de respuesta sí tiene sentido:

@Data
public class AvatarResponseDto {

    private Long id;
    private byte[] avatar;
    private String avatarContentType;
}

Aunque aquí también haría un pequeño cambio.

No devolvería el byte[] en el DTO
Haría algo como:

@Data
public class AvatarResponseDto {

    private Long id;
    private String avatarContentType;
}

Y tendría:

GET /users/{id}/avatar

para obtener realmente la imagen.

¿Por qué?

Porque si haces:

GET /users/15

y tu UserResponseDto contiene:

{
"id": 15,
"name": "Alejandro",
"avatar": "base64 gigantesco..."
}

cada vez que solicites el usuario estarías transportando toda la imagen.

Es mejor separar:

GET /users/15
↓
{
"id": 15,
"name": "Alejandro",
"email": "..."
}

y:

GET /users/15/avatar
↓
image/jpeg
↓
[bytes]

9. Tu diseño final podría quedar así
   Entidad
10. 
   @OneToOne(fetch = FetchType.LAZY)
   @JoinTable(
   name = "user_avatar",
   joinColumns = @JoinColumn(name = "user_id"),
   inverseJoinColumns = @JoinColumn(name = "avatar_id")
   )
   private Avatar avatar;

Avatar
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] avatar;

    @Column(nullable = false)
    private String avatarContentType;
}

Upload
@PostMapping("/{id}/avatar")
public ResponseEntity<AvatarResponseDto> uploadAvatar(
@PathVariable Long id,
@RequestParam("avatar") MultipartFile avatar) {

    // service

}

Obtener avatar
@GetMapping("/{id}/avatar")
public ResponseEntity<byte[]> getAvatar(@PathVariable Long id) {

    // service

}

Postman
POST http://localhost:8080/users/15/avatar

Body
└── form-data
└── avatar | File | avatar.jpg

La idea clave
No pienses en MultipartFile como otro tipo de byte[].

Piensa en las dos cosas en niveles diferentes:

                 HTTP
                  │
        ┌─────────┴─────────┐
        │                   │
      JSON              multipart
        │                   │
@RequestBody          MultipartFile
│                   │
└─────────┬─────────┘
│
Tu aplicación
│
▼
byte[]
│
▼
@Lob / BLOB
│
▼
Database

MultipartFile = cómo recibes el archivo desde HTTP.

byte[] = cómo representas los bytes del archivo en Java.

@Lob = cómo le indicas a JPA que esos bytes son un objeto binario grande que debe persistirse apropiadamente.

Y tu Avatar = la entidad de dominio que representa ese avatar en tu modelo de datos.
