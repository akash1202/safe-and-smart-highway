package com.example.akash.myhighway1.adapter;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.akash.myhighway1.DatabaseHandler;
import com.example.akash.myhighway1.user.FriendsActivity.MyContact;
import com.example.akash.myhighway1.R;
import com.example.akash.myhighway1.data.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vishal on 1/6/2018.
 */


//this adapter work for testit activity friend list

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.MyViewHolder> implements OnMapReadyCallback {
    public List<MyContact> ContactList;
    Context context;
    PopupMenu popupMenu;
    DatabaseHandler db;
    Double lang,lat;
    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;
    String responseOfSendRequest="",tempname=null,tempnumber=null;
    private String PREFRENCENAME="AKASHSASH";
    SharedPreferences sharedPreferences;
    FragmentManager fragmentManager;
    ProgressDialog process;
    Dialog ProfilePopup;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap=googleMap;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profilecircleImageView;
        TextView nameTextView;
        TextView numberTextView;
        ImageButton optionView;

       public MyViewHolder(View view){
          super(view);
          profilecircleImageView=view.findViewById(R.id.fimage2);
          nameTextView=view.findViewById(R.id.fname2);
          numberTextView=view.findViewById(R.id.fnumber2);
          optionView=view.findViewById(R.id.foptions2);
        }
    }
    public CustomAdapter2(Context context,SupportMapFragment supportMapFragment,GoogleMap googleMap, List<MyContact> ContactList){
        this.db=new DatabaseHandler(context);
        this.googleMap=googleMap;
        this.ContactList=ContactList;
        this.context=context;
        this.supportMapFragment=supportMapFragment;
        supportMapFragment.getMapAsync(this);
        sharedPreferences=context.getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
        ProfilePopup=new Dialog(this.context);
        process = new ProgressDialog(context);
        process.setMessage("Wait...");
        process.setCancelable(false);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View tempView=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singlecontact_card2,parent,false);
        return new MyViewHolder(tempView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        tempname=null;
        tempnumber=null;
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
        holder.profilecircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Bundle bundle=new Bundle();
                process.show();
                ProfilePopup.setContentView(R.layout.cutom_profile_popup);
                CircleImageView popupProfileImage=(CircleImageView) ProfilePopup.findViewById(R.id.popupprofileimage);
                TextView popupNameText=(TextView) ProfilePopup.findViewById(R.id.popupnametext);
                TextView popupLastLocationText=(TextView) ProfilePopup.findViewById(R.id.popuplastlocationtext);
                TextView popupMobileNumberText=(TextView) ProfilePopup.findViewById(R.id.popupmobilenumbertext);
                TextView txtClose=(TextView) ProfilePopup.findViewById(R.id.popupclosebutton);
                if(isNetworkAvailable(context)) {
                    Picasso.with(context)
                            .load(Uri.parse(ContactList.get(TempPosition).getImage()))
                            .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                            .error(R.drawable.com_facebook_profile_picture_blank_square)
                            .into(holder.profilecircleImageView);
                    popupNameText.setText(ContactList.get(TempPosition).getName());
                    String temp = ContactList.get(TempPosition).getNumber().replace(" ", "").trim();
                    int length = temp.length();
                    temp = length > 10 ? temp.substring((length - 1) - 9, length) : temp;
                    popupMobileNumberText.setText(temp);
                    txtClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ProfilePopup.dismiss();
                        }
                    });
                    //send request start
                    sendRequest sendrequest = new sendRequest(context, googleMap);
                    Log.d("address In Popup","In network available:"+temp);
                    String s1[] = {context.getString(R.string.appwebsite)+"/api/getprofile.php", "user", temp, "POPUP"};
                    sendrequest.execute(s1);
                    // send request end
                }
                else {
                    Log.d("address In Popup","not In network available");
                    Picasso.with(context)
                            .load(Uri.parse(ContactList.get(TempPosition).getImage()))
                            .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                            .error(R.drawable.com_facebook_profile_picture_blank_square)
                            .into(holder.profilecircleImageView);
                    popupNameText.setText(ContactList.get(TempPosition).getName());
                    String temp = ContactList.get(TempPosition).getNumber().replace(" ", "").trim();
                    int length = temp.length();
                    temp = length > 10 ? temp.substring((length - 1) - 9, length) : temp;
                    popupMobileNumberText.setText(temp);
                    txtClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ProfilePopup.dismiss();
                        }
                    });
                    process.cancel();
                    ProfilePopup.show();
                }
                /*String todoId=todoid[position];
                String  todoName=name[position];
                String todoTag=tag[position];
                String todoIsCompleted=isCompleted[position];
                String todoModified=modified[position];
                String todocreated=created[position];
                bundle.putString("todoid",todoId);
                bundle.putString("name",todoName);
                bundle.putString("tag",todoTag);
                bundle.putString("isCompleted",todoIsCompleted);
                bundle.putString("created",todocreated);
                bundle.putString("modified",todoModified);
                UpdateToDo updateToDo=new UpdateToDo();
                updateToDo.setArguments(bundle);
                FragmentTransaction transaction=fragmentManager.beginTransaction();
                transaction.add(updateToDo,"hello").commit();*/
            }
        });
        holder.optionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu=new PopupMenu(context,view);
                popupMenu.getMenu().add("On Map");
                popupMenu.getMenu().add("Message");
                popupMenu.getMenu().add("Remove");
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("On Map")){
                            sendRequest sendrequest=new sendRequest(context,googleMap);
                            String temp=holder.numberTextView.getText().toString().replace(" ","").trim();
                            int length=temp.length();
                            temp= length>10? temp.substring((length-1)-9,length):temp;
                            String s1[]={"https://myhighway.000webhostapp.com/api/getprofile.php","user",temp,"MAP"};
                            sendrequest.execute(s1);
                            Toast.makeText(context,"View clicked...",Toast.LENGTH_SHORT).show();
                        }
                        if(item.getTitle().equals("Message")){
                            Intent intentSMS=new Intent(Intent.ACTION_VIEW,Uri.parse("smsto:"+tempnumber));
                            ClipboardManager clipboardManager= (ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                            if(clipboardManager.hasPrimaryClip()){
                                ClipData.Item dataItem=clipboardManager.getPrimaryClip().getItemAt(0);
                                intentSMS.putExtra("sms_body",dataItem.getText().toString());
                            }
                            else{
                               /* GPSTracker tempTracker=new GPSTracker(context,(Activity) context);
                                if(tempTracker.cangetLocation()){
                                    googleMap.
                                }*/
                                //String mapLocation="http://www.google.com/maps/place/"+marker.getPosition().latitude+","+marker.getPosition().longitude
                                intentSMS.putExtra("sms_body","");
                            }
                            context.startActivity(intentSMS);
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
    public class sendRequest extends AsyncTask<String,String,String>{
        // AVLoadingIndicatorView loader=new AVLoadingIndicatorView(LoginActivity.this);
        Context context;
        GoogleMap googleMap;
        public sendRequest(Context context,GoogleMap googleMap){
            this.context=context;
            this.googleMap=googleMap;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            responseOfSendRequest="";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {super.onProgressUpdate(values);        }

        @Override
        protected String doInBackground(final String... strings) {
            RequestQueue requestQueue= Volley.newRequestQueue(context);
            StringRequest stringRequest=new StringRequest(Request.Method.POST, strings[0], new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //Toast.makeText(context,response,Toast.LENGTH_LONG).show();
                    process.cancel();
                    responseOfSendRequest=response;
                    if(!responseOfSendRequest.equals("")) {
                        try {
                            //Toast.makeText(context,""+responseOfSendRequest,Toast.LENGTH_LONG).show();
                            
                            JSONObject jsonObject = new JSONObject(responseOfSendRequest);
                            String location = jsonObject.getString("last_location");
                            String username = jsonObject.getString("username");
                            String lastseen = jsonObject.getString("last_seen");
                            String userImageURL=jsonObject.getString("userimage");
                            String userPhone=jsonObject.getString("primary_phone");
                            String userEmail=jsonObject.getString("email");
                            lastseen = lastseen.substring(0, 19);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date = simpleDateFormat.parse(lastseen);
                            SimpleDateFormat outformat = new SimpleDateFormat("EEE dd MMM yyyy HH:mm a"); //
                            lastseen = outformat.format(date);
                            Log.d("last_seen:", lastseen);
                            String loc[] = location.split(",");
                            lat = Double.parseDouble(loc[0]);
                            lang = Double.parseDouble(loc[1]);
                            LatLng current = new LatLng(lat, lang); //response users latlng
                            LatLng thisuserloc=new LatLng((new User(context)).getLastLocationLat(),(new User(context)).getLastLocationLong());
                            Location a=new Location("tempA");
                            a.setLatitude(lat);
                            a.setLongitude(lang);
                            Location b=new Location("tempB");
                            b.setLatitude((new User(context)).getLastLocationLat());
                            b.setLongitude((new User(context)).getLastLocationLong());
                            double distanceab=a.distanceTo(b);
                            distanceab=distanceab/1000;
                            String strdistanceab=String.format("%2.2f",distanceab);
                            String address = "";
                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                            List<Address> addresses;
                            addresses = geocoder.getFromLocation(lat, lang, 1);
                            address = addresses.get(0).getAddressLine(0); //for get Full address from location
                            String city = addresses.get(0).getLocality();
                            String state = addresses.get(0).getAdminArea();
                            String country = addresses.get(0).getCountryName();
                            String postalcode = addresses.get(0).getPostalCode();
                            String knownName = addresses.get(0).getFeatureName();
                           if(strings[3].equals("POPUP")){
                               Log.d("address In Popup",address);
                               CircleImageView popupProfileImage=(CircleImageView) ProfilePopup.findViewById(R.id.popupprofileimage);
                               TextView popupNameText=(TextView) ProfilePopup.findViewById(R.id.popupnametext);
                               TextView popupLastLocationText=(TextView) ProfilePopup.findViewById(R.id.popuplastlocationtext);
                               TextView popupMobileNumberText=(TextView) ProfilePopup.findViewById(R.id.popupmobilenumbertext);
                               TextView popupEmailText=(TextView) ProfilePopup.findViewById(R.id.popupemailtext);
                               TextView popuplastseenText=(TextView) ProfilePopup.findViewById(R.id.popuplastseentext);
                               TextView txtClose=(TextView) ProfilePopup.findViewById(R.id.popupclosebutton);
                               popupNameText.setText(username);
                               popupMobileNumberText.setText(userPhone);
                               popupEmailText.setText(userEmail);
                               popupLastLocationText.setText(address);
                               popuplastseenText.setText(lastseen);
                               txtClose.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View view) {
                                       ProfilePopup.dismiss();
                                   }
                               });
                               com.nostra13.universalimageloader.core.ImageLoader imageLoader= com.nostra13.universalimageloader.core.ImageLoader.getInstance();
                               imageLoader.init(ImageLoaderConfiguration.createDefault(context));
                               imageLoader.displayImage(userImageURL,popupProfileImage);
                               ProfilePopup.show();
                           }
                            else if(strings[3].equals("MAP")) {   CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(current)
                                    .zoom(17)
                                    .bearing(90)
                                    .tilt(80)
                                    .build();
                            if (googleMap != null) {
                                googleMap.addMarker(new MarkerOptions().position(current).title(username + "\n" + lastseen).snippet(strdistanceab+"kms :" + address + ""));
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
                            }
                        }
                            } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else if(strings[3].equals("POPUP")&&response.equals("")){
                        ProfilePopup.show();
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    process.cancel();
                    Toast.makeText(context,"something went wrong!",Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> params=new HashMap<String, String>();
                    for(int i=1;i+1<strings.length;i+=2)
                        params.put(strings[i],strings[i+1]);
                    return params;
                }
            };
            requestQueue.add(stringRequest);
           /* showProgressbar(2);
            try {
                Thread.sleep(3*1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
            loader.hide();
            }*/
            return null;
        }
    }

    public boolean isNetworkAvailable(Context context){
        ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (manager.getActiveNetworkInfo()!=null&&manager.getActiveNetworkInfo().isConnected());
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
