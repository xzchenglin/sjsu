package com.sjsu.lin.hw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.sjsu.lin.hw.handler.DeferWorkHandler;

import static com.sjsu.lin.hw.Utils.cnt;

public class ActivityMain extends AppCompatActivity {

    private TextView cntView;
    public static final String tag = "TestTag";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cntView = (TextView) findViewById(R.id.cnt_view_a);

        if (savedInstanceState != null && savedInstanceState.containsKey("cnt")) {
            Log.i("cnt", "Restoring state...");
            int si = savedInstanceState.getInt("cnt");
            cnt = si;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cntView.setText(getString(R.string.tread_cnt) + cnt);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cntView.setText(getString(R.string.tread_cnt) + ++cnt);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("cnt", "Saving state...");
        outState.putInt("cnt", cnt);
        super.onSaveInstanceState(outState);
    }

    public void start1(View v) {
        Intent intent = new Intent(ActivityMain.this, ActivityAccelerometer.class);
        startActivity(intent);
    }

    public void start2(View v) {
        Intent intent = new Intent(ActivityMain.this, ActivitySql.class);
        startActivity(intent);
    }

    public void start3(View view) {
        Intent intent = new Intent(ActivityMain.this, ActivityDownload.class);
        startActivity(intent);
    }

    public void finishActivityA(View v) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater(); //from activity
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        appendMenuItemText(item);
        if (item.getItemId() == R.id.menu_clear) {
            this.emptyText();
            return true;
        }
        if (item.getItemId() == R.id.menu_test_defered_handler) {
            this.testDeferedHandler();
            return true;
        }
        return true;
    }

    private TextView getTextView() {
        return (TextView) this.findViewById(R.id.text1);
    }

    private void appendMenuItemText(MenuItem menuItem) {
        String title = menuItem.getTitle().toString();
        TextView tv = getTextView();
        tv.setText(tv.getText() + "\n" + title);
    }

    private void emptyText() {
        TextView tv = getTextView();
        tv.setText("");
    }

    private DeferWorkHandler th = null;

    private void testDeferedHandler() {
        if (th == null) {
            th = new DeferWorkHandler(this);
        }
        this.appendText("Starting deferred");
        th.doDeferredWork();
    }

    public void appendText(String abc) {
        TextView tv = getTextView();
        tv.setText(tv.getText() + "\n" + abc);
    }

}
