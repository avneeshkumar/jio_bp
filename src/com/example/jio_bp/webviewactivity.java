package com.example.jio_bp;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;



public class webviewactivity extends Activity{
	private String redirect_dom = "http://www.ecell.in/jio_health";
	private String authcode;
	public static final String Code = ""; 
	public static final String MyPREFERENCES = "MyPrefs";
	public static final String apiname = "APIName"; 
	public static final String expires = "Expires"; 
	public static final String refreshtoken = "RefreshToken";
	public static final String accesstoken = "AccessToken";
	public static final String userid = "UserID";
	public static final String is_button_pressed = "is_Button_Pressed";
	SharedPreferences sharedpreferences;
	
	@Override
	public void onBackPressed() {
	    // Do Here what ever you want do on back press;
	   
  	 Intent intent = new Intent(getBaseContext(), MainActivity.class);
	 intent.putExtra("back_pressed",true);
	   
	 intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
     startActivity(intent);
  	   
  	 finish();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		String tempurl= getIntent().getExtras().getString("url");
		WebView webview = (WebView) findViewById(R.id.webView1);
    	webview.setWebViewClient(new WebViewClient());
    	webview.setWebViewClient(new MyBrowser());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl(tempurl);
	}
	
	private class MyBrowser extends WebViewClient {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    	  boolean shouldOverride = false;  
	    	    if (url.startsWith(redirect_dom)) { //if there is the domain name in the url where we get the code we do the following  
	    	      // DO SOMETHING
	    	   //System.out.println("webview activity ke end me hu");
	    	   authcode=url.replace(redirect_dom.concat("?code="),"");//getting the code=*** here
	    	   Editor editor = sharedpreferences.edit();
	    	   editor.putString(Code, authcode);
	    	   
	    	   
	    	   editor.commit();
	    	   
	    	   //getaccesstoken();
	    	  
	    	   //Intent intent = getIntent();
	    	   Intent intent = new Intent(getBaseContext(), MainActivity.class);
	    	   intent.putExtra("authcode",authcode);
	    	   
	    	   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
               startActivity(intent);
	    	   
               //startActivity(intent);
	    	   //setResult(RESULT_OK, intent); 
	    	   finish();
	    	   shouldOverride = true;  
	    	}  
	    	    //System.out.println("mai baki url load ker raha hu");
	    	    view.loadUrl(url);
	    	    return shouldOverride;  
	    	    
	         
	        
	      }
	   }
	
	

}
