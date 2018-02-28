package com.example.san.myapplication.NewDesign;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.san.myapplication.AppSingleton;
import com.example.san.myapplication.Module.Session;
import com.example.san.myapplication.R;
import com.example.san.myapplication.StudentDeatilsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudFragment extends Fragment
{

    private static final String URL = "http://stagingmobileapp.azurewebsites.net/api/login/StudentDetails";
    ProgressDialog progressDialog;
    public static String TAG = StudentDeatilsActivity.class.getSimpleName();
    int sessionAMST_Id = 0;
    int session_MI_ID = 0;
    int session_ASMAY_Id = 0;
    Session session;

    JSONObject sessionJsonResponse ;

    @InjectView(R.id.image_stud)
    ImageView imageView;
    @InjectView(R.id.edit_stud_name)
    TextView edit_stud_name;
    @InjectView(R.id.edit_stud_regno) TextView edit_stud_regno;
    @InjectView(R.id.edit_stud_doa) TextView edit_stud_doa;
    @InjectView(R.id.edit_stud_dob) TextView edit_stud_dob;
    @InjectView(R.id.edit_stud_contact) TextView edit_stud_contact;
    @InjectView(R.id.edit_stud_email) TextView edit_stud_email;

    public StudFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //    imageView = (ImageView) findViewById(R.id.image_stud);
        session = AppSingleton.getInstance(getActivity()).getInstance();

        View view = inflater.inflate(R.layout.fragment_stud, container, false);

        ButterKnife.inject(this, view);
        sessionAMST_Id = session.getAMST_Id();
        session_MI_ID = session.getMI_Id();
        session_ASMAY_Id = session.getASMAY_Id();
        sessionJsonResponse = session.getStudJsonResponse();

        //    Toast.makeText(StudentDeatilsActivity.this,sessionAMST_Id+"+++++++"+session_MI_ID, Toast.LENGTH_SHORT).show();

        if(sessionJsonResponse == null)
        volleyJsonObjectRequest(URL,sessionAMST_Id,session_MI_ID,session_ASMAY_Id);
        else
        studentDetailsParse(sessionJsonResponse);

        // Inflate the layout for this fragment
        return view;
    }


    /***************************** SERVER REQUEST AND RESPONSE HANDLING **********************************/
    /*****************************************************************************************************/
    public void volleyJsonObjectRequest(String url,final int sessionAMST_Id,final int session_MI_ID,final int session_ASMAY_Id)
    {
        String  REQUEST_TAG = "com.student.details";
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading student...");
        progressDialog.show();

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST,url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(final JSONObject response)
                    {
                        Log.d(TAG, response.toString());
                        try {
                            int ID = Integer.parseInt(response.getString("AMST_Id"));
                            if (ID == 0)
                            {
                                Toast.makeText(getActivity(), "Failed to access student data", Toast.LENGTH_SHORT).show();
                            } else {
//                                Toast.makeText(StudentDeatilsActivity.this, "got data", Toast.LENGTH_SHORT).show();
                                studentDetailsParse(response);
                                session.setStudJsonResponse(response);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    Toast.makeText(getActivity(),"TimeoutError or NoConnectionError", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(getActivity(),"Authentication Failure", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(getActivity(),"Error response", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(getActivity(),"Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(getActivity(),"Server response could not be parsed", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            public byte[] getBody() {

                Map<String, Integer> params = new HashMap<String, Integer>();

                /*params.put("username",userName);
                params.put("password",passWord);
                params.put("MI_Id",mi_id);
                params.put("Entry_Date","01/07/2016");*/

                params.put("AMST_Id",sessionAMST_Id);
                params.put("MI_Id",session_MI_ID);
                params.put("ASMAY_Id",session_ASMAY_Id);

                /*params.put("AMST_Id",4221);
                params.put("MI_Id",5);*/

                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectReq,REQUEST_TAG);

    }

    /*******************************************************************************************************/
    /*******************************************************************************************************/


    private void studentDetailsParse(JSONObject response)
    {
        try {
            String stud_name = response.getString("AMST_FirstName");
            String stud_regNo = response.getString("AMST_RegistrationNo");
            String stud_admNo = formatDate(response.getString("AMST_Date"));
            String stud_dob = formatDate(response.getString("AMST_DOB"));
            String stud_contactNo = response.getString("AMST_MobileNo");
            String stud_mailId = response.getString("AMST_emailId");
            String stud_photo = response.getString("AMST_Photoname");

            // BIND DATA TO VIEW
            edit_stud_name.setText(stud_name);
            edit_stud_regno.setText(stud_regNo);
            edit_stud_doa.setText(stud_admNo);
            edit_stud_dob.setText(stud_dob);
            edit_stud_contact.setText(stud_contactNo);  // Integer.toString(stud_contactNo)
            edit_stud_email.setText(stud_mailId);
            //  edit_stud_email.setText(stud_mailId);


            //    volleyImageLoader(stud_photo);
            profile_image(stud_photo);
            Log.d(TAG,stud_name+"+"+stud_regNo+"+"+stud_admNo+"+"+stud_dob+"+"+stud_contactNo+"+"+stud_mailId);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void profile_image(String imgUrl) {
        Glide.with(this)
                .load(imgUrl)
                .asBitmap()
                .placeholder(R.drawable.circle_crop)
                .error(R.mipmap.ic_launcher_round)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getActivity().getResources(),
                                Bitmap.createScaledBitmap(resource, 50, 50, false));
                        drawable.setCircular(true);
                        imageView.setImageDrawable(drawable);
                    }
                });
    }

    private String formatDate(String strDate)
    {
        String str_date = null;
        strDate = strDate.substring(0,9);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {

            Date d1 = df.parse(strDate);
            str_date = new SimpleDateFormat("dd-MM-yyyy").format(d1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str_date;
    }


}
