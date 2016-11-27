package ua.com.slaviksoft.mytranslate2.GoogleTranslateApi;

import android.net.Uri;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ua.com.slaviksoft.mytranslate2.Model.Language;

/**
 * Created by Slavik on 27.11.2016.
 * Main wrapper on Google.translate API
 */

public class GoogleTranslate{

    private String API_KEY = "AIzaSyDdcY11bZp2C3r2UNRmONIsTgp1q22BLks"; //this is wrong - only for demonstrate.
    private String BASE_URL = "https://translation.googleapis.com/language/translate/v2";

    //get languages list
    public void getSupportedLanguages(String target, OnLanguagesRequestListener listener){

        String request = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath("languages")
                .appendQueryParameter("key", API_KEY)
                .appendQueryParameter("target", target)
                .build().toString();

        RequestTask requestTask = new RequestTask(new OnSupportedLanguagesResult(listener));
        requestTask.execute(request);
    }

    //get a translation
    public void translate(String source, String target, String text, OnTranslateRequestListener listener){

        String request = Uri.parse(BASE_URL)
                .buildUpon()
                .appendQueryParameter("key", API_KEY)
                .appendQueryParameter("source", source)
                .appendQueryParameter("target", target)
                .appendQueryParameter("q", text)
                .build().toString();

        RequestTask requestTask = new RequestTask(new OnTranslateResult(listener));
        requestTask.execute(request);
    }

    // async request to server
    private class RequestTask extends AsyncTask<String, Void, String> {

        final private OnRequestListener listener;
        final private OkHttpClient client;
        private int error = 0;

        public RequestTask(OnRequestListener listener){
            this.listener = listener;
            this.client = new OkHttpClient();
        }

        @Override
        protected String doInBackground(String... params) {

            Request request = new Request.Builder()
                    .url(params[0])
                    .build();

            String result = "";
            Response response = null;
            try {
                response = client.newCall(request).execute();
                if (response.code() == 200){
                    result = response.body().string();
                } else{
                    error = 1;
                }
            } catch (IOException e) {
                e.printStackTrace();
                error = 2;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (listener != null) {
                if (error == 0) listener.onRequestSuccess(s);
                    else listener.onRequestFailure(error);
            }
        }
    }

    // callback on translate
    private class OnTranslateResult implements OnRequestListener{

        private final OnTranslateRequestListener listener;

        public OnTranslateResult(OnTranslateRequestListener listener){
            this.listener = listener;
        }

        @Override
        public void onRequestSuccess(String result) {
            System.out.println(result);

            Gson gson = new Gson();
            GoogleTranslations translations = gson.fromJson(result, GoogleTranslations.class);
            if(listener != null) listener.OnTranslateRequestSuccess(translations.toString());
        }

        @Override
        public void onRequestFailure(int error) {
            if(listener != null) listener.OnTranslateRequestFailure(error);
        }
    }

    // callback on languages list request
    private class OnSupportedLanguagesResult implements OnRequestListener{

        private final OnLanguagesRequestListener listener;
        private final Gson gson;

        public OnSupportedLanguagesResult(OnLanguagesRequestListener listener){
            this.listener = listener;
            this.gson = new Gson();
        }

        @Override
        public void onRequestSuccess(String result) {

            GoogleLanguages languages = gson.fromJson(result, GoogleLanguages.class);
            if(listener != null)
                listener.onLanguagesRequestSuccess(languages.getLanguages());
        }

        @Override
        public void onRequestFailure(int error) {
            if(listener != null) listener.onLanguagesRequestFailure(error);
        }
    }

    // callback to activity after the getting languages
    public interface OnLanguagesRequestListener{
        void onLanguagesRequestSuccess(List<Language> languages);
        void onLanguagesRequestFailure(int error);
    }

    // callback to activity after the translating
    public interface OnTranslateRequestListener{
        void OnTranslateRequestSuccess(String translatedText);
        void OnTranslateRequestFailure(int error);
    }

    // okhttp callback
    // error: 1 - wrong request, 2 - offline
    private interface OnRequestListener{
        void onRequestSuccess(String result);
        void onRequestFailure(int error);
    }

}
