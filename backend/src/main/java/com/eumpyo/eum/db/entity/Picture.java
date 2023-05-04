package com.eumpyo.eum.db.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "picture")
public class Picture {
    @Id
    @Column(name = "picture_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long picture_id;

    @Column(name = "image", length = 100)
    String image;

    @CreationTimestamp
    @Column(name = "created_date")
    LocalDateTime createdDate;

    @ManyToOne(targetEntity = Group.class, fetch = FetchType.LAZY)
    @JoinColumn(name="group_id")
    Group groupId;
}
