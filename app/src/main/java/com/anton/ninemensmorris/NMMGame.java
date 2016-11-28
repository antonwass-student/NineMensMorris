package com.anton.ninemensmorris;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 2016-11-28.
 */

public class NMMGame {

    private PlayerColor turn;

    private Node[] gameboard = new Node[24];
    private ArrayList<Checker> blueCheckers = new ArrayList();
    private ArrayList<Checker> redCheckers = new ArrayList();

    public NMMGame(){
        init();
    }

    private void playTurn(){
        List<Checker> checkers;
        if(turn == PlayerColor.BLUE)
            checkers = blueCheckers;
        else
            checkers = redCheckers;

        if(checkers.size() > 0 ){
            //do something
        }else{
            //do something else
        }
    }

    private void nextTurn(){
        if(turn == PlayerColor.BLUE){
            turn = PlayerColor.RED;
        }else{
            turn = PlayerColor.BLUE;
        }
    }

    private void init(){

        turn = PlayerColor.BLUE;

        for(int i = 0; i < 9; i++)
        {
            blueCheckers.add(new Checker(PlayerColor.BLUE));
            redCheckers.add(new Checker(PlayerColor.RED));
        }

        for(int i = 0; i <24; i++)
            gameboard[i] = new Node();

        setNeighbors();
    }


    private void setNeighbors(){
        gameboard[0].setNeighbors(null, gameboard[1], gameboard[7], null);
        gameboard[1].setNeighbors(null, gameboard[2], gameboard[9], gameboard[0]);
        gameboard[2].setNeighbors(null, null, gameboard[3], gameboard[1]);
        gameboard[3].setNeighbors(gameboard[2], null, gameboard[4], gameboard[11]);
        gameboard[4].setNeighbors(gameboard[3], null, null, gameboard[5]);
        gameboard[5].setNeighbors(gameboard[13], gameboard[4], null, gameboard[6]);
        gameboard[6].setNeighbors(gameboard[7], gameboard[5], null, null);
        gameboard[7].setNeighbors(gameboard[0], gameboard[15], gameboard[6], null);
        gameboard[8].setNeighbors(null, gameboard[9], gameboard[15], null);
        gameboard[9].setNeighbors(gameboard[1], gameboard[10], gameboard[17], gameboard[8]);
        gameboard[10].setNeighbors(null, null, gameboard[11], gameboard[9]);
        gameboard[11].setNeighbors(gameboard[10], gameboard[3], gameboard[12], gameboard[19]);
        gameboard[12].setNeighbors(gameboard[11], null, null, gameboard[13]);
        gameboard[13].setNeighbors(gameboard[21], gameboard[12], gameboard[5], gameboard[14]);
        gameboard[14].setNeighbors(gameboard[15], gameboard[13], null, null);
        gameboard[15].setNeighbors(gameboard[8], gameboard[23], gameboard[14], gameboard[7]);
        gameboard[16].setNeighbors(null, gameboard[17], gameboard[23], null);
        gameboard[17].setNeighbors(gameboard[9], gameboard[18], null, gameboard[16]);
        gameboard[18].setNeighbors(null, null, gameboard[19], gameboard[17]);
        gameboard[19].setNeighbors(gameboard[18], gameboard[11], gameboard[20], null);
        gameboard[20].setNeighbors(gameboard[19], null, null, gameboard[21]);
        gameboard[21].setNeighbors(null, gameboard[20], gameboard[13], gameboard[22]);
        gameboard[22].setNeighbors(gameboard[23], gameboard[21], null, null);
        gameboard[23].setNeighbors(gameboard[16], null, gameboard[22], gameboard[15]);
    }

    public enum PlayerColor{
        RED,
        BLUE
    }
}
