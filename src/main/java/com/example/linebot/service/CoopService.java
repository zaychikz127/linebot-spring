package com.example.linebot.service;

import com.example.linebot.model.Coop;
import com.example.linebot.repository.CoopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CoopService {

    @Autowired
    private CoopRepository coopRepository;

    // ค้นหาข้อมูล coop ตาม coop_code
    public Optional<Coop> getCoopByCode(String coopCode) {
        return coopRepository.findByCoopCode(coopCode);
    }
}
