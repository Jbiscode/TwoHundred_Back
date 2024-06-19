package org.duckdns.bidbuy.global.auth.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.duckdns.bidbuy.app.article.domain.ProductImage;
import org.duckdns.bidbuy.app.article.service.ImageUploadService;
import org.duckdns.bidbuy.app.user.domain.User;
import org.duckdns.bidbuy.app.user.domain.UserRole;
import org.duckdns.bidbuy.app.user.dto.EmailCheckReq;
import org.duckdns.bidbuy.app.user.exception.PasswordLengthException;
import org.duckdns.bidbuy.app.user.repository.UserRepository;
import org.duckdns.bidbuy.global.auth.domain.SignupRequest;
import org.duckdns.bidbuy.global.auth.exception.DuplicateIdExistException;
import org.duckdns.bidbuy.global.error.NullInputException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final ImageUploadService imageUploadService;

  public User createUser(SignupRequest userDTO, List<MultipartFile> list) throws IOException {
    Optional<User> isExist = userRepository.findByEmail(userDTO.getEmail());

    if (isExist.isPresent()) {
      throw new DuplicateIdExistException("이미 존재하는 사용자입니다.");
    }
    if (userDTO.getEmail() == null || userDTO.getEmail().isEmpty()
            || userDTO.getPassword() == null || userDTO.getPassword().isEmpty()
            || userDTO.getUsername() == null || userDTO.getUsername().isEmpty()
            || userDTO.getAddr1() == null || userDTO.getAddr1().isEmpty()
            || userDTO.getAddr2() == null || userDTO.getAddr2().isEmpty()) {
      throw new NullInputException("입력값을 확인해주세요.");
    }

    if(userDTO.getPassword().length() < 8) {
      throw new PasswordLengthException("비밀번호의 길이가 짧습니다.");
    }

    String imgUrl = "";
    if(list == null) {
      imgUrl = "s_uuid_7adc2b20-82c8-4f14-96f0-f68aa2613ac0";
    }else{
      MultipartFile[] multipartFiles = list.toArray(new MultipartFile[0]);
      List<Map<String, String>> uploadimageURL = imageUploadService.uploadImages(multipartFiles);
      for (int i = 0; i < uploadimageURL.size(); i++) {
        Map<String, String> imageUrlMap = uploadimageURL.get(i);
        imgUrl = imageUrlMap.get("thumbnail");
      }
    }

    User user = User.builder()
                                      .email(userDTO.getEmail())
                                      .username(userDTO.getUsername())
                                      .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
                                      .role(UserRole.USER)
                                      .addr1(userDTO.getAddr1())
                                      .addr2(userDTO.getAddr2())
                                      .createdDate(LocalDateTime.now())
                                      .modifiedDate(LocalDateTime.now())
                                      .profileImageUrl(imgUrl)
                                      .build();
    return userRepository.save(user);
  }

  public String findUser(EmailCheckReq emailCheckReq) {
      Optional<User> user =  userRepository.findByEmail(emailCheckReq.getEmail());
      if(user.isPresent()) {
        throw new DuplicateIdExistException("이미 존재하는 이메일입니다.");
      }else{
        return "notExist";
      }
  }
}
