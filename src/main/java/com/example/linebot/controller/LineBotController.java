package com.example.linebot.controller;

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
    private UserService UserService; // Assuming a UserService for database operations

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

            if ("Hello".equalsIgnoreCase(receivedMessage)) {
                lineMessagingService.pushMessage(userId, "Hi! How can I help you?");
            } else if ("get_test_data".equalsIgnoreCase(receivedMessage)) {
                List<User> users = UserService.getAllUsers(); // Fetch all users from the database
                StringBuilder responseMessage = new StringBuilder("All Users:\n");
                for (User user : users) {
                    responseMessage.append(user.toString()).append("\n");
                }
                lineMessagingService.pushMessage(userId, responseMessage.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok("OK");
    }
}
