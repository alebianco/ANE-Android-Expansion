package eu.alebianco.air.extensions.expansion.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Observable;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import eu.alebianco.air.extensions.expansion.model.ExpansionFile;
import org.apache.commons.io.IOUtils;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.vending.expansion.downloader.Helpers;

public class ExpansionExtractor extends Observable {

    private UnzipParameters mProps;

    public ExpansionExtractor(Context context, ExpansionFile[] xapks, File destination, boolean overwrite) {

        mProps = new UnzipParameters();
        mProps.context = context;
        mProps.xapks = xapks;
        mProps.destination = destination;
        mProps.overwrite = overwrite;
    }

    public void unzip() {

        new UnZipTask().execute(mProps);
    }

    private class UnzipParameters {

        public Context context;

        public ExpansionFile[] xapks;
        public File destination;
        public boolean overwrite;
    }

    private class UnZipTask extends AsyncTask<UnzipParameters, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressWarnings("rawtypes")
        @Override
        protected Boolean doInBackground(UnzipParameters... params) {

            UnzipParameters data = params[0];

            for (ExpansionFile xf : data.xapks) {

                String fileName = Helpers.getExpansionAPKFileName(data.context, xf.mIsMain, xf.mFileVersion);
                if (!Helpers.doesFileExist(data.context, fileName, xf.mFileSize, false)) {
                    return false;
                }

                File archive = new File(Helpers.generateSaveFileName(data.context, fileName));

                try {
                    ZipFile zipfile = new ZipFile(archive);
                    for (Enumeration e = zipfile.entries(); e.hasMoreElements(); ) {
                        ZipEntry entry = (ZipEntry) e.nextElement();
                        unzipEntry(zipfile, entry, data.destination.getPath(), data.overwrite);
                    }
                } catch (Exception e) {
                    return false;
                }

            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            setChanged();
            notifyObservers(result);
        }

        private void unzipEntry(ZipFile zipfile, ZipEntry entry, String outputDir, boolean overwrite) throws IOException {

            if (entry.isDirectory()) {
                createDir(new File(outputDir, entry.getName()));
                return;
            }

            File outputFile = new File(outputDir, entry.getName());

            if (!outputFile.getParentFile().exists()) {
                createDir(outputFile.getParentFile());
            }

            // if same filename & same length, overwrite only if requested.
            if (outputFile.exists() && outputFile.length() == entry.getSize() && !overwrite) {
                return;
            }

            BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

            try {
                IOUtils.copy(inputStream, outputStream);
            } finally {
                outputStream.close();
                inputStream.close();
            }
        }

        private void createDir(File dir) {

            if (dir.exists()) {
                return;
            }

            if (!dir.mkdirs()) {
                throw new RuntimeException("Can not create dir " + dir);
            }
        }
    }
} 