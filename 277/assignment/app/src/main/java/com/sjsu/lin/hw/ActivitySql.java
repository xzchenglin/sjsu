package com.sjsu.lin.hw;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

public class ActivitySql extends Activity {
    EditText etName, etDesc, etPrice, etReview, etFilter;
    SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql);

        etName = findViewById(R.id.et_name);
        etDesc = findViewById(R.id.et_desc);
        etPrice = findViewById(R.id.et_price);
        etReview = findViewById(R.id.et_review);
        etFilter = findViewById(R.id.et_filter);

        db = openOrCreateDatabase("productDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS product(name VARCHAR,des VARCHAR,price VARCHAR,review VARCHAR);");

        etFilter.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                showContent();
            }
        });

        showContent();
    }

    public void save(View v) throws InterruptedException {

        db.execSQL("INSERT INTO product VALUES('" + etName.getText() + "','" + etDesc.getText() + "','" + etPrice.getText() +
                "','" + etReview.getText() + "');");
        clearText();
        showContent();
    }

    public void showContent(){

        TextView tv = findViewById(R.id.tv3);

        String filterTxt = etFilter.getText() + "";
        Cursor c;
        if(StringUtils.isBlank(filterTxt)) {
            c = db.rawQuery("SELECT * FROM product", null);
        } else {
            String ss = "%" + filterTxt + "%";
            c = db.rawQuery("SELECT * FROM product where name like ? or des like ? or review like ?",
                    new String[]{ss, ss, ss});
        }
        if (c.getCount() == 0) {
            tv.setText("");
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            buffer.append("Name: " + c.getString(0) + " ");
            buffer.append("Description: " + c.getString(1) + " ");
            buffer.append("Price: " + c.getString(2) + " ");
            buffer.append("Review: " + c.getString(3) + "\n");
        }

        tv.setText(buffer);
    }

    public void clearText() {
        etDesc.setText("");
        etName.setText("");
        etPrice.setText("");
        etReview.setText("");
    }
}
