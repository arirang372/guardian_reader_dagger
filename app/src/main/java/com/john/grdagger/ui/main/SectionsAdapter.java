package com.john.grdagger.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.john.grdagger.models.GuardianSection;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.util.Collections.addAll;

/**
 * Created by john on 7/12/2017.
 */

public class SectionsAdapter extends BaseAdapter
{
    private final LayoutInflater inflater;
    private List<GuardianSection> sections;
    public SectionsAdapter(Context context, List<GuardianSection> sections)
    {
        addAll(sections);
        inflater = LayoutInflater.from(context);
        this.sections = sections;
    }

    @Override
    public int getCount() {
        return sections.size();
    }

    @Override
    public Object getItem(int position) {
        return sections.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

        GuardianSection c = (GuardianSection) getItem(position);
        holder.titleView.setText(c.id);
        holder.titleView.setTextColor(Color.WHITE);

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
