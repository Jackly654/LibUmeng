package com.gov.cn.application;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Environment;

import com.gov.cn.ImageUtils;
import com.gov.cn.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;

/**
 * Created by Administrator on 2015/12/28.
 */
public class GovApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ImageUtils.getInstance().init(
                new ImageLoaderConfiguration.Builder(this)
                        .defaultDisplayImageOptions(
                                new DisplayImageOptions.Builder()
                                        .showImageOnLoading(R.drawable.icon)
                                        .showImageForEmptyUri(R.drawable.icon)
                                        .cacheInMemory(true)
                                        .cacheOnDisc(true)
                                        .considerExifParams(true)
                                        .resetViewBeforeLoading(true)
                                        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                                        .bitmapConfig(Bitmap.Config.RGB_565)
                                        .build()
                        )
                        .writeDebugLogs()
                        .discCache(new UnlimitedDiscCache(new File(Environment
                                .getExternalStorageDirectory() + "/com.gov.cn"+"/images")))
                        .build()
        );
    }
}
