package com.example.akash.myhighway1.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.akash.myhighway1.DatabaseHandler;
import com.example.akash.myhighway1.R;
import com.example.akash.myhighway1.adapter.CustomAdapter;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.group.Group;
import com.onegravity.contactpicker.picture.ContactPictureType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    ListView listView;
    GridView gridView;
    RecyclerView recyclerView;
    FloatingActionButton addmore;
    String[] name1, number1, image1;
    RecyclerView.LayoutManager layoutManager;
    CustomAdapter customAdapter;
    Integer totalfriends = 0;
    Integer old1 = 0, new1 = 0;
    ArrayList<String> friendsname;
    List<MyContact> ContactList=null;
    DatabaseHandler db;
    String[] options={"View","Remove","Chat"};
    private String PREFRENCENAME="AKASHSASH";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final int REQUEST_CONTACT=12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        db=new DatabaseHandler(getApplicationContext());

        sharedPreferences=getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putInt("friends",db.getContactsCount()).commit();
        totalfriends=sharedPreferences.getInt("friends",0);
        ContactList=db.getAllContacts();
              // listView=(ListView) findViewById(R.id.flistview);
        recyclerView=(RecyclerView) findViewById(R.id.freyclerview);

        //registerForContextMenu(listView);
       //gridView=(GridView) findViewById(R.id.fgridview);
       //listView.setVisibility(View.INVISIBLE);
        Toolbar toolbar=(Toolbar) findViewById(R.id.toolbar1);
        //getActionBar().setCustomView(t);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FriendsActivity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       addmore= (FloatingActionButton) findViewById(R.id.addfriendButton);
       addmore.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               totalfriends=sharedPreferences.getInt("friends",0);
               old1=totalfriends;
               if(totalfriends<1000) {
                   Intent intent = new Intent(FriendsActivity.this, ContactPickerActivity.class)
                           .putExtra(ContactPickerActivity.EXTRA_THEME, R.style.ContactPicker_Theme_Light)
                           .putExtra(ContactPickerActivity.EXTRA_SELECT_CONTACTS_LIMIT, 1000 - totalfriends)
                           .putExtra(ContactPickerActivity.EXTRA_ONLY_CONTACTS_WITH_PHONE, true)
                           .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name())
                           .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, false)
                           .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION, ContactDescription.ADDRESS.name())
                           .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                           .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER, ContactSortOrder.AUTOMATIC.name());
                   startActivityForResult(intent, REQUEST_CONTACT);
               }
               else{
                   Toast.makeText(FriendsActivity.this, "You should remove before add more!!", Toast.LENGTH_LONG).show();
               }
           }
       });
       String[] name={"akash","vishal","gaurang"};
       String[] number={"9016435625","9510030257","8530123135"};
       String[] image={"9016435625","9510030257","8530123135"};
       // name12=name;
      //  number12=number;
        //image12=image;
        customAdapter = new CustomAdapter(FriendsActivity.this, ContactList);  //here use only context of current activity
        //gridView.setAdapter(customAdapter);                                   //for poppup support
        //  listView.setAdapter(customAdapter);
        layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(customAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK &&
                data != null && data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA)) {

            // we got a result from the contact picker
           /* String[] name1;
            String[] number1;
            String[] image1;*/

            // process contacts
            List<Contact> contacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);
            int i=0;
            name1=new String[contacts.size()];
            number1=new String[contacts.size()];
            image1=new String[contacts.size()];
            List<MyContact> ContactList= new ArrayList<>();
            for (Contact contact : contacts) {
                // process the contacts...
                name1[i]=contact.getDisplayName();
                number1[i]=contact.getPhone(0).toString();

                //Toast.makeText(this,"image:"+contact.getPhotoUri().toString(),Toast.LENGTH_LONG).show();
                if(contact.getPhotoUri()==null)
                image1[i]="";
                else
                    image1[i]=contact.getPhotoUri().toString();
                ContactList.add(new MyContact(name1[i],number1[i],image1[i]));
                db.addContact(new MyContact(name1[i],number1[i],image1[i]));
                i++;
            }

            // process groups
            List<Group> groups = (List<Group>) data.getSerializableExtra(ContactPickerActivity.RESULT_GROUP_DATA);
            for (Group group : groups) {
                // process the groups...
                Collection<Contact> contacts1 = (Collection<Contact>) group.getContacts();
                int j=0;
                for (Contact contact : contacts1) {
                    name1[j]=contact.getDisplayName();
                    number1[j]=contact.getPhone(0).toString();
                    if(contact.getPhotoUri()==null)
                        image1[j]="";
                    else
                    image1[j]=contact.getPhotoUri().toString();
                    ContactList.add(new MyContact(name1[i],number1[i],image1[i]));
                    db.addContact(new MyContact(name1[i],number1[i],image1[i]));
                    j++;
                }
            }

            //this.ContactList.addAll(ContactList);
           // recyclerView.removeAllViewsInLayout();
            //customAdapter=new CustomAdapter(getApplicationContext());

            //customAdapter.notifyDataSetChanged();
            new1=db.getContactsCount();
            customAdapter.updateContactList(db.getAllContacts());
            recyclerView.getAdapter().notifyItemChanged(0,db.getContactsCount());
            editor.putInt("friends",db.getContactsCount()).commit();
            //gridView.setAdapter(customAdapter);
          //  listView.setAdapter(customAdapter);
            //layoutManager= new LinearLayoutManager(getApplicationContext());
            //recyclerView.setLayoutManager(layoutManager);
            //recyclerView.setItemAnimator(new DefaultItemAnimator());
           // recyclerView.setAdapter(customAdapter);
            // registerForContextMenu(listView);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


   /* @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);

        *//*if(v.getId()==R.id.flistview){
            AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)menuInfo;
            MenuInflater inflater=getMenuInflater();
            inflater.inflate(R.menu.friend_options,menu);
            menu.setHeaderTitle(name1[info.position]);
        }*//*
    }*/

   /* @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
            return true;
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    public static class MyContact{
       public String name="";
       public String number="";
       public String image="";

        public MyContact(String name, String number, String image) {
        this.name=name;
        this.number=number;
        this.image=image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
