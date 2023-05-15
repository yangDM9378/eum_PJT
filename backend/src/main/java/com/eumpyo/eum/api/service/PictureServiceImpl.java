package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.PictureAddReq;
import com.eumpyo.eum.api.response.PictureDetailRes;
import com.eumpyo.eum.api.response.PictureGroupRes;
import com.eumpyo.eum.api.response.PicturePinRes;
import com.eumpyo.eum.common.util.S3Uploader;
import com.eumpyo.eum.db.entity.*;
import com.eumpyo.eum.db.repository.GroupRepository;
import com.eumpyo.eum.db.repository.PictureRepository;
import com.eumpyo.eum.db.repository.PinRepository;
import com.eumpyo.eum.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
    public List<PicturePinRes> findPicturePinList(Long pinId) {
        List<PicturePinRes> pictures = pictureRepository.findByPinIdOrderByCreatedDateDesc(pinId);
        return pictures;
    }

    @Override
    public PictureDetailRes findPictureDetail(Long pictureId) {
        Picture picture = pictureRepository.findById(pictureId)
                .orElseThrow(() -> new IllegalStateException("해당 사진이 존재하지 않습니다."));

        PictureDetailRes pictureDetailRes = PictureDetailRes
                .builder()
                .userId(picture.getUser().getUserId())
                .createdDate(picture.getCreatedDate())
                .image(picture.getImage())
                .userName(picture.getUser().getName())
                .build();
        return pictureDetailRes;
    }

    @Override
    public List<PictureGroupRes> findPictureGroupList(Long groupId) {
        List<PictureGroupRes> pictureGroupRes = pictureRepository.findByGroupId(groupId);

        return pictureGroupRes;
    }

    @Override
    public void addPicture(User user, PictureAddReq pictureAddReq, MultipartFile image) {

        Pin pin = pinRepository.findById(pictureAddReq.getPinId())
                .orElseThrow(() -> new IllegalStateException("Pin이 존재하지 않습니다."));

        Group group = groupRepository.findById(pictureAddReq.getGroupId())
                .orElseThrow(() -> new IllegalStateException("Group이 존재하지 않습니다."));

        Picture picture = Picture.builder()
                .group(group)
                .pin(pin)
                .user(user)
                .build();

        // 파일 S3에 저장
        if (image != null) {
            //make upload folder
            String uploadPath = rootPath + "/" + "picture" + "/" + "image" + "/";

            String fileName = image.getOriginalFilename();

            UUID uuid = UUID.randomUUID();
            String uploadFileName = uuid.toString() + "_" + fileName;

            try {
                log.debug(s3Uploader.upload(image, uploadPath + uploadFileName));
                picture.addImage(uploadPath + uploadFileName);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        pictureRepository.save(picture);
    }

    @Override
    public boolean removePicture(User user, Long pictureId) {
        Picture picture = pictureRepository.findById(pictureId)
                .orElseThrow(() -> new IllegalStateException("해당 사진이 존재하지 않습니다."));

        if (picture.getUser().getUserId() == user.getUserId()){
            s3Uploader.deleteS3(picture.getImage());
            pictureRepository.delete(picture);
            return true;
        } else {
            return false;
        }
    }
}
