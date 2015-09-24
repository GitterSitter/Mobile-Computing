package maps;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.example.trond.test.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class GoogleMaps extends FragmentActivity {
    private LatLng CUSTOM_LOCATION = new LatLng(-33.8892500, 151.1913480);
    private GoogleMap mMap; // Might be null if google Play services APK is not available.
    private LocationManager locationManager;
    public static Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();



/*

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Subscribe to the location manager's updates on the current location
        //Makes location request every 2,5 min
        this.locationManager.requestLocationUpdates("gps", (long) 150000, (float) 10.0, new LocationListener() {
            public void onLocationChanged(Location loc) {

                handleLocationChanged(loc);
            }

            public void onProviderDisabled(String arg0) {
                // TODO Auto-generated method stub

            }

            public void onProviderEnabled(String arg0) {
                // TODO Auto-generated method stub

            }

            public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
                // TODO Auto-generated method stub
            }
        });


        */
    }



    private void handleLocationChanged(Location loc){
        // Save the latest location
        this.currentLocation = loc;
        // Update the latitude & longitude TextViews
        System.out.println("Updated on move location: " + loc.getLatitude() + " " + loc.getLongitude());
    }

    public void handleReverseGeocodeClick(View v) {

        Location l = new Location("test");
        l.setLatitude(-33.8908440);
        l.setLongitude(151.2742910);
        currentLocation = l;

        if (this.currentLocation != null) {
            // Kickoff an asynchronous task to fire the reverse geocoding
            // request off to google
           ReverseGeocodeLookupTask task = new ReverseGeocodeLookupTask();
            task.applicationContext = this;
            task.execute();
        } else {
            // If we don't know our location yet, we can't do reverse
            // geocoding - display a please wait message

         //   showToast("Please wait until we have a location fix from the gps");
            Toast.makeText(GoogleMaps.this, "Please wait until we have a location fix from the gps", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    public void onClick_meetingPoint(View v) throws IOException {

        String adress = "TestStreetDownTown 2152";

        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(getCurrentLocation().getLatitude(), getCurrentLocation().getLongitude())).title("Meeting Point").snippet("Address " + adress));
        LatLng pos = marker.getPosition();
        marker.setSnippet(pos.toString());
        marker.showInfoWindow();
        marker.setDraggable(true);
        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        Geocoder geocoder = new Geocoder(this);
        Object t = geocoder.getFromLocation(getCurrentLocation().getLatitude(), getCurrentLocation().getLongitude(), 1);
       List<Address> te = geocoder.getFromLocation(getCurrentLocation().getLatitude(), getCurrentLocation().getLongitude(), 1);
        System.out.println(t.toString());

        for(Address ad : te){
            System.out.println(ad.toString() );
        }



    }


    public void onClick_myPosition(View v) {
        //Predefined location (Bondi beach)
        LatLng latLng = new LatLng(-33.8908440, 151.2742910);


        Location location = getCurrentLocation();
        String position = "Longtitude: " + location.getLongitude() + ". Latitude: " + location.getLatitude();
        System.out.println(position);
        // Show the current location in google Map (Bondi beach)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        // Enable MyLocation Layer of google Map
        mMap.setMyLocationEnabled(true);

        Location myLocation = getCurrentLocation();

        // set map type
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Show the current location in google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here!"));

        System.out.println(latitude + " " + longitude + "TEST");

    }





    public Location getCurrentLocation() {
        // Get LocationManager object from System Service LOCATION_SERVICE
        //LocationManager locationManager

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);



        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        return myLocation;
    }





}
