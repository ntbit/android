package vn.ava.mobilereader.myadapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.appcontroller.AppControllerVolley;
import vn.ava.mobilereader.model.CategoriesItem;
import vn.ava.mobilereader.util.SharePreference;
import vn.ava.mobilereader.view.ViewContent;
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

public class ListViewCategoriesDetailAdapter extends BaseAdapter {

	private Context context;
	private List<CategoriesItem> listCategories;
	private List<CategoriesItem> listCloneCategories;
	private ImageLoader imageLoader;
	private SharePreference share;

	public ListViewCategoriesDetailAdapter(Context context,
			List<CategoriesItem> list) throws NullPointerException {

		this.context = context;
		this.listCategories = list;
		listCloneCategories = new ArrayList<CategoriesItem>();
		this.listCloneCategories.addAll(listCategories);
		share = new SharePreference();

	}

	public int getCount() {

		return listCategories.size();
	}

	@Override
	public Object getItem(int posititon) {
		return listCategories.get(posititon);
	}

	@Override
	public long getItemId(int posititon) {
		return posititon;
	}

	@Override
	public View getView(final int position, View view, ViewGroup arg2) {

		imageLoader = AppControllerVolley.getInstance().getImageLoader();
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (view == null) {
			view = inflater.inflate(R.layout.custom_categories_detail_activity,
					null);
		}
		NetworkImageView image = (NetworkImageView) view
				.findViewById(R.id.imageViewIconTopic);
		TextView textTitle = (TextView) view
				.findViewById(R.id.textViewNameTopic);
		TextView textContent = (TextView) view
				.findViewById(R.id.textViewContentSnipTopic);
		TextView textPublish = (TextView) view
				.findViewById(R.id.textViewPublishTopic);

		image.setImageUrl(listCategories.get(position).getImage(), imageLoader);
		textTitle.setSelected(true);
		textTitle.setText(listCategories.get(position).getTitle());
		textContent.setText(listCategories.get(position).getDescript());
		textPublish.setText(listCategories.get(position).getPublish());
		notifyDataSetChanged();

		textContent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				sendIntent(position);
			}
		});

		return view;
	}

	private void sendIntent(int position) {

		Intent intent = new Intent(context, ViewContent.class);

		intent.putExtra("url", listCategories.get(position).getUrl());
		intent.putExtra("title", listCategories.get(position).getTitle());
		intent.putExtra("descript", listCategories.get(position).getDescript());
		intent.putExtra("publish", listCategories.get(position).getPublish());
		intent.putExtra("image", listCategories.get(position).getImage());
		intent.putExtra("id", listCategories.get(position).getId());
		context.startActivity(intent);
	}

	public void searchFilter(String text) {

		listCategories.clear();

		if (text.length() == 0) {

			listCategories.addAll(listCloneCategories);
			notifyDataSetChanged();

		} else {

			for (CategoriesItem item : listCloneCategories) {

				if (item.getTitle().toLowerCase(Locale.getDefault())
						.contains(text)) {

					listCategories.add(item);
				}
			}
			notifyDataSetChanged();
		}
	}

	public boolean checkFavoriteItem(CategoriesItem checkProduct) {
		
		boolean check = false;
		List<CategoriesItem> favorites = share.getFavourite(context);
		
		if (favorites != null) {
			
			for (CategoriesItem cate : favorites) {
				
				if (cate.equals(checkProduct)) {
					check = true;
					break;
				}
			}
		}
		return check;
	}

}
