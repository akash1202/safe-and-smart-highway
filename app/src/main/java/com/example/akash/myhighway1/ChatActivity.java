package com.example.akash.myhighway1;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatActivity extends AppCompatActivity {

    private EditText eMessage;
    private RecyclerView MessageList;
    private ImageButton sendMessage;
    AlertDialog dialog;
    CircleImageView chatProfileImage;
    TextView title,subtitle;
    private String PREFRENCENAME = "AKASHSASH";
    String latitude = "", longitude = "", phone = "", imageurl = "";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String username = "", userPhone = "";
    ImageLoader imageLoader;
    int flag = 0;
    int SENDER = 11,RECEIVER = 12;
    Menu tempMenu;
    int SOLVED=0;
    private DatabaseReference databaseReference, databaseReference1;
    FirebaseRecyclerAdapter<Message, MessageContainer> fbra;
    LinearLayoutManager linearLayoutManager;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sharedPreferences = getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        username = sharedPreferences.getString("userNamekey", "anonymous");
        userPhone = sharedPreferences.getString("userMobNumberkey", "anonymous");

        toolbar = (Toolbar) findViewById(R.id.toolbar10);
        setSupportActionBar(toolbar);
        chatProfileImage = (CircleImageView)findViewById(R.id.profileimageforum);
        title=(TextView) findViewById(R.id.actionbartitleforum);
        subtitle=(TextView) findViewById(R.id.actionbarsubtitleforum);
        if(getIntent().hasExtra("sender")) {
            flag = RECEIVER;
        }
        else {
            flag = SENDER;
        }
        //getSupportActionBar().setTitle("Forum");
        if(flag == SENDER) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            title.setText("My Problem");
            //getSupportActionBar().setTitle("My Problem");
            chatProfileImage.setVisibility(View.GONE);
            subtitle.setText(""+userPhone);
            //getSupportActionBar().setSubtitle("" + userPhone);
            databaseReference1 = FirebaseDatabase.getInstance().getReference().child(userPhone);
            HashMap<String, Object> map = new HashMap<>();
            map.put("status", "active");
            databaseReference1.updateChildren(map); //set problem status active
            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("status","Data changed"+dataSnapshot.getValue().toString());
                    if(dataSnapshot.child(phone).child("status").getValue(String.class).equals("solved")){
                        Log.d("status","solved");
                        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancelAll();
                        title.setText("Solved");
                        //getSupportActionBar().setTitle("Solved");
                        showExitAlert("Warning", "Problem Already Solved Do You Want to Exit?");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            databaseReference = FirebaseDatabase.getInstance().getReference().child(userPhone).child("message");

        } else {
            latitude = getIntent().getStringExtra("latitude");
            longitude = getIntent().getStringExtra("longitude");
            phone = getIntent().getStringExtra("sender");
            imageurl = getIntent().getStringExtra("userimage");
            imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
            imageLoader.displayImage(imageurl, chatProfileImage);
            title.setText("Forum");
            subtitle.setText(""+phone);
            //getSupportActionBar().setTitle("Forum");
            //getSupportActionBar().setSubtitle("" + phone);
            databaseReference1 = FirebaseDatabase.getInstance().getReference();
            databaseReference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("status","Data changed"+dataSnapshot.getValue().toString());
                    if(dataSnapshot.child(phone).child("status").getValue(String.class).equals("solved")){
                        Log.d("status","solved");
                        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        notificationManager.cancelAll();
                        title.setText("Solved");
                        //getSupportActionBar().setTitle("Solved");
                        tempMenu.findItem(R.id.idcm1chatsolved).setTitle("Status: Solved");
                        SOLVED=1;
                        showExitAlert("Warning", "Problem Already Solved Do You Want to Exit?");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            databaseReference1 = databaseReference1.child(phone);

            if(databaseReference1.child("status").getKey().toString().equals("solved")) { //check status of problem
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                title.setText("Solved");
                //getSupportActionBar().setTitle("Solved");
                SOLVED=1;
                showExitAlert("Alert","Problem Solved Do You Want to Exit?");
            }
            databaseReference = FirebaseDatabase.getInstance().getReference().child(phone).child("message"); //for get message
        }
        sendMessage = (ImageButton) findViewById(R.id.sendMessageButton);
        eMessage = (EditText) findViewById(R.id.typedMessage);

        MessageList = (RecyclerView) findViewById(R.id.recyclerviewofmessage);
        MessageList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        MessageList.setLayoutManager(linearLayoutManager);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String messageValue = eMessage.getText().toString().trim();
                if (!messageValue.equals("")) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if(flag == SENDER) {
            getMenuInflater().inflate(R.menu.chatmenu2, menu);
        } else {
            getMenuInflater().inflate(R.menu.chatmenu1, menu);
            this.tempMenu=menu;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idcm1chatlocation:
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<" + latitude + ">,<" + longitude + ">?q=<" + latitude + ">,<" + longitude + ">"));
                startActivity(mapIntent);
                break;
            case R.id.idcm1chatcall:
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:" + phone));
                startActivity(dialIntent);
                break;
            case R.id.idcm1chatinfo:
                break;
            case R.id.idcm1chatsolved:
                break;

            /*case R.id.idcm2chatlocation:
                break;
            case R.id.idcm2chatcall:
                break;*/
            case R.id.idcm2chatinfo:
                break;
            case R.id.idcm2chatsolved:
                /*HashMap<String, Object> map = new HashMap<>();
                map.put("status", "solved");
                databaseReference1.updateChildren(map);
                getSupportActionBar().setTitle("Solved");*/
                showExitAlert("Warning","If Problem Solved then Do You Want to Exit?");
                //Toast.makeText(ChatActivity.this,"Problem Solved",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fbra = new FirebaseRecyclerAdapter<Message, MessageContainer>
                (
                        Message.class,
                        R.layout.singlemessagelayout,
                        MessageContainer.class,
                        databaseReference
                ) {
            @Override
            protected void populateViewHolder(MessageContainer viewHolder, Message model, int position) {
                /*if(model.getUsername().equals(username)){
                    viewHolder.setGravity(Gravity.RIGHT);
                }
                else{
                    viewHolder.setGravity(Gravity.LEFT);
                }*/
                viewHolder.setUser(model.getUsername(), username);
                viewHolder.setContent(model.getContent());
                viewHolder.setTime(model.getTime());
            }


            /*@Override
            public MessageContainer onCreateViewHolder(ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.singleproblemlayout,null);
                //return new MessageContainer(view);
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            public void onBindViewHolder(MessageContainer viewHolder, int position) {
                //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
               // viewHolder.linearLayout.setLayoutParams(layoutParams);
                super.onBindViewHolder(viewHolder, position);
            }*/
        };

        fbra.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                //super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = fbra.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 || (positionStart >= (friendlyMessageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                    MessageList.scrollToPosition(positionStart);
                }
            }
        });
        MessageList.setAdapter(fbra);
    }

    public static class MessageContainer extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView message_user;
        TextView message_Content;
        TextView message_time;
        LinearLayout.LayoutParams layoutParams;

        public MessageContainer(View itemView) {
            super(itemView);
            message_user = (TextView) itemView.findViewById(R.id.musernametext);
            message_Content = (TextView) itemView.findViewById(R.id.mmessagetext);
            message_time = (TextView) itemView.findViewById(R.id.mtime);
            /*itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return false;
                    //for message action
                }
            });*/
            linearLayout = (LinearLayout) itemView.findViewById(R.id.singlemsglinearlayout);
        }

        public void setContent(String content) {
            message_Content.setText(content);
            //message_Content.setMovementMethod(LinkMovementMethod.getInstance());
        }

        public void setUser(String user, String currentusername) {
            /*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (currentusername.equals(user)) {
                Log.d("user:", user);
                layoutParams.gravity = Gravity.RIGHT;
                linearLayout.setLayoutParams(layoutParams);
            } else {
                layoutParams.gravity = Gravity.LEFT;
                linearLayout.setLayoutParams(layoutParams);
            }*/
            message_user.setText(user);
        }

        public void setTime(String time) {
            message_time.setText(time);
        }
        public void setGravity(int gravity){
            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = gravity;
            linearLayout.setLayoutParams(layoutParams);
        }
        public View getView(){
            return itemView;
        }

    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    @Override
    public void onBackPressed() {
        Log.d("Status:", "back pressed");
        if(flag==SENDER) {
            showExitAlert("Warning", "If Problem Solved then Do You Want to Exit?");
        }
        else{
            showExitAlert("Warning", "It Seems Problem May Not Solved...Do You Want to Exit?");
        }
        //super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if(flag==SENDER) {
            showExitAlert("Warning", "If Problem Solved then Do You Want to Exit?");
        }
        else{
            if(SOLVED==1)
            showExitAlert("Warning", "Do You Want to Exit?");
            else
            showExitAlert("Warning", "It Seems Problem May Not Solved...Do You Want to Exit?");
        }

        return true;
    }

    public void showExitAlert(String type, String Message) {    //for show exit application alertdialog

        Log.d("status:","exitalert");
        dialog = new AlertDialog.Builder(this)
                .setTitle(type)
                .setCancelable(false)
                .setMessage(Message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       if(flag==SENDER) {
                           HashMap<String, Object> map = new HashMap<>();
                           map.put("status", "solved");
                           databaseReference1.updateChildren(map);
                           title.setText("Solved");
                           //getSupportActionBar().setTitle("Solved");
                       }
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog!=null){
            dialog.dismiss();
        }
    }
}



