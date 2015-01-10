package vn.ava.mobilereader.view;

import com.viewpagerindicator.TitlePageIndicator;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.myadapter.TabPagerAdapter;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment {

	private ViewPager pager;
	private TabPagerAdapter adapter;
	private TitlePageIndicator title;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_activity, null);
		setupViewPager(view);
		
		return view;
		
	}
	private void setupViewPager(View v) {

		adapter = new TabPagerAdapter(getActivity());
		pager = (ViewPager)v.findViewById(R.id.viewpager);
		title = (TitlePageIndicator)v.findViewById(R.id.indicator);
		pager.setAdapter(adapter);
		pager.setCurrentItem(1);
		title.setViewPager(pager);
	}
	

}
