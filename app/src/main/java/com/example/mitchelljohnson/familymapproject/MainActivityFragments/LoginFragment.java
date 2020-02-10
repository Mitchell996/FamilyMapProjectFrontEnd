package com.example.mitchelljohnson.familymapproject.MainActivityFragments;

import android.os.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.text.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
//import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.*;

import com.example.mitchelljohnson.familymapproject.FilterActivity;
import com.example.mitchelljohnson.familymapproject.R;
import com.example.mitchelljohnson.familymapproject.SearchActivity.SearchActivity;
import com.example.mitchelljohnson.familymapproject.dataObjects.dataSet;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.event.eventSet;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.Login.loginRequest;
import com.example.mitchelljohnson.familymapproject.dataObjects.model;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person.*;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person.personResponse;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.person.personsResponse;
import com.example.mitchelljohnson.familymapproject.proxyServer;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.register.*;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.register.registerRequest;
import com.example.mitchelljohnson.familymapproject.RequestsAndResponses.register.registerResponse;
import com.example.mitchelljohnson.familymapproject.settingsActivity.SettingsActivity;

import android.view.MenuItem;

public class LoginFragment extends Fragment {

    EditText mServerPort;
    EditText mserverHost;
    EditText mtextPassword;
    EditText mFirstName;
    EditText mUserName;
    EditText mLastName;
    EditText mEmail;
    RadioButton mGender;
    RadioGroup radioGroup;
    String gender = "";
    View mcurrentView;

    boolean validLogin = false;

    Button mRegister;
    Button mSignin;
    int mCurrentIndex = 0;
    private static final String TAG = "LoginFragment";
    private static final String KEY_INDEX = "KeyLogin";

