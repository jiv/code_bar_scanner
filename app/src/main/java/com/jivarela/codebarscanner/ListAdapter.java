package com.jivarela.codebarscanner;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> items;

    static class ViewHolder {
        public TextView code;
        public TextView quantity;
    }

    public ListAdapter(Activity context, int resourceId, ArrayList<String> items) {
        super(context, resourceId, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.code = (TextView) rowView.findViewById(R.id.code);
            viewHolder.quantity = (TextView) rowView.findViewById(R.id.quantity);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        String code = items.get(position);
        holder.code.setText(code);

        return rowView;
    }
}
