package com.john.grdagger.ui.main;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.john.grdagger.models.GuardianContent;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by john on 7/12/2017.
 */

public class NewsListAdapter extends ArrayAdapter<GuardianContent>
{
    private final LayoutInflater inflater;
    @ColorInt
    private final int readColor;
    @ColorInt
    private final int unreadColor;

    public NewsListAdapter(Context context, List<GuardianContent> contents)
    {
        super(context, android.R.layout.simple_spinner_dropdown_item);
        setNotifyOnChange(false);
        addAll(contents);
        inflater = LayoutInflater.from(context);
        readColor = context.getResources().getColor(android.R.color.darker_gray);
        unreadColor = context.getResources().getColor(android.R.color.primary_text_light);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v = convertView;
        ViewHolder holder;
        if(v == null)
        {
            v = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
            holder = new ViewHolder(v);
            v.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) v.getTag();
        }

        GuardianContent c = getItem(position);
        holder.titleView.setText(c.webTitle);
        holder.titleView.setTextColor(c.getIsRead()? readColor : unreadColor);

        return v;
    }

    static class ViewHolder
    {
        @BindView(android.R.id.text1)
        TextView titleView;
        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }

}
