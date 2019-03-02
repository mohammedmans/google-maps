package senior.com.locationandmaps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

public class MyLocationProvider implements LocationListener{

    LocationManager locationManager;
    Context context;
    Location location;
    boolean canGetLocatin;

    final int MIN_TIME_BETWEEN_UPDATES = 5*1000;
    final int MIN_DISTANCE_BETWEEN_UPDATES = 5;

    interface OnMyLocationChangeListener{
        void LocationChanged(Location newLocation);
    }

    OnMyLocationChangeListener onLocationChangedListener;

    public void setOnLocationChangedListener(OnMyLocationChangeListener onLocationChangedListener) {
        this.onLocationChangedListener = onLocationChangedListener;
    }

    public MyLocationProvider(Context context){
        this.context=context;
        locationManager = (LocationManager)
                context.getSystemService(Context.LOCATION_SERVICE);
        location = null;
        canGetLocatin=false;
    }
    public boolean canGetMyLocation(){
        canGetLocatin=
                isGPSProviderEnabled() ||isNetworkProviderEnabled();
        return canGetLocatin;
    }
    public boolean isGPSProviderEnabled(){
       return
               locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
    public boolean isNetworkProviderEnabled(){
       return
               locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    public Location getMyLocation(){
        if(!isGPSProviderEnabled() && !isNetworkProviderEnabled()){
            canGetLocatin=false;
            return null;
        }

        String MyEnabledProvider =null;
        if(isNetworkProviderEnabled())
            MyEnabledProvider = LocationManager.NETWORK_PROVIDER;

         if(isGPSProviderEnabled())
            MyEnabledProvider = LocationManager.GPS_PROVIDER;

         locationManager.requestLocationUpdates(MyEnabledProvider,MIN_TIME_BETWEEN_UPDATES,MIN_DISTANCE_BETWEEN_UPDATES,this);

         // can return null at firsty because there in location at first
        // can check if location null
        location = locationManager.getLastKnownLocation(MyEnabledProvider);

        return location;
    }

    @Override
    public void onLocationChanged(Location location) {

        this.location = location;
        Log.e("locationListener","locationChanged");
        if(onLocationChangedListener!=null)
            onLocationChangedListener.LocationChanged(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.e("locationlistener",s+" is enabled");
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.e("locationlistener",s+" is disabled");

    }
}
