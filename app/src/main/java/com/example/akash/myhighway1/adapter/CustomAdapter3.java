package com.example.akash.myhighway1.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akash.myhighway1.Problem;
import com.example.akash.myhighway1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Vishal on 1/6/2018.
 */

public class CustomAdapter3 extends RecyclerView.Adapter<CustomAdapter3.MyViewHolder> implements Filterable {
    public List<Problem> ProblemList;
    public List<Problem> ProblemlistFiltered;
    Context context;
    PopupMenu popupMenu;
    SearchView searchView;
    String searchString;
    private String PREFRENCENAME = "AKASHSASH";
    SharedPreferences sharedPreferences;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profilecircleImageView;
        TextView pnameTextView;
        TextView pstatusTextView;
        ImageButton optionView;
        ImageButton expandButtonView;
        TextView plocation, pspeed, ptime;

        LinearLayout plocationlayout, pspeedlayout;

        public MyViewHolder(View view) {
            super(view);
            profilecircleImageView = (CircleImageView) view.findViewById(R.id.pimage);
            pnameTextView = view.findViewById(R.id.pname);
            optionView = view.findViewById(R.id.poptions);
            expandButtonView = view.findViewById(R.id.pexpand);
            pstatusTextView = view.findViewById(R.id.pstatus);
            plocation = view.findViewById(R.id.plocationtext);
            pspeed = view.findViewById(R.id.pspeedtext);
            ptime = view.findViewById(R.id.ptimetext);
            plocationlayout = view.findViewById(R.id.plocationlayout);
            pspeedlayout = view.findViewById(R.id.pspeedlayout);

        }
    }

    public CustomAdapter3(Context context, FragmentManager fragmentManager, List<Problem> ProblemList, SearchView searchView) {
        this.ProblemList = ProblemList;
        this.ProblemlistFiltered = ProblemList;
        this.context = context;
        this.searchView = searchView;
        sharedPreferences = context.getSharedPreferences(PREFRENCENAME, Context.MODE_PRIVATE);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View tempView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.singleproblemlayout, parent, false);
        return new MyViewHolder(tempView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        try {
            String tempname = "";
            String tempstatus = "";
            String templocation = "";
            String tempspeed = "";
            String temptime = "";
            tempname = getProblemName(this.ProblemlistFiltered.get(position).getProblemcode());
            tempstatus = this.ProblemlistFiltered.get(position).getStatus().equals("pending") ? "pending" + "..." : this.ProblemlistFiltered.get(position).getStatus();
            templocation = this.ProblemlistFiltered.get(position).getProblem_loc1() + "";
            tempspeed = this.ProblemlistFiltered.get(position).getSpeed() + "";
            temptime = this.ProblemlistFiltered.get(position).getTime1() + "";

            //SimpleDateFormat formatter=new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            //formatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
            //temptime=formatter.format(new Date(temptime));

            final Problem TempProblem = this.ProblemlistFiltered.get(position);
            final int TempPosition = position;
            holder.pnameTextView.setText(tempname);
            holder.pstatusTextView.setText(tempstatus);
            holder.plocation.setText(templocation);
            holder.pspeed.setText(tempspeed + "km/h");
            holder.ptime.setText(temptime);
            holder.plocationlayout.setVisibility(View.GONE);
            holder.pspeedlayout.setVisibility(View.GONE);
            holder.optionView.setVisibility(View.GONE);
            holder.expandButtonView.setVisibility(View.VISIBLE);

           /* int index= tempname.indexOf(searchString);
           SpannableStringBuilder ssb=new SpannableStringBuilder(searchString);
           ForegroundColorSpan fcs= new ForegroundColorSpan(Color.parseColor("#FFFFFF"));
           ssb.setSpan(fcs,index,index+searchString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
           holder.pnameTextView.setText(ssb);
        */
            /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    new TextHighlighter()
                            .addTarget(holder.pnameTextView)
                            .setBackgroundColor(R.color.colorWhatsapp1)
                            .setForegroundColor(R.color.whitecolor)
                            .highlight(newText,TextHighlighter.BASE_MATCHER);
                    return false;
                }
            });*/


            holder.plocationlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri googleMapParams = Uri.parse("google.navigation:q=" + holder.plocation.getText().toString());
                    Intent MapIntent = new Intent(Intent.ACTION_VIEW, googleMapParams);
                    MapIntent.setPackage("com.google.android.apps.maps");
                    if (MapIntent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(MapIntent);
                    }
                }
            });

            holder.expandButtonView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.plocationlayout.setVisibility(View.VISIBLE);
                    holder.pspeedlayout.setVisibility(View.VISIBLE);
                    holder.expandButtonView.setVisibility(View.GONE);
                    holder.optionView.setVisibility(View.VISIBLE);
                }
            });
            holder.optionView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(context, "hello", Toast.LENGTH_SHORT).show();
                    popupMenu = new PopupMenu(context, view);
                    popupMenu.getMenu().add("View");
                    popupMenu.getMenu().add("Message");
                    popupMenu.getMenu().add("Remove");
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if (item.getTitle().equals("View")) {
                                Toast.makeText(context, "View clicked...", Toast.LENGTH_SHORT).show();
                            }
                            if (item.getTitle().equals("Message")) {
                                Toast.makeText(context, "Message clicked...", Toast.LENGTH_SHORT).show();
                            }
                            if (item.getTitle().equals("Remove")) {
                                removeItem(TempPosition, TempProblem);
                            }
                            return false;
                        }
                    });

                }
            });
            Picasso.with(context)
                    .load(Uri.parse(getProblemPicture(this.ProblemlistFiltered.get(position).getProblemcode())))
                    .resize(50, 50)
                    .centerInside()
                    .placeholder(R.drawable.problem_electric)
                    .error(R.drawable.problem_electric)
                    .into(holder.profilecircleImageView);
        } catch (Exception e) {
            Log.d("Error(CustomAdapter3):", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return this.ProblemlistFiltered.size();
    }

    public void updateContactList(List<Problem> ProblemList) {
        this.ProblemList = ProblemList;
        notifyItemRangeChanged(0, this.ProblemList.size());
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charsequence = charSequence.toString();
                if (charsequence.isEmpty()) {
                    ProblemlistFiltered = ProblemList;
                } else {
                    List<Problem> FilteredList = new ArrayList<Problem>();
                    for (Problem problem : ProblemList) {
                        if ((getProblemName(problem.getProblemcode()).trim().toLowerCase()).contains(charsequence.toLowerCase())) {
                            Log.d("search:", charsequence.toLowerCase());
                            Log.d("found:", getProblemName(problem.getProblemcode()).toLowerCase());
                            FilteredList.add(problem);
                        }
                    }
                    ProblemlistFiltered = FilteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = ProblemlistFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ProblemlistFiltered = (ArrayList<Problem>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public void removeItem(int position, Problem TempProblem) {
        ProblemList.remove(position);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int totalproblems = sharedPreferences.getInt("problems", 0);
        if (totalproblems > 1)
            editor.putInt("problems", totalproblems - 1).commit();
        notifyItemRemoved(position);
        //notifyItemRangeChanged(position,ContactList.size());
        notifyItemRangeChanged(position, ProblemList.size());
        Toast.makeText(context, "Removed...", Toast.LENGTH_SHORT).show();
    }


    public String getProblemName(int id) {
        String problemName = "";
        switch (id) {
            case 0:
                problemName = "NA";
                break;
            case 1:
                problemName = "Normal";
                break;
            case 2:
                problemName = "Kidnap";
                break;
            case 3:
                problemName = "Accident Help";
                break;
            case 4:
                problemName = "Serious Accident";
                break;
            case 5:
                problemName = "Medical Help";
                break;
            case 6:
                problemName = "Other Person Help";
                break;
            case 7:
                problemName = "Technical Help";
                break;
            default:
                problemName = "NA";
                break;
        }
        return problemName;
    }

    public String getProblemPicture(int id) {
        String problemName = "";
        String problemPicture = "";
        switch (id) {
            case 0:
                problemName = "NA";
                problemPicture = getURLForResource(R.drawable.problem_electric);
                break;
            case 1:
                problemName = "Normal";
                problemPicture = getURLForResource(R.drawable.problem_electric);
                break;
            case 2:
                problemName = "Kidnap";
                problemPicture = getURLForResource(R.drawable.kidnapping);
                break;
            case 3:
                problemName = "Accident Help";
                problemPicture = getURLForResource(R.drawable.accident_help);
                break;
            case 4:
                problemName = "Serious Accident";
                problemPicture = getURLForResource(R.drawable.serious_person_accident);
                break;
            case 5:
                problemName = "Medical Help";
                problemPicture = getURLForResource(R.drawable.medical_help);
                break;
            case 6:
                problemName = "Other Person Help";
                problemPicture = getURLForResource(R.drawable.other_person_help);
                break;
            case 7:
                problemName = "Technical Help";
                problemPicture = getURLForResource(R.drawable.technical_help);
                break;
            default:
                problemName = "NA";
                problemPicture = getURLForResource(R.drawable.problem_electric);
                break;
        }
        return problemPicture;
    }


    public String getURLForResource(int resourceId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
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
