package com.example.pettomanagementsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContactListRecyclerViewAdapter extends RecyclerView.Adapter<ContactListRecyclerViewAdapter.contactViewHolder> {
    private final ContactListInterface contactListInterface;
    Context context;
    ArrayList<ContactModel> contactList;
    public ContactListRecyclerViewAdapter(Context context, ArrayList<ContactModel> contactList, ContactListInterface contactListInterface) {
        this.context = context;
        this.contactList = contactList;
        this.contactListInterface = contactListInterface;
    }

    @NonNull
    @Override
    public ContactListRecyclerViewAdapter.contactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_single_row_contact, parent, false);
        return new ContactListRecyclerViewAdapter.contactViewHolder(view, contactListInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactListRecyclerViewAdapter.contactViewHolder holder, int position) {
        holder.name.setText(contactList.get(position).getName());
        holder.phoneNo.setText(contactList.get(position).getPhoneNo());
        imageBehaviour(holder);

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public static class contactViewHolder extends RecyclerView.ViewHolder {
        TextView name, phoneNo;
        ImageView imageViewCall, imageViewEdit, imageViewDelete;
        public contactViewHolder(@NonNull View itemView, ContactListInterface contactListInterface) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewName);
            phoneNo = itemView.findViewById(R.id.textViewPhoneNo);
            imageViewCall = itemView.findViewById(R.id.imageCall);
            imageViewEdit = itemView.findViewById(R.id.imageEdit);
            imageViewDelete = itemView.findViewById(R.id.imageDelete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contactListInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            contactListInterface.onContactClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (contactListInterface != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            contactListInterface.onLongContactClick(position);
                        }
                    }
                    return true;
                }
            });
        }
    }

    private void imageBehaviour(contactViewHolder holder) {
        if (contactListInterface.getAddButtonVisibility() == View.GONE && contactListInterface.getEditButtonVisibility() == View.GONE && contactListInterface.getDeleteButtonVisibility() == View.GONE) {
            holder.imageViewCall.setVisibility(View.VISIBLE);
            holder.imageViewEdit.setVisibility(View.GONE);
            holder.imageViewDelete.setVisibility(View.GONE);
        }

        if (contactListInterface.getCallButtonVisibility() == View.GONE && contactListInterface.getEditButtonVisibility() == View.GONE && contactListInterface.getDeleteButtonVisibility() == View.GONE) {
            holder.imageViewCall.setVisibility(View.GONE);
            holder.imageViewEdit.setVisibility(View.GONE);
            holder.imageViewDelete.setVisibility(View.GONE);
        }

        if (contactListInterface.getCallButtonVisibility() == View.GONE && contactListInterface.getAddButtonVisibility() == View.GONE && contactListInterface.getDeleteButtonVisibility() == View.GONE) {
            holder.imageViewCall.setVisibility(View.GONE);
            holder.imageViewEdit.setVisibility(View.VISIBLE);
            holder.imageViewDelete.setVisibility(View.GONE);
        }

        if (contactListInterface.getCallButtonVisibility() == View.GONE && contactListInterface.getAddButtonVisibility() == View.GONE && contactListInterface.getEditButtonVisibility() == View.GONE) {
            holder.imageViewCall.setVisibility(View.GONE);
            holder.imageViewEdit.setVisibility(View.GONE);
            holder.imageViewDelete.setVisibility(View.VISIBLE);
        }
    }

    public void updateImageVisibility() {
        notifyDataSetChanged();
    }
}
