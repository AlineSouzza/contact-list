package com.example.contactlist.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.contactlist.R;
import com.example.contactlist.model.Contact;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistryContactActivity extends AppCompatActivity {
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

                if (contactName.equals("") || contactNumber.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage(R.string.info_ivalid_contact)
                            .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                    return;

                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Map<String, Object> contact1 = new HashMap<String, Object>();
                contact1.put("name", contactName);
                contact1.put("number", contactNumber);

                db.collection("Contacts").document().set(contact1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                contact.setName(contactName);
                                contact.setNumber(contactNumber);

                                returnIntent.putExtra("Contact", contact);
                                setResult(Activity.RESULT_OK, returnIntent);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                builder.setMessage(R.string.db_ivalid_contact)
                                        .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        });
            }
        });
    }
}