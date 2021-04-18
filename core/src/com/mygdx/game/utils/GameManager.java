package com.mygdx.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Logger;

import java.util.ArrayList;

public class GameManager {

    public static final Logger log = new Logger(GameManager.class.getName(), Logger.DEBUG);
    private  Preferences prefs;
    Json json;

    public static final GameManager INSTANCE = new GameManager();

    private GameManager() {
        json = new Json();
        loadOptions();
    }

    public void loadOptions() {
        json=new Json();
        FileHandle file = Gdx.files.local("options.json");
        if (!file.exists()) {
            prefs = new Preferences();
            saveOptions();
        } else {
            prefs = json.fromJson(Preferences.class, file.readString());
        }
    }

    public ArrayList<Result> loadResults(){
        FileHandle file = Gdx.files.local("results.json");
        ArrayList<Result> resultList;
        if (!file.exists()) {
            resultList=new ArrayList<Result>();
        } else {
            resultList=new ArrayList<Result>();
            ArrayList<JsonValue> list = json.fromJson(ArrayList.class, file.readString());
            for (JsonValue v : list) {
                resultList.add(json.readValue(Result.class, v));
                //log.debug(json.readValue(Result.class, v).toString());
            }
        }
        return (resultList);
    }

    public void saveResult(Result result){
        json=new Json();

        FileHandle file = Gdx.files.local("results.json");
        ArrayList<Result> resultList = loadResults();
        resultList.add(result);
        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(true);
        json.setOutputType(JsonWriter.OutputType.json);
        file.writeString(json.toJson(resultList), false);
        //log.debug(getBest3());

    }

    public String getBest3(){
        ArrayList<String> nameList = new ArrayList<String>();
        ArrayList<Integer> winList =  new ArrayList<Integer>();
        int iFirst=0, iSecond=0, iThird=0;
        ArrayList<Result> resultList = loadResults();
        if (resultList.size()==0)return("");
        for(Result v: resultList){
            if(nameList.contains(v.getWinner())){
                winList.set(nameList.indexOf(v.getWinner()),winList.get(nameList.indexOf(v.getWinner()))+1);

            }else{
                nameList.add(v.getWinner());
                winList.add(1);
            }
        }
        log.debug(""+nameList.toString());
        log.debug(""+winList.toString());
        for(int iStevec1=0; iStevec1<winList.size(); iStevec1++){
            if(winList.get(iStevec1)>=winList.get(iFirst)){
                //log.debug(""+iStevec1+"/"+winList.size());
                iThird=iSecond;
                iSecond=iFirst;
                iFirst=iStevec1;
            }else if(winList.get(iStevec1)>=winList.get(iSecond)||iSecond==iFirst){
                iThird=iSecond;
                iSecond=iStevec1;
            }else if(winList.get(iStevec1)>=winList.get(iThird)||iThird==iFirst||iThird==iSecond) {
                iThird = iStevec1;
            }
        }
        log.debug(""+iFirst+" "+iSecond+" "+ iThird);

        String sTemp="1. "+nameList.get(iFirst)+", wins: "+ winList.get(iFirst);
        if(iSecond!=iFirst){
            sTemp+="\n2. "+nameList.get(iSecond)+", wins: "+ winList.get(iSecond);
        }
        if(iSecond!=iThird){
            sTemp+="\n3. "+nameList.get(iThird)+", wins: "+ winList.get(iThird);
        }
        //log.debug(""+nameList.toString());
        //log.debug(""+winList.toString());

        return(sTemp);
    }

    public void saveOptions(){
        json=new Json();
        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(true);
        json.setOutputType(JsonWriter.OutputType.json);

        log.debug(json.prettyPrint(prefs));
        log.debug(prefs.getSound()+" "+prefs.getTileAmmount()+" "+prefs.getPlayer1name()+" "+prefs.getPlayer2name());
        //System.out.println(json.toJson(prefs, Preferences.class));
        String writeString = json.toJson(prefs);
        FileHandle file = Gdx.files.local("options.json");
        file.writeString(writeString, false);
    }

    public Preferences getPrefs() {
        return prefs;
    }

    public void setPrefs(Preferences prefs) {
        this.prefs = prefs;
    }
}
