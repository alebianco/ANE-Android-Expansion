package eu.alebianco.air.extensions.expansion.model;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import android.os.Messenger;

import com.adobe.fre.FREContext;
import com.google.android.vending.expansion.downloader.DownloadProgressInfo;
import com.google.android.vending.expansion.downloader.DownloaderServiceMarshaller;
import com.google.android.vending.expansion.downloader.IDownloaderClient;
import com.google.android.vending.expansion.downloader.IDownloaderService;

import eu.alebianco.air.extensions.expansion.XAPKContext;
import eu.alebianco.air.extensions.expansion.model.enums.DownloadEventCode;
import eu.alebianco.air.extensions.expansion.model.enums.StatusEventLevel;
import eu.alebianco.air.extensions.expansion.model.enums.ZipEventCode;
import eu.alebianco.air.extensions.expansion.utils.ExpansionExtractor;

public class DownloaderClient implements IDownloaderClient, Observer {

    private IDownloaderService mRemoteService;
    private FREContext context;

    public DownloadProgressInfo info;
    public int state = IDownloaderClient.STATE_IDLE;

    public DownloaderClient(FREContext context) {

        this.context = context;
    }

    public void authorizeCellularDownload() {

        if (mRemoteService != null) {
            mRemoteService.setDownloadFlags(IDownloaderService.FLAGS_DOWNLOAD_OVER_CELLULAR);
            mRemoteService.requestContinueDownload();
        }
    }

    public void pause() {

        if (mRemoteService != null) {
            mRemoteService.requestPauseDownload();
        }
    }

    public void resume() {

        if (mRemoteService != null) {
            mRemoteService.requestContinueDownload();
        }
    }

    public void cancel() {

        if (mRemoteService != null) {
            mRemoteService.requestAbortDownload();
        }
    }

    @Override
    public void update(Observable observable, Object data) {

        if ((Boolean) data) {
            context.dispatchStatusEventAsync(ZipEventCode.COMPLETE.getName(), StatusEventLevel.EXTRACTOR.getName());
        } else {
            context.dispatchStatusEventAsync(ZipEventCode.ERROR.getName(), StatusEventLevel.EXTRACTOR.getName());
        }
    }

    public void unzip(String path, final ExpansionFile[] xapks, boolean overwrite) {

        final File destination = new File(path);
        boolean ready = destination.exists() && destination.isDirectory() && destination.canWrite();

        if (ready) {
            ExpansionExtractor unzipper = new ExpansionExtractor(context.getActivity(), xapks, destination, overwrite);
            unzipper.addObserver(this);
            unzipper.unzip();
            context.dispatchStatusEventAsync(ZipEventCode.BEGIN.getName(), StatusEventLevel.EXTRACTOR.getName());
        }
    }

    @Override
    public void onDownloadProgress(DownloadProgressInfo info) {

        this.info = info;
        context.dispatchStatusEventAsync(DownloadEventCode.PROGRESS.getName(), StatusEventLevel.DOWNLOAD.getName());
    }

    @Override
    public void onDownloadStateChanged(int state) {

        if (this.state == state) return; // fix double fires.

        this.state = state;
        context.dispatchStatusEventAsync(DownloadEventCode.STATUS_CHANGE.getName(), StatusEventLevel.DOWNLOAD.getName());
    }

    @Override
    public void onServiceConnected(Messenger messenger) {

        mRemoteService = DownloaderServiceMarshaller.CreateProxy(messenger);
        mRemoteService.onClientUpdated(XAPKContext.stub.getMessenger());
    }

}
