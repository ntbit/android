package vn.ava.mobilereader.myadapter;

import java.util.ArrayList;
import java.util.List;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.Instant.IMobilereader;
import vn.ava.mobilereader.dao.DBAdapter;
import vn.ava.mobilereader.model.Categories;
import vn.ava.mobilereader.model.CategoriesItem;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

public class TabPagerAdapter extends PagerAdapter implements IMobilereader {

	private List<Categories> listCategories = null;
	private View view = null;
	private GridViewCategoriesAdapter adapter = null;
	private final Context context;
	private DBAdapter db;
	private ListView listArchive;
	private List<CategoriesItem> listArticle;
	private ListViewCategoriesDetailAdapter listAdapter;
	private final int TYPE_BOOKS = 30;
	private final int TYPE_CODING = 31;
	private final int MAX_LIMIT = 100;

	public TabPagerAdapter(Context context) {

		this.context = context;

	}

	@Override
	public String getPageTitle(int position) {
		return TITLE[position];
	}

	@Override
	public int getCount() {
		return TITLE.length;
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {

		switch (position) {
		case 6:
			view = LayoutInflater.from(context).inflate(R.layout.list_article,
					null);
			listArchive = (ListView) view.findViewById(R.id.listArticle);
			new SelectData(context, TYPE_BOOKS).execute();

			break;

		case 7:
			view = LayoutInflater.from(context).inflate(R.layout.list_article,
					null);
			listArchive = (ListView) view.findViewById(R.id.listArticle);

			new SelectData(context, TYPE_CODING).execute();

			break;
		default:

			view = LayoutInflater.from(context).inflate(
					R.layout.categories_activity, null);

			showGridView(view, position);

			break;
		}
		((ViewPager) container).addView(view, 0);

		return view;

	}

	private void showGridView(View view, int position) {

		final GridView gridViewCategories = (GridView) view
				.findViewById(R.id.gridViewCategories);

		listCategories = new ArrayList<Categories>();

		for (Categories c : new DBAdapter(context).getAllCategories()) {

			if (c.getType() == position) {

				listCategories.add(c);

			}
		}

		adapter = new GridViewCategoriesAdapter(context, listCategories);
		gridViewCategories.setAdapter(adapter);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {

		return view.equals(object);
	}

	@Override
	public AlertDialog showDialog(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	private class SelectData extends AsyncTask<Void, Void, Void> implements
			IMobilereader {

		private Context context;
		private int type;

		public SelectData(Context context, int type) {

			this.context = context;
			this.type = type;
		}

		@Override
		protected Void doInBackground(Void... params) {

			listArticle = db.getTypeCategoriesDetail(type, MAX_LIMIT);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (!listArticle.isEmpty()) {

				listAdapter = new ListViewCategoriesDetailAdapter(context,
						listArticle);
				listArchive.setAdapter(listAdapter);
				listAdapter.notifyDataSetChanged();

			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			db = new DBAdapter(context);
			db.open();
		}

		@Override
		public AlertDialog showDialog(String text) {

			return null;
		}

	}
}
