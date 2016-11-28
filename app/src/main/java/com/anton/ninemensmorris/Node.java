package com.anton.ninemensmorris;

/**
 * Created by Anton on 2016-11-28.
 */

public class Node {
    private Checker spot;
    private Node up;
    private Node right;
    private Node left;
    private Node down;

    public void setNeighbors(Node up, Node right, Node down, Node left){
        this.up = up;
        this.right = right;
        this.down = down;
        this.left = left;
    }
}
