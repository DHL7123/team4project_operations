package com.evo.evoproject.controller.user;

import com.evo.evoproject.domain.user.User;
import com.evo.evoproject.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class FindController {

    @Autowired
    private JavaMailSender mailSender;

    private static final Logger logger = LoggerFactory.getLogger(FindController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/find-id")
    @ResponseBody
    public Map<String, String> findId(@RequestParam String userName, @RequestParam String userEmail) {
        logger.info("Received findId request with userName: {} and userEmail: {}", userName, userEmail);
        User user = userService.findUserByUserEmail(userEmail);
        Map<String, String> response = new HashMap<>();
        if (user != null && user.getUserName().equals(userName)) {
            response.put("userId", user.getUserId());  // userId 대신 user.getUsername() 사용
        } else {
            response.put("error", "등록된 이메일이 없습니다.");
        }
        return response;
    }


    @PostMapping("/find-password")
    @ResponseBody
    public Map<String, String> findPassword(@RequestParam String userName, @RequestParam String userEmail, @RequestParam String userId) {
        logger.info("Received findPassword request with userName: {}, userEmail: {}, and userId: {}", userName, userEmail, userId);
        User user = userService.findUserByUserEmail(userEmail);
        Map<String, String> response = new HashMap<>();
        if (user != null && user.getUserName().equals(userName) && user.getUserId().equals(userId)) {
            String tempPassword = UUID.randomUUID().toString().substring(0, 8); // 임시비밀번호8자리 랜덤으로생성하기
            userService.updateUserPassword(user, tempPassword);
            sendTempPasswordEmail(userEmail, tempPassword);
            response.put("message", "임시 비밀번호가 이메일로 발송되었습니다.");
        } else {
            response.put("error", "등록된 이메일이 없습니다.");
        }
        return response;
    }
    @PostMapping("/change-password")
    @ResponseBody
    public Map<String, String> changePassword(@RequestParam String userId, @RequestParam String currentPassword,
                                              @RequestParam String newPassword) {
        logger.info("Received changePassword request for userId: {}", userId);
        User user = userService.findUserByUserId(userId);
        Map<String, String> response = new HashMap<>();
        if (user != null && userService.checkPassword(user, currentPassword)) {
            userService.updateUserPassword(user, newPassword);
            response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
        } else {
            response.put("error", "현재 비밀번호가 일치하지 않습니다.");
        }
        return response;
    }
    @PostMapping("/update-user")
    @ResponseBody
    public Map<String, String> updateUser(@RequestParam String userId, @RequestParam String userEmail,
                                          @RequestParam String userAddress1, @RequestParam String userAddress2,
                                          @RequestParam int userPhone, @RequestParam char userMarketing) {
        logger.info("Received updateUser request for userId: {}", userId);
        User user = userService.findUserByUserId(userId);
        Map<String, String> response = new HashMap<>();
        if (user != null) {
            user.setUserEmail(userEmail);
            user.setUserAddress1(userAddress1);
            user.setUserAddress2(userAddress2);
            user.setUserPhone(userPhone);
            user.setUserMarketing(userMarketing);
            userService.updateUserDetails(user);
            response.put("message", "사용자 정보가 성공적으로 수정되었습니다.");
        } else {
            response.put("error", "사용자를 찾을 수 없습니다.");
        }
        return response;
    }







    private void sendTempPasswordEmail(String toEmail, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("임시 비밀번호 안내");
        message.setText("안녕하세요,\n\n귀하의 임시 비밀번호는 다음과 같습니다: " + tempPassword + "\n\n비밀번호를 변경해주시기 바랍니다.");
        mailSender.send(message);
    }

}

