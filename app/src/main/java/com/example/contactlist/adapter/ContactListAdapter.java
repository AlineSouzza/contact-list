package com.example.contactlist.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlist.R;
import com.example.contactlist.model.Contact;
import com.example.contactlist.view.ContactListActivity;
import com.example.contactlist.view.RegistryContactActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    private final ArrayList<Contact> contactList;
    private Context context;
    private ContactListActivity activity;

    public ContactListAdapter(ArrayList<Contact> contactList, Context context, ContactListActivity activity) {
        this.contactList = contactList;
        this.context = context;
        this.activity = activity;
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
        private final ImageView deleteContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemContact = (LinearLayout) itemView.findViewById(R.id.item_contact);
            tvNameContact = (TextView) itemView.findViewById(R.id.name_contact);
            tvNumberContact = (TextView) itemView.findViewById(R.id.number_contact);
            phoneCall = (ImageView) itemView.findViewById(R.id.phone_call);
            deleteContact = (ImageView) itemView.findViewById(R.id.delete_contact);
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

        public ImageView getDeleteContact() {
            return deleteContact;
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
                ((Activity) context).startActivityForResult(intent, 2);
            }
        });

        holder.makePhoneCall().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + contactList.get(holder.getAdapterPosition()).getNumber());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                ((Activity) context).startActivity(callIntent);
            }
        });

        holder.getDeleteContact().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setTitle(R.string.title_atencao);
                builder.setMessage(R.string.text_delete_contact)
                        .setPositiveButton(R.string.button_yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                db.collection("Contacts").document(contactList.get(holder.getAdapterPosition())
                                                .getId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                contactList.remove(holder.getAdapterPosition());
                                                activity.checkList();

                                                Toast.makeText(v.getContext(), R.string.toast_deleted_contact_success, Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(v.getContext(), R.string.toast_deleted_contact_error, Toast.LENGTH_SHORT).show();
                                                Log.e("db", "Erro ao excluir o contato do banco de dados: " + e);
                                            }
                                        });

                                dialog.dismiss();

                            }
                        })
                        .setNegativeButton(R.string.button_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
