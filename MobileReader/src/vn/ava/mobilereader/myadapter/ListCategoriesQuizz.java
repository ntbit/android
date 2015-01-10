package vn.ava.mobilereader.myadapter;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.Instant.IMobilereader;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListCategoriesQuizz extends BaseAdapter implements IMobilereader {

	private Context context;

	public ListCategoriesQuizz(Context context) {

		this.context = context;

	}

	@Override
	public int getCount() {
		return TITLE_CATEGORIES_QUIZZ.length;
	}

	@Override
	public Object getItem(int position) {
		return TITLE_CATEGORIES_QUIZZ[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {

			convertView = inflater.inflate(
					R.layout.list_categories_quizz_custom, null);

		}

		ViewHolder.tvchapter = (TextView) convertView
				.findViewById(R.id.tvChapter);
		ViewHolder.imgIcon = (ImageView) convertView
				.findViewById(R.id.imgIconQuizz);
		ViewHolder.tvtitle = (TextView) convertView
				.findViewById(R.id.tvTiteCategoriesQuizz);

		ViewHolder.tvchapter.setText(CHAPTER[position]);
		ViewHolder.tvtitle.setText(TITLE_CATEGORIES_QUIZZ[position]);
		ViewHolder.imgIcon.setImageResource(ICON_CATERGORIES_QUIZZ[position]);
		
		return convertView;
	}

	private static class ViewHolder {

		static TextView tvtitle;
		static TextView tvchapter;
		static ImageView imgIcon;
	}

	@Override
	public AlertDialog showDialog(String text) {
		// TODO Auto-generated method stub
		return null;
	}

}
