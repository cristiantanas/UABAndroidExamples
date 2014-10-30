package com.example.uilistviewcustomadapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Custom adapter extending BaseAdapter.
 * 
 * Contains a list of UserData objects.
 *
 */
public class UserListCustomAdapter extends BaseAdapter {
	
	// Application context
	Context				context;
	
	// List of users that will be rendered in the ListView
	List<UserData>		userList;

	// Set the context and user list from the constructor
	public UserListCustomAdapter(Context context, List<UserData> userList) {
		this.context = context;
		this.userList = userList;
	}
	
	// AdapterViews call this method to know how many objects are to be displayed
	@Override
	public int getCount() {
		return userList.size();
	}

	// AdapterViews call this method to retrieve an object at the specified position
	@Override
	public Object getItem(int position) {
		return userList.get(position);
	}

	// AdapterViews call this method to get the object's ID at the specified position
	@Override
	public long getItemId(int position) {
		return position;
	}

	// AdapterViews call this method to retrieve the row View that they must 
	// display for the object at the specified position
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// The View corresponding to the ListView's row layout
		View currentView = convertView;
		
		// If the convertView element is not null, the ListView is asking to 
		// recycle the existing View, so there is no need to create the View; just update its content
		if ( currentView == null ) {
			LayoutInflater inflater = (LayoutInflater) context.
					getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			currentView = inflater.inflate(R.layout.list_item, parent, false);
		}
		
		UserData userData = userList.get(position);
		((TextView) currentView.findViewById(R.id.itemUserName)).setText(userData.getName());
		((TextView) currentView.findViewById(R.id.itemUserBirthday)).setText(userData.getBirthday());
		((ImageView) currentView.findViewById(R.id.itemIcon)).setImageResource(userData.getPicture());
		
		return currentView;
	}

}
