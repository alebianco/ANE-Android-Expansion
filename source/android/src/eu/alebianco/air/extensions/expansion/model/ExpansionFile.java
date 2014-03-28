package eu.alebianco.air.extensions.expansion.model;

public class ExpansionFile {

    public final boolean mIsMain;
    public final int mFileVersion;
    public final long mFileSize;

    public ExpansionFile(boolean isMain, int fileVersion, long fileSize) {
        mIsMain = isMain;
        mFileVersion = fileVersion;
        mFileSize = fileSize;
    }
}
