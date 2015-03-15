package info.papdt.swipeback.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import info.papdt.swipeback.R;
import info.papdt.swipeback.ui.model.AppModel;
import static info.papdt.swipeback.ui.utils.UiUtility.*;

public class AppAdapter extends BaseAdapter implements Filterable
{
	private List<AppModel> mList;
	private List<AppModel> mFullList;
	private LayoutInflater mInflater;
	
	public AppAdapter(Context context, List<AppModel> list) {
		mList = list;
		mFullList = list;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public AppModel getItem(int position) {
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

	@Override
	public Filter getFilter() {
		return new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				String query = constraint.toString().toLowerCase();
				List<AppModel> filtered = new ArrayList<AppModel>();
				
				if (query.equals("")) {
					filtered = mFullList;
				} else {
					for (AppModel app : mFullList) {
						if (app.title.toLowerCase().contains(query)
							|| app.packageName.toLowerCase().contains(query)) 
							
							filtered.add(app);
					}
				}
				
				FilterResults result = new FilterResults();
				result.count = filtered.size();
				result.values = filtered;
				
				return result;
			}

			@Override
			@SuppressWarnings("unchecked")
			protected void publishResults(CharSequence constraint, FilterResults result) {
				mList = (List<AppModel>) result.values;
				notifyDataSetChanged();
			}
		};
	}

}
