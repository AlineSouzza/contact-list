package com.example.contactlist.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.contactlist.R;
import com.example.contactlist.model.Contact;

import java.util.ArrayList;

public class RegistryContactActivity extends AppCompatActivity {

    private ArrayList<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry_contact);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText etName = findViewById(R.id.name_contact);
        EditText etNumber = findViewById(R.id.number_contact);
        Button saveButton = findViewById(R.id.save_contact);

        String nameContact = getIntent().getStringExtra("ContactName");
        String numberContact = getIntent().getStringExtra("ContactNumber");

        if (nameContact != null) {
            etName.setText(nameContact);
        }

        if (numberContact != null) {
            etNumber.setText(numberContact);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("NameContact", etName.getText().toString());
                returnIntent.putExtra("NumberContact", etNumber.getText().toString());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}