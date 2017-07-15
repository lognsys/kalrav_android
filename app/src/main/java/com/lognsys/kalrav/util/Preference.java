package com.lognsys.kalrav.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.lognsys.kalrav.model.DramaInfo;

import java.util.ArrayList;

/**
 * Created by admin on 3/22/2017.
 */

public class Preference {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog pDialog;
    public void getClear()
    {
        editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    public Preference(Context context) {
        // TODO Auto-generated constructor stub
        sharedPreferences = context.getSharedPreferences("Cache", Context.MODE_PRIVATE);

    }
    public void showDialog(Context context) {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hidepDialog(Context context) {

        if (pDialog !=null && pDialog.isShowing())
            pDialog.dismiss();
    }
    public void setCity(String city)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("city", city);
        editor.commit();
    }

    public String getCity()
    {
        return sharedPreferences.getString("city", null);
    }
    public void setIsLogin(Boolean isLogin)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", isLogin);
        editor.commit();
    }

    public Boolean getIsLogin()
    {
        return sharedPreferences.getBoolean("isLogin", false);
    }
    public void setIsFacebookLogin(Boolean isFbLogin)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFbLogin", isFbLogin);
        editor.commit();
    }

    public Boolean getIsFacebookLogin()
    {
        return sharedPreferences.getBoolean("isFbLogin", false);
    }
    public void setIsGoogleLogin(Boolean isGoogleLogin)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isGoogleLogin", isGoogleLogin);
        editor.commit();
    }

    public Boolean getIsGoogleLogin()
    {
        return sharedPreferences.getBoolean("isGoogleLogin", false);
    }

    public void setCustomer_id(String customer_id)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("customer_id", customer_id);
        editor.commit();
    }

    public String getCustomer_id()
    {
        return sharedPreferences.getString("customer_id", null);
    }

    public void setName(String name)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.commit();
    }

    public String getName()
    {
        return sharedPreferences.getString("name", null);
    }

    public void setMobile(String mobile)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("mobile", mobile);
        editor.commit();
    }

    public String getMobile()
    {
        return sharedPreferences.getString("mobile", null);
    }

    public void setEmail(String email)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.commit();
    }

    public String getEmail()
    {
        return sharedPreferences.getString("email", null);
    }

    public void setImage(String image)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("image", (String.valueOf(image)));
        editor.commit();
    }

    public String getImage()
    {
        return sharedPreferences.getString("image", null);
    }

     public void setLink(String link)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("link", link);
        editor.commit();
    }

    public String getLink()
    {
        return sharedPreferences.getString("link", null);
    }
    public void setPicture(String picture)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("picture", picture);
        editor.commit();
    }

    public String getPicture()
    {
        return sharedPreferences.getString("picture", null);
    }

    public void setTimezone(String timezone)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("timezone", timezone);
        editor.commit();
    }

    public String getTimezone()
    {
        return sharedPreferences.getString("timezone", null);
    }
    public void setDevice_token(String device_token)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("device_token", device_token);
        editor.commit();
    }

    public String getDevice_token()
    {
        return sharedPreferences.getString("device_token", null);
    }
    public void setDevice_type(String device_type)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("device_type", device_type);
        editor.commit();
    }

    public String getDevice_type()
    {
        return sharedPreferences.getString("device_type", null);
    }


    public void setIsSimilarEmailID(Boolean Is_similarEmailid)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IS_SIMILAR_EMAILID", Is_similarEmailid);
        editor.commit();
    }

    public Boolean getIsSimilarEmailID()
    {
        return sharedPreferences.getBoolean("IS_SIMILAR_EMAILID", false);
    }
    public void setGoogTokenId(String googTokenId)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("GOOG_TOKEN_ID", googTokenId);
        editor.commit();
    }

    public String getGoogTokenId()
    {
        return sharedPreferences.getString("GOOG_TOKEN_ID", null);
    }

    public void setGoogServerAuthcode(String googServerAuthcode)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("GOOG_SERVE_AUTHCODE", googServerAuthcode);
        editor.commit();
    }

    public String getGoogServerAuthcode()
    {
       return sharedPreferences.getString("GOOG_SERVE_AUTHCODE", null);
    }
    public void setUser_id(String user_id)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user_id", user_id);
        editor.commit();
    }

    public String getUser_id()
    {
        return sharedPreferences.getString("user_id", null);
    }



    public String getUser_Group_Name()
    {
        return sharedPreferences.getString("group", null);
    }
    public void setUser_Group_Name(String group)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("group", group);
        editor.commit();
    }

}
