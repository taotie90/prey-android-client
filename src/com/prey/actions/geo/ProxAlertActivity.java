package com.prey.actions.geo;

 

import android.os.Bundle;
import android.widget.Toast;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
 
 
 
import android.app.PendingIntent;
 
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
 
 
import android.location.LocationManager;
 
 
 

public class ProxAlertActivity extends Activity {


 
	    private static final long PROX_ALERT_EXPIRATION = -1;
	 
 
	    private static final String PROX_ALERT_INTENT ="com.prey.actions.geo.ProximityAlert";
 
	         ;
 
	     
	    private LocationManager locationManager;
	     
 
	     
 
	    
 
	    
	    private int pending_intent_unique_id=1;
	    
	    public static final int START=1;
	    public static final int STOP=2;
	    
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	         
	        super.onCreate(savedInstanceState);
	        
	        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	        
	        Bundle bundle = this.getIntent().getExtras();
			if (bundle != null) {
				
				int type=  bundle.getInt("type");
				if (type==START){
					double longitude =  bundle.getDouble("longitude") ;
					double latitude = bundle.getDouble("latitude") ;
					float radius=  bundle.getFloat("radius") ;
					start(latitude,longitude,radius);
				}
				if (type==STOP){
					stop();
				}
				
			}
			
	        
	          
	        
	    }
	     
 
	 
	    private void start( double longitude, double latitude,float radius ) {
	         
	        Intent intent = new Intent(PROX_ALERT_INTENT);
	      //  intent.putExtra("LOCATION_TITLE", "punto1");
	        PendingIntent proximityIntent = PendingIntent.getBroadcast(this, pending_intent_unique_id, intent, 0);
	         
	        locationManager.addProximityAlert(
	            latitude, // the latitude of the central point of the alert region
	            longitude, // the longitude of the central point of the alert region
	            radius, // the radius of the central point of the alert region, in meters
	            PROX_ALERT_EXPIRATION, // time for this proximity alert, in milliseconds, or -1 to indicate no expiration
	            proximityIntent // will be used to generate an Intent to fire when entry to or exit from the alert region is detected
	       );
	         
	       IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT); 
	       ProximityIntentReceiver receiver= new ProximityIntentReceiver();
	       registerReceiver(receiver, filter);
	        
		     
	       createNotification("Start");
			
	    }
	  
	    private static final int NOTIFICATION_ID = 800;
	    
	    private void stop(   ) {
	         
	    	try{
	    		
	    		 createNotification("Stop");
	    		 
	    		 Intent anIntent = new Intent(PROX_ALERT_INTENT);
	            PendingIntent operation = 
	                PendingIntent.getBroadcast(this, pending_intent_unique_id , anIntent, 0);
	            locationManager.removeProximityAlert(operation);
	            
	            
	    	}catch(Exception e){
	    		Toast.makeText(this, "Errro, causa:"+e.getMessage(),
	                    Toast.LENGTH_LONG).show();
	    	}
	        
	    }
 

 

	private void createNotification(String event) {
		
		NotificationManager notificationManager = (NotificationManager) getApplication().getSystemService(Context.NOTIFICATION_SERVICE);

		PendingIntent pendingIntent = PendingIntent.getActivity(getApplication(), 0, null, 0);

		Notification notification = new Notification();
		notification.when = System.currentTimeMillis();

		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;

		notification.defaults |= Notification.DEFAULT_VIBRATE;
		notification.defaults |= Notification.DEFAULT_LIGHTS;

		notification.ledARGB = Color.WHITE;
		notification.ledOnMS = 1500;
		notification.ledOffMS = 1500;
		
		notification.setLatestEventInfo(getApplication(), "Proximity Alert!", event, pendingIntent);

		notificationManager.notify(NOTIFICATION_ID, notification);
		

	}
	     
 
   
}