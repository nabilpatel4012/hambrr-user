package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val price: Double,
    val quantity: Int,
    val size: String,
    val addOns: List<String>, // Saved via TypeConverter
    val imageUrl: String
)

@Entity(tableName = "order_history")
data class OrderHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val restaurantName: String,
    val dateText: String,
    val itemCount: Int,
    val totalPrice: Double,
    val statusText: String, // "Delivered" or "Cancelled"
    val imageUrl: String
)

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: String = "current",
    val userName: String,
    val vibePoints: String,
    val ordersCount: Int,
    val isVibeMember: Boolean,
    val email: String,
    val address: String,
    val profileImageUrl: String,
    val paymentMethod: String
)

@Entity(tableName = "dining_reviews")
data class DiningReview(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val reviewerName: String,
    val rating: Int,
    val reviewText: String,
    val relativeTime: String,
    val reviewerAvatarUrl: String
)

class Converters {
    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return value.joinToString(",")
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        return if (value.trim().isEmpty()) emptyList() else value.split(",")
    }
}
