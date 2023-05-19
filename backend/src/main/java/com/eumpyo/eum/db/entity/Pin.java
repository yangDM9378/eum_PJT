package com.eumpyo.eum.db.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Pin {
    @Id
    @Column(name = "pin_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pinId;

    @Column(name = "title", length = 50)
    private String title;

    @Column(name = "content", length = 255)
    private String content;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "code", length = 32)
    private String code;

    @Column(name = "type", length = 10)
    private String type;

    @Column(name = "image", length = 100)
    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Builder
    public Pin(Long pinId, String title, String content, Double latitude, Double longitude, LocalDateTime createdDate, String code, String type, String image, User user, Group group) {
        this.pinId = pinId;
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdDate = createdDate;
        this.code = code;
        this.type = type;
        this.image = image;
        this.user = user;
        this.group = group;
    }

    public void addImage(String image) {
        this.image =image;
    }
}
