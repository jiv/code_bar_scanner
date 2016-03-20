package com.jivarela.codebarscanner;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemAdapter extends BaseAdapter {
    private final Context context;
    private final List<Product> items;

    static class ViewHolder {
        @Bind(R.id.code) public TextView code;
        @Bind(R.id.quantity) public TextView quantity;
        @Bind(R.id.remove_item) public ImageView remove_icon;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }
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
            convertView = inflater.inflate(R.layout.list_item, parent, false); //TODO: ver si cambiar por (list_item, nul)
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Product p = this.items.get(position);
        viewHolder.code.setText(p.getCode());
        if (!p.getQuantity().equals(0)){
            viewHolder.quantity.setText(String.format("%d", p.getQuantity()));
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
                update_value(s, p);
            }
        });

        viewHolder.remove_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.remove(p);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private void update_value(Editable s, Product p){
        if (!s.toString().isEmpty()) {
            p.setQuantity(Integer.parseInt(s.toString()));
        } else {
            p.setQuantity(0);
        }
    }
}
