package com.anton.ninemensmorris;

import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 2016-11-28.
 */

public class NMMGame {

    private String name;
    private PlayerColor winner;
    private PlayerColor turn;
    private GameStates currentState = GameStates.PLACE;

    private Node[] gameboard = new Node[24];
    private ArrayList<Checker> blueCheckers = new ArrayList();
    private ArrayList<Checker> redCheckers = new ArrayList();

    private List<Checker> currentCheckers;

    private Node selectedNode;

    private AnimatedChecker animation;

    private List<Node> availableMoves;
    private List<Node> availableNodes;
    private List<Node> placedCheckers;
    private List<Node> killableNodes;



    public NMMGame(){
        init();
    }

    public void handleInput(Node touchedNode){

        if(touchedNode != null) {
            //if node was touched. do nothing.
            switch (currentState) {
                case PLACE:
                    //attempt to place a checker where user touched.
                    placeChecker(touchedNode);
                    break;
                case KILL:
                    //attempt to delete checker
                    killNode(touchedNode);
                    break;
                case MOVE:
                    //show available nodes to move to
                    moveChecker(touchedNode);
                    break;
                case SELECT:
                    //show selectable nodes
                    selectNode(touchedNode);
                    break;
            }
        }
    }

    public void prepareTurn(){

        if(turn == PlayerColor.BLUE)
            currentCheckers = blueCheckers;
        else
            currentCheckers = redCheckers;

        if(currentCheckers.size() > 0 ){
            //place a checker
            availableNodes = calcAvailableNodes();
            currentState = GameStates.PLACE;
        }else{
            //select and move a checker
            if(checkClosedIn() == true){
                if(turn.equals(PlayerColor.BLUE))
                    winner = PlayerColor.RED;
                else
                    winner = PlayerColor.BLUE;

                currentState = GameStates.GAMEOVER;

                return;
            }

            placedCheckers = calcPlacedCheckers();
            currentState = GameStates.SELECT;

        }
    }

    public void placeChecker(Node putHere){

        if(putHere.getChecker() == null){
            putHere.setChecker(currentCheckers.remove(0));

            if(putHere.checkMill()){
                //kill someone!
                killableNodes = calcKillableNodes();
                currentState = GameStates.KILL;
            }else{
                nextTurn();
            }
        }
    }

    public void selectNode(Node selected){
        //show all available moves.
        if(selected.getChecker()!=null && selected.getChecker().getPlayer().equals(turn)){
            if(selected.getFreeNeighbors().size()>0){
                this.selectedNode = selected;
                this.availableMoves = selected.getFreeNeighbors();
                this.currentState = GameStates.MOVE;
            }
        }
    }

    public void moveChecker(Node to){

        List<Node> neighbors = selectedNode.getFreeNeighbors();
        if(selectedNode == to){
            currentState = GameStates.SELECT;
            selectedNode = null;
            return;
        }

        if(neighbors.contains(to)){
            this.animation = new AnimatedChecker(selectedNode, to);
            to.setChecker(selectedNode.takeChecker());
            //do some animation

            if(to.checkMill()){
                //kill someone!
                killableNodes = calcKillableNodes();
                currentState = GameStates.KILL;
            }else{
                nextTurn();
            }
        }
    }

    public AnimatedChecker getAnimation() {
        return animation;
    }

    public void setAnimation(AnimatedChecker animation) {
        this.animation = animation;
    }

    public Node getSelectedNode() {
        return selectedNode;
    }

    public void killNode(Node toKill){
        if(toKill.getChecker()!= null && !toKill.getChecker().getPlayer().equals(turn)){
            toKill.deleteChecker();
            nextTurn();
        }
    }

    public Node[] getGameboard(){
        return this.gameboard;
    }

    public GameStates getGameState(){
        return currentState;
    }

    public PlayerColor getTurn(){
        return turn;
    }