    private eventSet events;
    private personsResponse persons;


    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, LoginFragment.class);
        return intent;
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }



    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment
        setHasOptionsMenu(false);
            View v = inflater.inflate(R.layout.login_fragment, container, false);
            mcurrentView = v;
            mServerPort = (EditText) v.findViewById(R.id.serverPort);
            mserverHost = (EditText) v.findViewById(R.id.serverHost);
            mUserName = (EditText) v.findViewById(R.id.UserName);
            mtextPassword = (EditText) v.findViewById(R.id.Password);
            mFirstName = (EditText) v.findViewById(R.id.FirstName);
            mLastName = (EditText) v.findViewById(R.id.LastName);
            mEmail = (EditText) v.findViewById(R.id.Email);
            radioGroup = (RadioGroup) v.findViewById(R.id.rGroup);
            mGender = (RadioButton)radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());

            mRegister = (Button) v.findViewById(R.id.Register);
            mSignin = (Button) v.findViewById(R.id.Sign_in);
            mRegister.setEnabled(false);
            mSignin.setEnabled(false);


            mServerPort.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    buttonAvailability();
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
            mserverHost.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    buttonAvailability();
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
            mUserName.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    buttonAvailability();
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
            mtextPassword.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    buttonAvailability();
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
            mFirstName.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    buttonAvailability();
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
            mLastName.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    buttonAvailability();
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
            mEmail.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    buttonAvailability();
                }
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
            });
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                                                          // This will get the radiobutton that has changed in its check state
                    RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                                                          // This puts the value (true/false) into the variable
                    boolean isChecked = checkedRadioButton.isChecked();
                                                          // If the radiobutton that has changed in check state is now checked...
                    if (isChecked) {
                                gender = checkedRadioButton.getText().toString();

                        }
                        buttonAvailability();
                        }
            });



        mSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName = (EditText) v.findViewById(R.id.UserName);
                mtextPassword = (EditText) v.findViewById(R.id.Password);
                onSignInClicked();
            }
        });
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onRegisterClicked();

            }
        });

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
        }
        return v;
    }

    public void onSignInClicked(){
        mUserName = (EditText) mcurrentView.findViewById(R.id.UserName);
        mtextPassword = (EditText) mcurrentView.findViewById(R.id.Password);
        String userName = mUserName.getText().toString();
        String password = mtextPassword.getText().toString();
        loginRequest login = new loginRequest(userName, password);
        try {
            new asyncLogin().execute(login).get();
        }
        catch(Exception e){}
        if(validLogin) {
            runTheMap();
        }
    }

    public void runTheMap(){
        MapsActivity mapsActivity = new MapsActivity();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainFrameLayout, mapsActivity);
        fragmentTransaction.commit();
    }

    public void onRegisterClicked(){
        String userName = mUserName.getText().toString();
        String password = mtextPassword.getText().toString();
        String firstName = mFirstName.getText().toString();
        String lastName = mLastName.getText().toString();
        String email = mEmail.getText().toString();
        registerRequest register = new registerRequest(firstName, lastName, userName, password, decideGender(), email);
        try {
            new asyncRegister().execute(register).get();
        }
        catch(Exception e){}
        if(validLogin) {
            runTheMap();
        }
    }
    public String decideGender(){
        String genderChar;
        if(gender.equals("Male")){
            genderChar = "m";
        }
        else {
            genderChar = "f";
        }
        return genderChar;
    }

    public void buttonAvailability()
    {
        if(!(mServerPort.getText().toString().equals("")) || (mserverHost.getText().toString().equals("")) ){

                if(!(mUserName.getText().toString().equals("") || (mtextPassword.getText().toString().equals("")))) {
                    //mRegister.setEnabled(true);
                    mSignin.setEnabled(true);
                    if (!((mFirstName.getText().toString().equals("")) || (mEmail.getText().toString().equals(""))
                            || (gender.equals("")) || mLastName.getText().toString().equals(""))) {
                        mRegister.setEnabled(true);
                    }
                }
        }
        if((mServerPort.getText().toString().equals("")) || (mserverHost.getText().toString().equals("")) ) {
            mSignin.setEnabled(false);
            mRegister.setEnabled(false);
        }
            if((mUserName.getText().toString().equals("") || (mtextPassword.getText().toString().equals("")))) {
                //mRegister.setEnabled(true);
                mSignin.setEnabled(false);
                mRegister.setEnabled(false);
            }
        if (((mFirstName.getText().toString().equals("")) || (mEmail.getText().toString().equals(""))
                || (gender.equals("")) || mLastName.getText().toString().equals(""))) {
            mRegister.setEnabled(false);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private class asyncLogin extends AsyncTask<loginRequest, Void, personResponse>{

       // proxyServer proxy;
       String serverHost = mserverHost.getText().toString();
       String serverPort = mServerPort.getText().toString();

        @Override
        protected personResponse doInBackground(loginRequest... loginRequests) {
            registerResponse response = proxyServer.Login(serverHost, serverPort, loginRequests[0]);
            if(response == null){
                Toast message = Toast.makeText(getContext(), "an error occured in retrieving the data", Toast.LENGTH_LONG);
                message.show();
                return null;
            }
            if((response.message != null)) {
                return new personResponse(response.message);
            }
            personRequest newR = new personRequest(response.personID, response.authToken);
            personResponse toReturn = proxyServer.getPerson(serverHost, serverPort, newR);
            persons = proxyServer.getPersons(serverHost, serverPort, newR);
            events = proxyServer.getEvents(serverHost, serverPort, newR);
            dataSet data = new dataSet(events, persons, toReturn);
            model m = model.getInstance();
            m.setData(data);m.setServerHost(serverHost);
            m.setServerPort(serverPort);
            loginRequest toM = loginRequests[0];
            m.setLogin(toM);
            return toReturn;
        }


        @Override
        protected void onPostExecute(personResponse response)
        {
            if((response.message != null)) {
                Toast message = Toast.makeText(getContext(), response.message, Toast.LENGTH_LONG);
                message.show();
            }
            else if(!(response.firstName == null)){

                Toast message = Toast.makeText(getContext(), "Welcome, "+ response.firstName + " "  + response.lastName, Toast.LENGTH_LONG);
                validLogin = true;
                runTheMap();
                message.show();
            }
            else if(response.firstName == null){
                Toast message = Toast.makeText(getContext(), "Looks like something is wrong here", Toast.LENGTH_LONG);
                message.show();
            }
        }

    }
    private class asyncRegister extends AsyncTask<registerRequest, Void, personResponse>{
        String serverHost = mserverHost.getText().toString();
        String serverPort = mServerPort.getText().toString();

        @Override
        protected personResponse doInBackground(registerRequest... registerRequests) {
            registerResponse response = proxyServer.Register(serverHost, serverPort, registerRequests[0]);
            if(response == null){
                Toast message = Toast.makeText(getContext(), "an error occured in retrieving the data", Toast.LENGTH_LONG);
                message.show();
                return null;
            }
            if((response.message != null)) {
                return new personResponse(response.message);
            }
            personRequest newR = new personRequest(response.personID, response.authToken);
            personResponse toReturn = proxyServer.getPerson(serverHost, serverPort, newR);
            persons = proxyServer.getPersons(serverHost, serverPort, newR);
            events = proxyServer.getEvents(serverHost, serverPort, newR);
            dataSet data = new dataSet(events, persons, toReturn);
            model m = model.getInstance();
            m.setData(data);
            m.setServerHost(serverHost);
            m.setServerPort(serverPort);
            loginRequest toM = new loginRequest(registerRequests[0].getUserName(), registerRequests[0].getPassword());
            m.setLogin(toM);
            return toReturn;
        }


        @Override
        protected void onPostExecute(personResponse response)
        {

            if((response.message != null)) {
                Toast message = Toast.makeText(getContext(), response.message, Toast.LENGTH_LONG);
                message.show();
            }
            else if(!(response.firstName == null)){

                Toast message = Toast.makeText(getContext(), "Welcome, "+ response.firstName + " "  + response.lastName, Toast.LENGTH_LONG);
                validLogin = true;
                runTheMap();
                message.show();
            }
            else if(response.firstName == null){
                Toast message = Toast.makeText(getContext(), "Looks like something is wrong here", Toast.LENGTH_LONG);
                message.show();
            }
        }
    }


}
