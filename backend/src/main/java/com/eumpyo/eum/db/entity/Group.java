package com.eumpyo.eum.db.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "\"group\"")
@Getter
@NoArgsConstructor
public class Group {
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer groupId;

    @Column(name = "name", length = 20)
    String name;

    @Column(name = "created_date")
    LocalDateTime createdDate;

    @Column(name = "description", length = 255)
    String description;

    @Column(name = "image", length = 100)
    String image;

    @Builder
    public Group(Integer groupId, String name, LocalDateTime createdDate, String description, String image) {
        this.groupId = groupId;
        this.name = name;
        this.createdDate = createdDate;
        this.description = description;
        this.image = image;
    }
}