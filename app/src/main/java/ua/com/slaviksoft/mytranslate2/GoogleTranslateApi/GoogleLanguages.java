package ua.com.slaviksoft.mytranslate2.GoogleTranslateApi;

import java.util.List;

import ua.com.slaviksoft.mytranslate2.Model.Language;

/**
 * Created by Slavik on 27.11.2016.
 * Languages wrapper on JSON answer from google.translate.languages
 */

public class GoogleLanguages {

    private Data data;

    public List<Language> getLanguages() {
        return data.getLanguages();
    }

    private class Data {

        List<Language> languages;

        List<Language> getLanguages() {
            return languages;
        }

    }


}

