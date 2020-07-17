package com.example.akash.myhighway1;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.Toast;

import com.example.akash.myhighway1.user.UserComponent;
import com.example.akash.myhighway1.utils.Utils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.plugins.RxJavaPlugins;

public class BaseActivity extends AppCompatActivity {
    protected CompositeDisposable subscriptions = new CompositeDisposable();

    protected ApplicationComponent appComponent() {
        return MyHighwayApp.myHighwayApp(this).appComponent();
    }

    protected UserComponent userComponent() {
        return MyHighwayApp.myHighwayApp(this).userComponent();
    }

    protected void showMessage(String message) {
        if (TextUtils.isEmpty(message)) return;
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    protected void showMessage(@StringRes int message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    protected void showStickyMessage(String message) {
        if (TextUtils.isEmpty(message)) return;
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_INDEFINITE).show();
    }

    protected void showToastMessage(String message) {
        if (TextUtils.isEmpty(message)) return;
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToastMessage(@StringRes int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        resolveDependencies();
        super.onCreate(savedInstanceState);
        RxJavaPlugins.setErrorHandler(throwable -> {
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    private void initApplyWindowInsetsListener() {
        View root = ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        if (root != null && ViewCompat.getFitsSystemWindows(root) && Utils.hasLollipop()) {
            root.setOnApplyWindowInsetsListener(new InsetListener());
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
    final class InsetListener implements View.OnApplyWindowInsetsListener {

        @Override
        public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
            View[] topInsetViews = getSystemWindowInsetTopPaddingViews();
            if (topInsetViews != null) {
                int topInset = insets.getSystemWindowInsetTop();
                for (View view : topInsetViews) {
                    view.setPadding(view.getPaddingLeft(), topInset, view.getPaddingRight(), view.getPaddingBottom());
                }
            }
            if (consumeSystemWindowInsetTop()) {
                return insets.replaceSystemWindowInsets(insets.getSystemWindowInsetLeft(), 0,
                        insets.getStableInsetRight(), insets.getSystemWindowInsetBottom());
            } else {
                return v.onApplyWindowInsets(insets);
            }
        }
    }

    protected boolean consumeSystemWindowInsetTop() {
        return true;
    }

    protected View[] getSystemWindowInsetTopPaddingViews() {
        return null;
    }


//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    protected void increaseToolbarHeight(View toolbar) {
//        if (Utils.hasLollipop() && toolbar != null) {
//            Window window = getWindow();
//            window.setStatusBarColor(Color.TRANSPARENT);
//
//            ViewGroup.LayoutParams params = toolbar.getLayoutParams();
//            params.height += BaseUtils.getStatusBarHeight(this);
//            toolbar.requestLayout();
//        }
//    }

    /**
     * Callback for Subclass to resolve dependencies, called on super.onCreate();
     */
    protected void resolveDependencies() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!trackScreen()) {
//            EventTracker.trackScreen(this.getClass().getSimpleName());
        }
    }

    /**
     * Callback for Subclass to track screen, called on super.onStart();
     */
    protected boolean trackScreen() {
        return false;
    }

    @Override
    protected void onDestroy() {
        subscriptions.clear();
        super.onDestroy();
    }

    public interface BackHandler {
        boolean onBackPressed();
    }

}
