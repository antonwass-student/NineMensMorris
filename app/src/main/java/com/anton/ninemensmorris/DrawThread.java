package com.anton.ninemensmorris;

import android.graphics.Canvas;

/**
 * Created by Anton on 2016-11-30.
 */

public class DrawThread extends Thread{
    private boolean running = true;

    private CustomView view;

    final float FPS = 60.0f;

    final float TIME_PER_TICK = 1000/FPS;

    public DrawThread(CustomView view){
        this.view = view;
    }

    public void stopRunning(){
        this.running = false;
    }

    @Override
    public void run() {
        double start_time = System.currentTimeMillis();

        try{
            while(running){

                Canvas canvas = view.getHolder().lockCanvas();

                if(canvas!=null){
                    view.draw(canvas);

                    view.getHolder().unlockCanvasAndPost(canvas);
                    start_time = System.currentTimeMillis();
                }

                while(System.currentTimeMillis() - start_time < TIME_PER_TICK);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            this.interrupt();
        }
    }
}