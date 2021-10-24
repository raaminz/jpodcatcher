package com.raminzare.jpodcatcher;

import com.raminzare.jpodcatcher.model.Podcast;

public interface PodcastReader {
    Podcast loadRSS(String url) throws PodcastReaderException;
}
