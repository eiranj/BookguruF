package com.example.bookguru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button CreateNewBook, ManageBooks ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateNewBook = (Button) findViewById(R.id.CreateNewBook);
        ManageBooks = (Button) findViewById(R.id.ManageBooks);


        CreateNewBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, CreateNewBook.class);
                startActivity(in);
            }
        });
        ManageBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, ManageBooks.class);
                startActivity(in);
            }
        });
    }
}

