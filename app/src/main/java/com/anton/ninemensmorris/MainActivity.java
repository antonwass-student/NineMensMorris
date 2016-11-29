package com.anton.ninemensmorris;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

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

    CustomView view;
    NMMGame game;
    List<NMMGame> gamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        gamesList = new ArrayList();

        loadGames();

        loadSavedStateGame();

        if(this.game == null)
            this.game = new NMMGame();

        this.view = (CustomView)findViewById(R.id.surface_view);
        this.view.setGame(game);
        this.view.setStateText((TextView)findViewById(R.id.game_state));
    }

    @Override
    public void onBackPressed() {
        saveGame();
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        saveGame();
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        saveGame();
        super.onPause();
    }

    @Override
    protected void onStop() {
        saveGame();
        super.onStop();
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
            this.view.draw();
        }

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

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return game;
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    private void newGame(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game name");

        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                saveGame(); //save old games

                game = new NMMGame();
                game.setName(input.getText().toString());
                view.setGame(game);
                view.draw();
                gamesList.add(game);
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

    private void saveGame(){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!game.getGameState().equals(NMMGame.GameStates.GAMEOVER)){
            String activeGameJson = gson.toJson(game);
            //save in background.


            editor.putString("game", activeGameJson);
            editor.commit();
        }else{
            editor.remove("game");
            editor.commit();
        }

        //save to file system
        String filename = "games.dat";
        FileOutputStream outputStream = null;
        PrintWriter printWriter = null;

        try{
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            printWriter = new PrintWriter(outputStream);

            for(NMMGame g : gamesList){
                if(!g.getGameState().equals(NMMGame.GameStates.GAMEOVER)){
                    String json = gson.toJson(g);
                    printWriter.println(json);
                }
            }
            printWriter.flush();

        }catch(Exception e){
            e.printStackTrace();
        }finally {
            printWriter.close();
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void loadSavedStateGame(){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String prevGameJson = sharedPreferences.getString("game", "");

        if(!prevGameJson.equals("")){

            this.game = gson.fromJson(prevGameJson, NMMGame.class);
            this.game.setNeighbors();
        }
    }

    private void loadGames(){
        Gson gson = new Gson();
        try{
            InputStream inputStream = getApplicationContext().openFileInput("games.dat");

            if(inputStream!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String readString = "";
                gamesList.clear();
                while((readString = bufferedReader.readLine())!= null){
                    NMMGame loadedGame = gson.fromJson(readString, NMMGame.class);
                    loadedGame.setNeighbors();
                    gamesList.add(loadedGame);
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
