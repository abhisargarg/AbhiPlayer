package com.example.abhiplayer;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {

	
	EditText username, password;
	Button b;
	SQLiteDatabase db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		username = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		b = (Button) findViewById(R.id.button1);
		
		db = openOrCreateDatabase("abhidatabase", Context.MODE_PRIVATE, null);
		
		try {db.execSQL("create table userdetails (name varchar(20) , uname varchar(20) , pass varchar (20), utype varchar(10), gender varchar(10));"); }
		
		catch(Exception e) {
			
			Toast.makeText(this, "Table Already Exists!", Toast.LENGTH_SHORT).show();
		}
		
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String un = username.getText().toString();
				String ps = password.getText().toString();
				
				Cursor c = db.rawQuery("select utype from userdetails where uname = '" + un + "' and pass = '" + ps + "'", null);
				
				if(c.moveToNext()) {
					
					String type = c.getString(0);
					Intent a = new Intent(MainActivity.this, Music.class);
					startActivity(a);
					
					
				}
				
				else {
					
					Toast.makeText(MainActivity.this, "Invalid Username or Password.", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}

	
	
	public void register(View v) {
		
		Intent i = new Intent (this, Registration.class);
	startActivity(i); 
		
		
		
	} 
}
