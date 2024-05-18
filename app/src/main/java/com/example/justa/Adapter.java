package com.example.justa;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.myViewHolder> {

    static Context context;
    ArrayList<Request> list;

    static int counter;

    ImageView ivExitD;

    public Adapter(Context context, ArrayList<Request> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.request_listview, parent, false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {

        Request r = list.get(position);
        
        holder.address.setText(r.getAddress());
        holder.date.setText(r.getDate());
        holder.text.setText(r.getText());
        holder.phone.setText(r.getPhone());

        holder.sp.getInt("counter", 0);

        if(holder.sp.getString("type", null).equals("volunteer"))
        {
            holder.update.setVisibility(View.INVISIBLE);
            holder.iv.setVisibility(View.INVISIBLE);
            holder.checkBox.setVisibility(View.VISIBLE);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                r.setTaken(true);

                openDialog();

                if(r.isTaken())
                {
                    holder.checkBox.setVisibility(View.INVISIBLE);
                }

                holder.databaseReference.child("Requests").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            holder.databaseReference.child("Requests").child(r.getUid()).child("taken").setValue(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                counter++;

                holder.databaseReference.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(snapshot.hasChild(holder.sp.getString("phone", null)))
                        {
                            User current = snapshot.child(holder.sp.getString("phone", null)).getValue(User.class);

                            current.setCounter(counter);

                            holder.databaseReference.child("Users").child(holder.sp.getString("phone", null)).child("counter").setValue(current.getCounter());

                            SharedPreferences.Editor perfEditor = holder.sp.edit();
                            perfEditor.putInt("counter", current.getCounter());
                            perfEditor.commit();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        if(r.isTaken())
        {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static  class myViewHolder extends RecyclerView.ViewHolder{

        TextView text;
        TextView date;
        TextView address;
        TextView phone;
        TextView update;

        CheckBox checkBox;

        ImageView iv;

        SharedPreferences sp;

        DatabaseReference databaseReference;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            text = itemView.findViewById(R.id.tvTextLvR);
            date = itemView.findViewById(R.id.tvDateLvR);
            address = itemView.findViewById(R.id.tvAddressLvR);
            phone = itemView.findViewById(R.id.tvPhoneLvR);
            update = itemView.findViewById(R.id.tvUpdateLvR);

            checkBox = itemView.findViewById(R.id.cbConfirm);

            iv = itemView.findViewById(R.id.ivDelete);

            sp = context.getSharedPreferences("Login", MODE_PRIVATE);

            counter = sp.getInt("counter", 0);

            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
    }

    public void openDialog()
    {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.done_request_dialog);

        ivExitD = dialog.findViewById(R.id.ivExitD);

        ivExitD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
