package com.anton.ninemensmorris;

import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton on 2016-11-30.
 */

public class LoadGameTask extends AsyncTask<String, Void, List<NMMGame>> {

    MainActivity activity;

    public LoadGameTask(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected List<NMMGame> doInBackground(String... params) {
        Gson gson = new Gson();
        List<NMMGame> games = new ArrayList();
        try{
            String file = params[0];
            InputStream inputStream = activity.getApplicationContext().openFileInput(file);

            if(inputStream!=null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String readString = "";
                while((readString = bufferedReader.readLine())!= null){
                    NMMGame loadedGame = gson.fromJson(readString, NMMGame.class);
                    loadedGame.setNeighbors();
                    games.add(loadedGame);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
            return games;
        }

        return games;
    }

    @Override
    protected void onPostExecute(List<NMMGame> nmmGames) {
        activity.setGamesList(nmmGames);
        activity.showToast("Loaded " + nmmGames.size() + " games!");
    }

    @Override
    protected void onCancelled() {
        activity.showToast("Error: Could not load games!");
    }
}
