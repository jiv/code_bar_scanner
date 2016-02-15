package com.jivarela.codebarscanner;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Product> {
    private final Activity context;
    private final ArrayList<Product> items;

    static class ViewHolder {
        public TextView code;
        public TextView quantity;
    }

    public ListAdapter(Activity context, int resourceId, ArrayList<Product> items) {
        super(context, resourceId, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();;
        if (convertView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            convertView = inflater.inflate(R.layout.list_item, null);
            viewHolder.code = (TextView) convertView.findViewById(R.id.code);
            viewHolder.quantity = (TextView) convertView.findViewById(R.id.quantity);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ImageView remove_icon=(ImageView)convertView.findViewById(R.id.remove_item);
        remove_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Integer index = (Integer) v.getTag();
                //items.remove(index.intValue());
                items.remove(position);
                notifyDataSetChanged();

            }
        });

        Product p = items.get(position);
        viewHolder.code.setText(p.code);

//        viewHolder.quantity.setText(p.quantity);

        return convertView;
    }
}
