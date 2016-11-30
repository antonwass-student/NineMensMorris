package com.anton.ninemensmorris;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 2016-11-28.
 */

public class Node{
    private Checker checker;
    private transient Node up;
    private transient Node right;
    private transient Node left;
    private transient Node down;

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

    /***
     * Looks in horizontal and vertical directions if there are 3 checkers in a row.
     * @return
     */
    public boolean checkMill(){

        Node probe = this;
        int points = 1;
        //vertical
        //up
        while(probe!=null && probe.getChecker()!=null){
            probe = probe.getUp();
            if(probe!=null && probe.getChecker()!=null)
                if(probe.getChecker().getPlayer().equals(this.getChecker().getPlayer()))
                    points++;
        }
        //down
        probe = this;
        while(probe!=null && probe.getChecker()!=null){
            probe = probe.getDown();
            if(probe !=null && probe.getChecker()!=null)
                if(probe.getChecker().getPlayer().equals(this.getChecker().getPlayer()))
                    points++;
        }

        if(points == 3)
            return true;

        probe = this;
        points = 1;
        //vertical
        //right
        while(probe!=null && probe.getChecker()!=null){
            probe = probe.getRight();
            if(probe!=null && probe.getChecker()!=null)
                if(probe.getChecker().getPlayer().equals(this.getChecker().getPlayer()))
                    points++;
        }
        //left
        probe = this;
        while(probe!=null && probe.getChecker()!=null){
            probe = probe.getLeft();
            if(probe!=null && probe.getChecker()!=null)
                if(probe.getChecker().getPlayer().equals(this.getChecker().getPlayer()))
                    points++;
        }

        if(points == 3)
            return true;
        else
            return false;
    }

    public List<Node> getFreeNeighbors(){
        List<Node> nodes = new ArrayList();

        if(up != null && up.getChecker() == null)
            nodes.add(up);
        if(right != null && right.getChecker() == null)
            nodes.add(right);
        if(down != null && down.getChecker() == null)
            nodes.add(down);
        if(left != null && left.getChecker() == null)
            nodes.add(left);

        return nodes;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosXScaled(int width){
        float w = width/7;
        return (int)(w/2+posX*w);
    }

    public int getPosYScaled(int height){
        float h = height/7;

        return (int)(h/2+posY*h);
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
