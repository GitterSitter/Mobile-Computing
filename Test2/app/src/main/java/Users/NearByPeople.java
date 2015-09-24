package Users;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trond.test.R;

import java.util.ArrayList;

public class NearByPeople extends Activity {
    public final int EDIT_ITEM_REQUEST_CODE = 647;
    ListView listview;
    ArrayList<User> items;
    CustomListAdapter itemsAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.listview);
        items = new ArrayList<User>();
        items.add(new User("TestPerson1","30","Male","USYD"));

       // itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.activity_list_item,items);
        itemsAdapter = new CustomListAdapter(this,items);
        listview.setAdapter(itemsAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    User updateItem = (User) itemsAdapter.getItem(position);
                    Log.i("Friends", "Clicked item " + position + ": " + updateItem);
                    Intent intent = new Intent(NearByPeople.this, EditItem.class);
                    if (intent != null) {
                        // put "extras" into the bundle for access in the edit activity
                        intent.putExtra("item", updateItem.toString());
                        intent.putExtra("position", position);
                        // brings up the second activity
                        startActivityForResult(intent, EDIT_ITEM_REQUEST_CODE);
                        itemsAdapter.notifyDataSetChanged();
                    }


            }
        });

        setupListViewListener();

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_ITEM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Extract name value from result extras
                String editedItem = data.getExtras().getString("item");
                int position = data.getIntExtra("position", -1);
                items.set(position, new User("test",editedItem,"",""));
                Log.i("Updated Item in list:", editedItem + ",position:"
                        + position);
                Toast.makeText(this, "updated:" + editedItem, Toast.LENGTH_SHORT).show();
                itemsAdapter.notifyDataSetChanged();
            }
        }
    }


    private void setupListViewListener() {
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId) {
                Log.i("Friends", "Long Clicked item " + position);

                AlertDialog.Builder builder = new AlertDialog.Builder(NearByPeople.this);
                builder.setTitle("Delete")
                        .setMessage("Do you want to delete " + itemsAdapter.getItem(position).toString().replaceAll("\\d+.*","") + "? ")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                items.remove(position);
                                itemsAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                             //   dialog.dismiss();
                            }
                        });
                builder.create().show();
                return true;
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
}
