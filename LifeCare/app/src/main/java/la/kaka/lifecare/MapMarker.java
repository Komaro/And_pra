package la.kaka.lifecare;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import la.kaka.lifecare.DB.DB_Helper;

public class MapMarker extends FragmentActivity implements OnMapReadyCallback {

    // DB
    DB_Helper helper;
    SQLiteDatabase db;
    Cursor cursor;

    //Revision Value
    private static final double revision_value = 0.00001;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_marker);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        helper = new DB_Helper(this);
        db = helper.getReadableDatabase();

        // Date Format
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        //Revision
        double before_lati;
        double before_lon;

        try {
            cursor = db.rawQuery("SELECT * FROM Marker WHERE date = '" + sdf.format(date) + "'", null);

            cursor.moveToNext();

            mMap.addMarker(new MarkerOptions().position(new LatLng(cursor.getDouble(3), cursor.getDouble(2))));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(cursor.getDouble(3), cursor.getDouble(2)), 16));

            PolylineOptions line_set = new PolylineOptions();

            before_lati = cursor.getDouble(3);
            before_lon = cursor.getDouble(2);

            while(cursor.moveToNext())
            {
                //mMap.addMarker(new MarkerOptions().position(new LatLng(cursor.getDouble(3), cursor.getDouble(2))));

                if(Math.abs(cursor.getDouble(3) - before_lati) <= revision_value && Math.abs(cursor.getDouble(2) - before_lon) <= revision_value)
                {
                    line_set.add(new LatLng(cursor.getDouble(3), cursor.getDouble(2)));
                    Log.i("marker_msg", "MARKER : " + cursor.getDouble(3) + " " + cursor.getDouble(2));
                }

                before_lati = cursor.getDouble(3);
                before_lon = cursor.getDouble(2);
            }

            cursor.moveToLast();
            mMap.addMarker(new MarkerOptions().position(new LatLng(cursor.getDouble(3), cursor.getDouble(2))));

            mMap.addPolyline(line_set);
        }
        catch(SQLiteException ex)
        {
            Log.i("marker_msg", "MARKER : " + ex);
        }
    }
}
