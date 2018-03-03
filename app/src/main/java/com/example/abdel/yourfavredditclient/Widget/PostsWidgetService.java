package com.example.abdel.yourfavredditclient.Widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by abdel on 2/28/2018.
 */

public class PostsWidgetService extends RemoteViewsService
{

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new PostsWidgetRemoteViewFactory(this.getApplicationContext(),intent);
    }
}
