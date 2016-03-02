package com.jivarela.codebarscanner;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends BaseAdapter {
    private final Context context;
    private final List<Product> items;

    static class ViewHolder {
        public TextView code;
        public TextView quantity;
    }

    public ItemAdapter(Context context, List<Product> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Object getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.code = (TextView) convertView.findViewById(R.id.code);
            viewHolder.quantity = (TextView) convertView.findViewById(R.id.quantity);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Product p = this.items.get(position);
        viewHolder.code.setText(p.getCode());
        if (!p.getQuantity().equals("")){
            viewHolder.quantity.setText(p.getQuantity());
        }else{
            viewHolder.quantity.setText("");
        }

        viewHolder.quantity.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String new_value = "";
                if (!s.toString().isEmpty()) {
                    new_value = s.toString();
                }

                items.get(position).setQuantity(new_value);
            }
        });

        ImageView remove_icon = (ImageView) convertView.findViewById(R.id.remove_item);
        remove_icon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                items.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;


//
//
//        if (convertView == null) {
//            // Create a new view into the list.
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            rowView = inflater.inflate(R.layout.list_item, parent, false);
//        }else{
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//
//        // Set data into the view.
//        TextView tvCode = (TextView) rowView.findViewById(R.id.code);
//        TextView tvQuantity = (TextView) rowView.findViewById(R.id.quantity);
//
//        Product item = this.items.get(position);
//        tvCode.setText(item.getCode());
//        if (item.getQuantity() != 0){
//            tvQuantity.setText((String) Integer.toString(item.getQuantity()));
//        }
//
//        ImageView remove_icon=(ImageView)rowView.findViewById(R.id.remove_item);
//        remove_icon.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Integer index = (Integer) v.getTag();
//                //items.remove(index.intValue());
//                items.remove(position);
//                notifyDataSetChanged();
//
//            }
//        });
//
//        return rowView;
    }
}
