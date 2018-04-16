package com.example.elena.shopeasy.realm;

import io.realm.DynamicRealm;
import io.realm.RealmList;
import io.realm.RealmMigration;
import io.realm.RealmObject;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * Created by Absolute on 3/19/2018.
 */

public class RealmMigrations implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if (oldVersion ==4) {
            final RealmObjectSchema recipeInputSchema = schema.get("RecipeInput");
            schema.create("Ingredient").addField("quantity",double.class)
                    .addField("measure",String.class)
                    .addField("ingredient",String.class);
        //    recipeInputSchema.addRealmListField("mIngredientsList",);//to add field
            recipeInputSchema.addRealmListField("mIngredientsList",
                    schema.get("Ingredient"));

        }
    }
}