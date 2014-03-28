package eu.alebianco.air.extensions.expansion.services;

import com.google.android.vending.expansion.downloader.impl.DownloaderService;

import eu.alebianco.air.extensions.expansion.model.Security;
import eu.alebianco.air.extensions.expansion.receivers.XAPKAlarmReceiver;

public class XAPKDownloaderService extends DownloaderService {

    @Override
    public String getPublicKey() {
        return Security.MARKET_KEY;
    }

    @Override
    public byte[] getSALT() {
        return Security.SALT;
    }

    @Override
    public String getAlarmReceiverClassName() {
        return XAPKAlarmReceiver.class.getName();
    }
}