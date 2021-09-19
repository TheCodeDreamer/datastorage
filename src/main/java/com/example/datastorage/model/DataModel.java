package com.example.datastorage.model;

import java.util.Objects;

public class DataModel {

    private final Object data;
    private final long timeToDelete;

    public DataModel(Object data, long ttd) {
        this.data = data;
        this.timeToDelete = ttd;
    }

    public Object getData() {
        return this.data;
    }

    public long getTimeToDelete() {
        return timeToDelete;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataModel dataModel = (DataModel) o;
        return timeToDelete == dataModel.timeToDelete && Objects.equals(data, dataModel.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, timeToDelete);
    }
}
