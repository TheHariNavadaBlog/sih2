package com.example.sih2;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import co.lujun.androidtagview.ColorFactory;
import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EmpProfileFragment extends Fragment {
    SharedPrefrencesHelper sharedPrefrencesHelper;
    TextView firstname, lastname, username, email, cancel, skillsListTV, degreeTV, experienceTV, empDiscriptionTV;
    Button addNewSkillButton, addNewDegreeButton, degreeSubmitButton, addExperienceButton;
    CardView skillCard, degreeCD, innerDegreeCD, experienceCD, innerExperienceCV;
    Spinner specializationSpinner, topicSpinner, levelSpinner, degreeSpinner;
    private RequestQueue rQueue;
    String specialization, topic, level, degree;
    TagContainerLayout degreesLV;
    MyListView skillsLV;
    EditText empdiscriptionET;
    boolean isExperienceOpen;
    TagContainerLayout educationTags;
    CircleImageView displayPicture;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_emp_profile, container, false);

        sharedPrefrencesHelper = new SharedPrefrencesHelper(this.getActivity());
        firstname = view.findViewById(R.id.emp_firstname);
        lastname = view.findViewById(R.id.emp_lastname);
        username = view.findViewById(R.id.emp_username);
        email = view.findViewById(R.id.emp_email);
        addNewSkillButton = view.findViewById(R.id.addNewSkillButton);
        skillCard = view.findViewById(R.id.skillCard);
        skillsListTV = view.findViewById(R.id.skillsListTV);
        degreeCD = view.findViewById(R.id.degreeCD);
        degreeTV = view.findViewById(R.id.degreeTV);
        addNewDegreeButton = view.findViewById(R.id.addNewDegreeButton);
        innerDegreeCD = view.findViewById(R.id.innerDegreeCD);
        experienceTV = view.findViewById(R.id.experienceTV);
        experienceCD = view.findViewById(R.id.experienceCV);
        addExperienceButton = view.findViewById(R.id.addExperienceButton);
        innerExperienceCV = view.findViewById(R.id.innerExperienceCV);
        empDiscriptionTV = view.findViewById(R.id.empDiscriptionTV);
        displayPicture=view.findViewById(R.id.displayPicture);

        displayPicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popup = new PopupMenu(EmpProfileFragment.this.getActivity(), displayPicture);
                popup.getMenuInflater().inflate(R.menu.popup_edit, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(EmpProfileFragment.this.getActivity());
                        final ViewGroup viewGroup = view.findViewById(android.R.id.content);
                        final View dialogView = LayoutInflater.from(EmpProfileFragment.this.getActivity()).inflate(R.layout.popup_edit_display_picture, viewGroup, false);
                        builder.setView(dialogView);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        ImageView displayPicture;
                        Button cancel,delete,edit,submit;
                        displayPicture=dialogView.findViewById(R.id.displayPicture);

                        cancel=dialogView.findViewById(R.id.cancel);
                        delete=dialogView.findViewById(R.id.delete);
                        edit=dialogView.findViewById(R.id.edit);
                        submit=dialogView.findViewById(R.id.apply);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.cancel();
                            }
                        });
                        delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.cancel();
                            }
                        });
                        edit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.cancel();
                            }
                        });
                        submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.cancel();
                            }
                        });
                        return false;
                    }
                });
                popup.show();
                return false;
            }
        });


        isExperienceOpen = true;

        firstname.setText(sharedPrefrencesHelper.getFirstname());
        lastname.setText(sharedPrefrencesHelper.getLastname());
        username.setText(sharedPrefrencesHelper.getUsername());
        email.setText(sharedPrefrencesHelper.getEmail());
        if (sharedPrefrencesHelper.getDiscription().equals("null")) {
            empDiscriptionTV.setText("Long press to set a description for your profile");
        } else {
            empDiscriptionTV.setText(sharedPrefrencesHelper.getDiscription());
        }

        skillsLV = view.findViewById(R.id.skillsLV);
        degreesLV = view.findViewById(R.id.degreeLV);
        getEmpDiscription();

        empDiscriptionTV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {

                PopupMenu popup = new PopupMenu(EmpProfileFragment.this.getActivity(), empDiscriptionTV);
                popup.getMenuInflater().inflate(R.menu.popup_edit, popup.getMenu());
                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(EmpProfileFragment.this.getActivity());
                        final ViewGroup viewGroup = view.findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(EmpProfileFragment.this.getActivity()).inflate(R.layout.popup_change_emp_discription, viewGroup, false);
                        builder.setView(dialogView);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                        final EditText editText2 = dialogView.findViewById(R.id.empdiscriptionET);
                        editText2.setText(empDiscriptionTV.getText().toString());
                        editText2.requestFocus();

                        dialogView.findViewById(R.id.empdiscriptioncancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.cancel();
                            }
                        });
                        dialogView.findViewById(R.id.empdiscriptionokay).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sharedPrefrencesHelper.setDiscription(editText2.getText().toString());
                                empDiscriptionTV.setText(editText2.getText().toString());
                                updateEmpDiscription();
                                alertDialog.cancel();
                            }
                        });


                        return true;
                    }
                });

                popup.show();//showing popup menu
                return false;
            }
        });

        skillsLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View v, final int index, long arg3) {

                PopupMenu popup = new PopupMenu(EmpProfileFragment.this.getActivity(), skillsLV);
                popup.getMenuInflater().inflate(R.menu.popup_delete, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        View row = skillsLV.getAdapter().getView(index, view, container);
                        final TextView tempspecialization = row.findViewById(R.id.specializationTV);
                        final TextView temptopic = row.findViewById(R.id.topicTV);
                        final TextView templevel = row.findViewById(R.id.levelTV);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Delete Skill");
                        builder.setMessage("Do you want to delete the skill " + temptopic.getText().toString() + " under " + tempspecialization.getText().toString() + " ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        deleteEmpSkill(tempspecialization.getText().toString(), temptopic.getText().toString(), templevel.getText().toString());
                                        degreesLV.removeAllTags();
                                        updateSkillsLV();
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                        return false;
                    }
                });
                popup.show();//showing popup menu
                return false;
            }
        });

        degreesLV.setBackgroundColor(ColorFactory.NONE);
        degreesLV.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(int position, String text) {

            }

            @Override
            public void onTagLongClick(final int position, final String text) {
                final PopupMenu popup = new PopupMenu(EmpProfileFragment.this.getActivity(), degreesLV);
                popup.getMenuInflater().inflate(R.menu.popup_delete, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("Do you want to delete the degree " + text + " ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        deletEmpDegree(text);
                                        updateDegreeLV();
                                        dialog.cancel();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                        return false;
                    }

                });
                popup.show();//showing popup menu


            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {

            }

        });


        // When Skills is clicked and all actions under that


        updateSkillsLV();
        addNewSkillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EmpProfileFragment.this.getActivity());
                final ViewGroup viewGroup = view.findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(EmpProfileFragment.this.getActivity()).inflate(R.layout.popup_add_skills_emp, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                final ArrayList<String> topicsList, speciazationList, levelsList;
                topicsList = new ArrayList<String>();
                speciazationList = new ArrayList<String>();
                levelsList = new ArrayList<String>();

                specializationSpinner = dialogView.findViewById(R.id.specializationSpinner);
                topicSpinner = dialogView.findViewById(R.id.topicSpinner);
                levelSpinner = dialogView.findViewById(R.id.levelSpinnner);

                // Initializing specialization spinner
                initializeSpecializationSpinner(speciazationList);
                initializeLevelsSpinner(levelsList);

                levelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        level = (String) parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                // Setting topicSpinner using SpecializationSpinner

                specializationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        //Toast.makeText(EmpProfileFragment.this.getActivity(), (String) parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                        specialization = (String) parent.getItemAtPosition(position);
                        updateTopicsSpinner(topicsList);
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });

                topicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        topic = (String) (String) parent.getItemAtPosition(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Button addskillButton = dialogView.findViewById(R.id.addSkillButton);
                addskillButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadSkills();
                        updateSkillsLV();
                        alertDialog.cancel();
                    }
                });

                Button cancelSkills = dialogView.findViewById(R.id.cancel);
                cancelSkills.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });

            }


        });


        // When Degree is clicked and all actions under that

        updateDegreeLV();
        addNewDegreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EmpProfileFragment.this.getActivity());
                final ViewGroup viewGroup = view.findViewById(android.R.id.content);
                final View dialogView = LayoutInflater.from(EmpProfileFragment.this.getActivity()).inflate(R.layout.popup_add_degree, viewGroup, false);
                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();


                final ArrayList<String> degreesList = new ArrayList<>();
                initializeDegreeSpinner(degreesList);
                degreeSpinner = dialogView.findViewById(R.id.degreeSpinner);
                degreeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        degree = (String) parent.getItemAtPosition(position);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Button degreeCDcancel;
                degreeSubmitButton = dialogView.findViewById(R.id.degreeSubmitButton);
                degreeSubmitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadDegree();
                        updateDegreeLV();
                        alertDialog.cancel();
                    }
                });
                degreeCDcancel = dialogView.findViewById(R.id.degreeCDcancel);
                degreeCDcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.cancel();
                    }
                });
            }
        });


        // When Experience is clicked and everything under that
        experienceTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExperienceOpen) {
                    isExperienceOpen = false;
                    experienceCD.setVisibility(View.GONE);
                } else {
                    isExperienceOpen = true;
                    experienceCD.setVisibility(View.VISIBLE);

                    addExperienceButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            addExperienceButton.setVisibility(View.GONE);
                            innerExperienceCV.setVisibility(View.VISIBLE);
                            updateExperienceLV();

                            Button cancelExperience = view.findViewById(R.id.experienceCancel);
                            cancelExperience.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    addExperienceButton.setVisibility(View.VISIBLE);
                                    innerExperienceCV.setVisibility(View.GONE);
                                }
                            });

                        }
                    });
                }
            }
        });


        //End of the OncreateView
        return view;

    }

    private void setDisplayPicture() {
        CropImage.ActivityBuilder activity = CropImage.activity();
        activity.setGuidelines(CropImageView.Guidelines.ON);
        activity.setAspectRatio(1, 1);
        activity.start(getContext(),this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);

            Uri resultUri = result.getUri();
            int select=0;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                int nh = (int) (bitmap.getHeight() * (250.0 / bitmap.getWidth()));
                bitmap = Bitmap.createScaledBitmap(bitmap, 250, nh, true);
                Bitmap image = bitmap;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] b = byteArrayOutputStream.toByteArray();
                String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
               // SharedPreferences.Editor editor = prefs.edit();
                //editor.putString("namePreferance", itemNAme);
              //  editor.putString("svdimgs", imageEncoded);
               // editor.apply();
                displayPicture.setImageBitmap(bitmap);
                //Glide.with(this).load(path).into(profileImg);
                select = 1;
            } catch (IOException e) {
                select = 0;
            }

        }
    }

    private void deletEmpDegree(final String tagText) {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "deleteEmpDegree.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                Toast.makeText(getActivity(), "deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(EmpProfileFragment.this.getActivity(), "failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EmpProfileFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", sharedPrefrencesHelper.getUsername());
                params.put("degree", tagText);
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest3);
    }

    private void deleteEmpSkill(final String tempspecialization, final String temptopic, final String templevel) {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "deleteEmpSkill.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                //Toast.makeText(getActivity(), "Skill deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(EmpProfileFragment.this.getActivity(), "error", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EmpProfileFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", sharedPrefrencesHelper.getUsername());
                params.put("specialization", tempspecialization);
                params.put("topic", temptopic);
                params.put("level", templevel);
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest3);

    }

    private void getEmpDiscription() {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getEmpDiscription.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("details");
                                sharedPrefrencesHelper.setDiscription(jsonObject1.getString("discription"));
                                empDiscriptionTV.setText(sharedPrefrencesHelper.getDiscription());
                            } else {
                                //Toast.makeText(EmpProfileFragment.this.getActivity(), "error", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EmpProfileFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", sharedPrefrencesHelper.getUsername());
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest3);

    }

    private void updateEmpDiscription() {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "updateEmpDiscription.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {


                            } else {
                                //Toast.makeText(EmpProfileFragment.this.getActivity(), "error", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EmpProfileFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("discription", sharedPrefrencesHelper.getDiscription());
                params.put("username", sharedPrefrencesHelper.getUsername());
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest3);
    }

    private void updateExperienceLV() {
    }

    private void uploadDegree() {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "uploadDegree.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                //Toast.makeText(EmpProfileFragment.this.getActivity(), "Topics found", Toast.LENGTH_SHORT).show();
                                /*JSONArray jsonArray=jsonObject.getJSONArray("details");
                                for(int i=0; i<jsonArray.length();i++){
                                    JSONObject jsonObject3=jsonArray.getJSONObject(i);
                                    String tempvar=jsonObject3.getString("topicName");


                                }

                                 */
                                Toast.makeText(EmpProfileFragment.this.getActivity(), "Degree upload success", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(EmpProfileFragment.this.getActivity(), "Degree already exists", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EmpProfileFragment.this.getActivity(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmpProfileFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("degree", degree);
                params.put("username", sharedPrefrencesHelper.getUsername());
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest3);
    }

    private void initializeDegreeSpinner(final ArrayList<String> degreesList) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getDegrees.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                //Toast.makeText(EmpProfileFragment.this.getActivity(), "Topics found", Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                    String tempvar = jsonObject3.getString("dname");
                                    degreesList.add(tempvar);
                                    //Toast.makeText(EmpProfileFragment.this.getActivity(),tempvar , Toast.LENGTH_SHORT).show();
                                }
                                ArrayAdapter<String> degreeAdapter = new ArrayAdapter<String>(EmpProfileFragment.this.getActivity(), android.R.layout.simple_spinner_dropdown_item, degreesList);
                                degreeSpinner.setAdapter(degreeAdapter);
                                degreeAdapter.notifyDataSetChanged();


                            } else {
                                Toast.makeText(EmpProfileFragment.this.getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EmpProfileFragment.this.getActivity(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmpProfileFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("degrees", "getDegrees");
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest2);
    }

    private void updateDegreeLV() {
        final ArrayList<String> degreesList;
        degreesList = new ArrayList<>();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getEmpDegrees.php",
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
                                    String temptopic = jsonObject3.getString("dname");
                                    degreesList.add(temptopic);

                                }
                                educationTags = getView().findViewById(R.id.degreeLV);
                                educationTags.setTags(degreesList);
                                degreesLV.setTags(degreesList);

                            } else {
                                Toast.makeText(EmpProfileFragment.this.getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EmpProfileFragment.this.getActivity(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmpProfileFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", sharedPrefrencesHelper.getUsername());
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest2);
    }

    // function definitions
    private void updateSkillsLV() {
        final ArrayList<String> topicsList, specializationList, levelsList;
        topicsList = new ArrayList<>();
        specializationList = new ArrayList<>();
        levelsList = new ArrayList<>();

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getEmpSkills.php",
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

                                SkillsListAdapter skillsListAdapter = new SkillsListAdapter(EmpProfileFragment.this.getActivity(), topicsList, specializationList, levelsList);
                                skillsLV.setAdapter(skillsListAdapter);
                                skillsListAdapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(EmpProfileFragment.this.getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EmpProfileFragment.this.getActivity(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmpProfileFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                        Log.i("error :", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", sharedPrefrencesHelper.getUsername());
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest2);


    }

    private void initializeLevelsSpinner(final ArrayList<String> levelsList) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getLevels.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                //Toast.makeText(EmpProfileFragment.this.getActivity(), "Topics found", Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                    String tempvar = jsonObject3.getString("lname");
                                    levelsList.add(tempvar);
                                    //Toast.makeText(EmpProfileFragment.this.getActivity(),tempvar , Toast.LENGTH_SHORT).show();
                                }
                                ArrayAdapter<String> levelsAdapter = new ArrayAdapter<String>(EmpProfileFragment.this.getActivity(), android.R.layout.simple_spinner_dropdown_item, levelsList);
                                levelSpinner.setAdapter(levelsAdapter);
                                levelsAdapter.notifyDataSetChanged();


                            } else {
                                Toast.makeText(EmpProfileFragment.this.getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EmpProfileFragment.this.getActivity(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmpProfileFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("levels", "getLevels");
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest2);

    }

    private void updateTopicsSpinner(final ArrayList<String> topicsList) {
        topicsList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getTopics.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                //Toast.makeText(EmpProfileFragment.this.getActivity(), "Topics found", Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                    String tempvar = jsonObject3.getString("topicName");
                                    topicsList.add(tempvar);
                                    //Toast.makeText(EmpProfileFragment.this.getActivity(),tempvar , Toast.LENGTH_SHORT).show();
                                }
                                ArrayAdapter<String> topicsAdapter = new ArrayAdapter<String>(EmpProfileFragment.this.getActivity(), android.R.layout.simple_spinner_dropdown_item, topicsList);
                                topicSpinner.setAdapter(topicsAdapter);
                                topicsAdapter.notifyDataSetChanged();


                            } else {
                                Toast.makeText(EmpProfileFragment.this.getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EmpProfileFragment.this.getActivity(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmpProfileFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("specialization", specialization);
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest);
    }

    private void initializeSpecializationSpinner(final ArrayList<String> speciazationList) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "getSpecialization.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {
                                //Toast.makeText(EmpProfileFragment.this.getActivity(), "Topics found", Toast.LENGTH_SHORT).show();
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                                    String tempvar = jsonObject3.getString("sname");
                                    speciazationList.add(tempvar);
                                    //Toast.makeText(EmpProfileFragment.this.getActivity(),tempvar , Toast.LENGTH_SHORT).show();
                                }
                                ArrayAdapter<String> specializationAdapter = new ArrayAdapter<String>(EmpProfileFragment.this.getActivity(), android.R.layout.simple_spinner_dropdown_item, speciazationList);
                                specializationSpinner.setAdapter(specializationAdapter);
                                specializationAdapter.notifyDataSetChanged();


                            } else {
                                Toast.makeText(EmpProfileFragment.this.getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EmpProfileFragment.this.getActivity(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmpProfileFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("specialization", "getSpecialization");
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest);
    }

    private void uploadSkills() {
        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, getResources().getString(R.string.url) + "uploadSkill.php",
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.optString("success").equals("1")) {

                                Toast.makeText(EmpProfileFragment.this.getActivity(), "Skill upload success", Toast.LENGTH_SHORT).show();


                            } else {
                                Toast.makeText(EmpProfileFragment.this.getActivity(), "Skill already exists", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(EmpProfileFragment.this.getActivity(), "In catch " + e.toString(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmpProfileFragment.this.getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (specialization.equals("") || topic.equals("") || level.equals("")) {
                    Toast.makeText(EmpProfileFragment.this.getActivity(), "All fields are necessary", Toast.LENGTH_SHORT).show();
                } else {
                    params.put("specialization", specialization);
                    params.put("topic", topic);
                    params.put("level", level);
                    params.put("username", sharedPrefrencesHelper.getUsername());
                }
                return params;
            }
        };
        rQueue = Volley.newRequestQueue(EmpProfileFragment.this.getActivity());
        rQueue.add(stringRequest3);
    }


}

class SkillsListAdapter extends ArrayAdapter {
    ArrayList<String> topicsList, specializationList, levelsList;

    public SkillsListAdapter(Context context, ArrayList<String> topicsList, ArrayList<String> specializationList, ArrayList<String> levelsList) {
        super(context, R.layout.skills_custom_listview, R.id.topicTV, topicsList);
        this.topicsList = topicsList;
        this.specializationList = specializationList;
        this.levelsList = levelsList;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.skills_custom_listview, parent, false);
        TextView tasksTV = row.findViewById(R.id.topicTV);
        TextView specializationTV = row.findViewById(R.id.specializationTV);
        TextView levelTV = row.findViewById(R.id.levelTV);
        ProgressBar levelProgressbar = row.findViewById(R.id.level_progressbar);
        tasksTV.setText(topicsList.get(position));
        specializationTV.setText(specializationList.get(position));
        levelTV.setText(levelsList.get(position));
        if (levelsList.get(position).equals("Beginner")) {
            Drawable progressDrawable = levelProgressbar.getProgressDrawable().mutate();
            progressDrawable.setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
            levelProgressbar.setProgressDrawable(progressDrawable);
            ObjectAnimator progressAnimator;
            progressAnimator = ObjectAnimator.ofInt(levelProgressbar, "progress", 0, 25);
            progressAnimator.setDuration(1000);
            progressAnimator.start();
        } else if (levelsList.get(position).equals("Intermediate")) {
            Drawable progressDrawable = levelProgressbar.getProgressDrawable().mutate();
            progressDrawable.setColorFilter(Color.YELLOW, android.graphics.PorterDuff.Mode.SRC_IN);
            levelProgressbar.setProgressDrawable(progressDrawable);
            ObjectAnimator progressAnimator;
            progressAnimator = ObjectAnimator.ofInt(levelProgressbar, "progress", 0, 50);
            progressAnimator.setDuration(1000);
            progressAnimator.start();

        } else if (levelsList.get(position).equals("Advanced")) {

            Drawable progressDrawable = levelProgressbar.getProgressDrawable().mutate();
            progressDrawable.setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN);
            levelProgressbar.setProgressDrawable(progressDrawable);
            ObjectAnimator progressAnimator;
            progressAnimator = ObjectAnimator.ofInt(levelProgressbar, "progress", 0, 75);
            progressAnimator.setDuration(1000);
            progressAnimator.start();

        } else {

            Drawable progressDrawable = levelProgressbar.getProgressDrawable().mutate();
            progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
            levelProgressbar.setProgressDrawable(progressDrawable);
            ObjectAnimator progressAnimator;
            progressAnimator = ObjectAnimator.ofInt(levelProgressbar, "progress", 0, 100);
            progressAnimator.setDuration(1000);
            progressAnimator.start();
        }
        return row;

    }

}

class ExperienceListAdapter extends ArrayAdapter {
    ArrayList<String> experience, description;

    public ExperienceListAdapter(Context context, ArrayList<String> experience, ArrayList<String> description) {
        super(context, R.layout.experience_custom_listview, R.id.specializationTV, experience);
        this.experience = experience;
        this.description = description;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.experience_custom_listview, parent, false);
        TextView experienceTV = row.findViewById(R.id.specializationTV);
        TextView descriptionTV = row.findViewById(R.id.levelTV);

        experienceTV.setText(experience.get(position));
        descriptionTV.setText(description.get(position));
        return row;

    }
}

class MyListView extends ListView {

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
