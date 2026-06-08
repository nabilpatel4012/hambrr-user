package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
class GenZEatsRepository(private val database: AppDatabase) {

    val cartItems: Flow<List<CartItem>> = database.cartItemDao().getCartItems()
    val orders: Flow<List<OrderHistory>> = database.orderHistoryDao().getOrders()
    val userProfile: Flow<UserProfile?> = database.userProfileDao().getUserProfile()
    val reviews: Flow<List<DiningReview>> = database.diningReviewDao().getReviews()

    suspend fun insertCartItem(item: CartItem) {
        database.cartItemDao().insertCartItem(item)
    }

    suspend fun updateCartItem(item: CartItem) {
        database.cartItemDao().updateCartItem(item)
    }

    suspend fun deleteCartItem(item: CartItem) {
        database.cartItemDao().deleteCartItem(item)
    }

    suspend fun clearCart() {
        database.cartItemDao().clearCart()
    }

    suspend fun addOrder(order: OrderHistory) {
        database.orderHistoryDao().insertOrder(order)
    }

    suspend fun addReview(review: DiningReview) {
        database.diningReviewDao().insertReview(review)
    }

    suspend fun updateUserProfile(profile: UserProfile) {
        database.userProfileDao().insertUserProfile(profile)
    }

    suspend fun checkAndPrepopulate() {
        // Prepopulate default profile if not present
        val profile = database.userProfileDao().getUserProfile().firstOrNull()
        if (profile == null) {
            database.userProfileDao().insertUserProfile(
                UserProfile(
                    id = "current",
                    userName = "Alex Rivera",
                    vibePoints = "1.2k",
                    ordersCount = 42,
                    isVibeMember = true,
                    email = "your@vibe.com",
                    address = "Downtown, Seattle",
                    profileImageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLuf5gR5CxIZeks5Ehws-h41A3dfH51iX-I2CEdupSwVTN62CE8zIyvkuLA1fhXa9lM9PQqjSZNkEDl4VkkkzUqMOGAxAAuWd1fdoa7iA4E4GFNNOCg2U11I2-SjEPIdAFvjlwGHjg87tfD9YhjdGr9DlW3qXeL9F2hSwk-AZMJbJIU6AevjhWhcEblzjCX31i5hDDsovWJqTFMW6GLIB9wgovqa5ln5awXzRbhcPhxwZW_T1USIgf6__Q",
                    paymentMethod = "•••• •••• •••• 4242"
                )
            )
        }

        // Prepopulate default reviews if empty
        val existingReviews = database.diningReviewDao().getReviews().firstOrNull()
        if (existingReviews.isNullOrEmpty()) {
            database.diningReviewDao().insertReview(
                DiningReview(
                    reviewerName = "Zoe.Trend",
                    rating = 5,
                    reviewText = "Literally the best truffle fries I've ever had. 🍟 The interior is so aesthetic for the 'gram. Highly recommend the Smash Burger!",
                    relativeTime = "2h ago",
                    reviewerAvatarUrl = "https://lh3.googleusercontent.com/aida/AP1WRLtM11HoA2obfAB_THPnceWhnS0uOOMudF4lktk8w1kYpXIE4JabAIiy245rOOUMq4raxS0R84S-EoMpaDU3vq78f5uY7BMxa_RRcvcaSM_53WrJKijPYF4wOTCVZ1iIt0AwCQLVTC4enDzESoywbM1v5hhFMEJYygtGebJ8_tRrDoJribK-mAasa3JPc-e2bVrokQkeh2rqXAyz9b3ySSF4rGCDlhNgsfw50oBjrxoXCnybTh9ZRxgNpCA"
                )
            )
            database.diningReviewDao().insertReview(
                DiningReview(
                    reviewerName = "LeoTheFoodie",
                    rating = 4,
                    reviewText = "Vibe was immaculate but wait time was a bit long. Food made up for it though. Try the spicy mayo dip! 🌶️",
                    relativeTime = "Yesterday",
                    reviewerAvatarUrl = "https://lh3.googleusercontent.com/aida/AP1WRLt_A_PkJZGssfanm54hrJf6NEiNN-1GxhQxOcUitJCOQsMD39GTg1dMglYMBCUfTv6FfSf9sMeCb-GbZpDTC7eiXRQ2J6n_kgZdX1DHp6cm6LZm4gtquH53sj9KWq9CPaWG5ZJxMtV4O1t8L_H72Kz6ikPD307CazcEppwuhBX5kz2JuXw5xt3841yDNhY8gLVK8SalDdqWjcBvMOnIoBvkygjYL4oQ5qpxigsaXBVSRtOyHU528FQXrrI"
                )
            )
        }

        // Prepopulate default orders if empty
        val existingOrders = database.orderHistoryDao().getOrders().firstOrNull()
        if (existingOrders.isNullOrEmpty()) {
            database.orderHistoryDao().insertOrder(
                OrderHistory(
                    restaurantName = "Neon Pizza Co.",
                    dateText = "Oct 24, 2023 • 2 Items",
                    itemCount = 2,
                    totalPrice = 32.40,
                    statusText = "Delivered",
                    imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLsZounkupv3ALNgKu77C4N_5Uxis6j1YmR9YCve93ua1JrkurZvHLsq9o_f70LDWqrK4QJGNPMY3a-5vPC9oNJM3IsMByiE4XgwZmxvjWwKeaCXUYA_Rjq_RDfjKnCDL1fzmpWI86X_SX-s54cNxMBSUECYORBaLUqN0HDl0FJpE6QB4O-b1r28iwhB1bad5rZpc8eZZ1m186InvFa2PBszQgORyRT3rLuLUYW6hwy--vq9M7fNDCHbcYA"
                )
            )
            database.orderHistoryDao().insertOrder(
                OrderHistory(
                    restaurantName = "Zen Bowl Express",
                    dateText = "Oct 21, 2023 • 1 Item",
                    itemCount = 1,
                    totalPrice = 18.90,
                    statusText = "Delivered",
                    imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLvgsaj84gnzkNl9g0LCaj6QdB_u5MD8QrrSTDJy7x24OW8JGsOLWW8lwlrgnObpbk5y90MvM90trQNopTxOZNUat_uZZYYl_O8Oeg7XBz_K54c75BulRoD4D7Y4KKK6RiZNdqNWPvU0gYSAq2an-xZC13Pavoy_eUrMH8lgteZQOw3k504IGzYZQSpbRt-mQLzYLLnvdbr4W_fPbmSXVAktM2nLpbvuhxhNOURnf1LC58ATWRJWPMMHCyM"
                )
            )
            database.orderHistoryDao().insertOrder(
                OrderHistory(
                    restaurantName = "Glitch Burger",
                    dateText = "Oct 18, 2023 • 3 Items",
                    itemCount = 3,
                    totalPrice = 45.15,
                    statusText = "Cancelled",
                    imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLu-bFFOU9kNFSJLgdxd2vW0lqglvWmdn2WZByVsdoB3JfEAdLPnVyGMMViclZsXCRmyv-h3go-x92odomv-YVRCXaXkotrDx7r3Sg0a3Qjjkcfzx5P3BpN32nJLMCYvpA9RHxFa5iEJmASHlQ58INjrbED1W8K1djzvAASk9pA9bZIBXHHy1TubUy8Qt7dFcfVOLf4iBq3jnMx4o78qUVJ049edTDY3PVrlDVeXEcI8PqlFStf47fhEOA"
                )
            )
            database.orderHistoryDao().insertOrder(
                OrderHistory(
                    restaurantName = "Taco Glitch",
                    dateText = "Oct 15, 2023 • 4 Items",
                    itemCount = 4,
                    totalPrice = 26.00,
                    statusText = "Delivered",
                    imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLvIzPUEzScPTj1ZnzQpoRLWazQkV4L7SY-fiSlAHQXrM0vifi0kv7WCjHFYXcPYwjl3IWpJNrH9OZbWe_boHunblY0CLF1xk6rSElWXRnezWA1n7wvuY6pt0OQFj5Db78-qcfqj4sISDski7phvt9MXT4KK-DcSMq7j2xJn-IO9akS6pXmUVDU5f8DirGz5jaH5LA5EEkHt_pONTerpSAs7fI8gmXRNaO7C460qycpFWezmVxEXxftRl6o"
                )
            )
        }
    }
}
