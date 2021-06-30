package de.tum.in.ase.parser.utils;

import java.io.File;

/**
 * Utility class providing shared functionality for files
 */
public class FileUtils {

    /**
     * Returns true if the specified file's size is greater than the specified threshold.
     *
     * @param file the file to check
     * @param sizeInMegabytes the threshold in mega bytes
     * @return true if the size of the file is larger than the threshold
     */
    public static boolean isFilesizeGreaterThan(File file, long sizeInMegabytes) {
        long sizeInBytes = file.length();
        long sizeInMb = sizeInBytes / (1024 * 1024);
        return sizeInMb > sizeInMegabytes;
    }
}
