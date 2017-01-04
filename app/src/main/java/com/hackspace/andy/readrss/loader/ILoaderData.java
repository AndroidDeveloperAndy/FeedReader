package com.hackspace.andy.readrss.loader;

import android.support.annotation.NonNull;

public interface ILoaderData<T> {
    void endLoad(@NonNull T data);
}
