package vn.ava.mobilereader.Instant;

import android.app.AlertDialog;
import android.widget.Toast;
import vn.ava.mobilereader.R;

public interface IMobilereader {

	static final int DB_VERSION = 1;
	static final String DB_NAME = "codejava";
	static final String DB_TABLE = "categories";
	static final String DB_TABLE_DETAIL = "detail";
	static final String DB_TABLE_CONTENT = "content";

	static final String TB_ID = "id";
	static final String TB_TITLE = "title";
	static final String TB_IMAGE = "image";
	static final String TB_URL = "url";
	static final String TB_TYPE = "type";

	static final String TB_DT_ID = "id";
	static final String TB_DT_TITLE = "title";
	static final String TB_DT_IMAGE = "image";
	static final String TB_DT_URL = "url";
	static final String TB_DT_PUBLISH = "publish";
	static final String TB_DT_DESCRIP = "descript";
	static final String TB_DT_TYPE = "type";

	static final String TB_CT_ID = "id";
	static final String TB_CT_TITLE = "title";
	static final String TB_CT_CONTENT = "content";

	static final String TB_CREATE = "CREATE TABLE categories (id integer primary key autoincrement"
			+ ", title text null unique, image text null, url text null,type int null);";

	static final String TB_DETAIL_CREATE = "CREATE TABLE detail (id integer primary key autoincrement"
			+ ", title text null unique, image text null, url text null,descript text null,"
			+ "publish text null,type int null);";

	static final String TB_CONTENT_CREATE = "CREATE TABLE content (id integer primary key autoincrement"
			+ ", title text null unique, content text null);";

	static final String TB_DROP = "DROP TABLE IF EXISTS categories";

	static final String TB_DETAIL_DROP = "DROP TABLE IF EXISTS detail";

	static final String TB_CONTENT_DROP = "DROP TABLE IF EXISTS content";

	static final String[] URL = { "http://www.codejava.net/java-core",
			"http://www.codejava.net/java-se",
			"http://www.codejava.net/java-ee",
			"http://www.codejava.net/frameworks",
			"http://www.codejava.net/ides", "http://www.codejava.net/servers" };

	static final String[] TITLE = new String[] { "Java Core", "Java SE",
			"Java EE", "FrameWorks", "IDES", "Servers", "Books", "Coding" };

	static final int[] ICON = { R.drawable.ic_home_grey600_36dp,
			R.drawable.ic_local_atm_grey600_36dp,
			R.drawable.ic_star_outline_grey600_36dp,
			R.drawable.ic_quick_contacts_mail_grey600_24dp,
			R.drawable.ic_cast_connected_grey600_36dp,
			R.drawable.ic_content_paste_grey600_36dp,
			R.drawable.ic_help_grey600_36dp, 0, R.drawable.rss,
			R.drawable.facebook, R.drawable.twitter, R.drawable.google,
			R.drawable.delicious };
	static final String[] TITLE_NAV = { "Home", "Advertise", "Favourites",
			"Contact Us", "Subscription","Quizz", "About", "", "Rss", "Facebook",
			"Twitter", "Google+", "Delicious" };

	static final String HOST_MAIL = "gator3107.hostgator.com";
	static final String USER_LOGIN = "info@codejava.net";
	static final String PASS_LOGIN = "TWCKpiSqaDd(";
	static final String SMTP_HOST = "mail.smtp.host";
	static final String SMTP_AUTHEN = "mail.smtp.auth";
	static final String SMTP_PORT = "mail.smtp.port";
	static final String SMTP_SOCKET_PORT = "mail.smtp.socketFactory.port";
	static final String SMTP_CLASS = "mail.smtp.socketFactory.class";
	static final String TO_EMAIL = "info@codejava.net";

	static final String[] TITLE_CATEGORIES_QUIZZ = { "Java Core", "Java SE",
			"Java EE", "Frameworks", "IDEs", "Servers" };
	static final int[] ICON_CATERGORIES_QUIZZ = { R.drawable.christmas,
			R.drawable.christmas, R.drawable.christmas, R.drawable.christmas,
			R.drawable.christmas, R.drawable.christmas };
	static final String[] CHAPTER = { "15 chapter", "15 chapter", "15 chapter",
			"15 chapter", "15 chapter", "15 chapter" };

	AlertDialog showDialog(String text);

}
