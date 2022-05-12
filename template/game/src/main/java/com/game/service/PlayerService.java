package com.game.service;


import com.game.entity.Player;
import com.game.exception.NotValidDateException;
import com.game.exception.UserNotFoundException;
import com.game.repository.PlayerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service("playerService")
@Transactional
public class PlayerService {

    @Autowired
    PlayerRepo playerRepo;

    //добавление игрока
    public Player addPlayer(Player player) throws ParseException, NotValidDateException {
        if (!checkDate(player)) {
            throw new NotValidDateException();
        }
        lvlCount(player);
        untilNextLvlCount(player);
        return playerRepo.save(player);
    }

    //получить игрока по id
    public Player getPlayer(Long id) throws UserNotFoundException {
        if (id == 0) {
            throw new NullPointerException();
        }
        if (!playerRepo.findById(id).isPresent()) {
            throw new UserNotFoundException("Игрок с таким id не найден");
        }
        Player player = playerRepo.findById(id).get();
        return player;
    }
    //проверка даты для создания
    private boolean checkDate(Player player) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = sdf.parse("2000-01-01");
        Date date2 = sdf.parse("3000-01-01");
        if (player.getBirthday().getTime() >= date1.getTime() && player.getBirthday().getTime() <=
                date2.getTime()) {
            return true;
        }
        return false;
    }
    //удаление игрока
    public void delete(Long id) throws UserNotFoundException {
        if (id == 0) {
            throw new NullPointerException();
        }
        if (!playerRepo.findById(id).isPresent()) {
            throw new UserNotFoundException("Игрок с таким id не найден");
        }
        playerRepo.deleteById(id);
    }

    //подсчет уровня
    private void lvlCount(Player player) {
        int exp = player.getExperience();
        int lvl = (int) ((Math.sqrt(2500 + 200 * exp) - 50) / 100);
        player.setLevel(lvl);
    }

    //подсчет опыта до некст уровня
    private void untilNextLvlCount(Player player) {
        int lvl = player.getLevel();
        int exp = player.getExperience();
        int next = 50 * (lvl + 1) * (lvl + 2) - exp;
        player.setUntilNextLevel(next);
    }
}
