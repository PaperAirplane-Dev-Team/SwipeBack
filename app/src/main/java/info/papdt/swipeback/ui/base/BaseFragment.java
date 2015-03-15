package info.papdt.swipeback.ui.base;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v7.widget.Toolbar;

public abstract class BaseFragment extends Fragment
{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(getLayoutId(), container, false);
		onFinishInflate(v);
		return v;
	}

	protected GlobalActivity getGlobalActivity() {
		return (GlobalActivity) getActivity();
	}
	
	protected Toolbar getToolbar() {
		return getGlobalActivity().getToolbar();
	}
	
	protected String getExtraPass() {
		return getGlobalActivity().getExtraPass();
	}
	
	protected void setTitle(String title) {
		getGlobalActivity().getSupportActionBar().setTitle(title);
	}
	
	protected void showHomeAsUp() {
		getGlobalActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	protected abstract int getLayoutId();
	protected abstract void onFinishInflate(View view);
}
