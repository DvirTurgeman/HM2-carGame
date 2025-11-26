package com.guy.class26a_and_3;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TempActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ImageView[][] map = new ImageView[][] {
                {findViewById(R.id.imgCoin00), findViewById(R.id.imgCoin01), findViewById(R.id.imgCoin02)},
                {findViewById(R.id.imgCoin00), findViewById(R.id.imgCoin01)},findViewById(R.id.imgCoin12)
                {findViewById(R.id.imgCoin00), findViewById(R.id.imgCoin01)},findViewById(R.id.imgCoin12)
        };
    }
}
