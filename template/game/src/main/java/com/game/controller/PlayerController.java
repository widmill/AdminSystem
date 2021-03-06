package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.PlayerNotFoundException;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping("/rest/players")
    public ResponseEntity getAllPlayers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
            @RequestParam(value = "order", required = false) PlayerOrder order,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize
    ) {
        final List<Player> players = playerService.getPlayers(name, title, race, profession, after, before, banned, minExperience,
                maxExperience, minLevel, maxLevel);

        final List<Player> sortedPlayers = playerService.sortPlayers(players, order);

        return ResponseEntity.ok(playerService.getPage(sortedPlayers, pageNumber, pageSize));
    }

    @GetMapping("/rest/players/count")
    public ResponseEntity getPlayersCount(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "race", required = false) Race race,
            @RequestParam(value = "profession", required = false) Profession profession,
            @RequestParam(value = "after", required = false) Long after,
            @RequestParam(value = "before", required = false) Long before,
            @RequestParam(value = "banned", required = false) Boolean banned,
            @RequestParam(value = "minExperience", required = false) Integer minExperience,
            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
            @RequestParam(value = "minLevel", required = false) Integer minLevel,
            @RequestParam(value = "maxLevel", required = false) Integer maxLevel
    ) {
        return ResponseEntity.ok(playerService.getPlayers(name, title, race, profession, after, before, banned, minExperience,
                maxExperience, minLevel, maxLevel).size());
    }

    @PostMapping("/rest/players")
    public ResponseEntity add(@RequestBody Player player) {
        try {
            return ResponseEntity.ok(playerService.addPlayer(player));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("?????????????????? ????????????");
        }
    }


    @GetMapping(value = "/rest/players/{id}")
    public ResponseEntity getPlayer(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(playerService.getPlayer(id));
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("?????????? ?? id 0 ???? ????????????????????");
        } catch (PlayerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/rest/players/{id}")
    public ResponseEntity deletePlayer(@PathVariable("id") Long id) {
        try {
            playerService.delete(id);
            return ResponseEntity.ok().body("?????????? ????????????");
        } catch (NullPointerException e) {
            return ResponseEntity.badRequest().body("?????????? ?? 0 id ???? ????????????????????");
        } catch (PlayerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/rest/players/{id}")
    public ResponseEntity updatePlayer(@PathVariable("id") Long id,
                                       @RequestBody Player player) throws ParseException {
        if (id == 0) {
            return ResponseEntity.badRequest().body("???? ???????????????????? ???????????? ?? 0 id");
        }
        final ResponseEntity<Player> entity = getPlayer(id);
        final Player savedPlayer = entity.getBody();
        if (savedPlayer == null) {
            return entity;
        }

        final Player result;
        try {
            result = playerService.updatePlayer(savedPlayer, player);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("?????????????????? ????????????");
        }
        return ResponseEntity.ok(result);


    }
}