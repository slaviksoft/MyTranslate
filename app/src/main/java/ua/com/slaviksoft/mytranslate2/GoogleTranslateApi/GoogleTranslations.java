package ua.com.slaviksoft.mytranslate2.GoogleTranslateApi;

import android.text.Html;

import java.util.List;

/**
 * Created by Slavik on 27.11.2016.
 * Translations wrapper on JSON answer from google.translate.languages
 */

public class GoogleTranslations {

    private Translated data;

    public String toString() {
        return data.toString();
    }

    private class Translated {

        private List<TranslatedText> translations;

        public String toString() {

            StringBuilder sb = new StringBuilder();
            for (TranslatedText item : translations) {
                String str = Html.fromHtml((String) item.translatedText).toString();;
                sb.append(str).append("\n");
            }
            return sb.toString();
        }

    }

    private class TranslatedText {
        String translatedText;
    }

}



