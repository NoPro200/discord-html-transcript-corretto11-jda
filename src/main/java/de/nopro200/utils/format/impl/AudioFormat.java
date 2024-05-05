package de.nopro200.utils.format.impl;

import de.nopro200.utils.format.IFormatHelper;
import java.util.Arrays;
import java.util.List;

public class AudioFormat implements IFormatHelper {

    final List<String> formats = Arrays.asList("mp3", "wav", "ogg", "flac");

    @Override
    public List<String> formats() {
        return formats;
    }
}
