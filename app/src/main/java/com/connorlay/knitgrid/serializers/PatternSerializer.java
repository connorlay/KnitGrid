package com.connorlay.knitgrid.serializers;

import com.connorlay.knitgrid.models.Pattern;
import com.connorlay.knitgrid.models.Stitch;
import com.connorlay.knitgrid.models.StitchPatternRelation;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Created by connorlay on 4/13/16.
 */
public class PatternSerializer implements JsonSerializer, JsonDeserializer {
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

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (typeOfT != Pattern.class) {
            throw new IllegalArgumentException();
        }
        Pattern pattern = new Pattern();

        JsonObject jsonObject = json.getAsJsonObject();
        pattern.setName(jsonObject.get("name").getAsString());
        pattern.setRows(jsonObject.get("rows").getAsInt());
        pattern.setColumns(jsonObject.get("columns").getAsInt());
        pattern.setShowsEvenRows(jsonObject.get("show_even_rows").getAsBoolean());
        pattern.setUuid(jsonObject.get("uuid").getAsLong());

        JsonArray jsonArray = jsonObject.getAsJsonArray("stitches");

        for(JsonElement jsonElement : jsonArray) {
            JsonObject relationObject = jsonElement.getAsJsonObject();
            StitchPatternRelation relation = new StitchPatternRelation();
            relation.setRow(relationObject.get("row").getAsInt());
            relation.setCol(relationObject.get("column").getAsInt());
            relation.setUuid(relationObject.get("uuid").getAsInt());
            // TODO: set color here
            relation.setStitch(Stitch.findByUuid(relationObject.get("stitch").getAsJsonObject().get("uuid").getAsInt()));
            pattern.getStitchRelations().add(relation);
        }

        return pattern;
    }
}
