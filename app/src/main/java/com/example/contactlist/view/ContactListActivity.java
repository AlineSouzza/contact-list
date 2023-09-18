package com.example.contactlist.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import io.supercharge.shimmerlayout.ShimmerLayout;

public class ContactListActivity extends AppCompatActivity {
    private ContactListAdapter adapter;
    private ArrayList<Contact> contactList;
    private LinearLayout skeletonLayout;
    private ShimmerLayout shimmer;
    private LayoutInflater inflater;
    private RecyclerView recyclerView;
    private LinearLayout layoutEmptyContact;
    Context context = (Context) this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitle("Lista de contatos");
        setSupportActionBar(myToolbar);

        skeletonLayout = findViewById(R.id.skeletonLayout);
        shimmer = findViewById(R.id.shimmerSkeleton);
        this.inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        recyclerView = findViewById(R.id.recycler_view);
        layoutEmptyContact = findViewById(R.id.text_register_contact);

        contactList = new ArrayList<Contact>();
        adapter = new ContactListAdapter(contactList, context, this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        recyclerView.setAdapter(adapter);

        getSkeletonRowCount(this);
        loadContacts();
    }

    public int getSkeletonRowCount(Context context) {
        int pxHeight = getDeviceHeight(context);
        int skeletonRowHeight = (int) getResources()
                .getDimension(R.dimen.row_layout_height); //converts to pixel
        return (int) Math.ceil(pxHeight / skeletonRowHeight);
    }
    public int getDeviceHeight(Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return metrics.heightPixels;
    }

    public void showSkeleton(boolean show) {

        if (show) {
            skeletonLayout.removeAllViews();

            int skeletonRows = getSkeletonRowCount(this);
            for (int i = 0; i <= skeletonRows; i++) {
                ViewGroup rowLayout = (ViewGroup) inflater
                        .inflate(R.layout.layout_loading_contact_cell, null);
                skeletonLayout.addView(rowLayout);
            }
            shimmer.setVisibility(View.VISIBLE);
            shimmer.startShimmerAnimation();
            skeletonLayout.setVisibility(View.VISIBLE);
            skeletonLayout.bringToFront();
        } else {
            shimmer.stopShimmerAnimation();
            shimmer.setVisibility(View.GONE);
        }
    }

    public void animateReplaceSkeleton(View listView) {

        listView.setVisibility(View.VISIBLE);
        listView.setAlpha(0f);
        listView.animate().alpha(1f).setDuration(500).start();

        skeletonLayout.animate().alpha(0f).setDuration(500).withEndAction(new Runnable() {
            @Override
            public void run() {
                showSkeleton(false);
            }
        }).start();

    }

    protected void loadContacts() {
        showSkeleton(true);
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

                            checkList();
                            adapter.notifyDataSetChanged();
                            animateReplaceSkeleton(recyclerView);
                        } else {
                            showSkeleton(false);
                            Log.d("db", "get failed with ", task.getException());
                        }
                    }
                });
    }

    public void addContactClicked(View view) {
        Intent intent = new Intent(this, RegistryContactActivity.class);
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
            layoutEmptyContact.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        } else if (requestCode == 2) {
            loadContacts();
        }
    }
    public void checkList() {
        if (contactList.size() == 0) {
            layoutEmptyContact.setVisibility(View.VISIBLE);
        } else {
            layoutEmptyContact.setVisibility(View.GONE);
        }
    }
}
