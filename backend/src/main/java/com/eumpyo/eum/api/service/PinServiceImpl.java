package com.eumpyo.eum.api.service;

import com.eumpyo.eum.api.request.PinAddReq;
import com.eumpyo.eum.api.response.PinAlarmRes;
import com.eumpyo.eum.api.response.PinDetailsRes;
import com.eumpyo.eum.api.response.PinListRes;
import com.eumpyo.eum.common.util.S3Uploader;
import com.eumpyo.eum.db.entity.Group;
import com.eumpyo.eum.db.entity.Pin;
import com.eumpyo.eum.db.entity.User;
import com.eumpyo.eum.db.repository.GroupRepository;
import com.eumpyo.eum.db.repository.PinRepository;
import com.eumpyo.eum.db.repository.UserRepository;
import com.eumpyo.eum.db.repository.UserRoleRepository;
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
    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserRoleRepository userRoleRepository;
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
            String uploadPath = rootPath + "/" + "pin" + "/" + "image" + "/";
            File uploadFilePath = new File(rootPath, uploadPath);

            String fileName = image.getOriginalFilename();

            UUID uuid = UUID.randomUUID();
            String uploadFileName = uuid.toString() + "_" + fileName;

            try {
                log.debug(s3Uploader.upload(image, uploadPath + uploadFileName));
                pin.addImage(uploadPath + uploadFileName);
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
        List<PinListRes> pinResList = pinRepository.findPinList(user.getUserId());

        return pinResList;
    }

    @Override
    public PinDetailsRes findPin(Long pinId) {
        // 핀 없을 때 어떤 예외 발생
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(() -> new IllegalStateException("해당하는 핀이 존재하지 않습니다."));

        PinDetailsRes pinDetailsRes = PinDetailsRes.builder()
                .title(pin.getTitle())
                .content(pin.getContent())
                .image(pin.getImage())
                .createdDate(pin.getCreatedDate())
                .userName(pin.getUser().getName())
                .type(pin.getType())
                .build();

        return  pinDetailsRes;
    }

    @Override
    public PinAlarmRes findPinAlarm(User user, Long pinId) {
        // 핀 없을 때 어떤 예외 발생
        Pin pin = pinRepository.findById(pinId)
                .orElseThrow(() -> new IllegalStateException("해당하는 핀이 존재하지 않습니다."));

        // 유저롤값 읽어오기
        String userRole = userRoleRepository.getUserRole(user.getUserId(), pin.getUser().getUserId());

        if(userRole == null) {
            User author = userRepository.findById(pin.getUser().getUserId())
                    .orElseThrow(() -> new IllegalStateException("해당하는 유저가 존재하지 않습니다."));

            userRole = author.getName();
        }

        PinAlarmRes pinAlarmRes = PinAlarmRes.builder()
                .title(pin.getTitle())
                .role(userRole)
                .build();

        return  pinAlarmRes;
    }
}
