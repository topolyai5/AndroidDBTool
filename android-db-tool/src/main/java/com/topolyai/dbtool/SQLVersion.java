package com.topolyai.dbtool;

import android.support.annotation.RawRes;

public class SQLVersion {

    private int version;
    private int rawId;

    public SQLVersion(@RawRes int rawId, int version) {
        this.rawId = rawId;
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public int getRawId() {
        return rawId;
    }
}
