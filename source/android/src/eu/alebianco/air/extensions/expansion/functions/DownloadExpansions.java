/*
 * Air Native Extension for Google Analytics on iOS and Android
 * 
 * Author: Alessandro Bianco
 * http://alessandrobianco.eu
 *
 * Copyright (c) 2012 Alessandro Bianco
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package eu.alebianco.air.extensions.expansion.functions;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;

import eu.alebianco.air.extensions.expansion.XAPKContext;
import eu.alebianco.air.extensions.expansion.model.enums.DownloadEventCode;
import eu.alebianco.air.extensions.expansion.model.enums.StatusEventLevel;
import eu.alebianco.air.extensions.expansion.services.XAPKDownloaderService;
import eu.alebianco.air.extensions.utils.FREUtils;
import eu.alebianco.air.extensions.utils.LogLevel;

public class DownloadExpansions implements FREFunction {

    @Override
    public FREObject call(FREContext context, FREObject[] args) {

        try {
            Intent launchIntent = context.getActivity().getIntent();

            Intent notificationIntent = new Intent(context.getActivity(), context.getActivity().getClass());
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            notificationIntent.setAction(launchIntent.getAction());

            if (launchIntent.getCategories() != null) {
                for (String category : launchIntent.getCategories()) {
                    notificationIntent.addCategory(category);
                }
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(context.getActivity(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            int startResult = DownloaderClientMarshaller.startDownloadServiceIfRequired(context.getActivity(), pendingIntent, XAPKDownloaderService.class);

            if (startResult != DownloaderClientMarshaller.NO_DOWNLOAD_REQUIRED) {
                XAPKContext.stub = DownloaderClientMarshaller.CreateStub(XAPKContext.client, XAPKDownloaderService.class);
                XAPKContext.stub.connect(context.getActivity());
                return null;
            } else {
                context.dispatchStatusEventAsync(DownloadEventCode.COMPLETE.getName(), StatusEventLevel.DOWNLOAD.getName());
            }
        } catch (NameNotFoundException e) {
            FREUtils.logEvent(context, LogLevel.FATAL, "Cannot find own package! MAYDAY!");
            e.printStackTrace();
        }

        return null;
    }

}
