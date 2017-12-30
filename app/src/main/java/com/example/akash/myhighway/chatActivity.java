package com.example.akash.myhighway;

import com.example.akash.myhighway.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class chatActivity extends AppCompatActivity {

    private EditText eMessage;
    private RecyclerView MessageList;
    private Button sendMessage;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sendMessage=(Button)findViewById(R.id.sendMessageButton);
        eMessage=(EditText)findViewById(R.id.typedMessage);
        MessageList=(RecyclerView) findViewById(R.id.recyclerviewofmessage);
        MessageList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        MessageList.setLayoutManager(linearLayoutManager);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("message");



        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String messageValue=eMessage.getText().toString();
                final DatabaseReference newMessage=databaseReference.push();
                newMessage.child("content").setValue(messageValue);
             MessageList.scrollToPosition(MessageList.getAdapter().getItemCount());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Message,MessageContainer> fbra= new FirebaseRecyclerAdapter<Message,MessageContainer>
                (
                        Message.class,
                        R.layout.singlemessagelayout,
                        MessageContainer.class,
                        databaseReference
                )
        {
            @Override
            protected void populateViewHolder(MessageContainer viewHolder, Message model, int position) {
            viewHolder.setContent(model.getContent());
            }
        };
        MessageList.setAdapter(fbra);
    }

    public static class MessageContainer extends RecyclerView.ViewHolder{
        View mView;
        public MessageContainer(View itemView) {
            super(itemView);
            mView=itemView;
        }
        public void setContent(String content){
            TextView message_Content=(TextView) mView.findViewById(R.id.mmessagetext);
            message_Content.setText(content);
        }
        public void setUser(String user){
            TextView message_user=(TextView) mView.findViewById(R.id.musernametext);
            message_user.setText(user);
        }
    }
}
