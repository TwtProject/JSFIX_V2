package com.example.jsfix_v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class send_message extends AppCompatActivity {
    private CardView whatsapp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_main);

        whatsapp = (CardView) findViewById(R.id.whatsapp);
        whatsapp.setOnClickListener(new View.OnClickListener(){

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

    }

}
