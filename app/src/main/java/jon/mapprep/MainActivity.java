package jon.mapprep;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private MapView mapView;

    private LocationManager manager;
    private LocationListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        manager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        setupListener();
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION} , 1);
        }else{
            manager.requestLocationUpdates("gps", 2000,10, listener);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        System.out.println("onMapReady called");
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.setOnMapLongClickListener(latLng -> {
            Log.i("location","you clicked long on " + latLng.latitude + " " + latLng.longitude);
            moveCamera(latLng.latitude, latLng.longitude);
        });
        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(55.7, 12.5);
//        map.addMarker(new MarkerOptions().position(sydney).title("Marker in Copenhagen"));
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10));
    }

    private void setupListener() {
        listener = location -> {
            Log.i("location2", "new location: " + location.toString());
            moveCamera(location.getLatitude(), location.getLongitude());
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1){
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000,10,listener);
            }
        }
    }

    private void moveCamera(double lat, double lon ) {
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lon);
        map.clear();
        map.addMarker(new MarkerOptions().position(sydney).title("Marker"));
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        map.animateCamera(CameraUpdateFactory.zoomTo(7));
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}