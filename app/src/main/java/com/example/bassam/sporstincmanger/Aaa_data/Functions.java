package com.example.bassam.sporstincmanger.Aaa_data;

import android.content.Context;

import com.example.bassam.sporstincmanger.Backend.HttpCall;
import com.example.bassam.sporstincmanger.Interfaces.Constants;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Bassam on 25/3/2018.
 */

public class Functions {
    Context context;

    public Functions(Context context) {
        this.context = context;
    }

    public HttpCall joinDB(String table1, String table2, JSONObject where, String on){
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.join);

        HashMap<String, String> params = new HashMap<>();
        params.put("table1", table1);
        params.put("table2", table2);

        params.put("where", where.toString());
        params.put("on", on);

        httpCall.setParams(params);
        return httpCall;
    }

    public HttpCall joinDB(String table1, String table2, JSONObject where, String on, String select){
        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.join);

        HashMap<String, String> params = new HashMap<>();
        params.put("table1", table1);
        params.put("table2", table2);

        params.put("where", where.toString());
        params.put("on", on);
        params.put("select", select);

        httpCall.setParams(params);
        return httpCall;
    }

    public HttpCall searchDB(String table_name, JSONObject where_info) {

        final JSONArray[] result = {new JSONArray()};

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.selectData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table",table_name);
        params.put("where",where_info.toString());
        httpCall.setParams(params);

        return httpCall;

    }

    public HttpCall updateDB(String table_name, JSONObject where_info, JSONObject values) {

        final JSONArray[] result = {new JSONArray()};

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.updateData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table",table_name);
        params.put("where",where_info.toString());
        params.put("values",values.toString());
        httpCall.setParams(params);

        return httpCall;

    }

    public HttpCall insertToDB(String table_name, JSONObject values) {

        final JSONArray[] result = {new JSONArray()};

        HttpCall httpCall = new HttpCall();
        httpCall.setMethodtype(HttpCall.POST);
        httpCall.setUrl(Constants.insertData);
        HashMap<String,String> params = new HashMap<>();
        params.put("table",table_name);
        params.put("values",values.toString());
        httpCall.setParams(params);
        return httpCall;

    }


    public String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }

    public String getEmoji(String notes) {

        String[] data = notes.split(" ");
        StringBuilder full_notes = new StringBuilder();

        if (data[0].equals("Excellent") || data[0].equals("Good") || data[0].equals("Average") || data[0].equals("Bad")) {
            full_notes.append(data[0]).append(" ").append(getEmojiByUnicode(Integer.valueOf(data[1])));
            data[0] = data[1] = "";
        }

        for (int i=0; i<data.length; i++) {
            full_notes.append(" ").append(data[i]);
        }
        return full_notes.toString();
    }


    public String isValidPhone(String phone_num, String country)
    {
        boolean isValid;
        String phone = "";
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber NumberProto = phoneUtil.parse(phone_num, country);            //with default country
            isValid = phoneUtil.isValidNumber(NumberProto);                  //returns true
            if (isValid) {
                phone = phoneUtil.format(NumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL); //(202) 555-0100
            }

        }  catch (com.google.i18n.phonenumbers.NumberParseException e) {
            e.printStackTrace();
        }

        return phone;
    }

    public boolean isValidMail(String mail)
    {
        String expression = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(mail);
        if (matcher.matches())
        {
            return true;
        }
        else{
            return false;
        }
    }
}
