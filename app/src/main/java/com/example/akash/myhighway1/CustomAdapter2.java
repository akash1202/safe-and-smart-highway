package com.example.akash.myhighway1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
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
import com.example.akash.myhighway1.Friends.MyContact;
import com.example.akash.myhighway1.POJO.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
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

public class CustomAdapter2 extends RecyclerView.Adapter<CustomAdapter2.MyViewHolder> implements OnMapReadyCallback {
    public List<MyContact> ContactList;
    Context context;
    PopupMenu popupMenu;
    DatabaseHandler db;
    Double lang,lat;
    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;
    String responseOfSendRequest="";
    private String PREFRENCENAME="AKASHSASH";
    SharedPreferences sharedPreferences;

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
          profilecircleImageView=view.findViewById(R.id.fimage);
          nameTextView=view.findViewById(R.id.fname);
          numberTextView=view.findViewById(R.id.fnumber);
          optionView=view.findViewById(R.id.foptions);
        }
    }
    public CustomAdapter2(Context context, SupportMapFragment supportMapFragment,GoogleMap googleMap, List<MyContact> ContactList){
        this.db=new DatabaseHandler(context);
        this.googleMap=googleMap;
        this.ContactList=ContactList;
        this.context=context;
        this.supportMapFragment=supportMapFragment;
        supportMapFragment.getMapAsync(this);
        sharedPreferences=context.getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View tempView=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singlecontact_card,parent,false);
        return new MyViewHolder(tempView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
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
                            sendRequest sendrequest=new sendRequest(context,googleMap);
                            String temp=holder.numberTextView.getText().toString().replace(" ","").trim();
                            int length=temp.length();
                            temp= length>10? temp.substring((length-1)-9,length):temp;
                            String s1[]={"https://myhighway.000webhostapp.com/api/getprofile.php","user",temp};
                            sendrequest.execute(s1);
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
    public class sendRequest extends AsyncTask<String,String,String>{
        // AVLoadingIndicatorView loader=new AVLoadingIndicatorView(MainActivity.this);
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
                    responseOfSendRequest=response;
                    if(!responseOfSendRequest.equals("")) {
                        try {
                            //Toast.makeText(context,""+responseOfSendRequest,Toast.LENGTH_LONG).show();
                            JSONObject jsonObject = new JSONObject(responseOfSendRequest);
                            String location = jsonObject.getString("last_location");
                            String username = jsonObject.getString("username");
                            String lastseen=jsonObject.getString("last_seen");
                            lastseen=lastseen.substring(0,19);
                            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date=simpleDateFormat.parse(lastseen);
                            SimpleDateFormat outformat=new SimpleDateFormat("EEE dd MMM yyyy HH:mm");
                            lastseen=outformat.format(date);
                            Log.d("last_seen:",lastseen);
                            String loc[] = location.split(",");
                            lat = Double.parseDouble(loc[0]);
                            lang = Double.parseDouble(loc[1]);
                            LatLng current = new LatLng(lat, lang);
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
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(current)
                                    .zoom(17)
                                    .bearing(90)
                                    .tilt(80)
                                    .build();
                            if(googleMap!=null) {
                                googleMap.addMarker(new MarkerOptions().position(current).title(username+"\n"+lastseen).snippet(""+address + ""));
                                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
                             }
                            } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String,String> params=new HashMap<String, String>();
                    for(int i=1;i+1<=strings.length;i+=2)
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
