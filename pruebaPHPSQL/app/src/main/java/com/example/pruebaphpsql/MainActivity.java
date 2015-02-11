package com.example.pruebaphpsql;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends Activity {

	private ArrayList<Button> botones = new ArrayList<Button>(4);
	private TextView respuesta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		botones.add((Button)findViewById(R.id.topPlantas));
        botones.add((Button)findViewById(R.id.followers));
        botones.add((Button)findViewById(R.id.followed));
        botones.add((Button)findViewById(R.id.descubre));
		respuesta = (TextView)findViewById(R.id.respuesta);
		
		Log.d("uno", "Uno");

        for (Button boton : botones) {
            boton.setOnClickListener(new View.OnClickListener() {

<<<<<<< HEAD:pruebaPHPSQL/app/src/main/java/com/example/pruebaphpsql/MainActivity.java
                                         @Override
                                         public void onClick(View v) {
                                             // TODO Auto-generated method stub
                                             new Consulta(v.getId(), true, respuesta).execute("datos");
                                             Log.d("dos", "dos");
                                         }
                                     }
            );
        }
=======
		boton1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Consulta(1, false, respuesta).execute("datos");
				Log.d("dos", "dos");

			}
		}
				);

>>>>>>> origin/master:pruebaPHPSQL/src/com/example/pruebaphpsql/MainActivity.java
	}


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
