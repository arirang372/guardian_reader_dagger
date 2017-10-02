package com.john.grdagger.ui.details;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.john.grdagger.R;
import com.john.grdagger.models.GuardianContent;
import com.john.grdagger.ui.details.core.DetailsNewsPresenter;
import com.john.grdagger.ui.details.core.DetailsNewsView;
import com.john.grdagger.ui.details.dagger.DaggerDetailsNewsComponent;
import com.john.grdagger.ui.details.dagger.DetailsNewsModule;
import com.john.grdagger.utils.Utils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by john on 9/29/2017.
 */

public class DetailsNewsActivity extends AppCompatActivity
                                 implements DetailsNewsView
{

    @Inject
    DetailsNewsPresenter presenter;

    @BindView(R.id.wv_main)
    WebView wv_main;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.progressbar)
    ProgressBar progressbar;

    private Unbinder unbinder;

    private GuardianContent content;

    public static void startActivity(Activity context, GuardianContent content)
    {
        Bundle b = new Bundle();
        b.putParcelable("news_contents", content);
        Utils.startActivity(context, DetailsNewsActivity.class, b);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        DaggerDetailsNewsComponent.builder()
                                  .detailsNewsModule(new DetailsNewsModule(this))
                                  .build()
                                  .inject(this);

        setContentView(R.layout.activity_details);

        unbinder = ButterKnife.bind(this);

        progressbar.setVisibility(View.VISIBLE);
        Bundle b = getIntent().getExtras();
        if(b != null)
        {
            content = b.getParcelable("news_contents");

        }

        presenter.onCreate();
    }

    @Override
    public void onResume()
    {
        presenter.onResume();
        showNewsContent();
        super.onResume();
        this.presenter.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        this.presenter.onPause();
    }


    @Override
    public void onDestroy()
    {
        if(unbinder != null)
            unbinder.unbind();

        super.onDestroy();
        this.presenter.onDestroy();
    }

    private void showNewsContent()
    {
        toolbar.setTitle(content.webTitle);
        wv_main.getSettings().setJavaScriptEnabled(true);
        wv_main.loadUrl(content.webUrl);
    }

    @Override
    public GuardianContent getContent()
    {
        return this.content;
    }

    @Override
    public void showProgress() {
        progressbar.setAlpha(1.0f);
        progressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress()
    {
        try {
            if (progressbar.getVisibility() != View.GONE) {
                progressbar.animate().alpha(0f).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationCancel(Animator animation) {
                        progressbar.setVisibility(View.GONE);
                    }
                });
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
