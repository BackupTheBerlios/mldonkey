package org.eclipse.swt.internal.image;

/*
 * This file is only a file for some workarounds for gcj, don't mind if you get an error
 * within eclipse, as this file is not used by java, only needed for static linking with gcj
 * don't mind and please ignore ;-)
 */



public class ForceClassLoader /* not part of eclipse. fixes gcj ld issue */ {
    static
    {
        GIFFileFormat x = new GIFFileFormat();
        PNGFileFormat y = new PNGFileFormat();
        JPEGFileFormat z = new JPEGFileFormat();
        WinBMPFileFormat q = new WinBMPFileFormat();
        WinICOFileFormat p = new WinICOFileFormat();		
		gnu.java.locale.Calendar du = new gnu.java.locale.Calendar();
		gnu.java.locale.LocaleInformation li = new gnu.java.locale.LocaleInformation();
		
    }
}
