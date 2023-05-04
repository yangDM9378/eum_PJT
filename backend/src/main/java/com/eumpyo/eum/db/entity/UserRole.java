package com.eumpyo.eum.db.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_role")
public class UserRole {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name="base_user")
    private User baseUser;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name="target_user")
    private User targetUser;

    @Column(name="role", length = 20)
    private String role;

    @Builder
    public UserRole(User baseUser, User targetUser, String role) {
        this.baseUser = baseUser;
        this.targetUser = targetUser;
        this.role = role;
    }

    public void changeRole(String role){
        this.role = role;
    }
}
