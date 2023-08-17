package com.example.contactlist.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlist.R;
import com.example.contactlist.model.Contact;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    private final ArrayList<Contact> contactList;
    public ContactListAdapter(ArrayList<Contact> contactList) {
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact_cell, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameContact;
        private  final TextView numberContact;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nameContact = (TextView) itemView.findViewById(R.id.name_contact);
            numberContact = (TextView) itemView.findViewById(R.id.number_contact);
        }

        public TextView getNameContact() {
            return nameContact;
        }

        public TextView getNumberContact() {
            return numberContact;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListAdapter.ViewHolder holder, int position) {
        holder.getNameContact().setText(contactList.get(position).getName());
        holder.getNumberContact().setText(contactList.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
