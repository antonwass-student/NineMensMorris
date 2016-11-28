package com.anton.ninemensmorris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;

/**
 * Created by Anton on 2016-11-28.
 */

public class CustomView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    Paint paintRed;
    Paint paintBlue;
    Paint paintWhite;
    Paint paintBlack;
    NMMGame game;

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setGame(NMMGame game){
        this.game = game;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        drawNodes(game.getGameboard());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void init(){
        holder = getHolder();
        holder.addCallback(this);

        paintBlue = new Paint();
        paintBlue.setColor(Color.BLUE);

        paintRed = new Paint();
        paintRed.setColor(Color.RED);

        paintWhite = new Paint();
        paintWhite.setColor(Color.WHITE);

        paintBlack = new Paint();
        paintBlack.setColor(Color.BLACK);
    }

    public void drawNodes(Node[] nodes){
        Canvas canvas = holder.lockCanvas();

        drawBoard(canvas, nodes);

        Paint usePaint = null;
        for(int i = 0; i < nodes.length; i++){
            if(nodes[i].getChecker() == null)
                usePaint = paintBlack;
            else if(nodes[i].getChecker().getPlayer().equals(NMMGame.PlayerColor.BLUE))
                usePaint = paintBlue;
            else
                usePaint = paintRed;

            float w = canvas.getWidth()/7;
            float h = canvas.getHeight()/7;

            canvas.drawCircle(
                    w/2 + nodes[i].getPosX() * w,
                    h/2 + nodes[i].getPosY() * h,
                    20, usePaint);
        }

        holder.unlockCanvasAndPost(canvas);
    }
    
    private void drawBoard(Canvas canvas, Node[] nodes){
        canvas.drawRect(0,0, canvas.getWidth(), canvas.getHeight(), paintWhite);
        paintBlack.setStyle(Paint.Style.STROKE);
        paintBlack.setStrokeWidth(3);

        float w = canvas.getWidth()/7;
        float h = canvas.getHeight()/7;

        canvas.drawRect(
                w/2+nodes[0].getPosX()* w,
                h/2+nodes[0].getPosY()* h,
                w/2+nodes[4].getPosX()* w,
                h/2+nodes[4].getPosY()* h,
                paintBlack);
        canvas.drawRect(
                w/2+nodes[8].getPosX()* w,
                h/2+nodes[8].getPosY()* h,
                w/2+nodes[12].getPosX()*w,
                h/2+nodes[12].getPosY() * h,
                paintBlack);
        canvas.drawRect(
                w/2+nodes[16].getPosX()* w,
                h/2+nodes[16].getPosY()* h,
                w/2+nodes[20].getPosX()* w,
                h/2+nodes[20].getPosY()* h,
                paintBlack);

        canvas.drawLine(
                w/2+nodes[1].getPosX()*w,
                h/2+nodes[1].getPosY()*h,
                w/2+nodes[17].getPosX()*w,
                h/2+nodes[17].getPosY()*h,
                paintBlack);

        canvas.drawLine(
                w/2+nodes[3].getPosX()*w,
                h/2+nodes[3].getPosY()*h,
                w/2+nodes[19].getPosX()*w,
                h/2+nodes[19].getPosY()*h,
                paintBlack);

        canvas.drawLine(
                w/2+nodes[5].getPosX()*w,
                h/2+nodes[5].getPosY()*h,
                w/2+nodes[21].getPosX()*w,
                h/2+nodes[21].getPosY()*h,
                paintBlack);

        canvas.drawLine(
                w/2+nodes[7].getPosX()*w,
                h/2+nodes[7].getPosY()*h,
                w/2+nodes[23].getPosX()*w,
                h/2+nodes[23].getPosY()*h,
                paintBlack);

        paintBlack.setStyle(Paint.Style.FILL);
    }

    /*
    private class DrawThread implements Runnable{
        boolean running = true;

        @Override
        public void run() {

            while(running){
                Canvas canvas = holder.lockCanvas();

                canvas.drawRect(0,0,100,100, paint);

                holder.unlockCanvasAndPost(canvas);
            }

        }
    }*/
}
