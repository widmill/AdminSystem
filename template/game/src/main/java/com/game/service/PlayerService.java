package com.game.service;


import com.game.entity.Player;
import com.game.exception.NotValidDateException;
import com.game.exception.UserNotFoundException;
import com.game.repository.PlayerRepo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;

@Service("playerService")
@Transactional
public class PlayerService {

    @Autowired
    PlayerRepo playerRepo;


    private SessionFactory factory;

    public Player addPlayer(Player player) throws ParseException, NotValidDateException {
        if (!checkDate(player)) {
            throw new NotValidDateException();
        }
        return playerRepo.save(player);
        }

    public Player getPlayer(Long id ) throws UserNotFoundException {
        if (id == 0) {
            throw new NullPointerException();
        }
        if (!playerRepo.findById(id).isPresent()) {
            throw new UserNotFoundException("Игрок с таким id не найден");
        }
        Player player = playerRepo.findById(id).get();
        return player;
    }
    public boolean checkDate(Player player) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = sdf.parse("2000-01-01");
        Date date2 = sdf.parse("3000-01-01");
            if (player.getBirthDay().getTime() >= date1.getTime() && player.getBirthDay().getTime() <=
            date2.getTime()) {
                return true;
        }
            return false;
    }
    public void delete(Long id) throws UserNotFoundException {
        if (id == 0) {
            throw new NullPointerException();
        }
        if (!playerRepo.findById(id).isPresent()) {
            throw new UserNotFoundException("Игрок с таким id не найден");
        }
        playerRepo.deleteById(id);
    }
}
