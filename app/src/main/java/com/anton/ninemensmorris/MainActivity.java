package com.anton.ninemensmorris;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    CustomView view;
    NMMGame game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        this.game = (NMMGame)getLastCustomNonConfigurationInstance();
        if(this.game == null)
            this.game = new NMMGame();

        this.view = (CustomView)findViewById(R.id.surface_view);
        this.view.setGame(game);
        this.view.setStateText((TextView)findViewById(R.id.game_state));
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return game;
    }

    @Override
    protected void onStart(){
        super.onStart();
    }
}
