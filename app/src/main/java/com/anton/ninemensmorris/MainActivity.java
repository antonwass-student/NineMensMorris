package com.anton.ninemensmorris;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CustomView view;
    private NMMGame game;
    private List<NMMGame> gamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        this.view = (CustomView)findViewById(R.id.surface_view);
        this.view.setStateText((TextView)findViewById(R.id.game_state));

        LinearLayout main = (LinearLayout)findViewById(R.id.game_layout);

        //portrait

        if(getResources().getConfiguration().orientation == 1 )
            main.setOrientation(LinearLayout.VERTICAL);
        else
            main.setOrientation(LinearLayout.HORIZONTAL);


        gamesList = new ArrayList();

        loadGames();
        loadSavedStateGame();

        if(this.game == null)
            newGame();
        else{
            this.view.setGame(game);
        }
    }

    @Override
    public void onBackPressed() {
        //saveGame();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        //saveGame();
        super.onPause();
    }

    @Override
    protected void onStop() {
        //saveGame();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        saveGame();super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SubMenu subMenu = menu.getItem(1).getSubMenu();
        subMenu.clear();

        for(int i = 0; i < gamesList.size(); i++){
            subMenu.add(2, i, i, gamesList.get(i).getName());
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getGroupId() == 2){
            //submenu
            //load selected game.
            saveGame();
            this.game = gamesList.get(item.getItemId());
            this.game.setNeighbors();
            this.view.setGame(game);
            //this.view.draw();
            setTitle(game.getName());
            return true;
        }else{
            switch(item.getItemId()){
                case R.id.new_game:
                    newGame();
                    return true;
                case R.id.load_game:
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(game.getGameState().equals(NMMGame.GameStates.GAMEOVER)){
            gamesList.remove(game);
            invalidateOptionsMenu();
        }
        view.drawTextInfo();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    private void newGame(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        builder.setTitle("Options");

        final View optionsView = factory.inflate(R.layout.options_dialog, null);

        final EditText input = (EditText)optionsView.findViewById(R.id.game_name);

        final RadioGroup map_group = (RadioGroup)optionsView.findViewById(R.id.map_group);
        final RadioGroup mode_group = (RadioGroup)optionsView.findViewById(R.id.mode_group);



        input.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(optionsView);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                saveGame(); //save old games

                NMMGame.Map map;
                NMMGame.GameMode mode;

                RadioButton selectedMap = (RadioButton) map_group.findViewById(map_group.getCheckedRadioButtonId());
                RadioButton selectedMode = (RadioButton) mode_group.findViewById(mode_group.getCheckedRadioButtonId());

                switch(selectedMap.getText().toString()){
                    case "Big":
                        map = NMMGame.Map.BIG;
                        break;
                    case "Normal":
                        map = NMMGame.Map.NORMAL;
                        break;
                    default:
                        map = NMMGame.Map.NORMAL;
                }

                switch(selectedMode.getText().toString()){
                    case "Nine":
                        mode = NMMGame.GameMode.NINE;
                        break;
                    case "Six":
                        mode = NMMGame.GameMode.SIX;
                        break;
                    case "Three":
                        mode = NMMGame.GameMode.THREE;
                        break;
                    default:
                        mode = NMMGame.GameMode.NINE;
                        break;

                }

                game = new NMMGame(mode, map);
                game.setName(input.getText().toString());
                view.setGame(game);
                //view.draw();
                gamesList.add(game);
                setTitle(game.getName());
                invalidateOptionsMenu();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        builder.show();
    }

    /**
     * Saves all active games to the disk.
     * Also saves current active game so that the user can continue
     * his/her game when the app is opened again.
     */
    private void saveGame(){
        showToast("Saving games");
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(game!=null){
            if(!game.getGameState().equals(NMMGame.GameStates.GAMEOVER)){
                String activeGameJson = gson.toJson(game);
                //save in background.


                editor.putString("game", activeGameJson);
                editor.commit();
            }else{
                editor.remove("game");
                editor.commit();
            }
        }

        //save to file system
        //...using a task! ;)
        SaveGameTask task = new SaveGameTask(this);
        task.execute(gamesList);

    }

    /**
     * Loads an interrupted game. Not to be confused with games saved on the disk.
     */
    private void loadSavedStateGame(){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String prevGameJson = sharedPreferences.getString("game", "");

        if(!prevGameJson.equals("")){

            this.game = gson.fromJson(prevGameJson, NMMGame.class);

            if(game.getGameState().equals(NMMGame.GameStates.GAMEOVER)){
                game = null;
            }else{
                this.game.setNeighbors();
                setTitle(game.getName());
            }
        }
    }

    /**
     * Loads all games saved on the disk.
     */
    private void loadGames(){
        LoadGameTask task = new LoadGameTask(this);
        task.execute("games.dat");
    }

    public void showToast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setGamesList(List<NMMGame> games){
        this.gamesList = games;
        invalidateOptionsMenu();
    }
}
