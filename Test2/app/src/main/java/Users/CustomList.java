package Users;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.trond.test.R;

import java.util.ArrayList;


public class CustomList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);
        populateUsersList();
    }

    private void populateUsersList() {
        // Construct the data source
        //From Database
        ArrayList<User> arrayOfUsers = null; //= User.getUsers();
        // Create the adapter to convert the array to views
        CustomListAdapter adapter = new CustomListAdapter(this, arrayOfUsers);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvUsers);
        listView.setAdapter(adapter);
    }

}