package Users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.trond.test.R;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<User> {



    public CustomListAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // Lookup view for data population
        TextView name = (TextView) convertView.findViewById(R.id.userName);
        TextView age = (TextView) convertView.findViewById(R.id.userAge);
        TextView sex = (TextView) convertView.findViewById(R.id.userSex);
        TextView location = (TextView) convertView.findViewById(R.id.userLocation);
        // Populate the data into the template view using the data object
        name.setText(user.name);
        age.setText(user.age);
        sex.setText(user.sex);
        location.setText(user.location);
        // Return the completed view to render on screen
        return convertView;
    }
}