package vn.ava.mobilereader.view;

import vn.ava.mobilereader.R;
import vn.ava.mobilereader.myadapter.ListCategoriesQuizz;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Quizz extends Fragment{

	
	private ListCategoriesQuizz adapter;
	private ListView listCatergoriesQuizz;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.list_categories_quizz, null);
		initial(view);
		return view;
	}
	private void initial(View view){
		
		listCatergoriesQuizz = (ListView)view.findViewById(R.id.listCategoriesQuizz);
		adapter = new ListCategoriesQuizz(getActivity());
		listCatergoriesQuizz.setAdapter(adapter);
		
	}

	
}
