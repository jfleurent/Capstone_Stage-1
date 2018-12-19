package com.example.jeffr.capstone_stage2;

import android.app.Application;
import timber.log.Timber;

public class FindNDineApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    Timber.plant(new Timber.DebugTree());
  }
}
