package com.anton.ninemensmorris;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Anton on 2016-11-30.
 */

public class SaveGameTask extends AsyncTask<List<NMMGame>, Void, Boolean> {

    private MainActivity activity;

    public SaveGameTask(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(List<NMMGame>... params) {
        Gson gson = new Gson();
        List<NMMGame> gamesToSave = params[0];
        String filename = "games.dat";
        FileOutputStream outputStream = null;
        PrintWriter printWriter = null;

        try{
            outputStream = activity.openFileOutput(filename, Context.MODE_PRIVATE);
            printWriter = new PrintWriter(outputStream);

            for(NMMGame g : gamesToSave){
                if(!g.getGameState().equals(NMMGame.GameStates.GAMEOVER)){
                    String json = gson.toJson(g);
                    printWriter.println(json);
                }
            }
            printWriter.flush();
            return true;

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally {
            printWriter.close();
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        activity.showToast("Games saved successfully!");
    }

    @Override
    protected void onCancelled() {
        activity.showToast("Could not update ");
    }
}
