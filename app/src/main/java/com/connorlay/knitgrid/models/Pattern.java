package com.connorlay.knitgrid.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.List;

/**
 * Created by connorlay on 3/17/16.
 */
public class Pattern extends SugarRecord implements Parcelable {
    private String name;
    private int rows;
    private int columns;
    private boolean showEvenRows;
    private Long uuid;

    @Ignore
    private List<StitchPatternRelation> stitchPatternRelations;

    public Pattern() {
    }

    public Pattern(String name, int rows, int columns, boolean showEvenRows) {
        this.name = name;
        this.rows = rows;
        this.columns = columns;
        this.showEvenRows = showEvenRows;
    }

    protected Pattern(Parcel in) {
        setId(in.readLong());
        name = in.readString();
        rows = in.readInt();
        columns = in.readInt();
        showEvenRows = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(name);
        dest.writeInt(rows);
        dest.writeInt(columns);
        dest.writeByte((byte) (showEvenRows ? 1 : 0));
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

    public Long getUuid() {
        return uuid;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public boolean showsEvenRows() {
        return showEvenRows;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setShowsEvenRows(boolean showEvenRows) {
        this.showEvenRows = showEvenRows;
    }

    public List<StitchPatternRelation> getStitchRelations() {
        if (stitchPatternRelations == null) {
            stitchPatternRelations = StitchPatternRelation.find(StitchPatternRelation.class, "pattern = ?", String.valueOf(this.getId()));
        }
        return stitchPatternRelations;
    }

    public void saveStitchPatternRelatons() {
        if (stitchPatternRelations == null) {
            return;
        }
        for (StitchPatternRelation relation : stitchPatternRelations) {
            relation.save();
        }
    }

    public void setStitch(Stitch stitch, int row, int column) {
        // TODO: support editing an existing stitch relation?
        new StitchPatternRelation(this, stitch, row, column).save();
    }
}
