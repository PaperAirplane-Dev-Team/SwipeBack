package info.papdt.swipeback.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import info.papdt.swipeback.R;
import info.papdt.swipeback.ui.model.AppModel;
import static info.papdt.swipeback.ui.utils.UiUtility.*;

public class AppAdapter extends BaseAdapter
{
	private List<AppModel> mList;
	private LayoutInflater mInflater;
	
	public AppAdapter(Context context, List<AppModel> list) {
		mList = list;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		if (position >= getCount())
			return convertView;
		
		View v = convertView;
		if (v == null)
			v = mInflater.inflate(R.layout.app, container, false);
		
		AppModel app = mList.get(position);
		
		ImageView icon = $(v, R.id.app_icon);
		icon.setImageDrawable(app.icon);
		
		TextView title = $(v, R.id.app_name);
		title.setText(app.title);
		
		TextView pkg = $(v, R.id.app_pkg);
		pkg.setText(app.packageName);
		
		return v;
	}

}
