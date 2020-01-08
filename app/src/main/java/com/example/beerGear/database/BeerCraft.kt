package com.example.beerGear.database
 
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "beer_craft")
data class BeerCraft(

	@PrimaryKey(autoGenerate = false)
	@field:SerializedName("id")
	var id: Int = 0,

	@field:SerializedName("abv")
	var abv: String? = null,

	@field:SerializedName("ounces")
	var ounces: Double? = null,

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("style")
	var style: String? = null,

	@field:SerializedName("ibu")
	var ibu: String? = null,

	var inCart: Boolean = false
) {
	fun getAlcoholContent(): String {
		return "\u25CF Alcohol Content: $abv"
	}

	fun getIbuValue(): String {
		return "\u25CF IBU: $ibu"
	}
}