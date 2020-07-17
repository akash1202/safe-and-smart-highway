package com.example.akash.myhighway1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.akash.myhighway1.user.UserComponent;

import io.reactivex.disposables.CompositeDisposable;

public class BaseFragment extends Fragment implements BaseActivity.BackHandler {
    protected CompositeDisposable subscriptions = new CompositeDisposable();

    protected AppCompatActivity getAppCompatActivity() {
        return (AppCompatActivity) getActivity();
    }

    protected ApplicationComponent getAppComponent() {
        return MyHighwayApp.myHighwayApp(getActivity()).appComponent();
    }

    protected UserComponent getUserComponent() {
        return MyHighwayApp.myHighwayApp(getActivity()).userComponent();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resolveDependencies();
    }

    /**
     * Callback for Subclass to resolve dependencies, called on super.onCreate();
     */
    private void resolveDependencies() {
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Callback for Subclass to track screen, called on super.onStart();
     */
    protected boolean trackScreen() {
        return false;
    }

    protected void showMessage(String message) {
        View view = getView();
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    protected void showMessage(@StringRes int message) {
        View view = getView();
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    protected void showStickyMessage(String message) {
        View view = getView();
        if (view != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    protected void showToastMessage(String message) {
        if (TextUtils.isEmpty(message)) return;
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    protected void showToastMessage(@StringRes int message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisibleToUser();
        } else {
            onInvisibleToUser();
        }
    }

    protected void onInvisibleToUser() {

    }

    protected void onVisibleToUser() {

    }


    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onDestroy() {
        subscriptions.clear();
        super.onDestroy();
    }

    /**
     * Activity should implement this interface if it has it's own Toolbar and allows replacing it
     * with Fragment's Toolbar
     */
    public interface ToolbarHelper {
        void setCustomToolbar(Toolbar toolbar);

        void removeCustomToolbar();
    }
}
