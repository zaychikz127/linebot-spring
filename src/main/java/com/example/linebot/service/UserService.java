package com.example.linebot.service;

import com.example.linebot.model.User;
import com.example.linebot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ดึงข้อมูลผู้ใช้ทั้งหมด
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ดึงข้อมูลผู้ใช้ตาม ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
}
