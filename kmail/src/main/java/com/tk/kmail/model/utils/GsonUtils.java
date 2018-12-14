package com.tk.kmail.model.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by TangKai on 2017/11/19.
 */

public class GsonUtils {

    private static Gson gson;

    public static Gson gson() {
        if (gson == null)
            synchronized (GsonUtils.class) {
                if (gson == null)
                    gson = new Gson();
            }
        return gson;
    }
    public static boolean isJson(String jsonString) {
        if (TextUtils.isEmpty(jsonString))
            return false;
        try {
            new JSONObject(jsonString);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            new JSONArray(jsonString);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static G build(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonElement parse = jsonParser.parse(json);
        return new G(parse);
    }

    public static class G {
        private final JsonElement mO;

        public G(JsonElement o) {
            this.mO = o;
        }

        public G get(String key) throws Exception {
            if (mO.isJsonObject())
                return new G(mO.getAsJsonObject().get(key));
            throw new Exception("类型错误 this:Map tag:" + this.mO.getClass().getName());
        }

        public G getIndex(int index) throws Exception {
            if (mO.isJsonArray())
                return new G(mO.getAsJsonArray().get(index));
            throw new Exception("类型错误 this:List tag:" + this.mO.getClass().getName());
        }

        public String getString() throws Exception {
            return mO.getAsString();
        }


        public int getInt() throws Exception {
            return mO.getAsInt();
        }

        public double getDouble() throws Exception {
            return mO.getAsDouble();

        }

        public boolean getBoolean() throws Exception {
            return mO.getAsBoolean();
        }

        public long getLong() throws Exception {
            return mO.getAsLong();
        }

        public <T> T getClassObj(Class<T> clasz) {
            return gson().fromJson(mO, clasz);
        }

        public JsonElement getObj() {
            return mO;
        }

        public JsonObject getMap() {
            return (JsonObject) mO;
        }

        public JsonArray getList() {
            return (JsonArray) mO;
        }

        @Override
        public String toString() {
            if (mO != null)
                return mO.toString();
            return "null";
        }


    }

    public static class BuildJson {

        private final HashMap<String, Object> stringObjectHashMap;

        private BuildJson() {
            stringObjectHashMap = new HashMap<>();
        }

        public static BuildJson b() {
            return new BuildJson();
        }

        public static Object createArras(Object... obj) {
            return obj;
        }

        public BuildJson createObject(String key, Object value) {
            stringObjectHashMap.put(key, value);
            return this;
        }

        public Object endObj() {
            return stringObjectHashMap;
        }

    }
}
