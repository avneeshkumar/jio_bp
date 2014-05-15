package com.example.jio_bp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	
	private String cliid = "bf434f9aaa5f4e289fe1fc32b611141f";
	private String clisecid = "c45f2b4042ca48e8a19d36900a2c338b";
	private String redirect_dom = "http://www.ecell.in/jio_health";
	private static String url = "https://api.ihealthlabs.com:8443/api/OAuthv2/userauthorization.ashx?";
	private String authcode;
	public static final String Code = ""; 
	public static final String MyPREFERENCES = "MyPrefs";
	public static final String apiname = "APIName"; 
	public static final String expires = "Expires"; 
	public static final String refreshtoken = "RefreshToken";
	public static final String accesstoken = "AccessToken";
	public static final String lastdataid = "DataId";
	public static final String userid = "UserID"; 
	public static final String is_button_pressed = "is_Button_Pressed";
	//public static final String Place = "placeKey"; 

	SharedPreferences sharedpreferences;
	
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		FirstTimePreference prefFirstTime = new FirstTimePreference(getApplicationContext());
		if (prefFirstTime.runTheFirstTime("myKey")) {
			Editor editor = sharedpreferences.edit();
		  	editor.putString(is_button_pressed,"0");
		  	editor.putString(lastdataid,"NULL");
		    editor.commit();//Committing all the above changes to the editor
		}
		
	  	
	   
		//System.out.print(getIntent().hasExtra(authcode));
		if(getIntent().hasExtra("authcode")){
			
			//System.out.println("auth code ghusa");
            authcode=getIntent().getStringExtra("authcode");
            getIntent().removeExtra("authcode");
            Editor editor = sharedpreferences.edit();
		  	editor.putString(is_button_pressed, getIntent().getStringExtra(is_button_pressed));		  	
		    editor.commit();//Committing all the above changes to the editor
		    final TextView textViewToChange = (TextView) findViewById(R.id.textView2);
		    textViewToChange.setText("Fetching data...Please wait");
			getaccesstoken();
			
			
			
		}
		if(getIntent().hasExtra("back_pressed")){
			
			//System.out.println("back press me ghusa");
            
            getIntent().removeExtra("back_pressed");
           
		    final TextView textViewToChange = (TextView) findViewById(R.id.textView2);
		    textViewToChange.setText(R.string.nodata);
		    
			
			
			//getaccesstoken();
			
		}
		
		//handling the take bp button
		final Button takebpbutton = (Button) findViewById(R.id.takebp);
        takebpbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	
            	
            	if((Integer.parseInt(sharedpreferences.getString(is_button_pressed,"")))==1){
    				
    				AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
    				 
    		        // Setting Dialog Title
    		        alertDialog.setTitle("Not recorded previous data");
    		 
    		        // Setting Dialog Message
    		        alertDialog.setMessage("You have taken one reading which you have not submitted till now to our server. Do you want to proceed?");
    		 
    		        // Setting Icon to Dialog
    		       // alertDialog.setIcon(R.drawable.delete);
    		 
    		        // Setting Positive "Yes" Button
    		        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
    		            public void onClick(DialogInterface dialog,int which) {
    		 
    		            // Write your code here to invoke YES event
    		            //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
    		            
    		            dialog.cancel();
    		            Intent i;
    	            	PackageManager manager = getPackageManager();
    	            	try {
    	            	   i = manager.getLaunchIntentForPackage("androidNin1.Start");
    	            	if (i == null)
    	            	    throw new PackageManager.NameNotFoundException();
    	            	i.addCategory(Intent.CATEGORY_LAUNCHER);
    	            	Editor editor = sharedpreferences.edit();
    	    		  	editor.putString(is_button_pressed,"1");		  	
    	    		    editor.commit();//Committing all the above changes to the editor
    	            	startActivity(i);
    	            	} catch (PackageManager.NameNotFoundException e) {
    	            		
    	            		Toast.makeText(getApplicationContext(),"You dont have iHealth installed on your device",Toast.LENGTH_LONG).show();
    	            		

    	            	}
 
    		            }
    		        });
    		        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
    		            public void onClick(DialogInterface dialog, int which) {
    		            // Write your code here to invoke NO event
    		            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
    		            dialog.cancel();
    		           
    		            }
    		        });
    		 
    		        // Showing Alert Message
    		        alertDialog.show();
    		
            	}
            	else {
            		
            		Intent i;
	            	PackageManager manager = getPackageManager();
	            	try {
	            	   i = manager.getLaunchIntentForPackage("androidNin1.Start");
	            	if (i == null)
	            	    throw new PackageManager.NameNotFoundException();
	            	i.addCategory(Intent.CATEGORY_LAUNCHER);
	            	Editor editor = sharedpreferences.edit();
	    		  	editor.putString(is_button_pressed,"1");		  	
	    		    editor.commit();//Committing all the above changes to the editor
	            	startActivity(i);
	            	} catch (PackageManager.NameNotFoundException e) {
	            		
	            		Toast.makeText(getApplicationContext(),"You dont have iHealth installed on your device",Toast.LENGTH_LONG).show();
	            		

	            	}
            		
            		
            		
            	}
                
            	
            	
            	
            }
        });
        
        final Button getbpbutton = (Button) findViewById(R.id.getbp);
        getbpbutton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetJavaScriptEnabled")
			public void onClick(View v) {
            	
            	
            	final TextView textViewToChange = (TextView) findViewById(R.id.textView2);
    			//System.out.println(sharedpreferences.getString(is_button_pressed,""));
    			if((Integer.parseInt(sharedpreferences.getString(is_button_pressed,"")))==0){
    				
    				AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
    				 
    		        // Setting Dialog Title
    		        alertDialog.setTitle("BP NOT taken");
    		 
    		        // Setting Dialog Message
    		        alertDialog.setMessage("You have not taken the BP. You will get stale data. Are you sure you want to proceed?");
    		 
    		        // Setting Icon to Dialog
    		       // alertDialog.setIcon(R.drawable.delete);
    		 
    		        // Setting Positive "Yes" Button
    		        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
    		            public void onClick(DialogInterface dialog,int which) {
    		 
    		            // Write your code here to invoke YES event
    		            //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
    		            
    		            dialog.cancel();
    		            
    	    			textViewToChange.setText("Fetching data...Please wait");
    		            launch_view();
 
    		            }
    		        });
    		 
    		        // Setting Negative "NO" Button
    		        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
    		            public void onClick(DialogInterface dialog, int which) {
    		            // Write your code here to invoke NO event
    		            //Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
    		            dialog.cancel();
    		            //textViewToChange.setText(R.string.nodata);
    		            }
    		        });
    		 
    		        // Showing Alert Message
    		        alertDialog.show();
    				
    			}
    			else {
    				textViewToChange.setText("Fetching data...Please wait");
    				launch_view();
    			}
    			
            	
            	
            	
            	
            	
            }
        });

		
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private Boolean getaccesstoken() {
		
		
		
		AsyncHttpClient client = new AsyncHttpClient();//making a client for ajax get request
 	   String tempurl="https://api.ihealthlabs.com:8443/api/OAuthv2/userauthorization.ashx?";
 	   tempurl=tempurl.concat("code=").concat(authcode).concat("&client_id=").concat(cliid).concat("&client_secret=").concat(clisecid).concat("&grant_type=authorization_code&redirect_uri=").concat(redirect_dom).concat("&client_para=xxx");
 	   //System.out.println(tempurl);
   	    client.get(tempurl, new AsyncHttpResponseHandler() {
   	     @Override
   	     public void onSuccess(String response) {
   	      
   	      
   	         try {
				JSONObject reader = new JSONObject(response);
				
				if(reader.has("Error")){
			
					int code=reader.getInt("ErrorCode");
					if(code==4001){//yahan per refresh token call krna hai
						
						AsyncHttpClient client = new AsyncHttpClient();//making a client for ajax get request
					 	String tempurl="https://api.ihealthlabs.com:8443/api/OAuthv2/userauthorization.ashx?";
					 	tempurl=tempurl.concat("code=").concat(authcode).concat("&client_id=").concat(cliid).concat("&client_secret=").concat(clisecid).concat("&response_type=refresh_token&redirect_uri=").concat(redirect_dom).concat("&refresh_token=").concat(sharedpreferences.getString(refreshtoken, "")).concat("&client_para=xxx");
					 	
					 	client.get(tempurl, new AsyncHttpResponseHandler() {
					   	     @Override
					   	     public void onSuccess(String response) {
					   	    	
					   	    	try {
									JSONObject reader_temp = new JSONObject(response);
									String api_n = reader_temp.getString("APIName");
								  	String access_token = reader_temp.getString("AccessToken");
								  	int expire_on = Integer.parseInt(reader_temp.getString("Expires"));
								  	String refresh_token = reader_temp.getString("RefreshToken");
								  	//authcode = access_token;
								  	String user_id = reader_temp.getString("UserID");
								  	//putting all the values in shared preference
								  	Editor editor = sharedpreferences.edit();
								  	editor.putString(apiname, api_n);
								    editor.putString(accesstoken,access_token );
								    editor.putInt(expires, expire_on);
								    editor.putString(refreshtoken, refresh_token);
								    editor.putString(userid, user_id);
								    editor.commit();//commiting all the abovve changes to the editor
								    callbpdata();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					   	    	 
					   	     }
						
					 	});
					}else{
					
		                String tempurl;
		                tempurl = url.concat("client_id=").concat(cliid).concat("&response_type=code&redirect_uri=http://www.ecell.in/jio_health&APIName=OpenApiBP");
		                Intent intent = new Intent(getBaseContext(), webviewactivity.class);
		                intent.putExtra("url", tempurl);
		                intent.putExtra(is_button_pressed, sharedpreferences.getString(is_button_pressed,""));
		                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
		                finish();
	                    startActivity(intent);
	                    
	                    
					}
	            
				}
				else {
					
					
				  	
				  	String api_n = reader.getString("APIName");
				  	String access_token = reader.getString("AccessToken");
				  	int expire_on = Integer.parseInt(reader.getString("Expires"));
				  	String refresh_token = reader.getString("RefreshToken");
				  	
				  	String user_id = reader.getString("UserID");
				  	//putting all the values in shared preference
				  	Editor editor = sharedpreferences.edit();
				  	editor.putString(apiname, api_n);
				    editor.putString(accesstoken,access_token );
				    editor.putInt(expires, expire_on);
				    editor.putString(refreshtoken, refresh_token);
				    editor.putString(userid, user_id);
				    editor.commit();//commiting all the abovve changes to the editor
				    callbpdata();
				  	
				  	
					
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
   	         
   	         
   	     
   	         
   	     }
   	 });
   	    
   	 return true;
		
	}
	
	private Boolean launch_view() {
		
		
		
		//if(getIntent().hasExtra(name))
		
    	if (sharedpreferences.contains(Code))
        {
          // name.setText(sharedpreferences.getString(Code, ""));
    		authcode=sharedpreferences.getString(Code, "");
    		getaccesstoken();
    		
    		

        }
    	else {
    		Intent intent = new Intent(getBaseContext(), webviewactivity.class);
    		String tempurl;
            tempurl = url.concat("client_id=").concat(cliid).concat("&response_type=code&redirect_uri=http://www.ecell.in/jio_health&APIName=OpenApiBP");
             intent.putExtra("url", tempurl);
             startActivity(intent);
            
            
            
           
    	}
    	return true;
		
	}
	
	private Boolean callbpdata() {
		
		
		//System.out.println("bp data me aa raha hai");
		AsyncHttpClient client = new AsyncHttpClient();//making a client for ajax get request
		String user_id= sharedpreferences.getString(userid, "");
		String access_token=sharedpreferences.getString(accesstoken, "");
	    String tempurl="https://api.ihealthlabs.com:8443/openapiv2/user/".concat(user_id).concat("/bp.json/?");
	   tempurl=tempurl.concat("access_token=").concat(access_token).concat("&client_id=").concat(cliid).concat("&client_secret=").concat(clisecid).concat("&redirect_uri=").concat(redirect_dom).concat("&sc=5203db6603b74993b1e22073ae1e660a&sv=7b37940c8881463c8ef448ccd0dc7e5a");
	   
	  
	   client.get(tempurl, new AsyncHttpResponseHandler() {
	   @Override
	   public void onSuccess(String response) {
		   
		  
		
		   JSONObject reader_temp;
		   //System.out.println(response);
		try {
			reader_temp = new JSONObject(response);
			JSONArray api_n = reader_temp.getJSONArray("BPDataList");
			String str = api_n.getString(0);
			JSONObject reader_temp1;
			reader_temp1 = new JSONObject(str);
			if(reader_temp1.getString("DataID").equals(sharedpreferences.getString(lastdataid, ""))){
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
				 
		        // Setting Dialog Title
		        alertDialog.setTitle("BP NOT taken");
		 
		        // Setting Dialog Message
		        alertDialog.setMessage("You have not taken the BP. You will get stale data. Are you sure you want to proceed?");
		 
		        // Setting Icon to Dialog
		       // alertDialog.setIcon(R.drawable.delete);
		 
		        // Setting Positive "Yes" Button
		        alertDialog.setNeutralButton("ok", new DialogInterface.OnClickListener() {
		            public void onClick(DialogInterface dialog,int which) {
		 
		            // Write your code here to invoke YES event
		            //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
		            
		            dialog.cancel();
		            
	    			
		            }
		        });
		        
		        alertDialog.show();
				
				/*new AlertDialog.Builder(getApplicationContext())
			    .setTitle("Duplicate data")
			    .setMessage("The last data and the current data are the same.You may be getting stale data")
			    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // continue with delete
			        }
			     })
			    .setIcon(android.R.drawable.ic_dialog_alert)
			     .show();*/
			}
			String hr = reader_temp1.getString("HR");
			String hp = reader_temp1.getString("HP");
			String lp = reader_temp1.getString("LP");
			Editor editor = sharedpreferences.edit();
		  	editor.putString(lastdataid,reader_temp1.getString("DataID"));
		    editor.commit();//Committing all the above changes to the editor
		    
			String tobedisplayed = "HR value:".concat(hr).concat("\n\nHP Value:").concat(hp).concat("\n\nLP value:").concat(lp);
			
			final TextView textViewToChange = (TextView) findViewById(R.id.textView2);
			textViewToChange.setText(tobedisplayed);
			
			
			
			//System.out.println(str);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   
		   
		   
		   
	   }
		   
		   
	   	
		
	});
	   	
	   	Editor editor = sharedpreferences.edit();
	  	editor.putString(is_button_pressed, "0");
	    editor.commit();//Committing all the above changes to the editor
	  return true;
		
};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	


}
