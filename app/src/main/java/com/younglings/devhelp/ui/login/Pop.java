package com.younglings.devhelp.ui.login;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.younglings.devhelp.R;

import androidx.annotation.Nullable;

public class Pop extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_link_pop_up);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.5),(int)(height*.5));
    }
}
