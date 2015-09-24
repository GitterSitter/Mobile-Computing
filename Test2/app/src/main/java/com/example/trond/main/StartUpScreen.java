package com.example.trond.main;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class StartUpScreen extends Activity {


    private ImageView bg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
        bg = (ImageView) findViewById(R.id.imageView);


        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Intent intent = new Intent(this, AppMenu.class);
        //  myIntent.putExtra("key", value); //Optional parameters
        // Intent intent = getIntent();
        //  String value = intent.getStringExtra("key"); //if it's a string you stored.
         startActivity(intent);

    }



}
