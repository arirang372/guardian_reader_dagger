package com.john.grdagger.ui.main.core;

import com.john.grdagger.models.GuardianContent;
import com.john.grdagger.models.GuardianSection;
import java.util.List;

/**
 * Created by john on 9/29/2017.
 */

public interface MainView
{
    void setupToolBar(final List<GuardianSection> sections);
    void showNetworkLoading(Boolean networkInUse);
    void showList(List<GuardianContent> items);
    void hideProgress();
}
