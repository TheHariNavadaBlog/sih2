package com.example.sih2;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arasthel.asyncjob.AsyncJob;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmpHomeFragment extends Fragment{
    employeeHomeSelected listener;
    SharedPrefrencesHelper sharedPrefrencesHelper;
    RecyclerView empHomeRV;
    private RequestQueue rQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_emp_home,container,false);

        sharedPrefrencesHelper =new SharedPrefrencesHelper(this.getActivity());
        ImageSlider imageSlider=view.findViewById(R.id.slider);
        empHomeRV=view.findViewById(R.id.empHomeRV);

        List<SlideModel> slideModels=new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.betterfuture));
        slideModels.add(new SlideModel("https://news.efinancialcareers.com/binaries/content/gallery/efinancial-careers/articles/2019/05/google-san-fran.jpg","  Google"));
        slideModels.add(new SlideModel("https://www.ft.com/__origami/service/image/v2/images/raw/https%3A%2F%2Fd1e00ek4ebabms.cloudfront.net%2Fproduction%2Fe0ca7758-da2c-4f49-aa87-b35f4a7551a5.jpg?fit=scale-down&source=next&width=700","   Microsoft"));
        slideModels.add(new SlideModel("https://zdnet2.cbsistatic.com/hub/i/r/2017/03/13/9771951a-1439-4fab-8424-ca024674545e/resize/770xauto/735eb233abb7fad6d8bd505c8c2adf57/apple-event.jpg","   Apple"));
        slideModels.add(new SlideModel("https://cdnuploads.aa.com.tr/uploads/Contents/2019/02/06/thumbs_b_c_2f7bc0bac6400f27b8bdec0cf6d40f7d.jpg?v=181112","   WhatsApp"));
        slideModels.add(new SlideModel("https://www.infosys.com/content/dam/infosys-web/en/global-resource/media-resources/images/Bangalore-New-001.jpg","   Infosys"));
        slideModels.add(new SlideModel("https://cdn.mos.cms.futurecdn.net/6BhB3LboXX6eYMbwqgAxDn-650-80.jpg.webp","  IBM"));
        slideModels.add(new SlideModel("https://english.cdn.zeenews.com/sites/default/files/2020/05/18/861577-accenture.jpg","   Accenture"));
        slideModels.add(new SlideModel("https://content.techgig.com/thumb/msid-72051706,width-860,resizemode-4/TCS-joins-hands-with-Phoenix-Group-to-transform-Standard-Life-business.jpg?488935","   TCS"));
        slideModels.add(new SlideModel("https://i0.wp.com/nairametrics.com/wp-content/uploads/2019/02/JPMorgan-Chase-Co.....-JPMorgan-Chase.png?w=680&ssl=1","   JP Morgan"));
        slideModels.add(new SlideModel("https://img.etimg.com/thumb/msid-75353604,width-210,imgsize-415634,,resizemode-4,quality-100/mindtree-pti.jpg","   Mindtree"));
        slideModels.add(new SlideModel("https://www.latestlaws.com/media/2018/08/Wipro.jpg","   Wipro"));
        slideModels.add(new SlideModel("http://ddnews.gov.in/sites/default/files/axis%20bank%205.jpg","   Axis Bank"));
        slideModels.add(new SlideModel("https://media.glassdoor.com/l/4469/houston-rockets-office.jpg","   Toyota"));
        slideModels.add(new SlideModel("https://img.theweek.in/content/dam/week/news/biz-tech/images/2019/7/3/flipkart.jpg","   Flipkart"));
        slideModels.add(new SlideModel("https://media.glassdoor.com/l/15/8e/50/d1/headquarters.jpg","   Honda"));
        slideModels.add(new SlideModel("https://images.newindianexpress.com/uploads/user/imagelibrary/2020/4/20/w900X450/amazon_AP.jpg","   Amazon"));
        slideModels.add(new SlideModel("https://www.itbusiness.ca/wp-content/uploads/2018/02/GM-Markham-Facility-slide-12.jpg","   General Motors"));




        imageSlider.setImageList(slideModels,true);
        updateEmpHomeRV();
        return view;
    }

    private void updateEmpHomeRV() {
        AsyncJob.doInBackground(new AsyncJob.OnBackgroundJob() {
            @Override
            public void doOnBackground() {
                StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "updateEmpHomeRV.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                ArrayList<String> companyname = new ArrayList<>();
                                ArrayList<String> jobname = new ArrayList<>();
                                ArrayList<String> jobdiscription = new ArrayList<>();
                                ArrayList<String> matchpercentage = new ArrayList<>();
                                ArrayList<String> jobid = new ArrayList<>();
                                ArrayList<String> location = new ArrayList<>();
                                ArrayList<String> experience = new ArrayList<>();
                                ArrayList<String> compemail=new ArrayList<>();
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);

                                    String jobid1= jsonObject3.getString("jobid");
                                    String experience1=jsonObject3.getString("experience");
                                    String location1=jsonObject3.getString("location");
                                    String companyname1 = jsonObject3.getString("companyname");
                                    String jobname1 = jsonObject3.getString("jobname");
                                    String jobdiscription1 = jsonObject3.getString("jobdiscription");
                                    String matchpercentage1 = jsonObject3.getString("match_percentage");
                                    String compemail1=jsonObject3.getString("compemail");
                                    companyname.add(companyname1);
                                    jobname.add(jobname1);
                                    jobdiscription.add(jobdiscription1);
                                    matchpercentage.add(matchpercentage1);
                                    jobid.add(jobid1);
                                    experience.add(experience1);
                                    compemail.add(compemail1);
                                    location.add(location1);
                                }
                                //EmpJobListRVAdapter empJobListRVAdapter=new EmpJobListRVAdapter(companyname,jobid,jobname,jobdiscription,matchpercentage,getContext());
                                initJobsRV(companyname,jobname,jobdiscription,matchpercentage,jobid,location,experience,compemail);

                                //Toast.makeText(getActivity(), "Skill deleted", Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //Toast.makeText(EmpProfileFragment.this.getActivity(), "In catch "+e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmpHomeFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", sharedPrefrencesHelper.getUsername());
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpHomeFragment.this.getActivity());
        rQueue.add(stringRequest3);
                AsyncJob.doOnMainThread(new AsyncJob.OnMainThreadJob() {
                    @Override
                    public void doInUIThread() {
                        // Toast.makeText(getActivity(), "Result was: ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof employeeHomeSelected){
            listener = (employeeHomeSelected) context;

        }else{
            throw new ClassCastException(context.toString()+" must implement listener");
        }

    }

    public interface employeeHomeSelected{
        public void btnProfileClicked();
    }
    private void initJobsRV(ArrayList companyname,ArrayList jobname, ArrayList jobdiscription, ArrayList matchpercentage, ArrayList jobid,ArrayList location,ArrayList experience,ArrayList compemail) {
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        empHomeRV.setLayoutManager(lm);
        EmpJobListRVAdapter adapter = new EmpJobListRVAdapter(companyname,jobname,jobdiscription,matchpercentage,jobid,location,experience,compemail, getActivity());
        empHomeRV.setAdapter(adapter);
    }
}
class EmpJobListRVAdapter extends RecyclerView.Adapter<EmpJobListRVAdapter.ViewHolder> {
    SharedPrefrencesHelper sharedPrefrencesHelper;
    private  RequestQueue rQueue;
    public EmpJobListRVAdapter(ArrayList<String> companyname,ArrayList<String> jobname,ArrayList<String> jobdiscription,ArrayList<String> matchpercentage,ArrayList<String> jobid,ArrayList<String> location, ArrayList<String> experience,ArrayList<String> compemail,  Context mContext) {
        this.companyname=companyname;
        this.jobname=jobname;
        this.jobdiscription=jobdiscription;
        this.matchpercentage=matchpercentage;
        this.jobid=jobid;
        this.location=location;
        this.experience=experience;
        this.compemail=compemail;
        this.mContext = mContext;
    }

    private ArrayList<String> companyname = new ArrayList<>();
    private ArrayList<String> jobname = new ArrayList<>();
    private ArrayList<String> jobdiscription = new ArrayList<>();
    private ArrayList<String> matchpercentage = new ArrayList<>();
    private ArrayList<String> jobid = new ArrayList<>();
    private ArrayList<String> location = new ArrayList<>();
    private ArrayList<String> experience = new ArrayList<>();
    private ArrayList<String> compemail = new ArrayList<>();

    private Context mContext;

    @NonNull
    @Override
    public EmpJobListRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_listview_emp_home_rv, viewGroup, false);
        EmpJobListRVAdapter.ViewHolder holder = new EmpJobListRVAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final EmpJobListRVAdapter.ViewHolder viewHolder, final int i) {
        sharedPrefrencesHelper = new SharedPrefrencesHelper(mContext);
        viewHolder.companynameTV.setText(companyname.get(i));
        viewHolder.jobnameTV.setText(jobname.get(i));
        viewHolder.jobdiscriptionTV.setText(jobdiscription.get(i));
        viewHolder.jobidTV.setText(jobid.get(i));
        viewHolder.locationTV.setText(location.get(i));
        viewHolder.experienceTV.setText(experience.get(i));
        String s=matchpercentage.get(i);
        Float f=Float.parseFloat(s);
        if(f>100){
            viewHolder.matchpercentageTV.setText("100% qualified & You pocess more skills than required for this job");
        }else{
            int ff=(int)f.intValue();
            viewHolder.matchpercentageTV.setText(ff+"% of your skills are matching with the required skills for this job");
        }
        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                final ViewGroup viewGroup = v.findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(mContext).inflate(R.layout.company_details, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                Button applyButton;
                applyButton=dialogView.findViewById(R.id.applybutton);
                TextView comp_name=dialogView.findViewById(R.id.comp_name);
                final TextView comp_email=dialogView.findViewById(R.id.comp_email);
                TextView comp_discription=dialogView.findViewById(R.id.compDiscriptionTV);
                comp_name.setText(companyname.get(i));
                comp_email.setText(compemail.get(i));
                comp_discription.setText(jobdiscription.get(i));
                final ArrayList<String> topicsList, specializationList, levelsList;
                topicsList = new ArrayList<>();
                specializationList = new ArrayList<>();
                levelsList = new ArrayList<>();
                final MyListView skillsLV=dialogView.findViewById(R.id.skillsLV);
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "http://www.betterfuture.tech/android/sih/" + "getCompDetails.php",
                        new Response.Listener<String>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onResponse(String response) {
                                rQueue.getCache().clear();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    if (jsonObject.optString("success").equals("1")) {
                                        JSONArray jsonArray = jsonObject.getJSONArray("details");
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                            String temptopic = jsonObject3.getString("topicName");
                                            String tempspecialization = jsonObject3.getString("sname");
                                            String templevel = jsonObject3.getString("lname");
                                            topicsList.add(temptopic);
                                            specializationList.add(tempspecialization);
                                            levelsList.add(templevel);

                                        }

                                        SkillsListAdapter skillsListAdapter = new SkillsListAdapter(mContext, topicsList, specializationList, levelsList);
                                        skillsLV.setAdapter(skillsListAdapter);
                                        skillsListAdapter.notifyDataSetChanged();

                                    } else {
                                        Toast.makeText(mContext, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(mContext, "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                                Log.i("error :", error.toString());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("username", jobid.get(i));
                        return params;
                    }
                };
                rQueue = Volley.newRequestQueue(mContext);
                rQueue.add(stringRequest2);

                applyButton.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View arg0) {
                        Toast.makeText(mContext, "Applied", Toast.LENGTH_SHORT).show();
                        String to=compemail.get(i);
                        String subject="Job Application";
                        String message="";
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                        email.putExtra(Intent.EXTRA_SUBJECT, subject);
                        email.putExtra(Intent.EXTRA_TEXT, message);
                        //need this to prompts email client only
                        email.setType("message/rfc822");
                        mContext.startActivity(Intent.createChooser(email, "Choose an Email client :"));
                        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "http://www.betterfuture.tech/android/sih/" + "notifyCompany.php",
                                new Response.Listener<String>() {
                                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                    @Override
                                    public void onResponse(String response) {
                                        rQueue.getCache().clear();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                                        Log.i("error :", error.toString());
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("From", sharedPrefrencesHelper.getFirstname()+" "+sharedPrefrencesHelper.getLastname());
                                params.put("To",compemail.get(i));
                                return params;
                            }
                        };
                        rQueue = Volley.newRequestQueue(mContext);
                        rQueue.add(stringRequest2);

                    }
                });
            }
        });

    }
    @Override
    public int getItemCount() {
        return companyname.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView companynameTV,jobnameTV,jobdiscriptionTV,matchpercentageTV,jobidTV,experienceTV,locationTV;
        LinearLayoutCompat parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            companynameTV=(TextView)itemView.findViewById(R.id.Company_Name);
            jobnameTV=itemView.findViewById(R.id.Job_Name);
            jobdiscriptionTV=itemView.findViewById(R.id.Job_Discription);
            matchpercentageTV=itemView.findViewById(R.id.Match_Percentage);
            jobidTV=itemView.findViewById(R.id.jobId);
            experienceTV=itemView.findViewById(R.id.experiencerequiredTV);
            locationTV=itemView.findViewById(R.id.jobloactionTV);
            parentLayout=itemView.findViewById(R.id.parentLayout);
        }
    }

}
