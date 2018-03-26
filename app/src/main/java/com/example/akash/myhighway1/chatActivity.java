package com.example.akash.myhighway1;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


public class chatActivity extends AppCompatActivity {

    private EditText eMessage;
    private RecyclerView MessageList;
    private ImageButton sendMessage;
    private String PREFRENCENAME="AKASHSASH";
    String latitude="",longitude="";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String username="";
    private DatabaseReference databaseReference,databaseReference1;
    FirebaseRecyclerAdapter<Message,MessageContainer> fbra;
    LinearLayoutManager linearLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        latitude=getIntent().getStringExtra("latitude");
        longitude=getIntent().getStringExtra("longitude");
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar10);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Forum");
        if(latitude==null||longitude==null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else {
                getSupportActionBar().setTitle("Forum\n" + latitude + "" + longitude);
            }
        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        username=sharedPreferences.getString("userNamekey","anonymous");
        sendMessage=(ImageButton)findViewById(R.id.sendMessageButton);
        eMessage=(EditText)findViewById(R.id.typedMessage);

        MessageList=(RecyclerView) findViewById(R.id.recyclerviewofmessage);
        MessageList.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        MessageList.setLayoutManager(linearLayoutManager);

        databaseReference= FirebaseDatabase.getInstance().getReference().child("message");
       // databaseReference1= FirebaseDatabase.getInstance().getReference().child("userid");


        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String messageValue=eMessage.getText().toString().trim();
                if(!messageValue.equals("")) {
                    final DatabaseReference newMessage = databaseReference.push();
                    //final DatabaseReference newUser=databaseReference1.push();
                    String time = (new SimpleDateFormat("hh:mm a").format(new Date()));
                    newMessage.child("content").setValue(messageValue);
                    newMessage.child("username").setValue(username);
                    newMessage.child("time").setValue(time);
                    hideKeyboard();
                    eMessage.setText("");
                }

               // MessageList.scrollToPosition(MessageList.getAdapter().getItemCount());

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        fbra= new FirebaseRecyclerAdapter<Message,MessageContainer>
                (
                        Message.class,
                        R.layout.singlemessagelayout,
                        MessageContainer.class,
                        databaseReference
                )
        {
            @Override
            protected void populateViewHolder(MessageContainer viewHolder, Message model, int position) {
                viewHolder.setUser(model.getUsername(),username);
                viewHolder.setContent(model.getContent());
                viewHolder.setTime(model.getTime());
            }

        };

        fbra.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount=fbra.getItemCount();
                int lastVisiblePosition=linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(lastVisiblePosition==-1||(positionStart>=(friendlyMessageCount-1)&&lastVisiblePosition==(positionStart-1))){
                    MessageList.scrollToPosition(positionStart);
                }
            }
        });
        MessageList.setAdapter(fbra);
    }

    public static class MessageContainer extends RecyclerView.ViewHolder{
        View mView;
        LinearLayout linearLayout;
        public MessageContainer(View itemView) {
            super(itemView);
            mView=itemView;
            linearLayout=(LinearLayout) itemView.findViewById(R.id.singlemsglinearlayout);
        }
        public void setContent(String content){
            TextView message_Content=(TextView) mView.findViewById(R.id.mmessagetext);
            message_Content.setText(content);
        }
        public void setUser(String user,String currentusername){
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if(currentusername.equals(user)){
                Log.d("user:",user);
                layoutParams.gravity=Gravity.START;
                layoutParams.setMargins(75,5,5,5);
                linearLayout.setLayoutParams(layoutParams);
            }
            else{
                layoutParams.gravity=Gravity.END;
                layoutParams.setMargins(5,5,75,5);
                linearLayout.setLayoutParams(layoutParams);
            }
            TextView message_user=(TextView) mView.findViewById(R.id.musernametext);
            message_user.setText(user);
        }
        public void setTime(String time){
            TextView message_time=(TextView) mView.findViewById(R.id.mtime);
            message_time.setText(time);
        }

    }
    public void hideKeyboard(){
    View view=this.getCurrentFocus();
    if(view!=null){
        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
