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

import com.adobe.fre.FREASErrorException;
import com.adobe.fre.FREArray;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREInvalidObjectException;
import com.adobe.fre.FRENoSuchNameException;
import com.adobe.fre.FREObject;
import com.adobe.fre.FRETypeMismatchException;
import com.adobe.fre.FREWrongThreadException;

import eu.alebianco.air.extensions.expansion.model.ExpansionFile;
import eu.alebianco.air.extensions.expansion.XAPKContext;
import eu.alebianco.air.extensions.expansion.utils.FREUtils;
import eu.alebianco.air.extensions.expansion.utils.LogLevel;

public class UnzipExpansionContent implements FREFunction {

    @Override
    public FREObject call(FREContext context, FREObject[] args) {


        try {
            String path = args[0].getAsString();
            ExpansionFile[] xapks = getFiles((FREArray) args[1], context);
            boolean overwrite = args[2].getAsBool();

            XAPKContext.client.unzip(path, xapks, overwrite);
        } catch (Exception e) {
            FREUtils.logEvent(context, LogLevel.FATAL, "Unable to retrieve path. [%s: %s]", e.getClass(), e.getMessage());
        }

        return null;
    }

    private ExpansionFile[] getFiles(FREArray list, FREContext context) throws FREInvalidObjectException, FREWrongThreadException, IllegalStateException, FRETypeMismatchException, FREASErrorException, FRENoSuchNameException {

        ExpansionFile[] xapks = new ExpansionFile[(int) list.getLength()];

        for (int i = 0; i < list.getLength(); i++) {
            FREObject file = list.getObjectAt(i);

            boolean main = file.getProperty("main").getAsBool();
            int version = file.getProperty("version").getAsInt();
            long size = (long) file.getProperty("size").getAsInt();

            xapks[i] = new ExpansionFile(main, version, size);
        }

        return xapks;
    }

}
