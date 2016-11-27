package ua.com.slaviksoft.mytranslate2.Controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ua.com.slaviksoft.mytranslate2.Model.Language;

/**
 * Created by Slavik on 27.11.2016.
 */

public class LanguagesSpinnerAdapter extends ArrayAdapter<Language> {

    private int resource;
    private List<Language> listLanguages;

    public LanguagesSpinnerAdapter(Context context, int resource, List<Language> objects) {
        super(context, resource, objects);
        this.resource = resource;
        this.listLanguages = objects;
    }

    public int getPositionByLanguage(String language){
        for (Language item : listLanguages) {
            if (item.getCode().equals(language)){
                return getPosition(item);
            }
        }

        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
            ViewHolder viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.text1.setText(getItem(position).getName());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.text1.setText(getItem(position).getName());

        return convertView;

    }

    class ViewHolder{

        public TextView text1;

        ViewHolder(View v){
            text1    = (TextView) v.findViewById(android.R.id.text1);
        }
    }

}
