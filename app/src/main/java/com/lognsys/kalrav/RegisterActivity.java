package com.lognsys.kalrav;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.lognsys.kalrav.db.UserInfoDAO;
import com.lognsys.kalrav.db.UserInfoDAOImpl;
import com.lognsys.kalrav.model.UserInfo;
import com.lognsys.kalrav.util.Constants;
import com.lognsys.kalrav.util.KalravApplication;
import com.lognsys.kalrav.util.Services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.validation.Validator;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private UserInfo userInfo;
    private EditText editUsername;
    private EditText editName;
    private EditText editMobileNumber;
//    private Spinner spninnerGroupName;
    private AutoCompleteTextView autoCities;
    private Button btnRegister;
    ParserTask parserTask;
    PlacesTask placesTask;
//   String REST_REGISTER_USER="http://192.168.0.19:8080/createuser/0/dADAFSDBVXVXCV/deepu@gmail.com/deepu%20sharma/8767569879/location/provenance/2017-05-16%2017:09:51/true/false/device/google/city/state/401107/company/deepu/viv/Youth/Admin";
    String REST_REGISTER_USER="http://192.168.0.19:8080/createuser/";
    private String fb_id, google_id;
    // Initializing a String Array
    String[] groupName = new String[]{
            "Select Group Name",
            "Youth",
            "Couple",
            "Ladies",
    };
    ArrayList<UserInfo> userInfos;
    UserInfoDAOImpl userDaoImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userDaoImpl = new UserInfoDAOImpl(this);
        userInfos = (ArrayList<UserInfo>) getIntent().getSerializableExtra("userInfos");
        if(userInfos!=null){
          for (UserInfo userInfo : userInfos) {

            populateData(userInfo);

        }
        }
        else{
            populateData(null);

        }
    }

    private void populateData(UserInfo userInfo) {


        try {
            editUsername = (EditText) findViewById(R.id.editUsername);
            editName = (EditText) findViewById(R.id.editName);
            editMobileNumber = (EditText) findViewById(R.id.editMobileNumber);
//            spninnerGroupName = (Spinner) findViewById(R.id.spninnerGroupName);
            autoCities = (AutoCompleteTextView) findViewById(R.id.autoCities);
            btnRegister = (Button) findViewById(R.id.btnRegister);
            btnRegister.setOnClickListener(this);
            autoCities.setThreshold(1);

            autoCities.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d("", "Test s.toString() " + s.toString());
                    placesTask = new PlacesTask();
                    placesTask.execute(s.toString());
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                }
            });
            final List<String> groupList = new ArrayList<>(Arrays.asList(groupName));

            // Initializing an ArrayAdapter
            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                    this, R.layout.spinner_list_item, groupList) {
                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    tv.setBackgroundColor(Color.TRANSPARENT);
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };

      /*      spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_list_item);
            spninnerGroupName.setAdapter(spinnerArrayAdapter);

            spninnerGroupName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItemText = (String) parent.getItemAtPosition(position);
                    // If user change the default selection
                    // First item is disable and it is used for hint
                    if (position > 0) {
                        // Notify the selected item text
                        ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        Toast.makeText
                                (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                                .show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

      */      if (userInfo != null) {
                if (userInfo.getEmail() != null) {
                    editUsername.setText(userInfo.getEmail());
                }
                if (userInfo.getName() != null) {
                    editName.setText(userInfo.getName());
                }
                if (userInfo.getFb_id() != null) {
                    fb_id = userInfo.getFb_id();
                }
                if (userInfo.getGoogle_id() != null) {
                    google_id = userInfo.getGoogle_id();
                }
            } else {
                Log.e("", "Could not retrieve data from facebook graphapi...");
            }

        } catch (Exception e) {
            Log.d("", "Requesting facebook JSONException..." + e);

        }

    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("", "Exception" + e);
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onClick(View v) {
//        RegisteredTask task =new RegisteredTast().execute();
        if (editName.getText().toString().length() == 0) {
            editName.setError(getString(R.string.hint_name));
            editName.requestFocus();
        } else {
            if (editUsername.getText().toString().length() == 0) {
                editUsername.setError("Email not entered");
                editUsername.requestFocus();

            } else if (!Services.isEmailValid(editUsername.getText().toString())) {
                editUsername.setError("Email is not valid");
            } else if (editMobileNumber.getText().toString().length() == 0) {
                editMobileNumber.setError("Mobile number is Required");
                editMobileNumber.requestFocus();
            } else if (!Services.isValidMobileNo(editMobileNumber.getText().toString()) && editMobileNumber.getText().toString().length()<10) {
                editMobileNumber.setError("Please enter valid mobile no");
            } else if (autoCities.getText().toString().length() == 0) {
                autoCities.setError("City is not entered");
                autoCities.requestFocus();
            }  else {
               String username, realname,phone, city, groupname=null;
                username = editUsername.getText().toString();
                realname = editName.getText().toString();
                city = autoCities.getText().toString();
//                groupname = spninnerGroupName.getSelectedItem().toString();
                phone = editMobileNumber.getText().toString();
                KalravApplication.getInstance().getPrefs().setName(realname);
                KalravApplication.getInstance().getPrefs().setEmail(username);
                KalravApplication.getInstance().getPrefs().setMobile(phone);
                String auth_id = null;
                if (fb_id != null)
                    auth_id= fb_id;
                if (google_id != null)
                    auth_id= google_id;
                RegisteredTask task = new RegisteredTask(realname,username,auth_id,phone, city, groupname);
                task.execute();
            }
        }
    }
    //Register and inserting  user records
    private class RegisteredTask extends AsyncTask<String, Void, String> {
        String auth_id, username, realname,phone, city, groupname;

        ProgressDialog dialog;

        public RegisteredTask(String realname, String username, String auth_id, String phone, String city, String groupname) {
            this.username = username;
            this.realname = realname;
            this.city = city;
            this.groupname = groupname;
            this.phone = phone;
            this.auth_id=auth_id;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setMessage("Loading");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... place) {

            try {
                String postParameters, firstname, lastname;
                String[] splited=null;
                if(this.realname!=null && this.realname.length()>0 && this.realname.contains(" ")){
                    splited = this.realname.split(" ");
                    firstname = splited[0]==null?"":splited[0];
                    lastname = splited[1]==null?"":splited[1] ;
                }
                else{
                    firstname="-";
                    lastname="-";
                }
                /*http://192.168.0.19:8080/createuser/0/priyank%20doshi/doshipriyank@gmail.com/authid124fnfj
                /8097526387/location/provenance/2017-05-13%2000:00:00/true/true/device/address/city/state/401107/company_name
                /priyank/doshi/group/role*/
                Log.d("", "Test doInBackground this.auth_id " + this.auth_id);
                Log.d("", "Test doInBackground this.phone " + this.phone);

                postParameters=""+0+"/"+this.realname+"/"+this.username+"/"+this.auth_id+"/"+this.phone
                        +"/location/provenance/2017-05-16 17:09:51/"+true+"/"+true+"/android/address/"+this.city
                        +"/state/401107/company/"+firstname+"/"+lastname+"/None/None";
                postParameters=postParameters.replace(" ","%20");


                String urlString=REST_REGISTER_USER+postParameters;
                URL urlToRequest = new URL(urlString);
                Log.d("", "Test doInBackground urlToRequest " + urlToRequest);

                HttpURLConnection urlConnection =
                        (HttpURLConnection) urlToRequest.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type",
                        "application/json");
                Log.d("", "Test doInBackground urlConnection " + urlConnection);
                urlConnection.setFixedLengthStreamingMode(
                        postParameters.getBytes().length);
                Log.d("", "Test doInBackground postParameters " + postParameters);

                PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                out.print(postParameters);
                Log.d("", "Test doInBackground out " + out.toString());

                out.close();
                // handle issues
                int statusCode = urlConnection.getResponseCode();
                Log.d("", "Test doInBackground statusCode" + statusCode);
               /* if (statusCode != HttpURLConnection.HTTP_OK) {
                    // throw some exception
                    Log.d("", "Test doInBackground statusCode not equal" + statusCode);

                }*/
//
               if(statusCode==409){
                   Log.d("", "Test doInBackground Status-Code 409: Conflict. " + statusCode);
               }
               else if(statusCode==201){
                   Log.d("", "Test doInBackground Status-Code 200: OK. " + statusCode);
                   InputStream in =
                           new BufferedInputStream(urlConnection.getInputStream());
                   return getResponseText(in);
               }


            }
            catch (Exception e) {
                Log.d("", "Test doInBackground Exception " + e);
            }

            return null;
        }

        private String getResponseText(InputStream inputStream) throws IOException {
            BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine())!=null) {
                total.append(line).append('\n');
            }
            String ResponseString=total.toString();
            Log.d("", "Test getResponseText ResponseString " + ResponseString);

            r.close();
            return ResponseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("", "Test onPostExecute result " + result);
            try {
                if (result != null) {
                    JSONObject jsonObject = new JSONObject(result);

                    userInfo = new UserInfo();
                    userInfo.setId(jsonObject.getInt("id"));
                    userInfo.setName(jsonObject.getString("realname"));
                    userInfo.setEmail(jsonObject.getString("username"));
                    if (fb_id != null)
                        userInfo.setFb_id(fb_id);
                    if (google_id != null)
                        userInfo.setGoogle_id(google_id);
                    userInfo.setPhoneNo(jsonObject.getString("phone"));
                    userInfo.setCity(jsonObject.getString("city"));
                    userInfo.setLoggedIn(Constants.LOG_IN);
//                        //save to the database

                    KalravApplication.getInstance().getPrefs().setCustomer_id(String.valueOf(userInfo.getId()));
                    KalravApplication.getInstance().setGlobalUserObject(userInfo);
                    Log.d("", "Global object Reg " + KalravApplication.getInstance().getGlobalUserObject());
                    userDaoImpl.addUser(userInfo);
                    KalravApplication.getInstance().getPrefs().setIsLogin(true);

                    dialog.dismiss();
                    Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();


                } else {
                    dialog.dismiss();
                }
            } catch (Exception e) {

            }
        }
    }


    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyAWjz9llvfGuqNMhUTLLV3IvyFaUx37Mes";

            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                Log.d("", "Test UnsupportedEncodingException " + e1);

            }


            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from web service in background

                Log.d("", "Test Exception url " + url);
                data = downloadUrl(url);
                Log.d("", "Test Exception data " + data);

            } catch (Exception e) {
                Log.d("", "Test Exception " + e);

            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("", "Test Exception result " + result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }


    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<String>> {

        JSONObject jObject;

        @Override
        protected List<String> doInBackground(String... jsonData) {

            List<String> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);
                Log.d("", "Test Exception places " + places);

            } catch (Exception e) {
                Log.d("", "Test Exception Exception " + e);

            }
            return places;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            Log.d("", "Test Exception onPostExecute result " + result);

            String[] from = new String[]{"description"};

            int[] to = new int[]{android.R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.autocomplete_list_item, result);

            // Setting the adapter
            autoCities.setAdapter(adapter);
        }
    }
}
