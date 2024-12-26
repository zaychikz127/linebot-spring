package com.example.linebot.repository;

import com.example.linebot.model.Coop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CoopRepository extends JpaRepository<Coop, Long> {
    Optional<Coop> findByCoopCode(String coopCode);
}