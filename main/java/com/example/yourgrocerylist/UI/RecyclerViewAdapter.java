package com.example.yourgrocerylist.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.yourgrocerylist.Data.DataBaseHandler;
import com.example.yourgrocerylist.Model.Grocery;
import com.example.yourgrocerylist.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Grocery> groceryItems;
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Grocery> groceryItems) {
        this.context = context;
        this.groceryItems = groceryItems;         //customized grocery objects
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())   //ViewGroup is a base class for all layouts and holds views
                    .inflate(R.layout.list_row, parent, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {

        Grocery grocery = groceryItems.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateitemadded());

    }

    @Override
    public int getItemCount() {
        return groceryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;


        public ViewHolder(View view, Context ctx) {
            super(view);

            context = ctx;

            groceryItemName =  view.findViewById(R.id.name);
            quantity =  view.findViewById(R.id.quantity);
            dateAdded =  view.findViewById(R.id.dateadded);

            editButton =  view.findViewById(R.id.editbutton);
            deleteButton = view.findViewById(R.id.deletebutton);

           editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

       /*     view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //go to next screen/ DetailsActivity
                    int position = getAdapterPosition();
                    Grocery grocery = groceryItems.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name", grocery.getName());
                    intent.putExtra("quantity", grocery.getQuantity());
                    intent.putExtra("id", grocery.getId());
                    intent.putExtra("date", grocery.getDateitemadded());
                    context.startActivity(intent);


                }
            });
            */
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.editbutton:
                    int position = getAdapterPosition();
                    Grocery grocery = groceryItems.get(position);
                    editItem(grocery);

                    break;
                case R.id.deletebutton:
                    position = getAdapterPosition();
                    grocery = groceryItems.get(position);
                    deleteItem(grocery.getId());

                    break;


            }
        }

        public void deleteItem(final int id) {

            //create an AlertDialog
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton =  view.findViewById(R.id.nobutton);
            Button yesButton =  view.findViewById(R.id.yesbutton);

            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();


            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete the item.
                    DataBaseHandler db = new DataBaseHandler(context);
                    //delete item
                    db.deleteGrocery(id);  //deletes from database
                    groceryItems.remove(getAdapterPosition());  //deletes from grocerylist
                    notifyItemRemoved(getAdapterPosition());  //item with that position is removed
                                                              //from recyclerview
                    dialog.dismiss();


                }
            });

        }


        public void editItem(final Grocery grocery) {

            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup, null);

            final EditText groceryItem =  view.findViewById(R.id.groceryitem);
            final EditText quantity =  view.findViewById(R.id.groceryQty);
            final TextView title =  view.findViewById(R.id.titlegroc);

            title.setText("Edit Grocery");
            Button saveButton =  view.findViewById(R.id.savebutton);


            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DataBaseHandler db = new DataBaseHandler(context);

                    //Update item
                    grocery.setName(groceryItem.getText().toString());
                    grocery.setQuantity(quantity.getText().toString());

                    if (!groceryItem.getText().toString().isEmpty()
                            && !quantity.getText().toString().isEmpty()) {
                        db.updateGrocery(grocery);
                        notifyItemChanged(getAdapterPosition(),grocery);
                    }else {
                        Snackbar.make(v, "Add Grocery and Quantity", Snackbar.LENGTH_LONG).show();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                        }
                    }, 1200);
                  //  dialog.dismiss();

                }
            });

        }
    }

}
