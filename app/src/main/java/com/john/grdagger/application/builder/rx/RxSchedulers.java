package com.john.grdagger.application.builder.rx;

import rx.Scheduler;

/**
 * Created by ygharsallah on 30/03/2017.
 */

public interface RxSchedulers {


    Scheduler runOnBackground();

    Scheduler io();

    Scheduler compute();

    Scheduler androidThread();

    Scheduler internet();



}
