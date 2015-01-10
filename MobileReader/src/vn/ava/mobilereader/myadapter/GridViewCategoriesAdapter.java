package vn.ava.mobilereader.myadapter;

import java.util.ArrayList;
import java.util.List;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.appcontroller.AppControllerVolley;
import vn.ava.mobilereader.dao.DBAdapter;
import vn.ava.mobilereader.model.Categories;
import vn.ava.mobilereader.view.ListCategoriesItem;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class GridViewCategoriesAdapter extends BaseAdapter {

	private List<Categories> listCategories = new ArrayList<Categories>();

	private Context context;
	private ImageLoader imageLoader;
	private DBAdapter db;
	

	public GridViewCategoriesAdapter(Context context,
			List<Categories> listCategories) {

		this.context = context;
		this.listCategories = listCategories;
		imageLoader = AppControllerVolley.getInstance().getImageLoader();
		
		db = new DBAdapter(context);
	}

	@Override
	public int getCount() {
		return listCategories.size();
	}

	@Override
	public Object getItem(int position) {
		return listCategories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup viewGroup) {
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		if (view == null) {
			view = inflater.inflate(R.layout.custom_categories_activity, null);
		}
		
		NetworkImageView imageViewIcon = (NetworkImageView) view
				.findViewById(R.id.imageViewIconCategories);
		TextView textViewTitle = (TextView) view
				.findViewById(R.id.textViewCategories);
		
		imageViewIcon.setImageUrl(listCategories.get(position).getSrc(),
				imageLoader);
		textViewTitle.setText(listCategories.get(position).getTitle());
		
		imageViewIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				sendIntent(position);

			}
		});
		return view;
	}
	
	private void sendIntent(int position){
		
		Intent intent = new Intent(context, ListCategoriesItem.class);
		
		intent.putExtra("id", db.getIDCategories(listCategories.get(
				position).getSrc()) - 1);
		intent.putExtra("title", listCategories.get(position)
				.getTitle());
		intent.putExtra("url", listCategories.get(position).getUrl());
		context.startActivity(intent);
	}

}
