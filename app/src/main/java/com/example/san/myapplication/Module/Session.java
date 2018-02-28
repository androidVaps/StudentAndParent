package com.example.san.myapplication.Module;

import org.json.JSONObject;

/**
 * Created by vaps on 8/14/2017.
 */

public class Session
{

    int AMST_Id = 0;
    int MI_Id = 0;
    int ASMAY_Id = 0;
    int PAYABLE_AMOUNT = 0;

    JSONObject studJsonResponse = null;
    JSONObject eventJsonResponse = null;
    JSONObject feesJsonResponse = null;
    JSONObject examJsonResponse = null;

    public int getAMST_Id() {
        return AMST_Id;
    }

    public void setAMST_Id(int AMST_Id) {
        this.AMST_Id = AMST_Id;
    }

    public int getMI_Id() {
        return MI_Id;
    }

    public void setMI_Id(int MI_Id) {
        this.MI_Id = MI_Id;
    }

    public int getASMAY_Id() {
        return ASMAY_Id;
    }

    public void setASMAY_Id(int ASMAY_Id) {
        this.ASMAY_Id = ASMAY_Id;
    }

    public int getPAYABLE_AMOUNT() {
        return PAYABLE_AMOUNT;
    }

    public void setPAYABLE_AMOUNT(int PAYABLE_AMOUNT) {
        this.PAYABLE_AMOUNT = PAYABLE_AMOUNT;
    }

    public JSONObject getStudJsonResponse() {
        return studJsonResponse;
    }

    public void setStudJsonResponse(JSONObject studJsonResponse) {
        this.studJsonResponse = studJsonResponse;
    }

    public JSONObject getEventJsonResponse() {
        return eventJsonResponse;
    }

    public void setEventJsonResponse(JSONObject eventJsonResponse) {
        this.eventJsonResponse = eventJsonResponse;
    }

    public JSONObject getFeesJsonResponse() {
        return feesJsonResponse;
    }

    public void setFeesJsonResponse(JSONObject feesJsonResponse) {
        this.feesJsonResponse = feesJsonResponse;
    }

    public JSONObject getExamJsonResponse() {
        return examJsonResponse;
    }

    public void setExamJsonResponse(JSONObject examJsonResponse) {
        this.examJsonResponse = examJsonResponse;
    }
}
