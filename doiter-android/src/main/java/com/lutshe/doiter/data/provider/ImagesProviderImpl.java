package com.lutshe.doiter.data.provider;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.WindowManager;
import com.lutshe.doiter.R;
import com.lutshe.doiter.data.database.InitialDataSetup;
import com.lutshe.doiter.views.util.IoUtils;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.Trace;
import org.androidannotations.annotations.res.BooleanRes;

/**
 * Created by Arturro on 04.09.13.
 */
@EBean(scope = EBean.Scope.Singleton)
public class ImagesProviderImpl implements ImagesProvider {

    public static final int DEFAULT_BYTES_PER_PIXEL = 3;
    public static final int CACHE_SIZE_MULTIPLIER = 3;

    private static int requestsCount;

    @BooleanRes(R.bool.debug)
    Boolean isDebug;

    @RootContext
    Context context;

    @SystemService
    WindowManager windowManager;

    private LruCache<String, Bitmap> bitmapsCache;

    private synchronized void initCacheIfNeeded() {
        if (bitmapsCache == null) {
            Point size = new Point();
            windowManager.getDefaultDisplay().getSize(size);
            bitmapsCache = new LruCache<String, Bitmap>(getCacheSize(size)) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
        }
    }

    @Trace
    @Override
    public Bitmap getImage(String name) {
        requestsCount ++;
        initCacheIfNeeded();
        logCacheStatistics();

        Bitmap bitmap = bitmapsCache.get(name);
        if (bitmap != null) {
            return bitmap;
        }

        bitmap = loadImage(name);

        if (bitmap == null) {
            bitmap = getDefaultBitmap();
        } else {
            bitmapsCache.put(name, bitmap);
        }

        return bitmap;
    }

    private void logCacheStatistics() {
        if (isDebug && requestsCount % 5 == 0) {
            Log.i(getClass().getName(), "total requests count = " + requestsCount + " with " + bitmapsCache.hitCount() + " hits and " + bitmapsCache.missCount() + " misses.");
        }
    }

    private Bitmap loadImage(String name) {
        if (isImageFromResources(name)) {
            return BitmapFactory.decodeResource(context.getResources(), getResourceId(name));
        } else {
            return IoUtils.getBitmap(name, context);
        }
    }

    private boolean isImageFromResources(String name) {
        return InitialDataSetup.goalsImages.containsKey(name);
    }

    private int getResourceId(String name) {
        return InitialDataSetup.goalsImages.get(name);
    }

    private Bitmap getDefaultBitmap() {
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.bg);
    }

    private int getCacheSize(Point screenSize) {
        return DEFAULT_BYTES_PER_PIXEL * screenSize.x * screenSize.y * CACHE_SIZE_MULTIPLIER;
    }
}
