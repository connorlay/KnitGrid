package com.connorlay.knitgrid.serializers;

import com.connorlay.knitgrid.models.Pattern;
import com.connorlay.knitgrid.models.StitchPatternRelation;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by connorlay on 4/13/16.
 */
public class PatternSerializer implements JsonSerializer {
    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        if (typeOfSrc != Pattern.class) {
            throw new IllegalArgumentException();
        }
        Pattern pattern = (Pattern) src;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", pattern.getName());
        jsonObject.addProperty("rows", pattern.getRows());
        jsonObject.addProperty("columns", pattern.getColumns());
        jsonObject.addProperty("show_even_rows", pattern.showsEvenRows());

        JsonArray jsonArray = new JsonArray();
        for (StitchPatternRelation relation : pattern.getStitchRelations()) {
            JsonObject relationJsonObject = new JsonObject();
            relationJsonObject.addProperty("row", relation.getRow());
            relationJsonObject.addProperty("column", relation.getCol());
            relationJsonObject.addProperty("color", 0); // TODO: fix when we merge in other branch
            relationJsonObject.addProperty("stitch_id", relation.getStitch().getUuid());
            jsonArray.add(relationJsonObject);
        }
        jsonObject.add("stitches", jsonArray);
        return jsonObject;
    }
}
