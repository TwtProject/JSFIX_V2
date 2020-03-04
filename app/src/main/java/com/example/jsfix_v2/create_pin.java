package com.example.jsfix_v2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hanks.passcodeview.PasscodeView;

public class create_pin extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    PasscodeView passcodeView;
    String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_pin);

        pref =getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        passcodeView = findViewById(R.id.passcode_view);
        pin = pref.getString("pin", null);
//        Log.d("PIN : ",pin);

        if (pin!=null){
            Intent intent = new Intent(create_pin.this, in_pin.class);
            intent.putExtra("pin", pin);
            startActivity(intent);
        }

        passcodeView.setPasscodeLength(4).setListener(new PasscodeView.PasscodeViewListener() {
            @Override
            public void onFail() {

            }

            @Override
            public void onSuccess(String number) {
                passcodeView.setLocalPasscode(number);
                pin = number;
                editor.putString("pin", number); // Storing string
                editor.apply();
                Intent intent = new Intent(create_pin.this, in_pin.class);
                intent.putExtra("pin", pin);
                startActivity(intent);
            }
        });
    }
}
