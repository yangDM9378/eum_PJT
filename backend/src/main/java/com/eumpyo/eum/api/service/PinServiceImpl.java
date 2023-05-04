package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.PinAddReq;
import com.eumpyo.eum.api.response.PinListRes;
import com.eumpyo.eum.common.util.S3Uploader;
import com.eumpyo.eum.db.entity.Group;
import com.eumpyo.eum.db.entity.Pin;
import com.eumpyo.eum.db.entity.User;
import com.eumpyo.eum.db.repository.GroupRepository;
import com.eumpyo.eum.db.repository.PinRepository;
import com.eumpyo.eum.db.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PinServiceImpl implements PinService {
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final PinRepository pinRepository;
    private final S3Uploader s3Uploader;

    @Value("${cloud.aws.directory}")
    String rootPath;

    @Override
    @Transactional
    public void addPin(User user, PinAddReq pinAddReq, MultipartFile image) {
        String pinCode = UUID.randomUUID().toString().replaceAll("-", "");

        Group group = groupRepository.findByGroupId(pinAddReq.getGroupId())
                .orElseThrow(() -> new IllegalStateException("그룹이 존재하지 않습니다."));

        Pin pin = Pin.builder()
                .title(pinAddReq.getTitle())
                .content(pinAddReq.getContent())
                .latitude(pinAddReq.getLatitude())
                .longitude(pinAddReq.getLongitude())
                .type(pinAddReq.getType())
                .code(pinCode)
                .user(user)
                .group(group)
                .build();

        // 파일 S3에 저장
        if (image != null) {
            //make upload folder
            String uploadPath = rootPath + "/" + "group" + "/" + "image" + "/";
            File uploadFilePath = new File(rootPath, uploadPath);

            String fileName = image.getOriginalFilename();

            UUID uuid = UUID.randomUUID();
            String uploadFileName = uuid.toString() + "_" + fileName;

            try {
                log.debug(s3Uploader.upload(image, uploadPath + uploadFileName));
                pin.setImage(uploadFileName);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        pinRepository.save(pin);
    }

    @Override
    public List<PinListRes> findAllPin() {
        List<Pin> pinList = pinRepository.findAll();

        ArrayList<PinListRes> pinResList = new ArrayList<>();

        for (Pin pin : pinList) {
            pinResList.add(PinListRes.builder()
                    .pinId(pin.getPinId())
                    .latitude(pin.getLatitude())
                    .longitude(pin.getLongitude())
                    .build());
        }

        return pinResList;
    }

    @Override
    public List<PinListRes> findGroupPin(Long groupId) {
        List<Pin> pinList = pinRepository.findByGroup_GroupId(groupId);

        ArrayList<PinListRes> pinResList = new ArrayList<>();

        for (Pin pin : pinList) {
            pinResList.add(PinListRes.builder()
                    .pinId(pin.getPinId())
                    .latitude(pin.getLatitude())
                    .longitude(pin.getLongitude())
                    .build());
        }

        return pinResList;
    }

    @Override
    public List<PinListRes> findUserPin(User user) {
        List<PinListRes> pinResList = pinRepository.findByUser_UserId(user.getUserId());

        return pinResList;
    }
}
