package com.example.contactlist.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlist.R;
import com.example.contactlist.model.Contact;
import com.example.contactlist.view.RegistryContactActivity;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    private final ArrayList<Contact> contactList;
    private Context context;
    public ContactListAdapter(ArrayList<Contact> contactList, Context context) {
        this.contactList = contactList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_contact_cell, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout itemContact;
        private final TextView tvNameContact;
        private final TextView tvNumberContact;
        private final ImageView phoneCall;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemContact = (LinearLayout) itemView.findViewById(R.id.item_contact);
            tvNameContact = (TextView) itemView.findViewById(R.id.name_contact);
            tvNumberContact = (TextView) itemView.findViewById(R.id.number_contact);
            phoneCall = (ImageView) itemView.findViewById(R.id.phone_call);
        }

        public TextView getTvNameContact() {
            return tvNameContact;
        }

        public TextView getTvNumberContact() {
            return tvNumberContact;
        }

        public LinearLayout getItemContact() {
            return itemContact;
        }

        public ImageView makePhoneCall() {
            return phoneCall;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListAdapter.ViewHolder holder, int position) {
        holder.getTvNameContact().setText(contactList.get(position).getName());
        holder.getTvNumberContact().setText(contactList.get(position).getNumber());
        holder.getItemContact().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegistryContactActivity.class);
                intent.putExtra("Contact", contactList.get(holder.getAdapterPosition()));
                ((Activity) context).startActivityForResult(intent,2);
            }
        });

        holder.makePhoneCall().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + contactList.get(holder.getAdapterPosition()).getNumber());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                ((Activity) context).startActivity(callIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
