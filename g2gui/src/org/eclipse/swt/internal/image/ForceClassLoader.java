package org.eclipse.swt.internal.image;
public class ForceClassLoader /* not part of eclipse. fixes gcj ld issue */ {
    static
    {
        GIFFileFormat x = new GIFFileFormat();
        PNGFileFormat y = new PNGFileFormat();
        JPEGFileFormat z = new JPEGFileFormat();
        WinBMPFileFormat q = new WinBMPFileFormat();
        WinICOFileFormat p = new WinICOFileFormat();
    }
}