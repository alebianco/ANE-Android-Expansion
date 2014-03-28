package eu.alebianco.air.extensions.expansion;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.google.android.vending.expansion.downloader.IStub;
import eu.alebianco.air.extensions.expansion.functions.*;
import eu.alebianco.air.extensions.expansion.model.DownloaderClient;

import java.util.Map;

public class XAPKContext extends FREContext {

    public static DownloaderClient client;
    public static IStub stub;

    public void initialize() {

        client = new DownloaderClient(this);

        // TODO: use the google play zip library to handle expansion archives
        // TODO: improve expansion extraction to publish progress and status informations to clients
        // TODO: make the XAPK context the downloader client itself
    }

    @Override
    public void dispose() {

    }

    @Override
    public Map<String, FREFunction> getFunctions() {

        Map<String, FREFunction> functions = new java.util.HashMap<String, FREFunction>();

        functions.put("isSupported", new IsSupported());

        functions.put("setMarketSecurity", new SetMarketSecurity());
        functions.put("hasExpansionFiles", new HasExpansionFiles());

        functions.put("downloadExpansions", new DownloadExpansions());
        functions.put("getDownloadProgress", new GetDownloadProgress());
        functions.put("getDownloadState", new GetDownloadState());

        functions.put("showWiFiSettings", new ShowWiFiSettings());
        functions.put("authorizeMobileDownload", new AuthorizeMobileDownload());

        functions.put("pauseDownload", new PauseDownload());
        functions.put("resumeDownload", new ResumeDownload());
        functions.put("cancelDownload", new CancelDownload());

        functions.put("unzipExpansionContent", new UnzipExpansionContent());

        return functions;
    }

}
