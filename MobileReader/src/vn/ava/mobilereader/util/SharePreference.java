package vn.ava.mobilereader.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.gson.Gson;

import vn.ava.mobilereader.model.CategoriesItem;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SharePreference {

	public static final String PREFEREN_NAME = "post";
	public static final String FAVOURITE = "post_favourite";

	public SharePreference() {

	}

	public void saveFavourite(Context context,
			List<CategoriesItem> listCategories) {

		SharedPreferences preFerence;
		Editor editor = null;
		preFerence = context.getSharedPreferences(PREFEREN_NAME,
				context.MODE_PRIVATE);
		editor = preFerence.edit();
		Gson gson = new Gson();

		String jsonFavourite = gson.toJson(listCategories);

		editor.putString(FAVOURITE, jsonFavourite);

		editor.commit();

	}

	public void addFavourite(Context context, CategoriesItem categories) {

		List<CategoriesItem> listCate = getFavourite(context);
		
		if (listCate == null) {

			listCate = new ArrayList<CategoriesItem>();
		}
		listCate.add(categories);
		saveFavourite(context, listCate);

	}

	public List<CategoriesItem> getFavourite(Context context) {

		SharedPreferences settings;
		List<CategoriesItem> favorites = null;

		settings = context.getSharedPreferences(PREFEREN_NAME,
				Context.MODE_PRIVATE);

		if (settings.contains(FAVOURITE)) {

			String jsonFavorites = settings.getString(FAVOURITE, null);
			Gson gson = new Gson();
			CategoriesItem[] favoriteItems = gson.fromJson(jsonFavorites,
					CategoriesItem[].class);

			favorites = Arrays.asList(favoriteItems);
			favorites = new ArrayList<CategoriesItem>(favorites);

		} else {

			return favorites;
		}

		return (ArrayList<CategoriesItem>) favorites;
	}

	public void removeFavorite(Context context, CategoriesItem categories) {

		List<CategoriesItem> favorites = getFavourite(context);

		if (favorites != null) {

			favorites.remove(categories);

			saveFavourite(context, favorites);
		}
	}

}
