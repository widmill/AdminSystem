package com.game;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Date;

public class Test1 {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Player.class)
                .buildSessionFactory();


        Session session = factory.getCurrentSession();
        Player player = new Player("Ilya", "Loh", Race.HUMAN, Profession.WARRIOR,
                125, 125, 130, new Date(22-7-1997), false );



        session.beginTransaction();

        session.getTransaction().commit();



    }
}
