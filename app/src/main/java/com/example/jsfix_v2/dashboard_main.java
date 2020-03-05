package com.example.jsfix_v2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import java.util.Timer;
import java.util.TimerTask;

public class dashboard_main extends AppCompatActivity {
    private CardView whatsapp, img1;
    ViewPager viewPager;
    int images[] = {R.drawable.pic1 , R.drawable.pic2, R.drawable.pic3};
    int currentPageCnt = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_main);

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new slider_adapter(images, dashboard_main.this));

        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            @Override
            public void run() {
                if (currentPageCnt == images.length){
                    currentPageCnt = 0;
                }
                viewPager.setCurrentItem(currentPageCnt++, true);
            }
        };

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 2500, 2500);

        whatsapp = (CardView) findViewById(R.id.whatsapp);
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pesan = "Testing whatsapp";

                Intent kirimWA = new Intent(Intent.ACTION_SEND);
                kirimWA.setType("text/plain");
                kirimWA.putExtra(Intent.EXTRA_TEXT, pesan);
                kirimWA.putExtra("jid", "6282112927843" + "@s.whatsapp.net");
                kirimWA.setPackage("com.whatsapp");

                startActivity(kirimWA);
            }
        });

        img1 = (CardView) findViewById(R.id.img1);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(dashboard_main.this, web_view.class);
                startActivity(intent);
            }
        });
    }
}

