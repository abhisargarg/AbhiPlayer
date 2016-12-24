package com.example.abhiplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Registration extends Activity {

	
	EditText name, uname, pass;
	Button b;
	RadioGroup usertype, gender;
	String utype, gendr;
	SQLiteDatabase db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.fadein, R.anim.fadeout);
		setContentView(R.layout.activity_registration);
		
		name = (EditText) findViewById(R.id.editText1);
		uname = (EditText) findViewById(R.id.editText2);
		pass = (EditText) findViewById(R.id.editText3);
		usertype = (RadioGroup) findViewById(R.id.radioGroup1);
		gender = (RadioGroup) findViewById(R.id.radioGroup2);
		b = (Button) findViewById(R.id.button1);
		
		usertype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				
				if (checkedId == R.id.radio0) {utype = "Admin";}
				else {utype = "Customer";}
			}
		});
		
		gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				
				if (checkedId == R.id.radio2) {gendr = "Male";}
				else {gendr = "Female";}
			}
		});
		
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String nm = name.getText().toString();
				String unm = uname.getText().toString();
				String ps = pass.getText().toString();
				
				db.execSQL("insert into userdetails values ('" + nm + "','" + unm + "','" + ps + "','" + utype + "','" + gendr + "');");
				
				Toast.makeText(Registration.this, "Record Inserted!", Toast.LENGTH_SHORT).show();
				
				
				Intent i = new Intent (Registration.this, MainActivity.class);
				startActivity(i);
				
			}
		});
		
		db = openOrCreateDatabase("abhidatabase", Context.MODE_PRIVATE, null);
		
		try {db.execSQL("create table userdetails (name varchar(20) , uname varchar(20) , pass varchar (20), utype varchar(10), gender varchar(10));"); }
		
		catch(Exception e) {
			
			Toast.makeText(this, "Table Already Exists!", Toast.LENGTH_SHORT).show();
		}
	
	
	}

	
}
