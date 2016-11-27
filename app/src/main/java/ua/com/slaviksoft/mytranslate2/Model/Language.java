package ua.com.slaviksoft.mytranslate2.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Slavik on 27.11.2016.
 */

public class Language {

    @SerializedName("language")
    private String code;
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }
}
