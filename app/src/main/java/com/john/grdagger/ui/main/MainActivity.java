package com.john.grdagger.ui.main;

import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import com.john.grdagger.R;
import com.john.grdagger.application.GuardianApplication;
import com.john.grdagger.models.GuardianContent;
import com.john.grdagger.models.GuardianSection;
import com.john.grdagger.ui.main.core.MainPresenter;
import com.john.grdagger.ui.main.core.MainView;
import com.john.grdagger.ui.main.dagger.DaggerMainComponent;
import com.john.grdagger.ui.main.dagger.MainModule;
import com.john.grdagger.utils.Utils;
import java.util.List;
import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class MainActivity extends AppCompatActivity
                          implements MainView
{

    @Inject
    MainPresenter presenter;

    @BindView(R.id.progressbar)
    MaterialProgressBar progressBar;

    @BindView(R.id.sp_sections)
    Spinner sp_sections;

    @BindView(R.id.refresh_view)
    SwipeRefreshLayout refresh_view;

    @BindView(R.id.lv_news_contents)
    ListView lv_news_contents;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Unbinder unbinder;
    private ArrayAdapter<GuardianContent> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        DaggerMainComponent.builder()
                           .appComponent(GuardianApplication.getAppComponent())
                           .mainModule(new MainModule(this))
                           .build()
                           .inject(this);

        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        adapter = null;
        lv_news_contents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.listItemSelected(position);
            }
        });

        lv_news_contents.setEmptyView(getLayoutInflater().inflate(R.layout.empty_list, lv_news_contents, false));

        refresh_view.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshList();
            }
        });

        progressBar.setVisibility(View.INVISIBLE);
        presenter.onCreate();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(unbinder != null)
            unbinder.unbind();

        presenter.onDestroy();
    }


    @Override
    public void onBackPressed()
    {
        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                finish();
            }
        };

        Utils.createYesNoDialog(this, "Confirm", "Exit the application?","Yes", "No",positiveListener);
    }

    @Override
    public void setupToolBar(final List<GuardianSection> sections)
    {
        final SectionsAdapter adapter = new SectionsAdapter(this, sections);
        sp_sections.setAdapter(adapter);
        sp_sections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                presenter.titleSpinnerSelectionSelected(sections.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void showList(List<GuardianContent> items)
    {
        if(adapter == null)
        {
            adapter = new NewsListAdapter(this, items);
            lv_news_contents.setAdapter(adapter);
        }
        else
        {
            adapter.clear();
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showNetworkLoading(Boolean networkInUse)
    {
        progressBar.setVisibility(networkInUse? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void hideProgress()
    {
        refresh_view.setRefreshing(false);
    }
}
