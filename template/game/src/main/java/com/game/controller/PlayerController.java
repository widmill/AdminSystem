package com.game.controller;

import com.game.entity.Player;
import com.game.exception.UserNotFoundException;
import com.game.repository.PlayerRepo;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

//    @GetMapping("/rest/players")
//    public ResponseEntity getPlayersList() {
//        PlayerRepo playerRepo = null;
//        try {
//            return ResponseEntity.ok(playerRepo.findAll());
//        } catch (NullPointerException e) {
//            return ResponseEntity.badRequest().body("dsad");
//        }
//
//    }

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
    public ResponseEntity getPlayer(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(playerService.getPlayer(id));
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("Игрок с id 0 не существует");
        } catch (UserNotFoundException e) {
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

    @DeleteMapping("/rest/players/{id}")
    public ResponseEntity deletePlayer(@PathVariable("id") Long id) {
        try {
            playerService.delete(id);
            return ResponseEntity.ok().body("Игрок удален");
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("Игрок с 0 id не существует");
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}