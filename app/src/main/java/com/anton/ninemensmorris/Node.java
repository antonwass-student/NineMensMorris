package com.anton.ninemensmorris;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 2016-11-28.
 */

public class Node {
    private Checker checker;
    private Node up;
    private Node right;
    private Node left;
    private Node down;

    private int posX, posY;

    public void setNeighbors(Node up, Node right, Node down, Node left, int posX, int posY){
        this.up = up;
        this.right = right;
        this.down = down;
        this.left = left;
        this.posX = posX;
        this.posY = posY;
    }

    public Checker getChecker(){
        return this.checker;
    }

    public void setChecker(Checker ch){
        this.checker = ch;
    }

    public Checker takeChecker(){
        Checker ch = checker;
        this.checker = null;
        return ch;
    }

    public void deleteChecker(){
        this.checker = null;
    }

    public boolean checkMill(){

        Node probe = this;
        int points = 1;
        //vertical
        //up
        while(probe!=null  && probe.getChecker().getPlayer().equals(this.getChecker().getPlayer())){
            probe = probe.getUp();
            if(probe!=null && probe.getChecker().getPlayer().equals(this.getChecker().getPlayer()))
                points++;
        }
        //down
        probe = this;
        while(probe!=null && probe.getChecker().getPlayer().equals(this.getChecker().getPlayer())){
            probe = probe.getDown();
            if(probe!=null  && probe.getChecker().getPlayer().equals(this.getChecker().getPlayer()))
                points++;
        }

        if(points == 3)
            return true;

        probe = this;
        points = 1;
        //vertical
        //right
        while(probe!=null  && probe.getChecker().getPlayer().equals(this.getChecker().getPlayer())){
            probe = probe.getRight();
            if(probe!=null && probe.getChecker().getPlayer().equals(this.getChecker().getPlayer()))
                points++;
        }
        //left
        probe = this;
        while(probe!=null && probe.getChecker().getPlayer().equals(this.getChecker().getPlayer())){
            probe = probe.getLeft();
            if(probe!=null  && probe.getChecker().getPlayer().equals(this.getChecker().getPlayer()))
                points++;
        }

        if(points == 3)
            return true;
        else
            return false;
    }

    public List<Node> getFreeNeighbors(){
        List<Node> nodes = new ArrayList();

        if(up.getChecker() == null)
            nodes.add(up);
        if(right.getChecker() == null)
            nodes.add(right);
        if(down.getChecker() == null)
            nodes.add(down);
        if(left.getChecker() == null)
            nodes.add(left);

        return nodes;
    }

    public Node getUp() {
        return up;
    }

    public Node getRight() {
        return right;
    }

    public Node getLeft() {
        return left;
    }

    public Node getDown() {
        return down;
    }
}
