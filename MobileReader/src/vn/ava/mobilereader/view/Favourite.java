package vn.ava.mobilereader.view;

import java.util.ArrayList;
import java.util.List;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.model.CategoriesItem;
import vn.ava.mobilereader.myadapter.ListViewCategoriesDetailAdapter;
import vn.ava.mobilereader.util.SharePreference;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class Favourite extends Fragment {

	private ListView listView;
	private SharePreference share;
	private ListViewCategoriesDetailAdapter adapter;
	private List<vn.ava.mobilereader.model.CategoriesItem> listCategories;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.favourites, null);
		initial(view);

		listCategories = new ArrayList<CategoriesItem>();

		try {
			
			listCategories = share.getFavourite(getActivity());

			adapter = new ListViewCategoriesDetailAdapter(getActivity(),
					listCategories);
			listView.setAdapter(adapter);
			
		} catch (NullPointerException e) {
			
			Toast.makeText(getActivity(), "Empty", Toast.LENGTH_LONG).show();
		}

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent intent = new Intent(getActivity(), ViewContent.class);

				intent.putExtra("url", listCategories.get(position).getUrl());
				intent.putExtra("title", listCategories.get(position)
						.getTitle());
				intent.putExtra("descript", listCategories.get(position)
						.getDescript());
				intent.putExtra("publish", listCategories.get(position)
						.getPublish());
				intent.putExtra("image", listCategories.get(position)
						.getImage());
				intent.putExtra("id", listCategories.get(position).getId());
				startActivity(intent);
			}
		});

		return view;
	}

	private void initial(View view) {

		listView = (ListView) view.findViewById(R.id.listPreferences);
		share = new SharePreference();
		listCategories = new ArrayList<CategoriesItem>();
	}

}
