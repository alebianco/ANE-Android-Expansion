package com.google.android.vending.expansion.downloader.impl;
/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.view.View;
import android.widget.RemoteViews;
import com.google.android.vending.expansion.downloader.Helpers;

public class V3CustomNotification implements DownloadNotification.ICustomNotification {

    CharSequence mTitle;
    CharSequence mPausedText;
    CharSequence mTicker;
    int mIcon;
    long mTotalBytes = -1;
    long mCurrentBytes = -1;
    long mTimeRemaining;
    PendingIntent mPendingIntent;
    Notification mNotification = new Notification();


    @Override
    public void setIcon(int icon) {
        mIcon = icon;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
    }

    @Override
    public void setPausedText(CharSequence pausedText) {
        mPausedText = pausedText;
    }

    @Override
    public void setTotalBytes(long totalBytes) {
        mTotalBytes = totalBytes;
    }

    @Override
    public void setCurrentBytes(long currentBytes) {
        mCurrentBytes = currentBytes;
    }

    @Override
    public Notification updateNotification(Context c) {
        Notification n = mNotification;

        boolean hasPausedText = (mPausedText != null);
        n.icon = mIcon;

        n.flags |= Notification.FLAG_ONGOING_EVENT;

        // Build the RemoteView object
        RemoteViews expandedView = new RemoteViews(
                c.getPackageName(),
                c.getResources().getIdentifier("status_bar_ongoing_event_progress_bar", "layout", c.getPackageName()));


        if (hasPausedText) {
            expandedView.setViewVisibility(c.getResources().getIdentifier("progress_bar_frame", "id", c.getPackageName()), View.GONE);
            expandedView.setViewVisibility(c.getResources().getIdentifier("description", "id", c.getPackageName()), View.GONE);
            expandedView.setTextViewText(c.getResources().getIdentifier("paused_text", "id", c.getPackageName()), mPausedText);
            expandedView.setViewVisibility(c.getResources().getIdentifier("time_remaining", "id", c.getPackageName()), View.GONE);
        } else {
            expandedView.setTextViewText(c.getResources().getIdentifier("title", "id", c.getPackageName()), mTitle);
            // look at strings
            expandedView.setViewVisibility(c.getResources().getIdentifier("description", "id", c.getPackageName()), View.VISIBLE);
            expandedView.setTextViewText(c.getResources().getIdentifier("description", "id", c.getPackageName()), Helpers.getDownloadProgressString(mCurrentBytes, mTotalBytes));
            expandedView.setViewVisibility(c.getResources().getIdentifier("progress_bar_frame", "id", c.getPackageName()), View.VISIBLE);
            expandedView.setViewVisibility(c.getResources().getIdentifier("paused_text", "id", c.getPackageName()), View.GONE);
            expandedView.setProgressBar(c.getResources().getIdentifier("progress_bar", "id", c.getPackageName()), (int) (mTotalBytes >> 8), (int) (mCurrentBytes >> 8), mTotalBytes <= 0);
            expandedView.setViewVisibility(c.getResources().getIdentifier("time_remaining", "id", c.getPackageName()), View.VISIBLE);
            expandedView.setTextViewText(c.getResources().getIdentifier("time_remaining", "id", c.getPackageName()), c.getString(c.getResources().getIdentifier("time_remaining_notification", "string", c.getPackageName()), Helpers.getTimeRemaining(mTimeRemaining)));
        }
        expandedView.setTextViewText(c.getResources().getIdentifier("progress_text", "id", c.getPackageName()), Helpers.getDownloadProgressPercent(mCurrentBytes, mTotalBytes));
        expandedView.setImageViewResource(c.getResources().getIdentifier("appIcon", "id", c.getPackageName()), mIcon);
        n.contentView = expandedView;
        n.contentIntent = mPendingIntent;
        return n;
    }

    @Override
    public void setPendingIntent(PendingIntent contentIntent) {
        mPendingIntent = contentIntent;
    }

    @Override
    public void setTicker(CharSequence ticker) {
        mTicker = ticker;
    }

    @Override
    public void setTimeRemaining(long timeRemaining) {
        mTimeRemaining = timeRemaining;
    }

}
