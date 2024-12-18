package io.broadcast.engine.streaming.fork;

import io.broadcast.engine.record.extract.RecordExtractor;

public interface Source {

    static Source extracts(RecordExtractor<?> extractor) {
        return null;
    }
}
