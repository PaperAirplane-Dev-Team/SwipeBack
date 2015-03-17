package info.papdt.swipeback.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import info.papdt.swipeback.R;
import static info.papdt.swipeback.ui.utils.UiUtility.*;

public abstract class BaseListFragment extends BaseFragment 
{
	private ListView mList;

	@Override
	protected void onFinishInflate(View view) {
		mList = $(view, R.id.list);
		mList.setAdapter(buildAdapter());
		mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				BaseListFragment.this.onItemClick(pos);
			}
		});
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		new LoadDataTask().execute();
	}
	
	@Override
	protected int getLayoutId() {
		return R.layout.list;
	}
	
	protected ListView getListView() {
		return mList;
	}
	
	protected void onItemClick(int pos) {
		
	}
	
	protected void onDataLoaded() {
		
	}
	
	protected abstract BaseAdapter buildAdapter();
	protected abstract void loadData();
	
	private class LoadDataTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog prog;
		
		@Override
		protected void onPreExecute() {
			prog = new ProgressDialog(getActivity());
			prog.setMessage(getString(R.string.plz_wait));
			prog.setCancelable(false);
			prog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			loadData();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			prog.dismiss();
			((BaseAdapter) mList.getAdapter()).notifyDataSetChanged();
			onDataLoaded();
		}
		
	}
}
