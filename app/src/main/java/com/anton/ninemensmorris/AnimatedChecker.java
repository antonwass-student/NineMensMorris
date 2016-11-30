package com.anton.ninemensmorris;

/**
 * Created by Anton on 2016-11-30.
 */


public class AnimatedChecker {

    private float posX, posY;
    private Checker checker;

    private float dX, dY;
    private boolean done = false;

    Node from, to;

    int updates = 0;
    int frames = 15;

    public AnimatedChecker(Node from, Node to){
        //calc dX, dY
        this.checker = from.getChecker();
        this.from = from;
        this.to = to;

        this.posX = from.getPosX();
        this.posY = from.getPosY();

        dX = ((float)to.getPosX() - (float)from.getPosX())/(float)frames;
        dY = ((float)to.getPosY() - (float)from.getPosY())/(float)frames;

        from.setAnimated(true);
        to.setAnimated(true);
    }

    public void update(){
        //update position
        //when reached dest. set done=true;
        if(updates>=frames)
            animationDone();

        posX += dX;
        posY += dY;
        updates++;
    }

    public void animationDone(){
        done = true;
        from.setAnimated(false);
        to.setAnimated(false);
    }

    /**
     * Not scaled!
     * @return
     */
    public float getX(){
        return posX;
    }

    /**
     * Not scaled!
     * @return
     */
    public float getY(){
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

    public boolean isDone(){
        return done;
    }

    public Checker getChecker(){
        return checker;
    }
}
