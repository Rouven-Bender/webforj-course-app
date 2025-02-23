package rbender.controllers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import rbender.Application;

public class ConfigProvider {
    private static ConfigProvider instance = null;
    private Map<String, String> conf = null;

    private ConfigProvider() {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        try {
            conf = gson.fromJson(Application.getResourceAsString("config.json").orElse(""), type);
        } catch (IOException e){
            conf = new HashMap<String, String>();
        }
    }

    public String getConfig(String key) {
        return Optional.ofNullable(conf.get(key)).orElse("");
    }
   
    public static synchronized ConfigProvider getInstance(){
        if (instance == null) {
            instance = new ConfigProvider();
        }
        return instance;
    }
}
