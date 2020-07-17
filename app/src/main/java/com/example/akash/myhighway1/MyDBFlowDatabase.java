package com.example.akash.myhighway1;

import android.net.Uri;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.provider.ContentProvider;
import com.raizlabs.android.dbflow.annotation.provider.ContentUri;
import com.raizlabs.android.dbflow.annotation.provider.TableEndpoint;

@ContentProvider(authority = MyDBFlowDatabase.AUTHORITY,
        database = MyDBFlowDatabase.class,
        baseContentUri = MyDBFlowDatabase.BASE_CONTENT_URI)
@Database(name = MyDBFlowDatabase.NAME, version = MyDBFlowDatabase.VERSION)
public class MyDBFlowDatabase {
    public static final String NAME = "MyDBFlowDatabase";
    public static final int VERSION = 1;
    public static final String AUTHORITY = "akash.example.com.myhighway1.provider";
    public static final String BASE_CONTENT_URI = "content://";

    private static Uri buildUri(String... paths) {
        Uri.Builder builder = Uri.parse(MyDBFlowDatabase.BASE_CONTENT_URI + MyDBFlowDatabase.AUTHORITY).buildUpon();
        for (String path : paths) {
            builder.appendPath(path);
        }
        return builder.build();
    }

    @TableEndpoint(name = ItemsProviderModel.ENDPOINT, contentProvider = ItemsProviderModel.class)
    public static class ItemsProviderModel {
        public static final String ENDPOINT = "ItemProviderTable";
        @ContentUri(path = ItemsProviderModel.ENDPOINT, type = ContentUri.ContentType.VND_MULTIPLE + ENDPOINT)
        public static final Uri CONTENT_URI = buildUri(ENDPOINT);
    }

}
