package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.PictureAddReq;
import com.eumpyo.eum.common.util.S3Uploader;
import com.eumpyo.eum.db.entity.Group;
import com.eumpyo.eum.db.entity.Picture;
import com.eumpyo.eum.db.entity.Pin;
import com.eumpyo.eum.db.entity.User;
import com.eumpyo.eum.db.repository.GroupRepository;
import com.eumpyo.eum.db.repository.PictureRepository;
import com.eumpyo.eum.db.repository.PinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PictureServiceImpl implements PictureService {
    @Value("${cloud.aws.directory}")
    String rootPath;
    private final S3Uploader s3Uploader;

    private final PinRepository pinRepository;
    private final GroupRepository groupRepository;
    private final PictureRepository pictureRepository;
    @Override
    public void addPicture(User user, PictureAddReq pictureAddReq, MultipartFile image) {

        Pin pin = pinRepository.findById(Long.valueOf(pictureAddReq.getPinId()))
                .orElseThrow(() -> new IllegalStateException("Pin이 존재하지 않습니다."));

        Group group = groupRepository.findById(Long.valueOf(pictureAddReq.getGroupId()))
                .orElseThrow(() -> new IllegalStateException("Group이 존재하지 않습니다."));

        Picture picture = Picture.builder()
                .group(group)
                .pin(pin)
                .user(user)
                .build();

        // 파일 S3에 저장
        if (image != null) {
            //make upload folder
            String uploadPath = rootPath + "/" + "group" + "/" + "image" + "/";

            String fileName = image.getOriginalFilename();

            UUID uuid = UUID.randomUUID();
            String uploadFileName = uuid.toString() + "_" + fileName;

            try {
                log.debug(s3Uploader.upload(image, uploadPath + uploadFileName));
                picture.addImage(uploadFileName);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        pictureRepository.save(picture);
    }
}
