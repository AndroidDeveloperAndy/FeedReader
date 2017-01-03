package com.hackspace.andy.readrss.loader;

import android.support.annotation.NonNull;

/**
 * Created by Andy on 28.12.2016.
 */

public interface ILoaderData<T> {
    void endLoad(@NonNull T data);
}
