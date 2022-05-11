package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepo extends CrudRepository<Player, Long> {
}
