package com.raminzare.jpodcatcher;

import com.raminzare.jpodcatcher.model.Podcast;

public interface PodCatcherService {
    Podcast loadRSS(String url);
}
