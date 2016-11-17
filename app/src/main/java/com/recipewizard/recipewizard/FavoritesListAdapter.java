package com.recipewizard.recipewizard;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.Log;

import org.w3c.dom.Text;

public class FavoritesListAdapter extends BaseAdapter {

    private final List<Recipe> mItems = new ArrayList<Recipe>();
    private final Context mContext;

    private static final String TAG = "Lab-UserInterface";

    public FavoritesListAdapter(Context context) {

        mContext = context;

    }

    // Add a Badge to the adapter
    // Notify observers that the data set has changed

    public void add(Recipe recipe) {

        mItems.add(recipe);
        notifyDataSetChanged();

    }

    // Clears the list adapter of all items.

    public void clear() {

        mItems.clear();
        notifyDataSetChanged();

    }

    // Returns the number of ToDoItems

    @Override
    public int getCount() {

        return mItems.size();

    }

    // Retrieve the number of ToDoItems

    @Override
    public Object getItem(int pos) {

        return mItems.get(pos);

    }

    // Get the ID for the Badge
    // In this case it's just the position

    @Override
    public long getItemId(int pos) {

        return pos;

    }
    /*
    public boolean contains(String place, String country){
        Badge b;
        for(int i = 0; i < getCount(); i++){
            b = (Badge) getItem(i);
            if((b.getPlace().compareTo(place) == 0) && (b.getCountry().compareTo(country) == 0)){
                return true;
            }
        }
        return false;
    }
    */

    // Create a View for the Badge at specified position
    // Remember to check whether convertView holds an already allocated View
    // before created a new View.
    // Consider using the ViewHolder pattern to make scrolling more efficient
    // See: http://developer.android.com/training/improving-layouts/smooth-scrolling.html

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO - Get the current Badge
        final Recipe recipe = (Recipe) getItem(position);


        // TODO - Inflate the View for this Badge
        // from badge_item.xmll
        RelativeLayout itemLayout = null;
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if((position %2) == 0) {
            itemLayout = (RelativeLayout) layoutInflater.inflate(R.layout.favorites_tab_item_right, null);
        } else{
            itemLayout = (RelativeLayout) layoutInflater.inflate(R.layout.favorites_tab_item_left, null);
        }
        // Fill in specific Badge data
        // Remember that the data that goes in this View
        // corresponds to the user interface elements defined
        // in the layout file

        final TextView nameView = (TextView) itemLayout.findViewById(R.id.nameView);
        nameView.setText(recipe.getName());



        // TODO - Must also set up an OnCheckedChangeListener,
        // which is called when the user toggles the status checkbox

		/*statusView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

				if(statusView.isChecked()){
					badge.setStatus(Badge.Status.DONE);
				} else{
					badge.setStatus(Badge.Status.NOTDONE);
				}

                        
                        
                        
					}
				});*/

        // TODO - Display Priority in a TextView

        final ImageView imageView = (ImageView) itemLayout.findViewById(R.id.foodPictureView);
        imageView.setImageBitmap(recipe.getPicture());


        // TODO - Display Time and Date.
        // Hint - use Badge.FORMAT.format(badge.getDate()) to get date and
        // time String

        // Return the View you just created
        Log.d(TAG, "returning new badge view");
        return itemLayout;

    }
}
