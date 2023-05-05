package com.eumpyo.eum.db.entity;

import lombok.Builder;
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

    @ManyToOne(targetEntity = Pin.class, fetch = FetchType.LAZY)
    @JoinColumn(name="pin_id")
    Pin pinId;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    User userId;

    @Builder
    public Picture(Pin pin, Group group, String image, User user){
        this.pinId = pin;
        this.groupId = group;
        this.userId = user;
    }

    public void addImage(String image) {
        this.image = image;
    }
}
