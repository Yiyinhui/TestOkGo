//package com.example.testokgo;
//
//import android.util.JsonReader;
//
//import com.lzy.okgo.callback.AbsCallback;
//
//import java.lang.reflect.Type;
//
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//
//public abstract class JsonCallback<T> extends AbsCallback {
//
//    private Type type;
//    private Class<T> clazz;
//
//    public JsonCallback(Type type){
//        this.type = type;
//    }
//
//    public JsonCallback(Class<T> clazz){
//        this.clazz = clazz;
//    }
//
//    @Override
//    public Object convertResponse(okhttp3.Response response) throws Throwable {
//        ResponseBody body = response.body();
//        if(body==null) return null;
//
//        T data = null;
//        Gson gson = new Gson();
//        JsonReader jsonReader = new JsonReader(body.charStream());
//        if(type!=null) data = gson.fromJson(jsonReader,type);
//        if(clazz!=null) data = gson.fromJson(jsonReader,clazz);
//        return data;
//    }
//}
