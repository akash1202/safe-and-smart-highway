package com.example.akash.myhighway;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.group.Group;
import com.onegravity.contactpicker.picture.ContactPictureType;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public class Friends extends AppCompatActivity {

    ListView listView;
    Button addmore;
    String[] name12,number12,image12;
    public static final int REQUEST_CONTACT=12;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
       listView=(ListView) findViewById(R.id.flistview);
       addmore= (Button)findViewById(R.id.addfriendButton);
       addmore.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(Friends.this, ContactPickerActivity.class)
                       .putExtra(ContactPickerActivity.EXTRA_THEME,R.style.ContactPicker_Theme_Light)
                       .putExtra(ContactPickerActivity.EXTRA_SELECT_CONTACTS_LIMIT,10)
                       .putExtra(ContactPickerActivity.EXTRA_ONLY_CONTACTS_WITH_PHONE,true)
                       .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name())
                       .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, false)
                       .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION, ContactDescription.ADDRESS.name())
                       .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                       .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER, ContactSortOrder.AUTOMATIC.name());
               startActivityForResult(intent, REQUEST_CONTACT);
           }
       });
       String[] name={"akash","vishal","gaurang"};
       String[] number={"9016435625","9510030257","8530123135"};
       String[] image={"9016435625","9510030257","8530123135"};
        name12=name;
        number12=number;
        image12=image;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK &&
                data != null && data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA)) {

            // we got a result from the contact picker
            String[] name1;
            String[] number1;
            String[] image1;

            // process contacts
            List<Contact> contacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);
            int i=0;
            name1=new String[contacts.size()];
            number1=new String[contacts.size()];
            image1=new String[contacts.size()];
            for (Contact contact : contacts) {
                // process the contacts...
                name1[i]=contact.getDisplayName();
                number1[i]=contact.getPhone(0).toString();
                //Toast.makeText(this,"image:"+contact.getPhotoUri().toString(),Toast.LENGTH_LONG).show();
//                if(contact.getPhotoUri().equals(""))
                image1[i]="";
                /*else
                    image1[i]=contact.getPhotoUri().toString();*/
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
                    image1[j]=contact.getPhotoUri().toString();
                    j++;
                }
            }
            CustomAdapter customAdapter=new CustomAdapter(this,name1,number1,image1);
            listView.setAdapter(customAdapter);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}