package com.alergb.taskmanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "avatars")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Avatar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avatar_id")
    private Long id;

    @Lob
    @Column(name = "avatar")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private byte[] avatar;

    @Column(name = "avatar_content_type")
    private String avatarContentType;
}
