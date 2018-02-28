package com.example.san.myapplication.NewDesign;


import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.san.myapplication.AppSingleton;
import com.example.san.myapplication.Caland.CalendarEventsActivity;
import com.example.san.myapplication.Module.Session;
import com.example.san.myapplication.R;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventsFragment extends Fragment {

    private boolean undo = false;
    private CaldroidFragment caldroidFragment;
    private ProgressDialog progressDialog;
    private String TAG = CalendarEventsActivity.class.getSimpleName();
    private static String URL = "http://stagingmobileapp.azurewebsites.net/api/login/CalenderofEvents";
    String presentDate[];
    String abscenceDate[];
    Session session;
    Date minDate,maxDate;
    Calendar cal;
    int miid;
    int asmay;
    JSONObject JsonCalendarEventsResponse;
    JSONObject jsonEventResponce;


    public EventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        session = AppSingleton.getInstance(getActivity()).getInstance();
        miid = session.getMI_Id();
        asmay =session.getASMAY_Id();

        caldroidFragment = new CaldroidFragment();

        if (savedInstanceState != null)
        {
            caldroidFragment.restoreStatesFromKey(savedInstanceState,
                    "CALDROID_SAVED_STATE");
        }
        // If activity is created from fresh
        else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);

            caldroidFragment.setArguments(args);

        }

        // Attach to the activity
        FragmentTransaction t = getChildFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                /*Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();*/

                int month = date.getMonth();
