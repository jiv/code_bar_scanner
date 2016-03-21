package com.jivarela.codebarscanner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private final List<Product> items;

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.code) public TextView code;
        @Bind(R.id.quantity) public TextView quantity;
        @Bind(R.id.remove_item) public ImageView remove_icon;

        public ItemViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public ItemAdapter(List<Product> items) {
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);

        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        final Product p = items.get(position);
        holder.code.setText(p.getCode());
        if (!p.getQuantity().equals(0)){
            holder.quantity.setText(String.format("%d", p.getQuantity()));
        }else{
            holder.quantity.setText("");
        }

        holder.quantity.addTextChangedListener(new TextWatcher() {

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

        holder.remove_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = items.indexOf(p);
                items.remove(p);
                notifyItemRemoved(pos);
//            InputMethodManager imm = (InputMethodManager) (context.getSystemService(Context.INPUT_METHOD_SERVICE));
//            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    private void update_value(Editable s, Product p){
        if (!s.toString().isEmpty()) {
            p.setQuantity(Integer.parseInt(s.toString()));
        } else {
            p.setQuantity(0);
        }
    }
}
