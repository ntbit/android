package vn.ava.mobilereader.view;

import java.util.List;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.dao.DBAdapter;
import vn.ava.mobilereader.myadapter.ListViewCategoriesDetailAdapter;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CategoriesDetail extends Activity {

	private ListView listContent;
	private ListViewCategoriesDetailAdapter adapter;
	private List<vn.ava.mobilereader.model.CategoriesItem> listCategoriesDetail;
	private DBAdapter db;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.categories_detail_activity);
		setupActionbar();
		db = new DBAdapter(this);
		intent = this.getIntent();

		listCategoriesDetail = db.getTypeCategoriesDetail(intent.getExtras().getInt(
				"id"),5);

		listContent = (ListView) findViewById(R.id.listViewCategories);

		adapter = new ListViewCategoriesDetailAdapter(this,
				listCategoriesDetail);

		listContent.setAdapter(adapter);
		listContent.setOnItemClickListener(listViewClick);
		

	}

	private void setupActionbar() {

		ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);

		View customView = getLayoutInflater().inflate(
				R.layout.custom_actionbar, null);

		TextView textTitle = (TextView)customView.findViewById(R.id.textViewName);
		intent = this.getIntent();
		textTitle.setText(intent.getExtras().getString("title"));
		View buttonFavious = customView
				.findViewById(R.id.imageButtonIconFavious);
		LinearLayout layout = (LinearLayout) customView
				.findViewById(R.id.linear);
		layout.removeView(buttonFavious);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		actionBar.setCustomView(customView, params);

	}

	OnItemClickListener listViewClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intent = new Intent(getApplicationContext(), ViewContent.class);
			intent.putExtra("url", listCategoriesDetail.get(position).getUrl());
			intent.putExtra("title", listCategoriesDetail.get(position).getTitle());
			startActivity(intent);
			
		}
	};

}
