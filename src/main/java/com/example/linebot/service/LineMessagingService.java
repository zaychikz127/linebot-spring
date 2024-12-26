package com.example.linebot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class LineMessagingService {

    @Value("${line.bot.channel-token}")
    private String channelToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public void pushMessage(String to, String message) {
        String url = "https://api.line.me/v2/bot/message/push";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(channelToken);

        // สร้าง body ของ request
        Map<String, Object> body = Map.of(
            "to", to, 
            "messages", List.of(
                Map.of("type", "text", "text", message)
            )
        );

        // สร้าง request entity
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            // ส่ง POST request ไปยัง LINE API
            restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            // ถ้ามีข้อผิดพลาดในการส่ง request
            e.printStackTrace();
        }
    }
}
