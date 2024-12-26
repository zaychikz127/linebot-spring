package com.example.linebot.controller;

import com.example.linebot.model.User;
import com.example.linebot.model.Coop;
import com.example.linebot.service.LineMessagingService;
import com.example.linebot.service.UserService;
import com.example.linebot.service.CoopService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/line")
public class LineBotController {

    @Autowired
    private LineMessagingService lineMessagingService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoopService coopService; // เพิ่ม CoopService

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String requestBody) {
        try {
            JSONObject jsonObject = new JSONObject(requestBody);
            
            String userId = jsonObject.getJSONArray("events")
                                       .getJSONObject(0)
                                       .getJSONObject("source")
                                       .getString("userId");
            String receivedMessage = jsonObject.getJSONArray("events")
                                               .getJSONObject(0)
                                               .getJSONObject("message")
                                               .getString("text");

            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.status(400).body("Error: User ID is missing or invalid");
            }

            if (receivedMessage == null || receivedMessage.isEmpty()) {
                return ResponseEntity.status(400).body("Error: Received message is empty");
            }

            // ฟังก์ชัน "Hello" เพื่อตอบกลับคำทักทาย
            if ("Hello".equalsIgnoreCase(receivedMessage)) {
                lineMessagingService.pushMessage(userId, "Hi! How can I help you?");
            }
            // คำสั่ง "get_user_details" สำหรับดึงข้อมูลผู้ใช้ทั้งหมด
            else if ("get_user_details".equalsIgnoreCase(receivedMessage)) {
                List<User> users = userService.getAllUsers();
                StringBuilder responseMessage = new StringBuilder("All Users:\n");
                
                for (User user : users) {
                    responseMessage.append("ID: ").append(user.getId()).append(", ")
                                   .append("Name: ").append(user.getName()).append(", ")
                                   .append("Email: ").append(user.getEmail()).append("\n");
                }
                
                lineMessagingService.pushMessage(userId, responseMessage.toString());
            }
            // ตรวจสอบว่าเป็น coop_code 6 หลัก
            else if (receivedMessage.matches("^\\d{6}$")) {
                String coopCode = receivedMessage;

                // ค้นหาข้อมูล coop จาก coop_code
                Optional<Coop> coopOptional = coopService.getCoopByCode(coopCode);
                if (coopOptional.isPresent()) {
                    Coop coop = coopOptional.get();
                    String response = "Coop Details:\n" +
                                      "ID: " + coop.getId() + "\n" +
                                      "Coop ID: " + coop.getCoopId() + "\n" +
                                      "Name: " + coop.getName();
                    lineMessagingService.pushMessage(userId, response);
                } else {
                    lineMessagingService.pushMessage(userId, "Coop not found for code: " + coopCode);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: Internal server error");
        }

        return ResponseEntity.ok("OK");
    }
}
