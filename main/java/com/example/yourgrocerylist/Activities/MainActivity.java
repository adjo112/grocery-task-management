package com.example.yourgrocerylist.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.example.yourgrocerylist.Data.DataBaseHandler;
import com.example.yourgrocerylist.Model.Grocery;
import com.example.yourgrocerylist.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder dialogbuilder;
    private AlertDialog dialog;
    private EditText groceryitem;
    private EditText quantity;
    private Button savebutton;
    private DataBaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DataBaseHandler(this);
        byPassActivity();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPopupDialog();
            //            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
              //          .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void createPopupDialog(){
               dialogbuilder = new AlertDialog.Builder(this);
               View view = getLayoutInflater().inflate(R.layout.popup,null);
               groceryitem = view.findViewById(R.id.groceryitem);
               quantity    =  view.findViewById(R.id.groceryQty);
               savebutton = view.findViewById(R.id.savebutton);
               dialogbuilder.setView(view);
               dialog=dialogbuilder.create();
               dialog.show();
               savebutton.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                               //save to DB
                               //go to next screen
                       if (!groceryitem.getText().toString().isEmpty()
                               && !quantity.getText().toString().isEmpty()) {
                           saveGrocerytoDB(view);
                       }
                   }
               });
    }

    private void saveGrocerytoDB(View view) {
        Grocery grocery = new Grocery();
        String newGrocery = groceryitem.getText().toString();
        String newGroceryQuantity = quantity.getText().toString();

        grocery.setName(newGrocery);
        grocery.setQuantity(newGroceryQuantity);

        //Save to DB
        db.addGrocery(grocery);
        Snackbar.make(view, "Item Saved!", Snackbar.LENGTH_LONG).show();
        // Log.d("Item Added ID:", String.valueOf(db.getGroceriesCount()));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {  //after 1.2 sec run() method is called
                dialog.dismiss();
                //start a new activity
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        }, 1200); //  1 second.
    }
    public void byPassActivity() {
        //Checks if database is empty; if not, then we just
        //go to ListActivity and show all added items

        if (db.getGroceriescount() > 0) {
            startActivity(new Intent(MainActivity.this, ListActivity.class));
            finish();
        }

    }
}
