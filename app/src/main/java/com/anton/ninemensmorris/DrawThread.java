package com.anton.ninemensmorris;

import android.graphics.Canvas;
import android.provider.Settings;

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
        try{
            while(running){
                double draw_time = System.currentTimeMillis();
                Canvas canvas = view.getHolder().lockCanvas();

                if(canvas!=null){
                    view.draw(canvas);

                    view.getHolder().unlockCanvasAndPost(canvas);
                }
                int time_to_wait = (int)(TIME_PER_TICK - (System.currentTimeMillis() - draw_time));
                if(time_to_wait>0)
                    this.sleep(time_to_wait);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            this.interrupt();
        }
    }
}