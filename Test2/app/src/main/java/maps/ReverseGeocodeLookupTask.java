package maps;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class ReverseGeocodeLookupTask extends AsyncTask<Void, Void, String> {
    private ProgressDialog dialog;
    protected Context applicationContext;


    @Override
    protected void onPreExecute() {
        this.dialog = ProgressDialog.show(applicationContext, "Please wait...contacting the tubes.",
                "Requesting reverse geocode lookup", true);
    }

    @Override
    protected String doInBackground(Void... params) {
        String localityName = "";

          if (GoogleMaps.currentLocation != null){
            localityName = Geocoder.reverseGeocode(GoogleMaps.currentLocation);

        }

        return localityName;
    }

    @Override
    protected void onPostExecute(String result){
        this.dialog.cancel();
      //  Utilities.showToast("Your Locality is: " + result, applicationContext);
      //  Toast toast = new Toast(applicationContext);
//        toast.setText("Your Locality is: " + result);
      // toast.show();
        System.out.println("Your Locality is: " + result);
    }
}