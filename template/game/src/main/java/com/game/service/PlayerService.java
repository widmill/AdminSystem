package com.game.service;


import com.game.entity.Player;
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

    public Player get(Long id ) {
        Session session = factory.getCurrentSession();
        Player player = (Player) session.get(Player.class, id);
        return player;
    }
}
