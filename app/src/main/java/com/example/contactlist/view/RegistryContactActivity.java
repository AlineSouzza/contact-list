package com.example.contactlist.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.contactlist.R;
import com.example.contactlist.model.Contact;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistryContactActivity extends AppCompatActivity {
    private Contact contact;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText etName;
    private EditText etNumber;
    private Button saveButton;
    private Intent returnIntent;
    private Map<String, Object> contactMap;
    private String contactName;
    private String contactNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry_contact);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contact = (Contact) getIntent().getSerializableExtra("Contact");

        etName = findViewById(R.id.name_contact);
        etNumber = findViewById(R.id.number_contact);
        saveButton = findViewById(R.id.save_contact);

        etName.setText(contact.getName());
        etNumber.setText(contact.getNumber());

        setupSaveButton();
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnIntent = new Intent();

                contactName = etName.getText().toString();
                contactNumber = etNumber.getText().toString();

                contactMap = new HashMap<String, Object>();
                contactMap.put("name", contactName);
                contactMap.put("number", contactNumber);

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
                }

                if (contact.getName() != null || contact.getNumber() != null) {
                    updateContact();
                } else {
                    createContact();
                }
            }
        });
    }

    public void createContact() {
        db.collection("Contacts").document().set(contactMap)
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistryContactActivity.this);
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

    public void updateContact() {
        db.collection("Contacts").document(contact.getId())
                .update(contactMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
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
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistryContactActivity.this);
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
}