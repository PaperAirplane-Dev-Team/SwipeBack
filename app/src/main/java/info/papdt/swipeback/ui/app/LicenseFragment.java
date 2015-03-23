package info.papdt.swipeback.ui.app;

import android.view.View;
import android.webkit.WebView;

import info.papdt.swipeback.R;
import info.papdt.swipeback.ui.base.BaseFragment;
import static info.papdt.swipeback.ui.utils.UiUtility.*;

public class LicenseFragment extends BaseFragment
{

	@Override
	protected int getLayoutId() {
		return R.layout.webview;
	}

	@Override
	protected void onFinishInflate(View view) {
		showHomeAsUp();
		setTitle(getString(R.string.license));
		WebView w = $(view, R.id.web);
		w.loadUrl("file:///android_asset/licenses.html");
	}

}
