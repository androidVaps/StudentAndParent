package com.example.san.myapplication.NewDesign;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.san.myapplication.Adapters.FeesPayableAdapter;
import com.example.san.myapplication.AppSingleton;
import com.example.san.myapplication.FeesActivityNew1;
import com.example.san.myapplication.Module.FeesDetailsPay;
import com.example.san.myapplication.Module.Session;
import com.example.san.myapplication.PaymentGateway.NewPayUwebView;
import com.example.san.myapplication.PaymentGateway.PayUwebView;
import com.example.san.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeesFragment extends Fragment {

    public static final String TITLE = "Fees Details";
    @InjectView(R.id.term1amt)
    TextView txtTerm1;
    @InjectView(R.id.term2amt)
    TextView txtTerm2;
    @InjectView(R.id.term3amt)
    TextView txtTerm3;
    @InjectView(R.id.term4amt)
    TextView txtTerm4;
    @InjectView(R.id.imgViewTerm1)
    ImageView imgTerm1;
    @InjectView(R.id.imgViewTerm2)
    ImageView imgTerm2;
    @InjectView(R.id.imgViewTerm3)
    ImageView imgTerm3;
    @InjectView(R.id.imgViewTerm4)
    ImageView imgTerm4;
    @InjectView(R.id.checkBox1)
    CheckBox chk_term1;
    @InjectView(R.id.checkBox2)
    CheckBox chk_term2;
    @InjectView(R.id.checkBox3)
    CheckBox chk_term3;
    @InjectView(R.id.checkBox4)
    CheckBox chk_term4;
    @InjectView(R.id.netAmount)
    TextView txtNetAmount;
    @InjectView(R.id.paidAmount)
    TextView txtPaidAmount;
    @InjectView(R.id.balance)
    TextView txtBalance;
    @InjectView(R.id.concession)
    TextView txtConcession;
    @InjectView(R.id.payAmount)
    Button payAmount;
    int totalPayable;
    JSONObject jsonObject;
    JSONObject objectTerm1, objectTerm2, objectTerm3, objectTerm4;
    FeesDetailsPay detailsPay;
    List<FeesDetailsPay> detailsPaysList;
    private static final String URL = "http://stagingmobileapp.azurewebsites.net/api/login/StudentFeeTerm";
    HashMap<Integer,ArrayList<Boolean>> feesCheckboxHandler;
    ArrayList<Boolean> checksUnchecksArrayList;
    String termID = null;
    int payble = 0;
    Session session ;
    ProgressDialog progressDialog;
    public static String TAG = FeesFragment.class.getSimpleName();

    JSONObject sessionFeesJsonResponse;

    public FeesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_fees, container, false);
        ButterKnife.inject(this, view);

        session = AppSingleton.getInstance(getActivity()).getInstance();

        int miid = session.getMI_Id();
        int amst_id = session.getAMST_Id();
        int asmay = session.getASMAY_Id();
        sessionFeesJsonResponse = session.getFeesJsonResponse();

        if(sessionFeesJsonResponse == null)
            volleyJsonObjectFeesRequest(URL, miid, amst_id, asmay);
        else
            parseJSONData(sessionFeesJsonResponse);

        return view;
    }


    @OnClick({R.id.imgViewTerm1, R.id.imgViewTerm2, R.id.imgViewTerm3, R.id.imgViewTerm4,
            R.id.checkBox1, R.id.checkBox2, R.id.checkBox3, R.id.checkBox4, R.id.payAmount})

    public void clickEvent(View v) {
        // Toast.makeText(getActivity(), "Clicked Term 1", Toast.LENGTH_SHORT).show();
        if (v == imgTerm1) {
            int colorID = ((ColorDrawable) txtTerm1.getBackground()).getColor();
            customePopUp(colorID, objectTerm1, chk_term1.isChecked());
        }

        if (v == imgTerm2) {
            int colorID = ((ColorDrawable) txtTerm2.getBackground()).getColor();
            customePopUp(colorID, objectTerm2, chk_term2.isChecked());
        }

        if (v == imgTerm3) {
            int colorID = ((ColorDrawable) txtTerm3.getBackground()).getColor();
            customePopUp(colorID, objectTerm3, chk_term3.isChecked());
        }

        if (v == imgTerm4) {
            int colorID = ((ColorDrawable) txtTerm4.getBackground()).getColor();
            customePopUp(colorID, objectTerm4, chk_term4.isChecked());
        }

        if (v == chk_term1) {
            if (chk_term1.isChecked())
                totalPayable = Integer.parseInt(txtTerm1.getText().toString()) + totalPayable;
            else
                totalPayable = totalPayable - Integer.parseInt(txtTerm1.getText().toString());

            changeButtonStatus(totalPayable);
        }
        if (v == chk_term2) {
            if (chk_term2.isChecked())
                totalPayable = Integer.parseInt(txtTerm2.getText().toString()) + totalPayable;
            else
                totalPayable = totalPayable - Integer.parseInt(txtTerm2.getText().toString());

            changeButtonStatus(totalPayable);
        }
        if (v == chk_term3) {
            if (chk_term3.isChecked())
                totalPayable = Integer.parseInt(txtTerm3.getText().toString()) + totalPayable;
            else
                totalPayable = totalPayable - Integer.parseInt(txtTerm3.getText().toString());

            changeButtonStatus(totalPayable);
        }
        if (v == chk_term4) {
            if (chk_term4.isChecked())
                totalPayable = Integer.parseInt(txtTerm4.getText().toString()) + totalPayable;
            else
                totalPayable = totalPayable - Integer.parseInt(txtTerm4.getText().toString());

            changeButtonStatus(totalPayable);
        }

        if (v == payAmount) {
            if (totalPayable == 0) {
                payAmount.setText("Please select Term to pay");
            } else {
                //   Toast.makeText(getActivity(), "Next page for payment", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                Intent i = new Intent(getActivity(), NewPayUwebView.class);
                startActivity(i);

           //     Toast.makeText(getActivity(), "Changes being done under development!", Toast.LENGTH_SHORT).show();

            }

        }
    }

    public void changeButtonStatus(int totalPayable) {
        if (totalPayable == 0) {
            payAmount.setText("Please select Term to pay");
        } else {
            payAmount.setText("Pay Amount " + getString(R.string.Rs) + " " + totalPayable);
        }
    }

    public void customePopUp(int bgColor, JSONObject jsonObject, Boolean isChecked)
    {
        // Custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fees_custom_dialog);
        // dialog.setTitle("Title...");

        TextView txtNetAmount = (TextView) dialog.findViewById(R.id.netamount);
        TextView txtPaidAmount = (TextView) dialog.findViewById(R.id.paidamount);
        TextView txtConcession = (TextView) dialog.findViewById(R.id.concession);
        TextView txtFine = (TextView) dialog.findViewById(R.id.fine);
        TextView txtPayable = (TextView) dialog.findViewById(R.id.payable);
        TextView txtTitle = (TextView) dialog.findViewById(R.id.title);

        detailsPaysList = new ArrayList<FeesDetailsPay>();
        try {
            termID = jsonObject.getString("FMT_Id");
            JSONArray jsonArray = jsonObject.getJSONArray("customgroup");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                detailsPay = new FeesDetailsPay(termID,object.getString("FMG_Id"), object.getString("FMGG_GroupName"), object.getString("pending"), isChecked);
                detailsPaysList.add(detailsPay);
            }
        } catch (JSONException e) {
            e.printStackTrace();
//            }
        }

      /*  if(isChecked == true)
        {
            feesCheckboxHandler = AppSingleton.getInstance(getActivity()).getFeesChecks();
            checksUnchecksArrayList = new ArrayList<Boolean>(Collections.nCopies(detailsPaysList.size(), true));
            feesCheckboxHandler.put(Integer.parseInt(fmtId),checksUnchecksArrayList);
        }else if(isChecked == false) {
            feesCheckboxHandler = AppSingleton.getInstance(getActivity()).getFeesChecks();
            checksUnchecksArrayList = new ArrayList<Boolean>(Collections.nCopies(detailsPaysList.size(), false));
            feesCheckboxHandler.put(Integer.parseInt(fmtId),checksUnchecksArrayList);
        }*/

        RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerViewFeesDialog);
        FeesPayableAdapter payableAdapter = new FeesPayableAdapter(detailsPaysList,isChecked);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(payableAdapter);
        payableAdapter.notifyDataSetChanged();

        try
        {
            txtNetAmount.setText(jsonObject.getInt("TotalTermamount") + "");
            txtPaidAmount.setText(jsonObject.getInt("PaidAmount") + "");
            txtConcession.setText(jsonObject.getInt("Concession") + "");
            txtFine.setText(jsonObject.getInt("FineAmount") + "");
            txtPayable.setText(jsonObject.getInt("pending") + "");
            txtTitle.setText(jsonObject.getString("FMT_Name") + "");

        } catch (JSONException e) {
            e.printStackTrace();
        }


        LinearLayout ll1 = (LinearLayout) dialog.findViewById(R.id.layout1);
        LinearLayout ll2 = (LinearLayout) dialog.findViewById(R.id.layout2);
        LinearLayout ll3 = (LinearLayout) dialog.findViewById(R.id.layout3);
        LinearLayout ll4 = (LinearLayout) dialog.findViewById(R.id.layout4);
        LinearLayout ll5 = (LinearLayout) dialog.findViewById(R.id.layout5);

        ll1.setBackgroundColor(bgColor);
        ll2.setBackgroundColor(bgColor);
        ll3.setBackgroundColor(bgColor);
        ll4.setBackgroundColor(bgColor);
        ll5.setBackgroundColor(bgColor);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_cancel);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                FeesDetailsPay detailsPay;

               /* for (int i = 0; i < detailsPaysList.size(); i++)
                {
                    boolean b = detailsPaysList.get(i).getChecked();
                    if (b == true)
                    {
                        payble = Integer.parseInt(detailsPaysList.get(i).getFmgAmount()) + payble;
                        // detailsPaysList.get(i).setChecked(false);
                    }
                }

                Toast.makeText(getActivity(), "" + payble, Toast.LENGTH_SHORT).show();*/

                feesCheckboxHandler = AppSingleton.getInstance(getActivity()).getFeesChecks();
                checksUnchecksArrayList = feesCheckboxHandler.get(Integer.parseInt(termID));

               /* if(checksUnchecksArrayList != null)
                {
                    for (int i = 0; i < checksUnchecksArrayList.size(); i++)
                    {
                        boolean status = checksUnchecksArrayList.get(i);
                        if (status == true)
                        {
                            payble = Integer.parseInt(detailsPaysList.get(i).getFmgAmount()) + payble;

                        }else if(status == false){
                            payble = payble - Integer.parseInt(detailsPaysList.get(i).getFmgAmount());
                        }
                    }
                }
                Toast.makeText(getActivity(), "" + payble, Toast.LENGTH_SHORT).show();
                totalPayable = payble;
                changeButtonStatus(payble);*/
                totalPayable = session.getPAYABLE_AMOUNT();
                changeButtonStatus(session.getPAYABLE_AMOUNT());
            }
        });
        dialog.show();
    }


    /***************************** SERVER REQUEST AND RESPONSE HANDLING **********************************/
    /*****************************************************************************************************/
    public void volleyJsonObjectFeesRequest(String url, final int mi_id, final int amst_id, final int asmay_id) {
        String REQUEST_TAG = "com.fees.details";
        progressDialog = new ProgressDialog(getActivity());

        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            int totalNetAmount = response.getInt("TotalNetAmount");

                      //      setViewPager(response);
                            if(response != null) {
                                jsonObject = response;
                                parseJSONData(response);
                                session.setFeesJsonResponse(response);
                            }else{
                                Toast.makeText(getActivity(), "No Fees data Available", Toast.LENGTH_LONG).show();
                            }
                            //    Object temp = response.get("paymenthistory");

                        /*    JSONArray jsonArray = response.getJSONArray("paymenthistory");
                            JSONArray jsonArray = objectToJSONArray(temp);
                                if (totalNetAmount == 0 && jsonArray.length() == 0) {
                                    Toast.makeText(FeesActivityNew1.this, "Failed to access Fees data", Toast.LENGTH_SHORT).show();
                                } else {
//                                Toast.makeText(FeesActivity.this, "got Fees data", Toast.LENGTH_SHORT).show();
//                                FeesDetailsParse(response);
                                    setViewPager(response);
                                }
                           */

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
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
        }) {
            @Override
            public byte[] getBody() {

                Map<String, Integer> params = new HashMap<String, Integer>();

                /*params.put("username",userName);
                params.put("password",passWord);
                params.put("MI_Id",mi_id);
                params.put("Entry_Date","01/07/2016");*/

                /*params.put("AMST_Id",sessionAMST_Id);
                params.put("MI_Id",session_MI_ID);*/

                /*params.put("AMST_Id",4221);
                params.put("MI_Id",5);*/

                params.put("ASMAY_Id",3);    // Single history
                params.put("AMST_Id",944);  // 944
                params.put("MI_Id",5);

               /* params.put("AMST_Id",3144);   // Multiple history
                params.put("ASMAY_Id",3);
                params.put("MI_Id",5);*/

                /*params.put("ASMAY_Id",3);    // Multiple history
                params.put("AMST_Id",4378);
                params.put("MI_Id",5);*/

               /* params.put("ASMAY_Id",2);   // No paid history
                params.put("AMST_Id",136);
                params.put("MI_Id",4);*/

               /* params.put("ASMAY_Id", asmay_id);
                params.put("AMST_Id", amst_id);
                params.put("MI_Id", mi_id);*/

                return new JSONObject(params).toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        // Adding JsonObject request to request queue
        AppSingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectReq, REQUEST_TAG);

    }

    /*******************************************************************************************************/
    /*******************************************************************************************************/

   /********** Parsing JSON Response *****************/

    public void parseJSONData(JSONObject jsonObject) {
        try {
            txtNetAmount.setText(jsonObject.getInt("TotalNetAmount") + "");
            txtPaidAmount.setText(jsonObject.getInt("TotalPaidAmount") + "");
            txtBalance.setText(jsonObject.getInt("Totalpending") + "");
            txtConcession.setText(jsonObject.getInt("Totalconcession") + "");

            JSONArray jsonArray = jsonObject.getJSONArray("Feeterm_array");
            objectTerm1 = jsonArray.getJSONObject(0);
            objectTerm2 = jsonArray.getJSONObject(1);
            objectTerm3 = jsonArray.getJSONObject(2);
            objectTerm4 = jsonArray.getJSONObject(3);

            chk_term1.setText(objectTerm1.getString("FMT_Name") + "");
            chk_term2.setText(objectTerm2.getString("FMT_Name") + "");
            chk_term3.setText(objectTerm3.getString("FMT_Name") + "");
            chk_term4.setText(objectTerm4.getString("FMT_Name") + "");

            txtTerm1.setText(objectTerm1.getString("pending"));
            txtTerm2.setText(objectTerm2.getString("pending"));
            txtTerm3.setText(objectTerm3.getString("pending"));
            txtTerm4.setText(objectTerm4.getString("pending"));


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }




}
