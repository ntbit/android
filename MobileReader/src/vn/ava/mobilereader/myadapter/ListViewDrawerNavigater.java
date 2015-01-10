package vn.ava.mobilereader.myadapter;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.Instant.IMobilereader;
import vn.ava.mobilereader.model.ListDrawerItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewDrawerNavigater extends ArrayAdapter<ListDrawerItem> implements
		IMobilereader {

	private final int LAYOUT_ONE = 0;
	private final int LAYOUT_TWO = 1;
	
	private ListDrawerItem[] listItem;
	
	private Context context;

	public ListViewDrawerNavigater(Context context, int resource,
			ListDrawerItem[] listItem) {
		
		super(context, resource, listItem);
		
		this.listItem = listItem;
	}



	@Override
	public int getItemViewType(int position) {

		return listItem[position].getType();
		
	}

	@Override
	public int getViewTypeCount() {

		return 2;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {

		int type = getItemViewType(position);

		
		if (view == null) {
			
			if (type == LAYOUT_ONE) {
				
				view = LayoutInflater.from(getContext()).inflate(R.layout.custom_listview_drawernavigater,
						null);
				
				ImageView imageIcon = (ImageView) view
						.findViewById(R.id.imageViewIconNav);
				TextView textViewTitle = (TextView) view
						.findViewById(R.id.textViewTitleNav);

				imageIcon.setImageResource(listItem[position].getIcon());
				textViewTitle.setText(listItem[position].getText());
			}
			else if(type == LAYOUT_TWO){
				
				view = LayoutInflater.from(getContext()).inflate(R.layout.socical,
						null);
				TextView textViewTitle = (TextView) view
						.findViewById(R.id.tvSocical);
				textViewTitle.setText(listItem[position].getText());
				
				
			}
		}
		

		return view;
	}

	@Override
	public AlertDialog showDialog(String text) {
		// TODO Auto-generated method stub
		return null;
	}
}
