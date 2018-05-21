package com.example.akash.myhighway1;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.onegravity.contactpicker.contact.Contact;
import com.squareup.picasso.Picasso;
import com.example.akash.myhighway1.Friends.MyContact;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vishal on 1/6/2018.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{
    public List<MyContact> ContactList;
    Context context;
    PopupMenu popupMenu;
    DatabaseHandler db;
    private String PREFRENCENAME="AKASHSASH";
    SharedPreferences sharedPreferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profilecircleImageView;
        TextView nameTextView;
        TextView numberTextView;
        ImageButton optionView;

       public MyViewHolder(View view){
          super(view);
          profilecircleImageView=view.findViewById(R.id.fimage);
          nameTextView=view.findViewById(R.id.fname);
          numberTextView=view.findViewById(R.id.fnumber);
          optionView=view.findViewById(R.id.foptions);
        }
    }
    public CustomAdapter(Context context,List<MyContact> ContactList){
        this.db=new DatabaseHandler(context);
        this.ContactList=ContactList;
        this.context=context;
        sharedPreferences=context.getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View tempView=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singlecontact_card,parent,false);
        return new MyViewHolder(tempView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,int position) {
        String tempname=null;
        String tempnumber=null;
        final int TempPosition=position;
        if(this.ContactList.get(position).getName().length()>11)
            tempname=this.ContactList.get(position).getName().substring(0,11)+"..";
        else
            tempname=this.ContactList.get(position).getName();
        tempnumber=this.ContactList.get(position).getNumber().replace(" ","");
        tempnumber=tempnumber.replace("+","");
        final MyContact TempContact=this.ContactList.get(position);
        holder.nameTextView.setText(tempname);
        holder.numberTextView.setText(tempnumber);
        holder.optionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu=new PopupMenu(context,view);
                popupMenu.getMenu().add("View");
                popupMenu.getMenu().add("Message");
                popupMenu.getMenu().add("Remove");
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("View")){
                            Toast.makeText(context,"View clicked...",Toast.LENGTH_SHORT).show();
                        }
                        if(item.getTitle().equals("Message")){
                            Toast.makeText(context,"Message clicked...",Toast.LENGTH_SHORT).show();
                        }
                        if(item.getTitle().equals("Remove")){
                            removeItem(TempPosition,TempContact);
                        }
                        return false;
                    }
                });
            }
        });
        Picasso.with(context)
                .load(Uri.parse(ContactList.get(position).getImage()))
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_profile_picture_blank_square)
                .into(holder.profilecircleImageView);
    }


    @Override
    public int getItemCount() {   return this.ContactList.size();  }
    public void updateContactList(List<MyContact> ContactList){
        this.ContactList=ContactList;
        notifyItemRangeChanged(0,this.ContactList.size());
    }

   public void removeItem(int position,MyContact TempContact){
        db.deleteContact(TempContact);
        ContactList.remove(position);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        int totalfriends=sharedPreferences.getInt("friends",0);
        if(totalfriends>1)
        editor.putInt("friends",totalfriends-1).commit();
        notifyItemRemoved(position);
        //notifyItemRangeChanged(position,ContactList.size());
        notifyItemRangeChanged(position,ContactList.size());
       Toast.makeText(context,"Removed...",Toast.LENGTH_SHORT).show();
   }

}




/*public class CustomAdapter extends ArrayAdapter<String>{
  Context context;
  String[] name;
  String[] number;
  String[] image;
  String[] options={"View","Remove","Chat"};
  PopupMenu popupMenu;
    public  CustomAdapter(Context context,String[] name,String[] number,String[] image){
      super(context,R.layout.singlecontact_card,R.id.fnumber,number);
      this.context=context;
      this.name=name;
      this.number=number;
      this.image=image;
     // ArrayAdapter<String> adapter=new ArrayAdapter<String>()
  }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull  ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View tempView=inflater.inflate(R.layout.singlecontact_card,parent,false);
        CircleImageView profilecircleImageView=(CircleImageView) tempView.findViewById(R.id.fimage);
        TextView nameTextView=(TextView) tempView.findViewById(R.id.fname);
        TextView numberTextView=(TextView) tempView.findViewById(R.id.fnumber);
        ImageButton optionView=(ImageButton) tempView.findViewById(R.id.foptions);
       // Spinner spinner=(Spinner) tempView.findViewById(R.id.fSpinner);
       // Toast.makeText(context,"image:"+image[position],Toast.LENGTH_SHORT).show();
        Picasso.with(context)
                .load(Uri.parse(image[position]))
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .error(R.drawable.com_facebook_profile_picture_blank_square)
                .into(profilecircleImageView);
        String tempname=null;
        String tempnumber=null;
            if(name[position].length()>11)
            tempname=name[position].substring(0,11)+"..";
        else
            tempname=name[position];
        tempnumber=number[position].replace(" ","");
        tempnumber=tempnumber.replace("+","");
        nameTextView.setText(tempname);
        numberTextView.setText(tempnumber);
        profilecircleImageView.setImageURI(Uri.parse(image[position]));
        final int position1=position;
//        profilecircleImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context,name[position],Toast.LENGTH_SHORT).show();
//            }
//        });
//        numberTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(context,number[position],Toast.LENGTH_SHORT).show();
//            }
//        });
        optionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               popupMenu=new PopupMenu(context,view);
               popupMenu.getMenu().add("View");
               popupMenu.getMenu().add("Message");
               popupMenu.getMenu().add("Remove");
               popupMenu.show();
               popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                   @Override
                   public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("View")){
                            Toast.makeText(context,"View clicked...",Toast.LENGTH_SHORT).show();
                        }
                       if(item.getTitle().equals("Message")){
                        Toast.makeText(context,"Message clicked...",Toast.LENGTH_SHORT).show();
                       }
                       if(item.getTitle().equals("Remove")){
                           Toast.makeText(context,"Remove clicked...",Toast.LENGTH_SHORT).show();
                       }
                       return false;
                   }
               });
            }
        });
       *//*optionView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(context,"",Toast.LENGTH_SHORT).show();
           }

       });
        tempView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });*//*

        return tempView;
    }
}*/
