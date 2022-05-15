package com.game.service;


import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.exception.NonValidDateException;
import com.game.exception.NonValidExpException;
import com.game.exception.PlayerNotFoundException;
import com.game.repository.PlayerRepo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("playerService")
@Transactional
public class PlayerService {

    @Autowired
    private PlayerRepo playerRepo;

    public List<Player> getPlayers(
            String name,
            String title,
            Race race,
            Profession profession,
            Long after,
            Long before,
            Boolean banned,
            Integer minExperience,
            Integer maxExperience,
            Integer minLevel,
            Integer maxLevel

    ) {
        final Date afterDate = after == null ? null : new Date(after);
        final Date beforeDate = before == null ? null : new Date(before);
        final List<Player> list = new ArrayList<>();
        playerRepo.findAll().forEach((player) -> {
            if (name != null && !player.getName().contains(name)) return;
            if (title != null && !player.getTitle().contains(title)) return;
            if (race != null && player.getRace() != race) return;
            if (profession != null && player.getProfession() != profession) return;
            if (afterDate != null && player.getBirthday().before(afterDate)) return;
            if (beforeDate != null && player.getBirthday().after(beforeDate)) return;
            if (banned != null && player.getBanned().booleanValue() != banned.booleanValue()) return;
            if (minExperience != null && player.getExperience().compareTo(minExperience) < 0) return;
            if (maxExperience != null && player.getExperience().compareTo(maxExperience) > 0) return;
            if (minLevel != null && player.getLevel().compareTo(minLevel) < 0) return;
            if (maxLevel != null && player.getLevel().compareTo(maxLevel) > 0) return;

            list.add(player);
        });
        return list;
    }
    //сортировка
    public List<Player> sortPlayers(List<Player> players, PlayerOrder order) {
        if (order != null) {
            players.sort((player1, player2) -> {
                switch (order) {
                    case ID:
                        return player1.getId().compareTo(player2.getId());
                    case NAME:
                        return player1.getName().compareTo(player2.getName());
                    case EXPERIENCE:
                        return player1.getExperience().compareTo(player2.getExperience());
                    case BIRTHDAY:
                        return player1.getBirthday().compareTo(player2.getBirthday());
                    case LEVEL:
                        return player1.getLevel().compareTo(player2.getLevel());
                    default:
                        return 0;
                }
            });
        }
        return players;
    }

    //получение страницы
    public List<Player> getPage(List<Player> players, Integer pageNumber, Integer pageSize) {
        final Integer page = pageNumber == null ? 0 : pageNumber;
        final Integer size = pageSize == null ? 3 : pageSize;
        final int from = page * size;
        int to = from + size;
        if (to > players.size()) to = players.size();
        return players.subList(from, to);
    }

    //добавление игрока
    public Player addPlayer(Player player) throws ParseException, NonValidDateException, NonValidExpException {
        if (!checkDate(player)) {
            throw new NonValidDateException();
        }
        if (!checkExp(player)) {
            throw new NonValidExpException();
        }
        lvlCount(player);
        untilNextLvlCount(player);
        return playerRepo.save(player);
    }

    //получить игрока по id
    public Player getPlayer(Long id) throws PlayerNotFoundException {
        if (id == 0) {
            throw new NullPointerException();
        }
        if (!playerRepo.findById(id).isPresent()) {
            throw new PlayerNotFoundException("Игрок с таким id не найден");
        }
        return playerRepo.findById(id).get();
    }

    //проверка даты для создания
    private boolean checkDate(Player player) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = sdf.parse("01-01-2000");
        Date date2 = sdf.parse("01-01-3000");
        if (player.getBirthday().getTime() >= date1.getTime() && player.getBirthday().getTime() <=
                date2.getTime()) {
            return true;
        }
        return false;
    }

    //проверка экспы для создания
    private boolean checkExp(Player player) {
        if (player.getExperience() >= 0 && player.getExperience() <= 10_000_000) {
            return true;
        } else {
            return false;
        }
    }

    //удаление игрока
    public void delete(Long id) throws PlayerNotFoundException {
        if (id == 0) {
            throw new NullPointerException();
        }
        if (!playerRepo.findById(id).isPresent()) {
            throw new PlayerNotFoundException("Игрок с таким id не найден");
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

    //update
    public Player updatePlayer(Player oldPlayer, Player newPlayer) throws IllegalArgumentException, ParseException {
        boolean shouldChangeRating = false;

        final String name = newPlayer.getName();
        if (name != null) {
            if (isStringValid(name)) {
                oldPlayer.setName(name);
            } else {
                throw new IllegalArgumentException();
            }
        }
        final String title = newPlayer.getTitle();
        if (title != null) {
            if (isStringValid(title)) {
                oldPlayer.setTitle(title);
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (newPlayer.getRace() != null) {
            oldPlayer.setRace(newPlayer.getRace());
        }
        if (newPlayer.getProfession() != null) {
            oldPlayer.setProfession(newPlayer.getProfession());
        }
        final Date birthday = newPlayer.getBirthday();
        if (birthday != null) {
            if (checkDate(newPlayer)) {
                oldPlayer.setBirthday(birthday);
                shouldChangeRating = true;
            } else {
                throw new IllegalArgumentException();
            }
        }
        if (newPlayer.getBanned() != null) {
            oldPlayer.setBanned(newPlayer.getBanned());
            shouldChangeRating = true;
        }
        final Integer experience = newPlayer.getExperience();
        if (experience != null) {
            if (checkExp(newPlayer)) {
                oldPlayer.setExperience(experience);
                shouldChangeRating = true;
            } else {
                throw new IllegalArgumentException();
            }
            lvlCount(oldPlayer);
            untilNextLvlCount(oldPlayer);
        }

        playerRepo.save(oldPlayer);
        return oldPlayer;
    }
    private boolean isStringValid(String value) {
        final int maxStringLength = 50;
        return value != null && !value.isEmpty() && value.length() <= maxStringLength;
    }
}
