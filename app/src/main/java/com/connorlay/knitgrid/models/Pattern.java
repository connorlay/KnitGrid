package com.connorlay.knitgrid.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by connorlay on 3/17/16.
 */
public class Pattern extends SugarRecord implements Parcelable {
    private String name;
    private int rows;
    private int cols;
    private boolean alternating;

    public Pattern() {

    }

    public Pattern(String name, int rows, int columns, boolean alternating) {
        this.name = name;
        this.rows = rows;
        this.cols = columns;
        this.alternating = alternating;
    }

    protected Pattern(Parcel in) {
        setId(in.readLong());
        name = in.readString();
        rows = in.readInt();
        cols = in.readInt();
        alternating = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(name);
        dest.writeInt(rows);
        dest.writeInt(cols);
        dest.writeByte((byte) (alternating ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Pattern> CREATOR = new Creator<Pattern>() {
        @Override
        public Pattern createFromParcel(Parcel in) {
            return new Pattern(in);
        }

        @Override
        public Pattern[] newArray(int size) {
            return new Pattern[size];
        }
    };

    public String getName() {
        return name;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return cols;
    }

    public boolean isAlternating() {
        return alternating;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.cols = columns;
    }

    public void setAlternating(boolean alternating) {
        this.alternating = alternating;
    }

    public List<StitchPatternRelation> getStitchRelations() {
        return StitchPatternRelation.find(StitchPatternRelation.class, "pattern = ?", String.valueOf(this.getId()));
    }

    public void setStitch(Stitch stitch, int row, int column) {
        new StitchPatternRelation(this, stitch, row, column).save();
    }
}
