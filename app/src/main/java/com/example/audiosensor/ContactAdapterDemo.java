package com.example.audiosensor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactAdapterDemo extends RecyclerView.Adapter<ContactAdapterDemo.Viewhoder> {

    List<RecordingItem> contacts;
    IonClickContact ionClickContact;

    public void setIonClickContact(IonClickContact ionClickContact) {
        this.ionClickContact = ionClickContact;
    }

    public ContactAdapterDemo(List<RecordingItem> contacts) {
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public Viewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_recording, parent, false);

        Viewhoder viewhoder = new Viewhoder(view);
        return viewhoder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewhoder holder, int position) {

        final RecordingItem contact = contacts.get(position);

        holder.tvName.setText(contact.getName());

        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickContact.onClickName(contact);
            }
        });


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ionClickContact.onClickImage(contact);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class Viewhoder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imageView;

        public Viewhoder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.filename);
            imageView = itemView.findViewById(R.id.radio);
        }
    }
}
