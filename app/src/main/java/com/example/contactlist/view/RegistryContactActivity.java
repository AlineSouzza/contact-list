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

public class RegistryContactActivity extends AppCompatActivity {
    private boolean isEditMode;
    private Contact contact;

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

        contact = (Contact) getIntent().getSerializableExtra("Contact");

        etName.setText(contact.getName());
        etNumber.setText(contact.getNumber());

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                String contactName = etName.getText().toString();
                String contactNumber = etNumber.getText().toString();

                contact.setName(contactName);
                contact.setNumber(contactNumber);

                returnIntent.putExtra("Contact", contact);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }
}