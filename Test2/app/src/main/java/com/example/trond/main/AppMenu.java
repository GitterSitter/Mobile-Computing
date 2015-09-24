package com.example.trond.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.authentication.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.http.NextServiceFilterCallback;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilter;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterRequest;
import com.microsoft.windowsazure.mobileservices.http.ServiceFilterResponse;

import java.net.MalformedURLException;

import users.Friends;
import maps.GoogleMaps;
import users.NearByPeople;

public class AppMenu extends AppCompatActivity {

    private MobileServiceClient mClient;
    private ProgressBar mProgressBar;
    private TextView textField;
    public static String userID;
    public String test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        FacebookSdk.sdkInitialize(getApplicationContext());

        try {
            // Create the Mobile Service Client instance, using the provided

            // Mobile Service URL and key
            mClient = new MobileServiceClient(
                    "https://databasecloud.azure-mobile.net/",
                    "PYYepJugqpXHGjtyoYZqnLBebxHCzZ89",
                    this).withFilter(new ProgressFilter());


            authenticate();


        } catch (MalformedURLException e) {
            createAndShowDialog(new Exception("There was an error creating the Mobile Service. Verify the URL"), "Error");
        } catch (Exception e){
            createAndShowDialog(e, "Error");
        }


        textField = (TextView) findViewById(R.id.textView);

        textField.setText("Logged in as: " +userID  + " " + test);
    }



    private void authenticate() {
        // Login using the Google provider.

        ListenableFuture<MobileServiceUser> mLogin = mClient.login(MobileServiceAuthenticationProvider.Facebook);
        mClient.getCurrentUser();
        Futures.addCallback(mLogin, new FutureCallback<MobileServiceUser>() {

            @Override
            public void onFailure(Throwable exc) {
                createAndShowDialog((Exception) exc, "Error");
            }

            @Override
            public void onSuccess(MobileServiceUser user) {

                createAndShowDialog(String.format(
                        "You are now logged in - %1$2s",
                        user.getUserId()), "Success");
                      userID = user.getUserId();
                        test  =  user.getUserId();

            }
        });


    }








    public void startMap(View v){
        Intent intent = new Intent(this, GoogleMaps.class);
      //  myIntent.putExtra("key", value); //Optional parameters
      // Intent intent = getIntent();
      //  String value = intent.getStringExtra("key"); //if it's a string you stored.
          startActivity(intent);
    }

    public void startFriends(View v){

        Intent intent = new Intent(this, Friends.class);
        startActivity(intent);
    }

    public void startDiscover(View v){

        Intent intent = new Intent(this, NearByPeople.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    private void createAndShowDialogFromTask(final Exception exception, String title) {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        createAndShowDialog(exception, "Error");
                    }
                });
    }


    /**
     * Creates a dialog and shows it
     *
     * @param exception
     *            The exception to show in the dialog
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(Exception exception, String title) {
        Throwable ex = exception;
        if(exception.getCause() != null){
            ex = exception.getCause();
        }
        createAndShowDialog(ex.getMessage(), title);
    }

    /**
     * Creates a dialog and shows it
     *
     * @param message
     *            The dialog message
     * @param title
     *            The dialog title
     */
    private void createAndShowDialog(final String message, final String title) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(message);
        builder.setTitle(title);
        builder.create().show();
    }
    private class ProgressFilter implements ServiceFilter {

        @Override
        public ListenableFuture<ServiceFilterResponse> handleRequest(ServiceFilterRequest request, NextServiceFilterCallback nextServiceFilterCallback) {

            final SettableFuture<ServiceFilterResponse> resultFuture = SettableFuture.create();


            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (mProgressBar != null) mProgressBar.setVisibility(ProgressBar.VISIBLE);
                }
            });

            ListenableFuture<ServiceFilterResponse> future = nextServiceFilterCallback.onNext(request);

            Futures.addCallback(future, new FutureCallback<ServiceFilterResponse>() {
                @Override
                public void onFailure(Throwable e) {
                    resultFuture.setException(e);
                }

                @Override
                public void onSuccess(ServiceFilterResponse response) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (mProgressBar != null)
                                mProgressBar.setVisibility(ProgressBar.GONE);
                        }
                    });

                    resultFuture.set(response);
                }
            });

            return resultFuture;
        }
    }







    public void logout() {
        boolean shouldRedirectToLogin = true;
        //Clear the cookies so they won't auto login to a provider again
       // CookieSyncManager.createInstance(this);

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        //  cookieManager.flush();
        //Clear the user id and token from the shared preferences
        SharedPreferences settings = this.getSharedPreferences("User", 0);
        SharedPreferences.Editor preferencesEditor = settings.edit();
        preferencesEditor.clear();
        preferencesEditor.commit();
        //Clear the user and return to the auth activity
        mClient.logout();
        //Take the user back to the auth activity to relogin if requested
        if (shouldRedirectToLogin) {
            Intent logoutIntent = new Intent(this, AppMenu.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(logoutIntent);
        }
    }



    public void logoutt(View v){

       try {
           mClient.logout();

           AlertDialog alertDialog = new AlertDialog.Builder(this).create();
           alertDialog.setTitle("Alert");
           alertDialog.setMessage("You are logged out of Facebook!");
           alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Login",
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                           FacebookSdk.sdkInitialize(getApplicationContext());
                           authenticate();
                       }
                   });
           alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Continue",
                   new DialogInterface.OnClickListener() {
                       public void onClick(DialogInterface dialog, int which) {
                           //  finish();
                           dialog.dismiss();
                       }
                   });
           alertDialog.show();

       }catch(Exception e){
         // e.printStackTrace();
       }finally {
           logout();
       }
    }






}


