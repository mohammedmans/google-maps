package senior.com.locationandmaps;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import senior.com.locationandmaps.Base.BaseActivity;

public class MainActivity extends BaseActivity implements OnMapReadyCallback
,MyLocationProvider.OnMyLocationChangeListener{


    final int MY_PERMISSIONS_REQUEST_LOCATION = 400;
    final int MY_PERMISSIONS_REQUEST_CAMERA = 500;
    final int PLACE_PICKER_REQUEST_CODE =6500;
    MyLocationProvider myLocationProvider;
    PlaceAutocompleteFragment autocompleteFragment;
    MapView mapView;
    GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myLocationProvider = new MyLocationProvider(activity);
        myLocationProvider.setOnLocationChangedListener(this);
        mapView= findViewById(R.id.map_view);
       // mapView = (MapView) getSupportFragmentManager().findFragmentById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        findViewById(R.id.fab)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                        try {
                            startActivityForResult(builder.build(activity),
                                    PLACE_PICKER_REQUEST_CODE);
                        } catch (GooglePlayServicesRepairableException e) {
                            e.printStackTrace();
                        } catch (GooglePlayServicesNotAvailableException e) {
                            e.printStackTrace();
                            ShowMessage(getString(R.string.warning),getString(R.string.google_play_services_not_available))
                                    ;
                        }

                    }
                });
        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().
                        findFragmentById(R.id.place_autocomplete_fragment);

     autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
         @Override
         public void onPlaceSelected(Place place) {

             if(googleMap!=null) {
                         googleMap.addMarker(new MarkerOptions()
                                 .position(place.getLatLng())
                                 .title(place.getName().toString()));
                 googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
                 // Zoom in, animating the camera.
                 googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                 // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                 googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
             }
         }

         @Override
         public void onError(Status status) {

         }
     });

       // startActivity(new Intent(activity,MapsActivity.class));
        if(isLocationAllowed()){
            ShowLocation();
        }else {
            RequestLocationPermession();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void LocationChanged(Location newLocation) {
        Log.e("mainactivity","locationChanged");

        if(myLocationMarker!=null){

            LatLng myLatLng = new LatLng(newLocation.getLatitude(),newLocation.getLongitude());
            myLocationMarker.setPosition(myLatLng);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));
            // Zoom in, animating the camera.
            googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
         }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        ShowLocation();

    }

    private void RequestLocationPermession() {

        // an explanation only if the user has already denied that permission
        if (
                // that returns true if the user has previously denied the request,
                // and returns false if a user has denied a permission and selected
            // the Don't ask again option in the permission request dialog,
            // or if a device policy prohibits the permission.
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
          ShowConfirmationDialog(getString(R.string.warning), getString(R.string.access_Location)
                    , getString(R.string.accept), getString(R.string.cancel)
                    , new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        }
                    },null);

        } else {
            // this will first time if refused to give per will enter first condition
            // No explanation needed; request the permission
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    ShowLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(activity, "cannot access location", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_CAMERA:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    ShowLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(activity, "cannot access location", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    boolean isLocationAllowed(){
        if (
                ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }

        return true;
    }
    Marker myLocationMarker;
   public void ShowLocation(){

        if(myLocationProvider.canGetMyLocation()){
            Location location = myLocationProvider.getMyLocation();
            if(location!=null){
                TextView textView = findViewById(R.id.location_text_view);
                textView.setText(location.toString());
                LatLng myLatLng= new LatLng(location.getLatitude(),location.getLongitude());
                if(googleMap!=null) {
                    myLocationMarker =
                            googleMap.addMarker(new MarkerOptions()
                                    .position(myLatLng)
                                    .title("myLocation")
                            .icon(BitmapDescriptorFactory.fromBitmap( BitmapFactory.decodeResource(getResources(),
                                    R.drawable.ic_car_marker))));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));
                    // Zoom in, animating the camera.
                    googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                    // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                }

            }
        }else {
            ShowConfirmationDialog(getString(R.string.warning), getString(R.string.please_enable_gps)
                    , "ok", "cancel", new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    },null);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PLACE_PICKER_REQUEST_CODE){
            if(resultCode==RESULT_OK){
                Place place = PlacePicker.getPlace(activity, data);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                if(googleMap!=null) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(place.getLatLng())
                            .title(place.getName().toString()));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 15));
                    // Zoom in, animating the camera.
                    googleMap.animateCamera(CameraUpdateFactory.zoomIn());
                    // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                }
            }

        }

    }
}
