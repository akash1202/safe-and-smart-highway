package com.example.akash.myhighway;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vishal on 1/6/2018.
 */

public class CustomAdapter extends ArrayAdapter<String>{
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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
       /* optionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               popupMenu=new PopupMenu(context,view);
               popupMenu.getMenu().add("View");
               popupMenu.getMenu().add("Message");
               popupMenu.getMenu().add("Remove");
            }
        });*/
        numberTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

        return tempView;
    }

    public void showOption(Context context,int id){
    }
}
