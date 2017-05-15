package com.jivarela.codebarscanner.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.jivarela.codebarscanner.R;
import com.jivarela.codebarscanner.classes.Product;
import com.jivarela.codebarscanner.listeners.EditTextListener;
import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ItemViewHolder> {
    private final ArrayList<Product> items;
    private Context mContext;

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.code) public TextView code;
        @Bind(R.id.quantity) public TextView quantity;
        @Bind(R.id.remove_item) public ImageView remove_icon;
        public EditTextListener editTextListener;

        public ItemViewHolder(View view, EditTextListener editTextListener){
            super(view);
            ButterKnife.bind(this, view);
            this.editTextListener = editTextListener;
            this.quantity.addTextChangedListener(editTextListener);
            this.quantity.setSelectAllOnFocus(true);
        }
    }

    public ProductsAdapter(Context context, ArrayList<Product> items) {
        this.mContext = context;
        this.items = items;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(v, new EditTextListener());
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        holder.editTextListener.updatePosition(items, position);
        final Product p = items.get(position);
        holder.code.setText(p.getCode());
        if (!p.getQuantity().equals(0)){
            holder.quantity.setText(String.format("%d", p.getQuantity()));
        }else{
            holder.quantity.setText("");
        }

        holder.remove_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = items.indexOf(p);
                items.remove(p);
                notifyItemRemoved(pos);
                notifyDataSetChanged();
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
