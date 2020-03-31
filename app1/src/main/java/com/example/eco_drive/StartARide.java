package com.example.eco_drive;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class StartARide extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    //widgets
   // private AutoCompleteTextView SourceTF;
    private EditText  SourceTF,DestinationTF;
    private String Vehicle_TypeTF,Fuel_TypeTF,Journey_TimeTF;
    private Button StartRideTF;
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private Spinner spinner2;
    private ArrayAdapter<CharSequence> adapter2;
    private Spinner spinner10;
    private ArrayAdapter<CharSequence> adapter10;
    private Spinner spinner9;
    private ArrayAdapter<CharSequence> adapter9;
    private Spinner spinner11;
    private ArrayAdapter<CharSequence> adapter11;


    //Variables
    private static final String TAG = "CKKK";
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private boolean mLocationPermissionGranted = false;
    private Location mLastKnownLocation;
    private LatLng mDefaultLocation;
    private static final int PERMISSIONS_REQUEST_ACCESS_CODE = 12345;
    private static final float DEFAULT_ZOOM = 15f;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    /*private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    /*private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_aride);

        Log.i(TAG, "onCreate: "+"again");

       /* mGoogleApiClient = new GoogleApiClient.Builder(StartARide.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();*/

        SourceTF = findViewById(R.id.sourceF);
        DestinationTF = findViewById(R.id.destinationF);
        StartRideTF = findViewById(R.id.startrideF);
        /*SourceTF.setThreshold(3);
        SourceTF.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        SourceTF.setAdapter(mPlaceArrayAdapter);*/

        getLocationPermission();
        BottomSheetViews();

    }

   /* private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

        }
    };*/

    @Override
    public void onConnected(Bundle bundle) {
      //  mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        Log.i(TAG, "Google Places API connected.");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
      /*  Log.e(TAG, "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());

        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();*/
    }

    @Override
    public void onConnectionSuspended(int i) {
     /*   mPlaceArrayAdapter.setGoogleApiClient(null);
        Log.e(TAG, "Google Places API connection suspended.");*/
    }

    private void BottomSheetViews(){

        spinner11 = findViewById(R.id.spinner11);
        adapter11 = ArrayAdapter.createFromResource(this,R.array.minutes,android.R.layout.simple_spinner_item);
        adapter11.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner11.setAdapter(adapter11);
        spinner11.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int a, long b) {
                    Journey_TimeTF = parent.getItemAtPosition(a).toString();
           //     Toast.makeText(getBaseContext(), parent.getItemAtPosition(a)+ " selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner10 = (Spinner) findViewById(R.id.spinner10);
        adapter10 = ArrayAdapter.createFromResource(this,R.array.time_zones,android.R.layout.simple_spinner_item);
        adapter10.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner10.setAdapter(adapter10);
        spinner10.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int a, long b) {

                Journey_TimeTF = parent.getItemAtPosition(a).toString();
               // Toast.makeText(getBaseContext(), parent.getItemAtPosition(a)+ " selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner9 = findViewById(R.id.spinner9);
        adapter9 = ArrayAdapter.createFromResource(this,R.array.hours,android.R.layout.simple_spinner_item);
        adapter9.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner9.setAdapter(adapter9);
        spinner9.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int a, long b) {

                Journey_TimeTF = parent.getItemAtPosition(a).toString();
                Log.i(TAG, "onItemSelected: "+parent.getSelectedItem());

              //  Toast.makeText(getBaseContext(), parent.getItemAtPosition(a)+ " selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner = findViewById(R.id.spinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.vehicle_types,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Vehicle_TypeTF = parent.getItemAtPosition(position).toString();
                //      Toast.makeText(getBaseContext(), parent.getItemAtPosition(position)+ " selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner2 = findViewById(R.id.spinner2);
        adapter2 = ArrayAdapter.createFromResource(this,R.array.fuel_types,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {

                Fuel_TypeTF = parent.getItemAtPosition(i).toString();
          //      Toast.makeText(getBaseContext(), parent.getItemAtPosition(i)+ " selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void moveCamera(LatLng latLng,float DEFAULT_ZOOM,String title){

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM));
        MarkerOptions options = new MarkerOptions().position(latLng).title(title);
        mMap.addMarker(options);

    }

    private void getLocationPermission() {

        String[] permission = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
               Log.i(TAG, "getLocationPermission: i am here");
                mLocationPermissionGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this, permission, PERMISSIONS_REQUEST_ACCESS_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permission, PERMISSIONS_REQUEST_ACCESS_CODE);
        }

    }

    private void initMap(){
         mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(StartARide.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                            mLocationPermissionGranted = false;
                            Log.i(TAG, "onRequestPermissionsResult: " + mLocationPermissionGranted + ": " + grantResults[i]);
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    Log.i("ck", "onRequestPermissionsResult: " + mLocationPermissionGranted);
                    initMap();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(StartARide.this, "map is ready", Toast.LENGTH_SHORT).show();
        if (mLocationPermissionGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            init();
        }
    }

    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            mLastKnownLocation = (Location) task.getResult();
                            moveCamera(new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()), DEFAULT_ZOOM,"My Location");
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                           /* mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                           // mMap.getUiSettings().setMyLocationButtonEnabled(false);*/
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void init(){

        SourceTF.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if(actionID == EditorInfo.IME_ACTION_SEARCH
                || actionID == EditorInfo.IME_ACTION_DONE
                || keyEvent.getAction() == keyEvent.ACTION_DOWN
                || keyEvent.getAction() == keyEvent.KEYCODE_ENTER){

                    geoLocate(SourceTF.getText().toString());
                }
                return false;
            }
        });

        DestinationTF.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if(actionID == EditorInfo.IME_ACTION_SEARCH
                        || actionID == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == keyEvent.ACTION_DOWN
                        || keyEvent.getAction() == keyEvent.KEYCODE_ENTER){

                    geoLocate(DestinationTF.getText().toString());

                }
                return false;
            }
        });
    }


    private void geoLocate(String SearchSource) {

        Geocoder geocoder = new Geocoder(StartARide.this);
        List<Address>  list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(SearchSource,1);
        }
        catch (Exception e){
            Log.e(TAG,"geolocation Exception " + e.getMessage());
        }

        if(list.size() > 0){
            Address address = list.get(0);
            Log.d(TAG, "geoLocate: "+ address.toString());

            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM,address.getAddressLine(0));
        }
    }


    @Override
    public void onLocationChanged(Location location) {
    }


    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
