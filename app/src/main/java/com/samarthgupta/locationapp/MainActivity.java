package com.samarthgupta.locationapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.renderscript.Double2;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tv5, tv6, tv7, tv8;
    ImageView iv1;
    private String LAT;
    private String LONG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv5 = (TextView) findViewById(R.id.textView5);
        tv6 = (TextView) findViewById(R.id.textView6);
        tv7 = (TextView) findViewById(R.id.textView7);
        tv8 = (TextView) findViewById(R.id.textView8);

        iv1=  (ImageView)findViewById(R.id.imageView);

        //Business Logic
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener listener = new OurListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(), "Please enable permissions", Toast.LENGTH_LONG).show();
            return;
        }
        lm.requestLocationUpdates(lm.GPS_PROVIDER, 0, 0, listener);


    }

    private class OurListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            tv5.setText(Double.toString(location.getLatitude()));
            tv6.setText(Double.toString(location.getLongitude()));
            tv7.setText(Double.toString(location.getAltitude()));
            tv8.setText(Double.toString(location.getSpeed()));

            LAT = Double.toString(location.getLatitude());
            LONG = Double.toString(location.getLongitude());

            new GetMap().execute();

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

    private class GetMap extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {

            Bitmap image = null;

            try {
                URL mapurl = new URL("https://maps.googleapis.com/maps/api/staticmap?" + "center=" + LAT +"," + LONG+ "&zoom=16" + "&size=600x300"+"&maptype=roadmap" +
                        "&markers=color:blue%7Clabel:S%7C"+ LAT +","+LONG);

                InputStream stream = (InputStream)mapurl.openConnection().getContent();
                image = BitmapFactory.decodeStream(stream);


            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
            return image;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            iv1.setImageBitmap(bitmap);
        }
    }


}
