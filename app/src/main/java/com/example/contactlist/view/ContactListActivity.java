package com.example.contactlist.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.contactlist.R;
import com.example.contactlist.adapter.ContactListAdapter;
import com.example.contactlist.model.Contact;

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

        if (contactList.size() == 0) {
            textRegisterContact.setVisibility(View.VISIBLE);
        }
    }

    public void addContactClicked(View view) {
        Intent intent = new Intent(this, RegistryContactActivity.class);
        intent.putExtra("Contact", new Contact(contactList.size(), null, null));
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
            for (int i = 0; i < contactList.size(); i++) {
                if( contactList.get(i).getId() == contact.getId()){
                    contactList.set(i, contact);
                    break;
                }
            }
        }

        adapter.notifyDataSetChanged();
    }
}