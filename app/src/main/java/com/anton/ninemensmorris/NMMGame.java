package com.anton.ninemensmorris;

import java.util.ArrayList;

/**
 * Created by Anton on 2016-11-28.
 */

public class NMMGame {

    private ArrayList<Checker> blueCheckers = new ArrayList();
    private ArrayList<Checker> redCheckers = new ArrayList();

    public NMMGame(){
        createCheckers();
    }


    private void createCheckers(){
        for(int i = 0; i < 9; i++)
        {
            blueCheckers.add(new Checker(PlayerColor.BLUE));
            redCheckers.add(new Checker(PlayerColor.RED));
        }
    }

    public enum PlayerColor{
        RED,
        BLUE
    }
}
