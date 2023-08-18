package com.example.contactlist.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.EditText;

import com.example.contactlist.R;

public class RegistryContactActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry_contact);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText name = findViewById(R.id.name_contact);
        EditText number = findViewById(R.id.number_contact);

        String nameContact = getIntent().getStringExtra("ContactName");
        String numberContact = getIntent().getStringExtra("ContactNumber");

        if (nameContact != null) {
            name.setText(nameContact);
        }

        if (numberContact != null) {
            number.setText(numberContact);
        }
    }
}