package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GenZEatsRepository

    val cartItems: StateFlow<List<CartItem>>
    val orders: StateFlow<List<OrderHistory>>
    val userProfile: StateFlow<UserProfile?>
    val reviews: StateFlow<List<DiningReview>>

    private val _isUserLoggedIn = MutableStateFlow(true) // Start with logged in to jump straight to Home, or support easy log out
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    private val _isLoginLoading = MutableStateFlow(false)
    val isLoginLoading: StateFlow<Boolean> = _isLoginLoading.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    // Favorite/Bookmarked restaurants and food IDs
    private val _favoriteIds = MutableStateFlow<Set<String>>(setOf())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    // Search vibes query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = GenZEatsRepository(database)

        cartItems = repository.cartItems
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        orders = repository.orders
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        userProfile = repository.userProfile
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

        reviews = repository.reviews
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Initialize prepopulation
        viewModelScope.launch {
            repository.checkAndPrepopulate()
        }
    }

    fun toggleFavorite(id: String) {
        val current = _favoriteIds.value.toMutableSet()
        if (current.contains(id)) {
            current.remove(id)
        } else {
            current.add(id)
        }
        _favoriteIds.value = current
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Login interaction
    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (email.trim().isEmpty() || !email.contains("@")) {
            _loginError.value = "Enter a valid vibe email! (e.g. your@vibe.com)"
            return
        }
        if (password.length < 4) {
            _loginError.value = "Oops! Password must be at least 4 vibes long."
            return
        }

        _loginError.value = null
        _isLoginLoading.value = true

        viewModelScope.launch {
            // Simulate stylish network response delay
            kotlinx.coroutines.delay(1200)
            _isUserLoggedIn.value = true
            _isLoginLoading.value = false
            onSuccess()
        }
    }

    fun logout() {
        _isUserLoggedIn.value = false
    }

    // Add Burger to Cart details
    fun addToCart(name: String, priceDouble: Double, quantity: Int, size: String, addOns: List<String>, imageUrl: String) {
        viewModelScope.launch {
            // Check if matches an existing item with same size & modifiers
            val currentCart = cartItems.value
            val existing = currentCart.find { it.name == name && it.size == size && it.addOns == addOns }
            if (existing != null) {
                repository.updateCartItem(
                    existing.copy(quantity = existing.quantity + quantity)
                )
            } else {
                repository.insertCartItem(
                    CartItem(
                        name = name,
                        price = priceDouble,
                        quantity = quantity,
                        size = size,
                        addOns = addOns,
                        imageUrl = imageUrl
                    )
                )
            }
        }
    }

    fun updateCartItemQty(id: Int, newQty: Int) {
        viewModelScope.launch {
            if (newQty <= 0) {
                // Delete
                val item = cartItems.value.find { it.id == id }
                if (item != null) repository.deleteCartItem(item)
            } else {
                val item = cartItems.value.find { it.id == id }
                if (item != null) {
                    repository.updateCartItem(item.copy(quantity = newQty))
                }
            }
        }
    }

    fun writeReview(reviewer: String, rating: Int, text: String) {
        if (text.trim().isEmpty()) return
        viewModelScope.launch {
            repository.addReview(
                DiningReview(
                    reviewerName = reviewer,
                    rating = rating,
                    reviewText = text,
                    relativeTime = "Just now",
                    reviewerAvatarUrl = "https://lh3.googleusercontent.com/aida/AP1WRLuf5gR5CxIZeks5Ehws-h41A3dfH51iX-I2CEdupSwVTN62CE8zIyvkuLA1fhXa9lM9PQqjSZNkEDl4VkkkzUqMOGAxAAuWd1fdoa7iA4E4GFNNOCg2U11I2-SjEPIdAFvjlwGHjg87tfD9YhjdGr9DlW3qXeL9F2hSwk-AZMJbJIU6AevjhWhcEblzjCX31i5hDDsovWJqTFMW6GLIB9wgovqa5ln5awXzRbhcPhxwZW_T1USIgf6__Q"
                )
            )
        }
    }

    // Checkout / Place Order action
    fun checkoutCart(onSuccess: () -> Unit) {
        val currentItems = cartItems.value
        if (currentItems.isEmpty()) return

        val totalPr = currentItems.sumOf { it.price * it.quantity }
        val orderDesc = "${currentItems.first().name} and ${currentItems.sumOf { it.quantity } - 1} more items"
        val itemC = currentItems.sumOf { it.quantity }

        viewModelScope.launch {
            // Register ordered item in OrderHistory
            repository.addOrder(
                OrderHistory(
                    restaurantName = "Burger Nirvana", // Predefined restaurant from cart details
                    dateText = "Just now • $itemC Items",
                    itemCount = itemC,
                    totalPrice = totalPr,
                    statusText = "Delivered",
                    imageUrl = currentItems.first().imageUrl
                )
            )
            // Empty shopping cart
            repository.clearCart()

            // Update user point totals
            val oldProfile = userProfile.value
            if (oldProfile != null) {
                val newPointsFloat = (oldProfile.vibePoints.replace("k", "").toFloatOrNull() ?: 0f) + 0.1f
                val roundedPoints = String.format("%.1fk", newPointsFloat)
                repository.updateUserProfile(
                    oldProfile.copy(
                        ordersCount = oldProfile.ordersCount + 1,
                        vibePoints = roundedPoints
                    )
                )
            }
            onSuccess()
        }
    }
}
