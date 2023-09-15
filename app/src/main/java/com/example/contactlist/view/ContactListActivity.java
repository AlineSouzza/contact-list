package com.example.contactlist.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.contactlist.R;
import com.example.contactlist.adapter.ContactListAdapter;
import com.example.contactlist.model.Contact;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {
    private ContactListAdapter adapter;
    private ArrayList<Contact> contactList;
    private LinearLayout textRegisterContact;
    Context context = (Context) this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Lista de contatos");
        setSupportActionBar(myToolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        textRegisterContact = findViewById(R.id.text_register_contact);

        contactList = new ArrayList<Contact>();
        adapter = new ContactListAdapter(contactList, context);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(adapter);

        loadContacts();
    }


    protected void loadContacts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        contactList.clear();

        db.collection("Contacts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                contactList.add(new Contact(document.getId(), document.getString("name"), document.getString("number")));
                            }

                            if (contactList.size() == 0) {
                                textRegisterContact.setVisibility(View.VISIBLE);
                            }

                            adapter.notifyDataSetChanged();
                        } else {
                            Log.d("db", "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void addContactClicked(View view) {
        Intent intent = new Intent(this, RegistryContactActivity.class);
        intent.putExtra("Contact", new Contact(null, null, null));
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        Contact contact = (Contact) data.getSerializableExtra("Contact");

        if (requestCode == 1) {
            contactList.add(contact);
            textRegisterContact.setVisibility(View.GONE);
        } else if (requestCode == 2) {
            loadContacts();
        }

        adapter.notifyDataSetChanged();
    }
}