//                if(minDate.getTime() <= date.getTime() && date.getTime() <= maxDate.getTime())
//                  if(!(date.before(minDate) || date.after(maxDate)))
//                if(date.compareTo(minDate) > 0 && date.compareTo(maxDate) <= 0)

                /*Long lg = new Long(1 * 24 * 3600 * 1000);
                minDate = minDate.getDay() - lg ;*/

                //  cal.get(cal.DAY_OF_MONTH);
             /*    cal.add(cal.DAY_OF_MONTH,-1);
                minDate = cal.getTime();*/

                calendarEventJsonParsingPOPUP(date);
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
               /* Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();*/

               /******** Check for data available in session if not connect to server****************/

                jsonEventResponce = session.getEventJsonResponse();
                if(jsonEventResponce == null)
                    volleyJsonObjectCalanderEventRequest(URL,asmay,miid,month);
                else
                    calendarEventJsonParsing(month);


            }

            @Override
            public void onLongClickDate(Date date, View view) {
               /* Toast.makeText(getActivity(),
                        "Long click " + formatter.format(date),
                        Toast.LENGTH_SHORT).show();*/
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    /*Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();*/
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);


        return view;
    }


    /**
     * Save current states of the Caldroid here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }


    }


    /***************************** SERVER REQUEST AND RESPONSE HANDLING **********************************/
    public void volleyJsonObjectCalanderEventRequest(String url, final int ASMAY_Id,final int MI_Id,final int month)
    {
        String REQUEST_TAG = "com.fees.details";
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading Events...");
        progressDialog.show();

        JsonObjectRequest jsonObjectReq = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Log.d(TAG, response.toString());
                        try {
                            session.setEventJsonResponse(response);
                         //   JsonCalendarEventsResponse = response;
                            calendarEventJsonParsing(month);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener()
                {
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

                params.put("ASMAY_Id", ASMAY_Id);
                params.put("MI_Id", MI_Id);

               /* params.put("ASMAY_Id", 3);
                params.put("MI_Id", 5);*/

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


    private void setCustomResourceForDates()
    {

            /*presentDate = {"21/3/2017","22/3/2017","23/3/2017","24/3/2017","27/3/2017","28/3/2014"};
            abscenceDate[] = {"25/3/2017","26/3/2017","20/3/2017"};*/


        cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, 2017);
        cal.set(Calendar.MONTH, 7);
        cal.set(Calendar.DAY_OF_MONTH, 24);

        Calendar cal1 = Calendar.getInstance();
        cal1.set(Calendar.YEAR, 2017);
        cal1.set(Calendar.MONTH, 7);
        cal1.set(Calendar.DAY_OF_MONTH, 24);

        //   long milliTime = cal.getTimeInMillis();

        // Min date is last 7 days
        //   cal.add(Calendar.DATE, -1);

        //    Date blueDate = cal.getTime();

        minDate = cal.getTime();
        maxDate = cal1.getTime();


        // Max date is next 7 days
       /* cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 5);
        Date greenDate = cal.getTime();*/

        if (caldroidFragment != null) {
//            ColorDrawable blue = new ColorDrawable(getResources().getColor(R.color.blue));
            Drawable blue = getResources().getDrawable(R.drawable.blue);
            ColorDrawable red = new ColorDrawable(getResources().getColor(R.color.caldroid_light_red));

//            caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
            //    caldroidFragment.setBackgroundDrawableForDate(green, greenDate);
//            caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            //    caldroidFragment.setTextColorForDate(R.color.white, greenDate);

            /*caldroidFragment.setBackgroundDrawableForDate(blue, minDate);
            caldroidFragment.setBackgroundDrawableForDate(green, maxDate);
            caldroidFragment.setTextColorForDate(R.color.white, minDate);
            caldroidFragment.setTextColorForDate(R.color.white, maxDate);*/

            caldroidFragment.setBackgroundDrawableForDate(blue, minDate);
            caldroidFragment.setBackgroundDrawableForDate(red, maxDate);
            caldroidFragment.setSelectedDates(maxDate, maxDate);





            /*for (String pDate : presentDate)
            {
                String parts[] = pDate.split("/");

                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month-1);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                Date blueDate = calendar.getTime();
                caldroidFragment.setBackgroundDrawableForDate(blue, blueDate);
                caldroidFragment.setTextColorForDate(R.color.white, blueDate);
            }

            for (String pDate : abscenceDate)
            {
                String parts[] = pDate.split("/");

                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month-1);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                Date greenDate = calendar.getTime();
                caldroidFragment.setBackgroundDrawableForDate(red, greenDate);
                caldroidFragment.setTextColorForDate(R.color.white, greenDate);
            }*/

            caldroidFragment.refreshView();
        }
    }

    public void calendarEventJsonParsing(int selectedMonth)
    {
        try
        {
            JsonCalendarEventsResponse = session.getEventJsonResponse();

            JSONArray jsonArrayMonthlist = session.getEventJsonResponse().getJSONArray("monthlist");

            for(int i = 0; i < jsonArrayMonthlist.length() ; i++)
            {
                JSONObject month = jsonArrayMonthlist.getJSONObject(i);
                int jsonMonth_id = month.getInt("month_id");
                // getMonthEventInfo(jsonMonth_id);

                if(selectedMonth == jsonMonth_id)
                {
                    JSONArray eventList = month.getJSONArray("event_list");

                    /*for(int j = 0 ; j < eventList.length() ; j++)
                    {
                        String eventStartDate = null;
                        String eventEndDate = null;

                        JSONObject eventNames = eventList.getJSONObject(j);
                        eventStartDate = eventNames.getString("COEE_EStartDate");
                        eventEndDate   = eventNames.getString("COEE_EEndDate");
                        String eventName  = eventNames.getString("COEME_EventName");

                        Log.d(TAG,eventStartDate+"::"+eventEndDate+"::"+j);

                        showEvents(eventStartDate,eventEndDate);

                    }*/

                    JSONObject eventNames = eventList.getJSONObject(0);
                    String eventStartDate = eventNames.getString("COEE_EStartDate");
                    String eventEndDate   = eventNames.getString("COEE_EEndDate");
                    String eventName  = eventNames.getString("COEME_EventName");

                    Log.d(TAG,eventStartDate+"::"+eventEndDate);


                    showEvents(eventStartDate,eventEndDate);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void calendarEventJsonParsingPOPUP(Date selectedDate)
    {
        try
        {
            JSONArray jsonArrayMonthlist = JsonCalendarEventsResponse.getJSONArray("monthlist");

            for(int i = 0; i < jsonArrayMonthlist.length() ; i++)
            {
                JSONObject month = jsonArrayMonthlist.getJSONObject(i);
                int jsonMonth_id = month.getInt("month_id");
                // getMonthEventInfo(jsonMonth_id);

                final int m =selectedDate.getMonth() + 1;

                Log.d(TAG,m+"+++++++++++++++++++");

                if(m == jsonMonth_id)
                {
                    JSONArray eventList = month.getJSONArray("event_list");

                    JSONObject eventNames = eventList.getJSONObject(0);
                    String eventStartDate = eventNames.getString("COEE_EStartDate");
                    String eventEndDate   = eventNames.getString("COEE_EEndDate");
                    String eventName  = eventNames.getString("COEME_EventName");

                    Log.d(TAG,eventStartDate+"::"+eventEndDate);

                    showEventsPOPUP(eventStartDate,eventEndDate,eventName,selectedDate);

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void showEvents(String startDate,String endDate)
    {
        String startParts[] = startDate.split("/");
        String endParts[] = endDate.split("/");

        int startDay = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]);
        int startYear = Integer.parseInt(startParts[2]);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, startYear);
        startCalendar.set(Calendar.MONTH, startMonth-1);
        startCalendar.set(Calendar.DAY_OF_MONTH, startDay);

        int endDay = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]);
        int endYear = Integer.parseInt(endParts[2]);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, endYear);
        endCalendar.set(Calendar.MONTH, endMonth-1);
        endCalendar.set(Calendar.DAY_OF_MONTH, endDay);

        Date eventStart = startCalendar.getTime();
        Date eventEnd = endCalendar.getTime();

        caldroidFragment.setSelectedDates(eventStart, eventEnd);
        caldroidFragment.refreshView();
    }

    public void showEventsPOPUP(String startDate,String endDate,String eventName,Date selectedDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); //Or whatever format fits best your needs.
        String dateStr = sdf.format(selectedDate);

        System.out.println("++++++++++++++++++++++"+dateStr);
        if(startDate.equals(dateStr))
        {
            Toast.makeText(getActivity(), eventName, Toast.LENGTH_SHORT).show();
        }

        String startParts[] = startDate.split("/");
        String endParts[] = endDate.split("/");

        int startDay = Integer.parseInt(startParts[0]);
        int startMonth = Integer.parseInt(startParts[1]);
        int startYear = Integer.parseInt(startParts[2]);

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.YEAR, startYear);
        startCalendar.set(Calendar.MONTH, startMonth-1);
        startCalendar.set(Calendar.DAY_OF_MONTH, startDay);

        int endDay = Integer.parseInt(endParts[0]);
        int endMonth = Integer.parseInt(endParts[1]);
        int endYear = Integer.parseInt(endParts[2]);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(Calendar.YEAR, endYear);
        endCalendar.set(Calendar.MONTH, endMonth-1);
        endCalendar.set(Calendar.DAY_OF_MONTH, endDay);

        Date eventStart = startCalendar.getTime();
        Date eventEnd = endCalendar.getTime();




        /*if(eventStart.compareTo(selectedDate) == 0)
        {
            Toast.makeText(CalendarEventsActivity.this, eventName, Toast.LENGTH_SHORT).show();
        }*/

        //       if(selectedDate.getDay() == eventStart.getDay())

        if((eventStart.compareTo(selectedDate) * selectedDate.compareTo(eventEnd)) >= 0)
        {
            Toast.makeText(getActivity(), eventName, Toast.LENGTH_SHORT).show();

        }else{
            //   Toast.makeText(CalendarEventsActivity.this, eventName, Toast.LENGTH_SHORT).show();
        }
        caldroidFragment.refreshView();
    }

}
