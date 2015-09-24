package users;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trond.main.R;

import java.util.ArrayList;

public class Friends extends Activity {
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
        items.add(new User("TestPerson2","40","Female","Church St,Camperdown"));

       // itemsAdapter = new ArrayAdapter<String>(this,android.R.layout.activity_list_item,items);
        itemsAdapter = new CustomListAdapter(this,items);

        listview.setAdapter(itemsAdapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                    User updateItem = (User) itemsAdapter.getItem(position);

                    Log.i("Friends", "Clicked item " + position + ": " + updateItem);
                    Intent intent = new Intent(Friends.this, EditItem.class);
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



    public CustomListAdapter getFriendList(){
        //Unchecked
        return itemsAdapter;
    }

    public ArrayList<User> getFriendListUsers(){
        //Unchecked
        return items;
    }

    public String showUsers(){
        String userList = "";
        for(User user : items){
            userList += user.toString() + " \n";
        }
        return userList;
    }
}