    public PlayerColor getWinner(){return winner;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Checker> getBlueCheckers() {
        return blueCheckers;
    }

    public ArrayList<Checker> getRedCheckers() {
        return redCheckers;
    }

    private List<Node> calcKillableNodes(){
        List<Node> nodes = new ArrayList();
        for(Node n : gameboard){
            if(n.getChecker() != null)
                if(!n.getChecker().getPlayer().equals(turn))
                    nodes.add(n);

        }
        return nodes;
    }

    private boolean checkClosedIn(){
        for(Node n: gameboard)
            if(n.getChecker() != null)
                if(n.getChecker().getPlayer().equals(turn))
                    if(n.getFreeNeighbors().size() > 0)
                        return false;

        return true;
    }

    private void nextTurn(){
        if(turn == PlayerColor.BLUE){
            turn = PlayerColor.RED;
        }else{
            turn = PlayerColor.BLUE;
        }

        prepareTurn();
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

        prepareTurn();
    }

    private List<Node> calcPlacedCheckers(){
        List<Node> nodes = new ArrayList();

        for(Node n : gameboard){
            if(n.getChecker() != null && n.getChecker().getPlayer().equals(turn)){
                nodes.add(n);
            }
        }

        return nodes;
    }

    public List<Node> calcAvailableNodes(){
        List<Node> nodes = new ArrayList();

        for(Node n : gameboard){
            if(n.getChecker() == null){
                nodes.add(n);
            }
        }

        return nodes;
    }

    public void setNeighbors(){
            gameboard[0].setNeighbors(null, gameboard[1], gameboard[7], null, 0, 0);
            gameboard[1].setNeighbors(null, gameboard[2], gameboard[9], gameboard[0], 3, 0);
            gameboard[2].setNeighbors(null, null, gameboard[3], gameboard[1], 6, 0);
            gameboard[3].setNeighbors(gameboard[2], null, gameboard[4], gameboard[11], 6, 3);
            gameboard[4].setNeighbors(gameboard[3], null, null, gameboard[5], 6, 6);
            gameboard[5].setNeighbors(gameboard[13], gameboard[4], null, gameboard[6], 3, 6);
            gameboard[6].setNeighbors(gameboard[7], gameboard[5], null, null, 0, 6);
            gameboard[7].setNeighbors(gameboard[0], gameboard[15], gameboard[6], null, 0, 3);
            gameboard[8].setNeighbors(null, gameboard[9], gameboard[15], null, 1, 1);
            gameboard[9].setNeighbors(gameboard[1], gameboard[10], gameboard[17], gameboard[8], 3, 1);
            gameboard[10].setNeighbors(null, null, gameboard[11], gameboard[9], 5, 1);
            gameboard[11].setNeighbors(gameboard[10], gameboard[3], gameboard[12], gameboard[19], 5, 3);
            gameboard[12].setNeighbors(gameboard[11], null, null, gameboard[13], 5, 5);
            gameboard[13].setNeighbors(gameboard[21], gameboard[12], gameboard[5], gameboard[14], 3, 5);
            gameboard[14].setNeighbors(gameboard[15], gameboard[13], null, null, 1, 5);
            gameboard[15].setNeighbors(gameboard[8], gameboard[23], gameboard[14], gameboard[7], 1, 3);
            gameboard[16].setNeighbors(null, gameboard[17], gameboard[23], null, 2, 2);
            gameboard[17].setNeighbors(gameboard[9], gameboard[18], null, gameboard[16], 3, 2);
            gameboard[18].setNeighbors(null, null, gameboard[19], gameboard[17], 4, 2);
            gameboard[19].setNeighbors(gameboard[18], gameboard[11], gameboard[20], null, 4, 3);
            gameboard[20].setNeighbors(gameboard[19], null, null, gameboard[21], 4, 4);
            gameboard[21].setNeighbors(null, gameboard[20], gameboard[13], gameboard[22], 3, 4);
            gameboard[22].setNeighbors(gameboard[23], gameboard[21], null, null, 2, 4);
            gameboard[23].setNeighbors(gameboard[16], null, gameboard[22], gameboard[15], 2, 3);
    }

    public enum PlayerColor{
        RED,
        BLUE
    }

    public enum GameStates{
        PLACE,
        SELECT,
        MOVE,
        KILL,
        GAMEOVER
    }

    public enum GameMode{
        NINE,
        TWELVE
    }
}
