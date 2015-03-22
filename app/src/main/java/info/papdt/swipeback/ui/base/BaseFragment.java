package info.papdt.swipeback.ui.base;

import android.app.Fragment;
import android.content.Intent;
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
		
		getGlobalActivity().setOnReturnCallback(new GlobalActivity.OnReturnCallback() {
			@Override
			public void onReturn() {
				BaseFragment.this.onReturn();
			}
		});
		
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
	
	protected void hideHomeAsUp() {
		getGlobalActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
	}
	
	protected void onReturn() {
		getActivity().finish();
	}
	
	protected void startFragment(String name) {
		Intent i = new Intent();
		i.setAction(Intent.ACTION_MAIN);
		i.setClass(getActivity(), GlobalActivity.class);
		i.putExtra("fragment", name);
		startActivity(i);
	}
	
	protected void startFragment(String name, String pass) {
		Intent i = new Intent();
		i.setAction(Intent.ACTION_MAIN);
		i.setClass(getActivity(), GlobalActivity.class);
		i.putExtra("fragment", name);
		i.putExtra("pass", pass);
		startActivity(i);
	}
	
	protected abstract int getLayoutId();
	protected abstract void onFinishInflate(View view);
}
