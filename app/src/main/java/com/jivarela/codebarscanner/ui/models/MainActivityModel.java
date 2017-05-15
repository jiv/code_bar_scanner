package com.jivarela.codebarscanner.ui.models;

import com.jivarela.codebarscanner.classes.Product;
import com.jivarela.codebarscanner.managers.ProductsManager;
import com.squareup.otto.Bus;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import static com.jivarela.codebarscanner.utils.StorageHelper.getCodeBarScannerExternalDirectory;

public class MainActivityModel {
    private Bus mBus;
    private ProductsManager mProductsManager;

    public MainActivityModel(Bus bus, ProductsManager manager) {
        this.mBus = bus;
        this.mProductsManager = manager;
    }

    public void addNewCode(String barcode) {
        mProductsManager.addProduct(new Product(barcode));
        mBus.post(new NewBarCodeOnSuccessEvent(mProductsManager.getProductsList().size() - 1));
    }

    public void exportProductCodes() {
        if (mProductsManager.getProductsList().isEmpty()) {
            mBus.post(new ProductsListIsEmptyEvent());
        }else if (items_ok()) {
            mBus.post(new ExportProductsEvent());
        } else {
            mBus.post(new ItemsWithoutQuantityEvent());
        }
    }

    public void create_file(String separator, String company){
        String appExternalStorageDir = getCodeBarScannerExternalDirectory();

        File new_file = new File(appExternalStorageDir, company + "_" + current_date() + ".txt");

        ArrayList<Product> products = mProductsManager.getProductsList();
        try {
            FileOutputStream f = new FileOutputStream(new_file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(f));

            for (int i = 0; i < products.size(); i++) {
                Product p = products.get(i);
                bw.write(p.getCode() + separator + p.getQuantity() + "\r\n");
            }

            bw.flush();
            bw.close();
            f.close();
        } catch ( IOException e ){
            e.printStackTrace();
        }

        mBus.post(new CreateFileOnSuccessEvent(new_file.getAbsolutePath()));
    }

    private boolean items_ok(){
        ArrayList<Product> products = mProductsManager.getProductsList();

        boolean ok = true;
        int i = 0;
        while (ok && i < products.size()){
            Product p = products.get(i);
            ok = (!p.getCode().isEmpty() && !p.getQuantity().equals(0));
            i++;
        }
        return ok;
    }

    private String current_date(){
        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        return dateFormat.format(date);
    }


    public static class ProductsListIsEmptyEvent {}
    public static class ItemsWithoutQuantityEvent {}
    public static class ExportProductsEvent {}

    public static class NewBarCodeOnSuccessEvent {
        public static int PRODUCTS_LIST_SIZE;

        NewBarCodeOnSuccessEvent(int listSize) {
            PRODUCTS_LIST_SIZE = listSize;
        }

    }

    public static class CreateFileOnSuccessEvent {
        public static String FILE_PATH;

        CreateFileOnSuccessEvent(String filePath) {
            FILE_PATH = filePath;
        }
    }
}
