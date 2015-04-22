package com.dumposk129.create.stories.app.api;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Quiz {

    public static JSONObject getAllQuiz(){
        JSONParser jParser = new JSONParser();
        List<NameValuePair> params = new ArrayList<>();
        return jParser.makeHttpRequest(ApiConfig.hostname(API._SHOWQUIZ) , ApiConfig.GET, params);
    }

    public static JSONObject getAllAnswer(){
        JSONParser jParser = new JSONParser();
        List<NameValuePair> params = new ArrayList<>();
        return jParser.makeHttpRequest(ApiConfig.hostname(API._LISTCHOICE) , ApiConfig.GET, params);
    }

    public static JSONObject getAllQuestion(){
        JSONParser jParser = new JSONParser();
        List<NameValuePair> params = new ArrayList<>();
        return jParser.makeHttpRequest(ApiConfig.hostname(API._SHOWQUESTION) , ApiConfig.GET, params);
    }

}
