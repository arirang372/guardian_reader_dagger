package com.john.grdagger.ui.details.core;

import com.john.grdagger.models.GuardianContent;

/**
 * Created by john on 9/29/2017.
 */

public interface DetailsNewsView {

    void showProgress();
    void hideProgress();
    GuardianContent getContent();
}
