package com.game.controller;

import com.game.entity.Player;
import com.game.repository.PlayerRepo;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/rest/players")
    public ResponseEntity getPlayers() {
        try {
            return ResponseEntity.ok("Сервер работает");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }

    @PostMapping("/rest/players/")
    public ResponseEntity add(@RequestBody Player player) {
        try {
                playerService.addPlayer(player);
            return ResponseEntity.ok("игрок был успешно добавлен");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");
        }
    }


    @GetMapping(value = "/rest/players/{id}")
    public ResponseEntity getPlayer(@RequestParam @PathVariable Long id) {
        try {
            return ResponseEntity.ok(playerService.getPlayer(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/rest/players/count")
    public ResponseEntity countPlayers() {
        try {
            return ResponseEntity.ok("Сервер работает");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Произошла ошибка");

        }
    }
}