package com.eumpyo.eum.db.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "`group`")
@Getter
@NoArgsConstructor
@ToString
public class Group {
    @Id
    @Column(name = "group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long groupId;

    @Column(name = "name", length = 20)
    String name;

    @CreationTimestamp
    @Column(name = "created_date")
    LocalDateTime createdDate;

    @Column(name = "description", length = 255)
    String description;

    @Column(name = "image", length = 100)
    String image;

    @Column(name = "group_code", length = 32, unique = true)
    String groupCode;

    @Builder
    public Group(Long groupId, String name, LocalDateTime createdDate, String description, String image, String groupCode) {
        this.groupId = groupId;
        this.name = name;
        this.createdDate = createdDate;
        this.description = description;
        this.image = image;
        this.groupCode = groupCode;
    }

    public void addImage(String image) {
        this.image = image;
    }
}