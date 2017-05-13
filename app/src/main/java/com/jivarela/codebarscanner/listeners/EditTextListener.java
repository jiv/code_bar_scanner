package com.jivarela.codebarscanner.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import com.jivarela.codebarscanner.classes.Product;
import java.util.ArrayList;

public class EditTextListener implements TextWatcher {
    private int position;
    private ArrayList<Product> items;

    public void updatePosition(ArrayList<Product> items, int position) {
        this.items = items;
        this.position = position;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        Product p = items.get(position);

        if (!charSequence.toString().isEmpty()) {
            p.setQuantity(Integer.parseInt(charSequence.toString()));
        } else {
            p.setQuantity(0);
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
