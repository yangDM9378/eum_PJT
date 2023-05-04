package com.eumpyo.eum.api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * 그룹 생성
 */
@Getter
@Setter
public class GroupAddReq {
    // 이름
    String name;

    // 설명
    String description;

    // 그룹 이미지
    String image;

    @Builder
    public GroupAddReq(String name, String description, String image) {
        this.name = name;
        this.description = description;
        this.image = image;
    }
}
