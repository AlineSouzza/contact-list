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
import com.example.contactlist.utils.MaskEditUtil;
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
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry_contact);

        contact = (Contact) getIntent().getSerializableExtra("Contact");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(contact != null ? R.string.edit_contact : R.string.new_contact);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etName = findViewById(R.id.name_contact);
        etNumber = findViewById(R.id.number_contact);
        saveButton = findViewById(R.id.save_contact);

        etNumber.addTextChangedListener(MaskEditUtil.mask(etNumber, MaskEditUtil.FORMAT_PHONE));

        if (contact != null) {
            etName.setText(contact.getName());
            etNumber.setText(contact.getNumber());
        }

        setupSaveButton();
    }

    private void setupSaveButton() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactName = etName.getText().toString();
                String contactNumber = etNumber.getText().toString();

                Map<String, Object> contactMap = new HashMap<String, Object>();
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

                    return;
                }

                if (contact != null) {
                    updateContact(contactMap);
                } else {
                    createContact(contactMap);
                }
            }
        });
    }

    public void createContact(Map<String, Object> contactMap) {
        String documentId = db.collection("Contacts").document().getId();
        db.collection("Contacts").document(documentId).set(contactMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        contact = new Contact(
                                documentId,
                                contactMap.get("name").toString(),
                                contactMap.get("number").toString()
                        );

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("Contact", contact);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("db", "error " + e);

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

    public void updateContact(Map<String, Object> contactMap) {
        db.collection("Contacts").document(contact.getId())
                .update(contactMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        contact.setName(contactMap.get("name").toString());
                        contact.setNumber(contactMap.get("number").toString());

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("Contact", contact);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("db", "error " + e);
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