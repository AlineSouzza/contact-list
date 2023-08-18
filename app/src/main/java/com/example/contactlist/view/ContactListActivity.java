package com.example.contactlist.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.contactlist.R;
import com.example.contactlist.adapter.ContactListAdapter;
import com.example.contactlist.model.Contact;

import java.util.ArrayList;

public class ContactListActivity extends AppCompatActivity {
    private ContactListAdapter adapter;

    private ArrayList<Contact> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Lista de contatos");
        setSupportActionBar(myToolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        contactList = new ArrayList<Contact>();

        adapter = new ContactListAdapter(contactList);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(adapter);
    }

    public void registryContactClicked(View view) {
        Intent intent = new Intent(this, RegistryContactActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String resultNameContact = data.getStringExtra("NameContact");
        String resultNumberContact = data.getStringExtra("NumberContact");

        contactList.add(new Contact(1, resultNameContact, resultNumberContact));

        adapter.notifyDataSetChanged();
    }
}