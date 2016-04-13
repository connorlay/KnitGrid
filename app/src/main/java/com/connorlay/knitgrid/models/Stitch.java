package com.connorlay.knitgrid.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.connorlay.knitgrid.R;
import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by Magisus on 2/13/2016.
 */
// TODO: This will eventually be the model for a database table to store stitches (SugarORM?)
public class Stitch extends SugarRecord implements Parcelable, Serializable {
    String abbreviation;
    String details;
    String instructions;
    int iconID;
    boolean isDefault;

    public Stitch() {

    }

    // TODO: Right now I'm passing the id of the drawable on creation. Not sure if there's a way
    // TODO: to better associate the abbreviation with the image title.
    public Stitch(String abbreviation, int iconID, String details, String instructions,
                  boolean isDefault) {
        this.abbreviation = abbreviation;
        this.details = details;
        this.instructions = instructions;
        this.iconID = iconID;
        this.isDefault = isDefault;
    }

    protected Stitch(Parcel in) {
        abbreviation = in.readString();
        details = in.readString();
        instructions = in.readString();
        iconID = in.readInt();
        isDefault = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(abbreviation);
        dest.writeString(details);
        dest.writeString(instructions);
        dest.writeInt(iconID);
        dest.writeByte((byte) (isDefault ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Stitch> CREATOR = new Creator<Stitch>() {
        @Override
        public Stitch createFromParcel(Parcel in) {
            return new Stitch(in);
        }

        @Override
        public Stitch[] newArray(int size) {
            return new Stitch[size];
        }
    };

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getDetails() {
        return details;
    }

    public String getInstructions() {
        return instructions;
    }

    public int getIconID() {
        return iconID;
    }

    public void setIconID(int iconID) {
        this.iconID = iconID;
    }

    public static void generateDefaultStitches() {
        new Stitch("k1", R.drawable.k, "Knit one stitch.", "Instructions to come!", true).save();
        new Stitch("p", R.drawable.p, "Purl one stitch.", "Instructions to come!", true).save();
        new Stitch("k2tog", R.drawable.k2tog, "Knit two stitches together", "Instructions to " +
                "come!", true).save();
        new Stitch("ssk", R.drawable.ssk, "Slip two stitches, knit into front", "Instructions to " +
                "come!", true).save();
        new Stitch("yo", R.drawable.yo, "Yarn over", "Instructions to come!", true).save();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Stitch stitch = (Stitch) o;

        return abbreviation.equals(stitch.abbreviation);

    }

    @Override
    public int hashCode() {
        return abbreviation.hashCode();
    }
}
