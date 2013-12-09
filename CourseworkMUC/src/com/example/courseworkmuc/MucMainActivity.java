package com.example.courseworkmuc;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import android.location.Location;

import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import android.widget.Spinner;

import android.view.View.OnClickListener;

public class MucMainActivity extends Activity implements OnItemSelectedListener
{
	private GoogleMap mMap;
	Spinner mapTypeSpinner;	
	String[] maptypes = {"Normal", "Terrain", "Satelite"};		
	private int userIcon, scotstounI, ibroxI, seccI, kelvingroveI, hockeyI, velodromeI, celticI, tollcrossI, hampdenI, cathkinI,
				strathclydeI, edinburghI, dundeeI;
	private LocationManager locMan;
	private Marker userMarker, scotstounM, ibroxM, seccM, kelvingroveM, hockeyM, velodromeM, celticM, tollcrossM, hampdenM, cathkinM,
			       strathclydeM, edinburghM, dundeeM;
	private CheckBox chUserLocOnOFF;
	
	double defaultLat = 55.85432829452839;
	double defaultLng = -4.268357989501965;

    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muc_main);
        
        //get drawable IDs
      	userIcon = R.drawable.yellow_point;      	
      	scotstounI = R.drawable.squash; 
      	ibroxI = R.drawable.rugby;
      	seccI = R.drawable.boxing;
      	kelvingroveI = R.drawable.bowls;
      	hockeyI = R.drawable.hockey;
      	velodromeI = R.drawable.athetics; 
      	celticI = R.drawable.party;   	 
      	tollcrossI = R.drawable.aqua;     	
      	hampdenI = R.drawable.athetics;      	 
      	cathkinI = R.drawable.cycleing;
      	strathclydeI  = R.drawable.tri; 	 
    	edinburghI  = R.drawable.athetics;  	 
    	dundeeI = R.drawable.shooting;    	
      	
      	
        //ArrayAdapter <String> adapter = new ArrayAdapter<String>(MucMainActivity.this, android.R.layout.simple_spinner_item, paths);
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(MucMainActivity.this, R.layout.spinner_layout, maptypes);
        mapTypeSpinner = (Spinner) findViewById(R.id.spinner1);
        mapTypeSpinner.setAdapter(adapter);       
        mapTypeSpinner.setOnItemSelectedListener(this);       

        //checks if the map has been instantiated or not if it hasn't then the map gets istantiated
        if(mMap == null)
        {
        	//passes the map fragment ID from the layout XML and casts it to a map fragment object
        	//and gets the google map object.
        	mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        }
        
        if(mMap != null)
        {
        	defaultSetting();        	
        	addListenerOnChkIos();
        }
        
        
        //showUserLocation();          
        
    }


    //@Override
   // public boolean onCreateOptionsMenu(Menu menu) 
    //{
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.muc_main, menu);
        //return true;
    //}


	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		// TODO Auto-generated method stub
		int position = mapTypeSpinner.getSelectedItemPosition();
		switch(position)
		{
		case 0:
			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			
			break;
			
		case 1:
			mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			break;
			
		case 2:
			mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			break;
		
		}
	}


	@Override
	public void onNothingSelected(AdapterView<?> arg0) 
	{
		// TODO Auto-generated method stub
		
	}
	
	private void showUserLocation()
	{
		//get location manager
				locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
				//get last location
				Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				double lat = lastLoc.getLatitude();
				double lng = lastLoc.getLongitude();
				//create LatLng
				LatLng lastLatLng = new LatLng(lat, lng);

				//remove any existing marker
				if(userMarker!=null) userMarker.remove();
				//create and set marker properties
				userMarker = mMap.addMarker(new MarkerOptions()
				.position(lastLatLng)
				.title("You are here")
				.icon(BitmapDescriptorFactory.fromResource(userIcon))
				.snippet("Your last recorded location"));
				//move to location
				mMap.animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 1000, null);		

	}	
	
	public void defaultSetting()
	{
		
		//sets the map type
    	mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    	// sets the default location of the map to Glasgow
    	mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(defaultLat, defaultLng), 12.0f), null );
    	//mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(55.85432829452839, -4.268357989501965)), 3000, null);    	
    	//mMap.addMarker(new MarkerOptions().position(new LatLng(defaultLat,defaultLng)).title("hello"));
    	addMarkers();
	}
	
	public void addMarkers()
	{
		scotstounM = mMap.addMarker(new MarkerOptions().position(new LatLng(55.88069,-4.34025)).icon(BitmapDescriptorFactory.fromResource(scotstounI)));
		ibroxM = mMap.addMarker(new MarkerOptions().position(new LatLng(55.85360,-4.30454)).icon(BitmapDescriptorFactory.fromResource(ibroxI)));
		seccM = mMap.addMarker(new MarkerOptions().position(new LatLng(55.86070,-4.28761)).icon(BitmapDescriptorFactory.fromResource(seccI)));
		kelvingroveM = mMap.addMarker(new MarkerOptions().position(new LatLng(55.86782,-4.28875)).icon(BitmapDescriptorFactory.fromResource(kelvingroveI)));
		hockeyM = mMap.addMarker(new MarkerOptions().position(new LatLng(55.84496,-4.23671)).icon(BitmapDescriptorFactory.fromResource(hockeyI)));
		velodromeM = mMap.addMarker(new MarkerOptions().position(new LatLng(55.84496,-4.23671)).icon(BitmapDescriptorFactory.fromResource(velodromeI)));
		celticM = mMap.addMarker(new MarkerOptions().position(new LatLng(55.84959,-4.20555)).icon(BitmapDescriptorFactory.fromResource(celticI)));
		tollcrossM = mMap.addMarker(new MarkerOptions().position(new LatLng(55.84505,-4.17607)).icon(BitmapDescriptorFactory.fromResource(tollcrossI)));
		hampdenM = mMap.addMarker(new MarkerOptions().position(new LatLng(55.82570,-4.25239)).icon(BitmapDescriptorFactory.fromResource(hampdenI)));
		cathkinM = mMap.addMarker(new MarkerOptions().position(new LatLng(55.79550,-4.22329)).icon(BitmapDescriptorFactory.fromResource(cathkinI)));
		strathclydeM = mMap.addMarker(new MarkerOptions().position(new LatLng(55.78529,-4.01481)).icon(BitmapDescriptorFactory.fromResource(strathclydeI)));
		edinburghM = mMap.addMarker(new MarkerOptions().position(new LatLng(55.93920,-3.17273)).icon(BitmapDescriptorFactory.fromResource(edinburghI)));
		dundeeM = mMap.addMarker(new MarkerOptions().position(new LatLng(56.49302,-2.74663)).icon(BitmapDescriptorFactory.fromResource(dundeeI)));
		//dundeeM = mMap.addMarker(new MarkerOptions().position(new LatLng(56.49302,-2.74663)).title("DUNDEE"));		
	}
	
	public void addListenerOnChkIos() 
	{
		 
		chUserLocOnOFF = (CheckBox) findViewById(R.id.checkBox);
	 
		chUserLocOnOFF.setOnClickListener(new OnClickListener() 
		{ 
	 
		  @Override
		  public void onClick(View v) 
		  {
	                //is chkIos checked?
			if (((CheckBox) v).isChecked()) 
			{
				//Toast.makeText(MucMainActivity.this,
			 	   //"Bro, try Android :)", Toast.LENGTH_LONG).show();
				showUserLocation(); 
			}
			else
				defaultSetting();	 
		   }
		  });	 
	 }


	
	
}
