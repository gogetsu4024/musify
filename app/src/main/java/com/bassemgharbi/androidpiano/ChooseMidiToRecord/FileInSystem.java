package com.bassemgharbi.androidpiano.ChooseMidiToRecord;

public class FileInSystem {
    private String name;
    private String path;
    private int trackCount;

    FileInSystem(String name, String path, int trackCount) {
        this.name = name;
        this.path = path;
        this.trackCount = trackCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(int trackCount) {
        this.trackCount = trackCount;
    }
}

