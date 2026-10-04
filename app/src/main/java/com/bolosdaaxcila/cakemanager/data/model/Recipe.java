package com.bolosdaaxcila.cakemanager.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes",
        foreignKeys = @ForeignKey(entity = Product.class,
                parentColumns = "id",
                childColumns = "productId",
                onDelete = ForeignKey.RESTRICT),
        indices = {@Index("productId")})
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long productId;
    private String name;
    private String description;

    public Recipe(long productId, String name, String description) {
        this.productId = productId;
        this.name = name;
        this.description = description;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
