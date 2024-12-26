package com.example.linebot.controller;

import com.example.linebot.model.Message;
import com.example.linebot.model.User;
import com.example.linebot.service.LineMessagingService;
import com.example.linebot.service.UserService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/line")
public class LineBotController {

    @Autowired
    private LineMessagingService lineMessagingService;

    @Autowired
    private UserService userService; // ใช้ userService แทน UserService (หลีกเลี่ยงปัญหาตัวพิมพ์ใหญ่)

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody String requestBody) {
        try {
            // แปลงข้อมูลที่ได้รับจาก webhook เป็น JSONObject
            JSONObject jsonObject = new JSONObject(requestBody);
            
            // ดึงค่า userId และ message จากข้อมูลที่ได้รับ
            String userId = jsonObject.getJSONArray("events")
                                       .getJSONObject(0)
                                       .getJSONObject("source")
                                       .getString("userId");
            String receivedMessage = jsonObject.getJSONArray("events")
                                               .getJSONObject(0)
                                               .getJSONObject("message")
                                               .getString("text");

            // ตรวจสอบว่า userId หรือ receivedMessage เป็นค่าว่างหรือไม่
            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.status(400).body("Error: User ID is missing or invalid");
            }

            if (receivedMessage == null || receivedMessage.isEmpty()) {
                return ResponseEntity.status(400).body("Error: Received message is empty");
            }

            // สร้าง Message object (ไม่เก็บในฐานข้อมูล)
            Message message = new Message(userId, receivedMessage);

            // ส่งข้อความตอบกลับตามเงื่อนไข
            if ("Hello".equalsIgnoreCase(receivedMessage)) {
                lineMessagingService.pushMessage(userId, "Hi! How can I help you?");
            } else if ("get_test_data".equalsIgnoreCase(receivedMessage)) {
                // ดึงข้อมูลจากฐานข้อมูล (UserService)
                List<User> users = userService.getAllUsers(); // Fetch all users from the database
                StringBuilder responseMessage = new StringBuilder("All Users:\n");
                for (User user : users) {
                    responseMessage.append(user.toString()).append("\n");
                }
                // ส่งข้อความตอบกลับ
                lineMessagingService.pushMessage(userId, responseMessage.toString());
            }

            // คุณสามารถเก็บ message object หรือใช้งานเพิ่มเติมตามต้องการที่นี่ได้

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: Internal server error");
        }

        return ResponseEntity.ok("OK");
    }
}
