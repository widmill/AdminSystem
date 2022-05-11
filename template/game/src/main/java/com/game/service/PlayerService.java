package com.game.service;


import com.game.entity.Player;
import com.game.exception.UserNotFoundException;
import com.game.repository.PlayerRepo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("playerService")
@Transactional
public class PlayerService {

    @Autowired
    PlayerRepo playerRepo;


    private SessionFactory factory;

    public Player addPlayer(Player player) {
        return playerRepo.save(player);
        }

    public Player getPlayer(Long id ) throws UserNotFoundException {
        Player player = playerRepo.findById(id).get();
        if (player == null) {
            throw new UserNotFoundException("Пользователь не был найден");
        }
        return player;
    }
}
