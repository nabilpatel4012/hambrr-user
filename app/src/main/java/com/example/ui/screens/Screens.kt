package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.data.CartItem
import com.example.data.OrderHistory
import com.example.data.UserProfile
import com.example.ui.MainViewModel

// BRAND STYLING CONSTANTS (Vibrant Cravings Palette)
val PrimaryOrange = Color(0xFFA73A00)
val PrimaryContainerOrange = Color(0xFFFF5C00)
val SoftBg = Color(0xFFFCF9F8)
val DarkText = Color(0xFF1C1B1B)
val LightGreyText = Color(0xFF5B4137)
val SurfaceContainerLowest = Color(0xFFFFFFFF)
val SurfaceContainerLow = Color(0xFFF6F3F2)
val SurfaceContainer = Color(0xFFF0EDEC)
val SurfaceContainerHigh = Color(0xFFEBE7E7)
val SurfaceContainerHighest = Color(0xFFE5E2E1)
val TealAccent = Color(0xFF006970)
val SoftTealBg = Color(0xFFE0F4F6)
val SoftPeachBg = Color(0xFFFFECE5)
val OutlinedCoral = Color(0xFFE4BEB1)
val SoftLightGrey = Color(0xFF8F7065)

@Composable
fun AppBottomNavBar(
    selectedRoute: String,
    onNavigate: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = SoftBg,
        shadowElevation = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            NavBarItem(
                label = "Home",
                icon = Icons.Default.Home,
                selected = selectedRoute == "home",
                testTag = "home_tab",
                onClick = { onNavigate("home") }
            )
            NavBarItem(
                label = "Categories",
                icon = Icons.Default.GridView,
                selected = selectedRoute == "explore",
                testTag = "categories_tab",
                onClick = { onNavigate("explore") }
            )
            NavBarItem(
                label = "Orders",
                icon = Icons.Default.ReceiptLong,
                selected = selectedRoute == "orders",
                testTag = "orders_tab",
                onClick = { onNavigate("orders") }
            )
            NavBarItem(
                label = "Profile",
                icon = Icons.Default.Person,
                selected = selectedRoute == "profile",
                testTag = "profile_tab",
                onClick = { onNavigate("profile") }
            )
        }
    }
}

@Composable
fun RowScope.NavBarItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val contentColor = if (selected) PrimaryOrange else SoftLightGrey
    val containerColor = if (selected) SoftPeachBg else Color.Transparent

    Box(
        modifier = Modifier
            .weight(1f)
            .testTag(testTag)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(containerColor)
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = contentColor
            )
        }
    }
}

// 1. WELCOME BACK (LOGIN SCREEN)
@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val isLoading by viewModel.isLoginLoading.collectAsState()
    val error by viewModel.loginError.collectAsState()

    // High fidelity bg pattern
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBg)
    ) {
        // Red food dot pattern overlay placeholder
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Brand Header logo & tagline
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.RestaurantMenu,
                    contentDescription = "Menu Icon",
                    tint = PrimaryOrange,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = "GenZ Eats",
                    fontSize = 26.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Black,
                    color = PrimaryOrange
                )
            }

            Text(
                text = "Welcome Back",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DarkText,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Ready for another delicious bite?",
                fontSize = 16.sp,
                color = LightGreyText,
                modifier = Modifier.padding(bottom = 36.dp)
            )

            // Email Field
            Text(
                text = "Email Address",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LightGreyText,
                modifier = Modifier.padding(start = 12.dp, bottom = 6.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("email_input")
                    .padding(bottom = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryContainerOrange,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = SurfaceContainerLowest,
                    unfocusedContainerColor = SurfaceContainerLow
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AlternateEmail,
                        contentDescription = "At Email icon",
                        tint = SoftLightGrey
                    )
                },
                placeholder = {
                    Text(
                        text = "your@vibe.com",
                        color = SoftLightGrey.copy(alpha = 0.6f)
                    )
                },
                singleLine = true
            )

            // Password Field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Password",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = LightGreyText
                )
                Text(
                    text = "Forgot Password?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryOrange,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Password reset code sent to vibes!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("password_input")
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryContainerOrange,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = SurfaceContainerLowest,
                    unfocusedContainerColor = SurfaceContainerLow
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock Icon",
                        tint = SoftLightGrey
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Toggle password visibility",
                            tint = SoftLightGrey
                        )
                    }
                },
                placeholder = {
                    Text(
                        text = "••••••••",
                        color = SoftLightGrey.copy(alpha = 0.6f)
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                singleLine = true
            )

            // Live validation error
            if (error != null) {
                Text(
                    text = error!!,
                    color = Color.Red,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 12.dp, start = 8.dp)
                )
            }

            // CTA Sign In Button
            Button(
                onClick = {
                    viewModel.login(email, password) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("sign_in_button")
                    .shadow(12.dp, shape = RoundedCornerShape(16.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainerOrange),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = "Sign In", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowForward, contentDescription = "Forward Icon", tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Or continue with dividers
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier.weight(1f),
                    color = OutlinedCoral.copy(alpha = 0.3f)
                )
                Text(
                    text = "OR CONTINUE WITH",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SoftLightGrey.copy(alpha = 0.5f),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    letterSpacing = 1.5.sp
                )
                Divider(
                    modifier = Modifier.weight(1f),
                    color = OutlinedCoral.copy(alpha = 0.3f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Social Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Google
                Button(
                    onClick = {
                        Toast.makeText(context, "Google registration loaded!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .size(72.dp)
                        .padding(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerHigh),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues()
                ) {
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida/AP1WRLvX6oliWdlFVsH6EgnPI8kOw73ThccuZsdlRSV4SElR3keKLcSgsdx-tj-KzEWxGumqLsUUnVLJLxX9WhokleVyT_ATzKhbU42GqWv4QZEGWeImvqYAEHRhevYcOw4fEUmHKg8RxDs0JIlz1fwP-2wFY3BzilT4s7A4NbuHl7z_CH-SmBwa_VCoK3vAKjwW11G3Q8l8bzPFwOM984ekMUZEdQPaYk4bgNWARv8ka8Eg57C26FPlzDaJKfI",
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(32.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Apps grid
                Button(
                    onClick = {
                        Toast.makeText(context, "Explore menu loaded!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .size(72.dp)
                        .padding(4.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerHigh),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues()
                ) {
                    Icon(
                        imageVector = Icons.Default.Apps,
                        contentDescription = "Apps Icon",
                        tint = DarkText,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Footer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Don't have an account? ", color = LightGreyText)
                Text(
                    text = "Create Account",
                    fontWeight = FontWeight.Bold,
                    color = PrimaryOrange,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Account flow launched!", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Clinging food illustrative piece at bottom right
            Box(
                modifier = Modifier
                    .size(160.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(RoundedCornerShape(24.dp))
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida/AP1WRLtuwaR4dECSlJlHHryrI0mYm3mgfVe29A6a5lBsLBhR60zzL5FTfvwCM70TBHrsejnLq5SPyYMxNEWQqKsq3gYsIY1bB2NPZWQVhc-gapkZUVlDaqxb2SCThGjKIimzF5RrTSfdadK-qmL981uHAiQj2WNq40H3SSs3e4sCAlF4IHZkmlY9FoTND3A6NOOBHlnMisEqCO8Nvj7e0zb3AuKu2uZODJZoV-r0pPeTd4Jb81SeYEB_NyEdgCI",
                    contentDescription = "Cheesy pizza slice logo deco",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// 2. HOME SCREEN (MAIN LISTINGS & CHIPS)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val searchVal by viewModel.searchQuery.collectAsState()
    val cart by viewModel.cartItems.collectAsState()
    val totalCartItemsCount = cart.sumOf { it.quantity }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 76.dp) // Nav bar padding
        ) {
            // Header bar
            HomeScreenHeader(navController)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                // Headline Title
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
                        Text(
                            text = "What's on your mind?",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkText,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Cool Search Bar
                        OutlinedTextField(
                            value = searchVal,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("home_search_bar"),
                            placeholder = {
                                Text(
                                    text = "Search for food, vibes, or cravings...",
                                    color = SoftLightGrey,
                                    fontSize = 14.sp
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search icon",
                                    tint = PrimaryOrange
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryContainerOrange,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = SurfaceContainerLow,
                                unfocusedContainerColor = SurfaceContainerLow
                            ),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )
                    }
                }

                // Food category scrolling quick-chips
                item {
                    HomeScreenChips()
                }

                // Featured Hits Horizontal lists
                item {
                    HomeScreenFeaturedList(navController)
                }

                // Popular Near You List Title
                item {
                    Text(
                        text = "Popular Near You",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkText,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
                    )
                }

                // Popular Items Vertical lists
                item {
                    HomeScreenPopularList(navController)
                }
            }
        }

        // Cart Floating Action Button
        if (totalCartItemsCount > 0) {
            FloatingActionButton(
                onClick = { navController.navigate("orders") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 90.dp, end = 20.dp)
                    .size(64.dp)
                    .testTag("floating_cart_fab"),
                containerColor = PrimaryOrange,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Cart Icon",
                        modifier = Modifier.size(28.dp),
                        tint = Color.White
                    )
                    // Badge count
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(20.dp)
                            .background(TealAccent, CircleShape)
                            .border(1.5.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$totalCartItemsCount",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Bottom Navigation Bar
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            AppBottomNavBar(
                selectedRoute = "home",
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    }
}

@Composable
fun HomeScreenHeader(navController: NavController) {
    Surface(
        color = SoftBg.copy(alpha = 0.95f),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Location clicker
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.clickable { }
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location Indicator",
                    tint = PrimaryOrange,
                    modifier = Modifier.size(20.dp)
                )
                Column {
                    Text(
                        text = "Deliver to",
                        fontSize = 11.sp,
                        color = LightGreyText,
                        fontWeight = FontWeight.Medium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Downtown, Seattle",
                            fontSize = 13.sp,
                            color = DarkText,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = "Expand Location",
                            tint = DarkText,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Title
            Text(
                text = "GenZ Eats",
                fontSize = 22.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Black,
                color = PrimaryOrange
            )

            // Dynamic User Profile icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(2.dp, PrimaryContainerOrange, CircleShape)
                    .clickable { navController.navigate("profile") }
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida/AP1WRLuePDoqZabwjEZzfdyXYej4gN2wSw9pZd5tV-stYIcQjkBsivD1ck3RBRY6PsAFukhMI-fXYJ4fRj5tsUOYpJPYXodvBy5RyV6Fbjx_3HI1OEQtaW00_iObV2TWNuuaOv-NvfMEruXz9cWM7FUJ1XKxYRcOnHKiuJt3nq3PUyefEEyqk_VAuMu4chP2uHA41x1DZT1cSGayo4pu8eilhgxov-KebKyvUZXuHaasaFauAGYWH8EwELAj0Jg",
                    contentDescription = "User avatar tiny topbar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun HomeScreenChips() {
    val options = listOf(
        "All Vibes" to "",
        "Pizza 🍕" to "pizza",
        "Sushi 🍣" to "sushi",
        "Burgers 🍔" to "burgers",
        "Healthy 🥗" to "healthy",
        "Boba 🧋" to "boba"
    )
    var selectedIndex by remember { mutableStateOf(0) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(options.size) { index ->
            val isSelected = index == selectedIndex
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(if (isSelected) PrimaryOrange else SurfaceContainerHigh)
                    .clickable { selectedIndex = index }
                    .padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = options[index].first,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else DarkText
                )
            }
        }
    }
}

@Composable
fun HomeScreenFeaturedList(navController: NavController) {
    val featuredList = listOf(
        FeaturedItem(
            name = "Neon Pepperoni Pizza",
            badgeText = "Top Choice",
            desc = "Spicy, cheesy, and legendary.",
            timeText = "20-25 min",
            imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLtUirTt0-2tEmlm4qRikUreTtHZauYLOq5SsXafXO2CjEWusOZEytoGjx85HSbx3Yx9zNlMjisC9_GgR3s5jisW4iWr3WAv8TQBxaWpdb2pzSUjiBujf9Ekfu4gLlDX6AYc_SpChjp21jsnI15SsrD1XNMIyyT98w5mcY1sUa6hf5e5B03UmV7UJgf7ElT8G4Mfs5cP8MimrdlQZ2CFyUAvFxN3-lGkydS57rqBMWxW-4H0q5vtpFCTqww"
        ),
        FeaturedItem(
            name = "The Glow-Up Burger",
            badgeText = "New Drop",
            desc = "Double wagyu with secret drip.",
            timeText = "15-20 min",
            imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLsKgrC8IiUkVPEM06Ycx52V_iCv4ymHfwNt4mUvZdoo2hkf40dKv8nBbpW2UMV1-M-eqhD6BbB1t_5m773ycJgyTuebGf3ZBluXYInyscdCZNfalyPfVvrCeRzxFBHkbsOOge3g4ssW0ZetTKSQuifA51pv-bygsaMBm7-IJEX9GE4106CKz40L4tWbQUztVpeSWuSsI-tvSZAL7r-83hfopM7maEv5dGX27jtEEhiiNg_mNct9VLgKTtY"
        )
    )

    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = "Featured Hits",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DarkText
            )
            Text(
                text = "See all",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryOrange,
                modifier = Modifier.clickable { }
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.height(340.dp)
        ) {
            items(featuredList) { item ->
                Box(
                    modifier = Modifier
                        .width(280.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(32.dp))
                        .clickable {
                            if (item.name.contains("Burger")) {
                                navController.navigate("product_detail")
                            } else {
                                navController.navigate("explore")
                            }
                        }
                ) {
                    // Large visual image
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = item.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Overlay gradient
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f)),
                                    startY = 200f
                                )
                            )
                    )

                    // Text overlay content
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (item.badgeText.contains("Drop")) TealAccent else PrimaryOrange)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = item.badgeText.uppercase(),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = item.name,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            lineHeight = 26.sp
                        )

                        Text(
                            text = item.desc,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Time duration Icon",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = item.timeText,
                                fontSize = 12.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreenPopularList(navController: NavController) {
    val items = listOf(
        PopularItem(
            name = "Tokyo Drift Sushi",
            rating = "4.8",
            tags = "Sushi • Japanese • $$$",
            delivery = "Free Delivery",
            time = "25 min",
            imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLtPZrUMwZTzZKGlFTC-7pXj5JalDWsDqHb11WJsh9aVAQeYed4JslBHmG2or3I1onjaiDiYaGgGWAI6qZeNGwLLJITytvAb98WCx25xaGPxwY0AcNiVMkGp9hC8PhIkNKvx9FnRe5d3Ku_lV-U0FyrOSIUjjpoKipnqKlLHor1XCACp86uEUM2_WsBLvG27VsyRcoIBxD7HMVRpxutFLto6yv5JES2EdW4q8Hew7FQRAZ8SIs9eZBNvLA"
        ),
        PopularItem(
            name = "Main Character Bowls",
            rating = "4.5",
            tags = "Healthy • Salads • $$",
            delivery = "$2.99 Delivery",
            time = "15 min",
            imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLu8aaOWeMQcugwoGALbPe8lrHffJKIQKW9oxB8yVsntuQvTz6pk7oek-hSjuKIrIXgYsLfHnHeI6N5gIYEFTnhKES1c4Vx8B0yc91fhZEg0W2JcXZv-jqKu_Mc6ePXZlmaoCo_iQHbtK2QKiUHlOsxu5pA5yFzBaBxGVXJzivdL6xWWtFIhusCuZ_AI5imsLFyt5Jf7mB1GEs9zJazp-AW79NxoASz_vLo6tVLzHYQDnFgme-HOGNpxmeY"
        ),
        PopularItem(
            name = "Vibe Check Grill",
            rating = "4.9",
            tags = "Burgers • American • $$",
            delivery = "Free Delivery",
            time = "30 min",
            imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLu7TqcVhTRL44xXlqKO_FeU-3TfomMo-Cta0-9LgS9sUFVFDCbg_N0zOdoqyGz7INFpLUz_ayKerwMZQUm7WZFYpDQ1SJqrPuzAtstJJClEgy9Ltb86sEG1Y1AF3irjqxUGMJ1daYBenFAKzE96eOUB2RR3QrtTaOHgAR8-oZcPphWurX5U4OuLOZVgkOd7ocphrNkl5L05vFOcE6dsxMxnuRLPclPmNrcJp02zOY1KJLXo5saJgPq9DA"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { navController.navigate("restaurant_detail") }
                    .background(Color.Transparent)
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Square food img icon
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = item.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Rating badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SurfaceContainerHigh)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star ratings",
                                tint = PrimaryOrange,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = item.rating,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText
                            )
                        }
                    }

                    Text(
                        text = item.tags,
                        fontSize = 13.sp,
                        color = LightGreyText,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsBike,
                                contentDescription = "Bike delivery",
                                tint = LightGreyText,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = item.delivery,
                                fontSize = 11.sp,
                                color = LightGreyText,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = "Timing indicator",
                                tint = LightGreyText,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = item.time,
                                fontSize = 11.sp,
                                color = LightGreyText,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

// 3. EXPLORE VIBES SCREEN (BENTO-ISH CATEGORIES GRID)
@Composable
fun ExploreVibesScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val searchVal by viewModel.searchQuery.collectAsState()

    val categories = listOf(
        ExploreCategory(
            name = "Asian Fusion",
            isTrending = true,
            isLarge = true,
            imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLvqECyEtaI-Gn4dHYnVQpqz2gor1SYwSmvRK0QB6fTLMEzIrrLq_zWF9zk7iA4J1m-Wsy7oDtW3pyGuE9mzqM5xP-268Iu-HsVa5zjxRX4MChIAT2vHuDydpnM0MyYaqwklwa1y4gQTjgyV8jih9FOgpyYMsertPOc-7H9tI1EIDMNsVgZ6UKJczdsPddQ8TcZ0rf1LYOiCSDmB3EJnq8iQsO5nFRflDs5wZFuc-PeMaLnpI6w4YXg7pg",
            testTag = "category_asian"
        ),
        ExploreCategory(
            name = "Italian",
            isTrending = false,
            isLarge = false,
            imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLsAo-K1B9HndP3dg_btjp9FuzEC9ZK9R7qbQGM41pS8UtyQ9YH_UBirKc-fuTqPvZKfZfw_1ieq158cYpxQbaQN206KemyyAuzP2XIMEA7pfcCq7_P1nAUPISMIVhmsExYN1XvO_VN9OKb0sYXzVFydutZdVxx6_pR5mZJJ56HMM8RjBVMJjGjEU8evvHLe_WqBoaU3rhWK6mVyEZWygiLc2O_8GsC4-KH5ONYU9L3BMBOpRqwSR82xJso",
            testTag = "category_italian"
        ),
        ExploreCategory(
            name = "Fast Food",
            isTrending = false,
            isLarge = false,
            imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLuhzeH3bkKbPGUud0MmPeI2X82-e0ogB5h6A-8cUKxUlNYSsV23H9w3R74QB--6bnNNbaNK9TOQT-cvTSXUEdHPk5K-lEPP10XKMtfdte2FZkRn1aetgMtDmPDMYlAuNnfAEmxFfnhjJ0tzMryPiLt5Mu-1K_DLq2iotFhVxtTOaHIiMbYPvWVN3AcBJpCeKYRVLARtzFYnH6bQI2mEeMjIUAl57teEBQYJrzlUbBE5MWIZYX0vp16jOIQ",
            testTag = "category_fastfood"
        ),
        ExploreCategory(
            name = "Desserts",
            isTrending = false,
            isLarge = false,
            imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLsUEhhLayWY38UOVItWQ1FQY8kjw411oqeCehnBVWdfaaxb3QluM9fVI9AjSlYzacfuRD9S2j2JcGRk53p6kcZ3FZuqcuy-MsRg06rzHsrdJtQhiPGLLckisp7i9Y-3d179LlPHsvBPzzgzKX40R7lVuLCuR7jBZVlgWDhnEUkHYR6hQ2_rC1Czbl1wTe36RJukyQXudcvD9LjwAQWe0F7MnQzRQApPevchyvjQJjrIw--H6A-5fbPiMA",
            testTag = "category_desserts"
        ),
        ExploreCategory(
            name = "Beverages",
            isTrending = false,
            isLarge = false,
            imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLsnHsYNbmEY0VUrXveou4gYGBK634ort_LmfKfbGujIIz4pq5IH9zT3kJ3A3Doq_YF40uZnutHG4-QlwivkaiceN71Y71KSdgJ-w-o6qyH4B2wWF3ZxAXg5Q_VAWuz7_hh5CmiGLj-0RSBzVIHmi5f-sYu4OEQAQthYOjNiHePZaLjSpDoeWNb87fxOwrwL-wn4i4wKRly6xMcszY4uglNbvLth2Lv8rHyJ4JKtUgYK012zZvU_uZajRds",
            testTag = "category_beverages"
        ),
        ExploreCategory(
            name = "Healthy Eats",
            isTrending = false,
            isLarge = true,
            imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLuk9FOzqLNj7u5B-Nxfxmhl1UxwB4XdRiATbPbydEC5fKUoFJph4PnM0ojW55gV1mXOPD-T9k3dxrYyGjCNck6cIZPJFEuNXudUTlOeJ_MBWAcWzDzLwv55sjZcF68ToymRqvRT3694izyCYVALz7a0JCjGfhmG-Gxcf4xDk4Pzjng_8VnkVCWN278ebRK8JXphuIPzDY3HaMuF_FLYOPuP_ABL2BCjFcdNkqgTBtbLMuq45e0Ed7vqGrg",
            testTag = "category_healthy"
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 76.dp)
        ) {
            // General top bar
            StandardHeaderTitle(titleLabel = "Explore Vibes")

            // Grid content
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Search cravings box
                item {
                    OutlinedTextField(
                        value = searchVal,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        placeholder = {
                            Text(text = "Craving something specific?", color = SoftLightGrey, fontSize = 14.sp)
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search category", tint = SoftLightGrey)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryContainerOrange,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = SurfaceContainerLow,
                            unfocusedContainerColor = SurfaceContainerLow
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )
                }

                // Header subtitle
                item {
                    Column(modifier = Modifier.padding(top = 4.dp)) {
                        Text(
                            text = "Explore Vibes",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkText
                        )
                        Text(
                            text = "Find your next meal by category",
                            fontSize = 15.sp,
                            color = LightGreyText
                        )
                    }
                }

                // Bento list layout custom arrangement
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Category 1: Asian Fusion (Large, full width, Trending)
                        BentoCategoryCard(categories[0], heightDp = 180, onCardClick = { navController.navigate("product_detail") })

                        // Small category cards side-by-side
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                BentoCategoryCard(categories[1], heightDp = 140, onCardClick = { navController.navigate("restaurant_detail") })
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                BentoCategoryCard(categories[2], heightDp = 140, onCardClick = { navController.navigate("product_detail") })
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                BentoCategoryCard(categories[3], heightDp = 140, onCardClick = { navController.navigate("restaurant_detail") })
                            }
                            Box(modifier = Modifier.weight(1f)) {
                                BentoCategoryCard(categories[4], heightDp = 140, onCardClick = { navController.navigate("restaurant_detail") })
                            }
                        }

                        // Category 6: Healthy Eats (Large, full width, Eco rating)
                        BentoCategoryCard(categories[5], heightDp = 140, onCardClick = { navController.navigate("restaurant_detail") })
                    }
                }

                // Dietary Filters Label
                item {
                    Text(
                        text = "DIETARY FILTERS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightGreyText,
                        modifier = Modifier.padding(top = 16.dp),
                        letterSpacing = 1.5.sp
                    )
                }

                // Dietary Pills Row
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DietaryFilterPill("Vegan", isSelected = true)
                        DietaryFilterPill("Gluten-Free", isSelected = false)
                        DietaryFilterPill("Keto", isSelected = false)
                        DietaryFilterPill("Halal", isSelected = false)
                    }
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            AppBottomNavBar(
                selectedRoute = "explore",
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    }
}

@Composable
fun StandardHeaderTitle(titleLabel: String) {
    Surface(
        color = SoftBg.copy(alpha = 0.95f),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Pin indicator logo",
                    tint = PrimaryOrange,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Central Park, NY",
                    fontSize = 14.sp,
                    color = LightGreyText,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "GenZ Eats",
                fontSize = 22.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Black,
                color = PrimaryOrange
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainerHigh)
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida/AP1WRLuePDoqZabwjEZzfdyXYej4gN2wSw9pZd5tV-stYIcQjkBsivD1ck3RBRY6PsAFukhMI-fXYJ4fRj5tsUOYpJPYXodvBy5RyV6Fbjx_3HI1OEQtaW00_iObV2TWNuuaOv-NvfMEruXz9cWM7FUJ1XKxYRcOnHKiuJt3nq3PUyefEEyqk_VAuMu4chP2uHA41x1DZT1cSGayo4pu8eilhgxov-KebKyvUZXuHaasaFauAGYWH8EwELAj0Jg",
                    contentDescription = "User profile thumbnail top bar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun BentoCategoryCard(
    category: ExploreCategory,
    heightDp: Int,
    onCardClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightDp.dp)
            .clip(RoundedCornerShape(32.dp))
            .testTag(category.testTag)
            .clickable(onClick = onCardClick)
    ) {
        AsyncImage(
            model = category.imageUrl,
            contentDescription = category.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)),
                        startY = 100f
                    )
                )
        )

        // Contents
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp)
        ) {
            if (category.isTrending) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PrimaryOrange)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "TRENDING",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.name,
                    fontSize = if (category.isLarge) 24.sp else 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                if (category.name == "Healthy Eats") {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = "Eco symbol",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else if (category.isTrending) {
                    Icon(
                        imageVector = Icons.Default.ElectricBolt,
                        contentDescription = "Bolt indicator",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DietaryFilterPill(label: String, isSelected: Boolean) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(if (isSelected) SoftPeachBg else SurfaceContainer)
            .border(
                1.dp,
                if (isSelected) PrimaryOrange.copy(alpha = 0.3f) else Color.Transparent,
                RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) PrimaryOrange else DarkText
        )
    }
}

// 4. PRODUCT DETAIL SCREEN (THE ULTIMATE SMASH BURGER DETAILED COMPONENT)
@Composable
fun ProductDetailScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val favs by viewModel.favoriteIds.collectAsState()
    val isFav = favs.contains("ultimate_smash_burger")
    val context = LocalContext.current

    var selectedSize by remember { mutableStateOf("Standard") }
    var extraCheese by remember { mutableStateOf(false) }
    var bacon by remember { mutableStateOf(false) }
    var noOnions by remember { mutableStateOf(false) }
    var spicyDip by remember { mutableStateOf(false) }

    var quantity by remember { mutableStateOf(1) }

    // Price calculation
    val basePrice = 14.50
    val sizeAdded = if (selectedSize == "The Beast") 4.00_000_00 else 0.0
    val totalPerSingle = basePrice + sizeAdded
    val totalPrice = totalPerSingle * quantity

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp) // Bottom CTA spacing
        ) {
            // Hero section with image aspect 4:5
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 5f)
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida/AP1WRLsGr0rgYo7TxmEeIPO4rGY1WK-wcNU-5GmrgJ7MD2jsg2V3KynPxtaAdDSsYM5V2Ja4cwDVvaCUqDvtV0XqhQHWH-kUz4ekhiSSegUsFCWnfojJLwMlsq3qZxqzdI52KO6umiJpWqZI0Q2Hz5SfpnrIKW_LLa97hxdh3WTArgPg9Lx0OQPkMWcr6Fv7FufENrHCsQ_AeX4SkifDZ6uMFirCfeEbKQJiiu_9WMx3ghoapYoKhKloPr7emDE",
                    contentDescription = "The Ultimate Smash Burger epic close-up",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Top Floating back navigation actions
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .size(48.dp)
                            .background(SoftBg.copy(alpha = 0.85f), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Go back",
                            tint = DarkText
                        )
                    }

                    // Fav & Share
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.toggleFavorite("ultimate_smash_burger") },
                            modifier = Modifier
                                .size(48.dp)
                                .background(SoftBg.copy(alpha = 0.85f), CircleShape)
                        ) {
                            Icon(
                                imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite product",
                                tint = if (isFav) Color.Red else DarkText
                            )
                        }

                        IconButton(
                            onClick = {
                                Toast.makeText(context, "Link copied to vibes clipboard!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .background(SoftBg.copy(alpha = 0.85f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share item link",
                                tint = DarkText
                            )
                        }
                    }
                }

                // Shadowed bottom scrim inside image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, SoftBg),
                                startY = 0f
                            )
                        )
                )
            }

            // Text Content canvas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                // Title and pricing row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "The Ultimate\nSmash Burger",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkText,
                        lineHeight = 32.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "$14.50",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryOrange
                    )
                }

                // Average and Reviews count
                Row(
                    modifier = Modifier.padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Icon(imageVector = Icons.Default.Star, contentDescription = "", tint = PrimaryOrange, modifier = Modifier.size(16.dp))
                        Icon(imageVector = Icons.Default.Star, contentDescription = "", tint = PrimaryOrange, modifier = Modifier.size(16.dp))
                        Icon(imageVector = Icons.Default.Star, contentDescription = "", tint = PrimaryOrange, modifier = Modifier.size(16.dp))
                        Icon(imageVector = Icons.Default.Star, contentDescription = "", tint = PrimaryOrange, modifier = Modifier.size(16.dp))
                        Icon(imageVector = Icons.Default.StarHalf, contentDescription = "", tint = PrimaryOrange, modifier = Modifier.size(16.dp))
                    }
                    Text(
                        text = "4.8 (1.2k+ reviews)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightGreyText
                    )
                }

                // Long description
                Text(
                    text = "Our signature double-patty wagyu smash burger with house-made truffle aioli, aged cheddar, and crispy shallots on a butter-toasted artisan brioche bun. Experience the crunch.",
                    fontSize = 15.sp,
                    color = LightGreyText,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Select size chips selector
                Text(
                    text = "Select Size",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkText,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    // Standard
                    val standardSelected = selectedSize == "Standard"
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (standardSelected) PrimaryContainerOrange else Color.Transparent)
                            .border(2.dp, if (standardSelected) Color.Transparent else OutlinedCoral, RoundedCornerShape(24.dp))
                            .clickable { selectedSize = "Standard" }
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Standard",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (standardSelected) Color.White else LightGreyText
                        )
                    }

                    // The Beast
                    val beastSelected = selectedSize == "The Beast"
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(if (beastSelected) PrimaryContainerOrange else Color.Transparent)
                            .border(2.dp, if (beastSelected) Color.Transparent else OutlinedCoral, RoundedCornerShape(24.dp))
                            .clickable { selectedSize = "The Beast" }
                            .padding(horizontal = 24.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "The Beast (+ $4.00)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (beastSelected) Color.White else LightGreyText
                        )
                    }
                }

                // Customize modifier title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Customize",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkText
                    )
                    Text(
                        text = "Pick as many as you want",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightGreyText
                    )
                }

                // Checkbox items configuration in grid bento columns
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        CustomizeCard(label = "Extra Cheese", checked = extraCheese, onToggle = { extraCheese = it })
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        CustomizeCard(label = "Bacon", checked = bacon, onToggle = { bacon = it })
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        CustomizeCard(label = "No Onions", checked = noOnions, onToggle = { noOnions = it })
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        CustomizeCard(label = "Spicy Dip", checked = spicyDip, onToggle = { spicyDip = it })
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // High Protein and Farm fresh dynamic labeling chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(SoftTealBg)
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.LocalFireDepartment, contentDescription = "Fire logo", tint = TealAccent, modifier = Modifier.size(16.dp))
                        Text(text = "High Protein", color = TealAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(SoftPeachBg)
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Eco, contentDescription = "Eco logo", tint = PrimaryOrange, modifier = Modifier.size(16.dp))
                        Text(text = "Farm Fresh", color = PrimaryOrange, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Sticky Bottom CTA Quantity and action bar
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding(),
            color = SoftBg.copy(alpha = 0.95f),
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quantity Selector pill shape
                Row(
                    modifier = Modifier
                        .height(56.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .background(SurfaceContainerHigh)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier.testTag("quantity_decrease")
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease count", tint = LightGreyText)
                    }
                    Text(
                        text = "$quantity",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        modifier = Modifier.width(18.dp),
                        textAlign = TextAlign.Center
                    )
                    IconButton(
                        onClick = { quantity++ },
                        modifier = Modifier.testTag("quantity_increase")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Increase count", tint = LightGreyText)
                    }
                }

                // Add to Cart Main Action button
                val addOnsList = remember(extraCheese, bacon, noOnions, spicyDip) {
                    val list = mutableListOf<String>()
                    if (extraCheese) list.add("Cheese")
                    if (bacon) list.add("Bacon")
                    if (noOnions) list.add("No Onions")
                    if (spicyDip) list.add("Spicy Dip")
                    list
                }

                Button(
                    onClick = {
                        viewModel.addToCart(
                            name = "The Ultimate Smash Burger",
                            priceDouble = totalPerSingle,
                            quantity = quantity,
                            size = selectedSize,
                            addOns = addOnsList,
                            imageUrl = "https://lh3.googleusercontent.com/aida/AP1WRLsGr0rgYo7TxmEeIPO4rGY1WK-wcNU-5GmrgJ7MD2jsg2V3KynPxtaAdDSsYM5V2Ja4cwDVvaCUqDvtV0XqhQHWH-kUz4ekhiSSegUsFCWnfojJLwMlsq3qZxqzdI52KO6umiJpWqZI0Q2Hz5SfpnrIKW_LLa97hxdh3WTArgPg9Lx0OQPkMWcr6Fv7FufENrHCsQ_AeX4SkifDZ6uMFirCfeEbKQJiiu_9WMx3ghoapYoKhKloPr7emDE"
                        )
                        Toast.makeText(context, "Added $quantity Smash Burger to Your Eats!", Toast.LENGTH_SHORT).show()
                        navController.navigateUp()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                        .testTag("add_to_cart_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainerOrange),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Add to Cart", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text(
                            text = String.format("$%.2f", totalPrice),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomizeCard(
    label: String,
    checked: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(if (checked) SoftPeachBg else SurfaceContainerLow)
            .border(
                2.dp,
                if (checked) PrimaryContainerOrange else Color.Transparent,
                RoundedCornerShape(16.dp)
            )
            .clickable { onToggle(!checked) }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (checked) PrimaryOrange else Color.Transparent)
                    .border(2.dp, if (checked) Color.Transparent else SoftLightGrey, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (checked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Checked custom option",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

// 5. RESTAURANT DETAIL SCREEN (BURGER NIRVANA DEEP COMPONENT VIEWS)
@Composable
fun RestaurantDetailScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val reviews by viewModel.reviews.collectAsState()
    val context = LocalContext.current

    var writeReviewText by remember { mutableStateOf("") }
    var writeReviewRating by remember { mutableStateOf(5) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp) // Nav bar bar space
        ) {
            // Parallax epic banner header image (Burger Nirvana design layout)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(397.dp)
            ) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida/AP1WRLsdn51-4o16peweBywkTEgR-jtCicDMtF9OmfcL8oqXSD-_58b36axxPXqJqiyTwhz7_VWQ4i-ZLxINs4dnCacqx0NttVYt1kT5A9nO_8V_9PMcaPdz9HqjCPz1cbMSXIKZO2BSi1PuTBI2WmLQp8k878trWa_OTY8EH31d9T0wMXEUiwM9h2fUiYqeZ-9l7LV0a3zwzsJ9WfIWz-MCB8MGK81pqYfu8gX-DlhfZRizQrbZVfO2E8Yikg",
                    contentDescription = "Burger Nirvana restaurant beautiful neon vibe interior",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Navigation top standard overlay
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier
                            .size(40.dp)
                            .background(SoftBg.copy(alpha = 0.85f), CircleShape)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Go back", tint = PrimaryOrange)
                    }

                    Text(
                        text = "GenZ Eats",
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(2.dp, PrimaryContainerOrange, CircleShape)
                    ) {
                        AsyncImage(
                            model = "https://lh3.googleusercontent.com/aida/AP1WRLuUnPTCuo7Y5mMtIL5H9XfJH2PMtePGWx-a-LEEu0Tb1Ep1xvY_3VbBk2hIc8mYHKLAicCKCZpfGMbvOsO2ofGQxhXYgDSD3ZckEm0x3YVkzjLXKDJGNbp4blHKbc4BjXt8T_vSo9aADp3HYj9cxHf9292S43C1kbCXXCCqDs5VfhFc3ikV6SqiKMO7iwpQsrsHg7ADE5yj2B56C8crFQzMsATd3Vd8EdaOPcYc1n3dfB4dnPL0FDt4MA",
                            contentDescription = "User profile thumbnail",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Banner bottom label scrim
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f)),
                                startY = 150f
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(PrimaryOrange)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(text = "Top Rated", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.25f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Star, contentDescription = "Reviews count rating", tint = Color.White, modifier = Modifier.size(12.dp))
                            Text(text = "4.9 (2k+)", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(
                        text = "Burger Nirvana",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = "Artisanal Burgers & Hand-Cut Fries",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // Quick Call & Directions buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .offset(y = (-20).dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        Toast.makeText(context, "Dialing Burger Nirvana vibes...", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .shadow(8.dp, RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Call, contentDescription = "Call phone icon", tint = Color.White)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Call Now", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Button(
                    onClick = {
                        Toast.makeText(context, "Directions map code launched!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp)
                        .shadow(2.dp, RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerHighest),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, OutlinedCoral.copy(alpha = 0.5f))
                ) {
                    Icon(imageVector = Icons.Default.Directions, contentDescription = "Get directions vector", tint = PrimaryOrange)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Directions", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkText)
                }
            }

            // Details cards list
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Location Details Row card
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainerLow)
                        .border(1.dp, OutlinedCoral.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(PrimaryOrange.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Pin indicator logo", tint = PrimaryOrange)
                    }

                    Column {
                        Text(text = "Location", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkText)
                        Text(text = "128 Downtown Street, Foodie District, NY 10001", fontSize = 14.sp, color = LightGreyText)
                    }
                }

                // Timing details
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(SurfaceContainerLow)
                        .border(1.dp, OutlinedCoral.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(PrimaryOrange.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Schedule, contentDescription = "Clock indicators", tint = PrimaryOrange)
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Opening Hours", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkText)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Today: 11:00 AM - 11:00 PM", fontSize = 14.sp, color = LightGreyText)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(TealAccent.copy(alpha = 0.1f))
                                    .padding(vertical = 2.dp, horizontal = 6.dp)
                            ) {
                                Text(text = "OPEN", color = TealAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                // Static minimalistic visual mapping coordinates
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Find Us",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkText
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(192.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(2.dp, SurfaceContainerHighest, RoundedCornerShape(16.dp))
                ) {
                    AsyncImage(
                        model = "https://lh3.googleusercontent.com/aida/AP1WRLvJO4M39-q51UDO3fshoAHSFeUZikyTxmBFCpgpksxfDXj_EM-C_2wKpcSByuyNDBpsg0Sd4xCkNE5DQTCTaqGKCsQOWA7lgwBj36cWzl_WTfRQyAmLimWMkZ3FF23u3icigmGLTtIQx2PhDpCmNbiwLZHiCf5DWrtUBgd0G19qAOPhSDgtVHwM_2FzmTuTlM_Wwu2GWAM5hvZM3U0uMW2mG_hnUbplXeUSlbNP2siST95PwD0hM5ARwqk",
                        contentDescription = "City center high contrast map illustration placeholder",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Bouncing central marker logo
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(48.dp)
                            .background(PrimaryOrange, CircleShape)
                            .shadow(12.dp, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Active map pin icon", tint = Color.White, modifier = Modifier.size(30.dp))
                    }
                }

                // Customer reviews / vibe list
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Vibe Check",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkText
                    )
                }

                // Interactive write review box
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    border = BorderStroke(1.dp, OutlinedCoral.copy(alpha = 0.3f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Add Your Vibe Review", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkText)
                        Spacer(modifier = Modifier.height(8.dp))

                        // Star selection indicators
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            (1..5).forEach { stars ->
                                val active = stars <= writeReviewRating
                                Icon(
                                    imageVector = if (active) Icons.Default.Star else Icons.Default.StarOutline,
                                    contentDescription = "$stars stars selection",
                                    tint = PrimaryOrange,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clickable { writeReviewRating = stars }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = writeReviewText,
                            onValueChange = { writeReviewText = it },
                            placeholder = {
                                Text(text = "Review the aesthetic, flavor vibes, crispy shallots...", fontSize = 13.sp, color = SoftLightGrey)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .testTag("writereview_text_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryContainerOrange,
                                unfocusedBorderColor = SurfaceContainerHighest,
                                focusedContainerColor = SurfaceContainerLow,
                                unfocusedContainerColor = SurfaceContainerLow
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                if (writeReviewText.trim().isEmpty()) {
                                    Toast.makeText(context, "Review can't be empty, vibe check!", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.writeReview("You (Vibe Foodie)", writeReviewRating, writeReviewText)
                                    writeReviewText = ""
                                    writeReviewRating = 5
                                    Toast.makeText(context, "Review posted! Aesthetic updated.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier
                                .align(Alignment.End)
                                .testTag("write_review_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Post Vibe", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Render lists of review entities
                reviews.forEach { rev ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                        border = BorderStroke(1.dp, OutlinedCoral.copy(alpha = 0.2f)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(SurfaceContainerHigh)
                                    ) {
                                        AsyncImage(
                                            model = rev.reviewerAvatarUrl,
                                            contentDescription = rev.reviewerName,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    Column {
                                        Text(text = rev.reviewerName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = DarkText)
                                        Row {
                                            (1..5).forEach { star ->
                                                Icon(
                                                    imageVector = if (star <= rev.rating) Icons.Default.Star else Icons.Default.StarOutline,
                                                    contentDescription = "",
                                                    tint = PrimaryOrange,
                                                    modifier = Modifier.size(12.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                Text(
                                    text = rev.relativeTime,
                                    fontSize = 11.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = LightGreyText.copy(alpha = 0.6f)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = rev.reviewText,
                                fontSize = 14.sp,
                                color = LightGreyText,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }

                // Standard placeholder review link elements
                Button(
                    onClick = {
                        Toast.makeText(context, "Full list of 152 reviews loaded!", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    border = BorderStroke(2.dp, Brush.sweepGradient(listOf(PrimaryOrange, OutlinedCoral, PrimaryOrange))),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "See All 152 Reviews", color = DarkText, fontWeight = FontWeight.Bold)
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            AppBottomNavBar(
                selectedRoute = "explore",
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    }
}

// 6. PROFILE SCREEN (ALEX RIVERA STATS & LIST MENU OPTIONS)
@Composable
fun ProfileScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val profile by viewModel.userProfile.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp)
        ) {
            // Top header bar standard
            StandardHeaderTitle(titleLabel = "Alex Rivera Profile")

            // Visual Profile avatar and member badge
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.size(128.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .border(4.dp, PrimaryContainerOrange, CircleShape)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = profile?.profileImageUrl ?: "https://lh3.googleusercontent.com/aida/AP1WRLuf5gR5CxIZeks5Ehws-h41A3dfH51iX-I2CEdupSwVTN62CE8zIyvkuLA1fhXa9lM9PQqjSZNkEDl4VkkkzUqMOGAxAAuWd1fdoa7iA4E4GFNNOCg2U11I2-SjEPIdAFvjlwGHjg87tfD9YhjdGr9DlW3qXeL9F2hSwk-AZMJbJIU6AevjhWhcEblzjCX31i5hDDsovWJqTFMW6GLIB9wgovqa5ln5awXzRbhcPhxwZW_T1USIgf6__Q",
                            contentDescription = "Alex Rivera Epic Stylized Profile",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Floating mini-pencil button
                    IconButton(
                        onClick = {
                            Toast.makeText(context, "Launch Edit Profile camera vibes!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                            .background(PrimaryOrange, CircleShape)
                            .shadow(4.dp, CircleShape)
                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit profiles", tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = profile?.userName ?: "Alex Rivera",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DarkText
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(SoftTealBg)
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Badge tick verified logo",
                        tint = TealAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Vibe Member",
                        color = TealAccent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Stats Bento Grid columns
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Orders Total Point Card
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "${profile?.ordersCount ?: 42}",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryOrange
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Orders",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LightGreyText
                        )
                    }
                }

                // Vibe Points
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLow),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = profile?.vibePoints ?: "1.2k",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TealAccent
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Vibe Points",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LightGreyText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Lists profiles navigation button menu items
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ProfileOptionRow(
                    label = "Edit Profile",
                    icon = Icons.Default.Person,
                    containerColor = SoftPeachBg,
                    tintColor = PrimaryOrange
                ) {
                    Toast.makeText(context, "Edit Profile parameters loaded!", Toast.LENGTH_SHORT).show()
                }

                ProfileOptionRow(
                    label = "Payment Methods",
                    icon = Icons.Default.Payments,
                    containerColor = SoftTealBg,
                    tintColor = TealAccent
                ) {
                    Toast.makeText(context, "Your payment type: MasterCard ${profile?.paymentMethod}", Toast.LENGTH_LONG).show()
                }

                ProfileOptionRow(
                    label = "Delivery Addresses",
                    icon = Icons.Default.Map,
                    containerColor = SoftPeachBg,
                    tintColor = PrimaryContainerOrange
                ) {
                    Toast.makeText(context, "Default delivery to: Downtown, Seattle", Toast.LENGTH_SHORT).show()
                }

                ProfileOptionRow(
                    label = "Notifications",
                    icon = Icons.Default.Notifications,
                    containerColor = SurfaceContainerHighest,
                    tintColor = DarkText,
                    badgeCount = 1
                ) {
                    Toast.makeText(context, "All notification settings are turned on!", Toast.LENGTH_SHORT).show()
                }

                ProfileOptionRow(
                    label = "Help Center",
                    icon = Icons.Default.HelpCenter,
                    containerColor = SurfaceContainerLow,
                    tintColor = SoftLightGrey
                ) {
                    Toast.makeText(context, "GenZ Help Vibe chat launched!", Toast.LENGTH_SHORT).show()
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Log out Button
            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp)
                    .height(56.dp)
                    .testTag("logout_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = BorderStroke(2.dp, Color.Red),
                shape = RoundedCornerShape(28.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = "Exit icon", tint = Color.Red)
                    Text(text = "Log out", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Red)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Version 2.4.1 (GenZ Edition)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = SoftLightGrey.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            AppBottomNavBar(
                selectedRoute = "profile",
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    }
}

@Composable
fun ProfileOptionRow(
    label: String,
    icon: ImageVector,
    containerColor: Color,
    tintColor: Color,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(containerColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = label, tint = tintColor)
                }

                Text(
                    text = label,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (badgeCount > 0) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(PrimaryContainerOrange, CircleShape)
                    )
                }

                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Forward link",
                    tint = SoftLightGrey
                )
            }
        }
    }
}

// 7. ORDERS SCREEN (YOUR EATS - ACTIVE CART & PAST ORDER LISTINGS)
@Composable
fun OrdersScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val cart by viewModel.cartItems.collectAsState()
    val pastOrders by viewModel.orders.collectAsState()
    val context = LocalContext.current

    var searchVal by remember { mutableStateOf("") }

    val combinedCartPrice = cart.sumOf { it.price * it.quantity }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 76.dp)
        ) {
            StandardHeaderTitle(titleLabel = "Your Eats")

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header details title
                item {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        Text(
                            text = "Your Eats",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DarkText
                        )
                        Text(
                            text = "Relive your favorite flavor moments.",
                            fontSize = 15.sp,
                            color = LightGreyText
                        )
                    }
                }

                // Active shopping cart layout widget (if items exist)
                if (cart.isNotEmpty()) {
                    item {
                        Text(
                            text = "Active Cravings Basket",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TealAccent,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    items(cart) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                    ) {
                                        AsyncImage(
                                            model = item.imageUrl,
                                            contentDescription = item.name,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(text = item.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkText)
                                        Text(text = "Specs: ${item.size} Size", fontSize = 12.sp, color = LightGreyText)
                                        if (item.addOns.isNotEmpty()) {
                                            Text(text = "Add-ons: ${item.addOns.joinToString(", ")}", fontSize = 12.sp, color = LightGreyText)
                                        }
                                        Text(
                                            text = String.format("$%.2f each", item.price),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = PrimaryOrange,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Live quantity adjuster
                                    Row(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(SurfaceContainerLow)
                                            .padding(horizontal = 4.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        IconButton(
                                            onClick = { viewModel.updateCartItemQty(item.id, item.quantity - 1) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease count", tint = LightGreyText)
                                        }
                                        Text(
                                            text = "${item.quantity}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DarkText
                                        )
                                        IconButton(
                                            onClick = { viewModel.updateCartItemQty(item.id, item.quantity + 1) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(imageVector = Icons.Default.Add, contentDescription = "Increase count", tint = LightGreyText)
                                        }
                                    }

                                    Text(
                                        text = String.format("$%.2f", item.price * item.quantity),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkText
                                    )
                                }
                            }
                        }
                    }

                    // Checkout CTA Summary block
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SoftPeachBg),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Total Vibe Price:", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = DarkText)
                                    Text(
                                        text = String.format("$%.2f", combinedCartPrice),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = PrimaryOrange
                                    )
                                }
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = {
                                        viewModel.checkoutCart {
                                            Toast.makeText(context, "Order Placed! Check details on Your Eats history.", Toast.LENGTH_LONG).show()
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                        .testTag("checkout_button"),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainerOrange),
                                    shape = RoundedCornerShape(26.dp)
                                ) {
                                    Text("Checkout Basket & Deliver 🚀", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                }
                            }
                        }
                    }
                }

                // Or search past orders input bar
                item {
                    Text(
                        text = "Order History Vibe",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkText,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                }

                item {
                    OutlinedTextField(
                        value = searchVal,
                        onValueChange = { searchVal = it },
                        placeholder = {
                            Text(text = "Search past orders...", color = SoftLightGrey, fontSize = 14.sp)
                        },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Search, contentDescription = "Search history icon", tint = SoftLightGrey)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryContainerOrange,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = SurfaceContainerLow,
                            unfocusedContainerColor = SurfaceContainerLow
                        ),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )
                }

                // Render dynamic list of OrderHistory
                val filteredOrders = pastOrders.filter {
                    it.restaurantName.contains(searchVal, ignoreCase = true)
                }

                if (filteredOrders.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.SentimentDissatisfied,
                                contentDescription = "Empty state icon",
                                tint = SoftLightGrey,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                "No history matching vibes!",
                                color = LightGreyText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(filteredOrders) { order ->
                        OrderCard(order = order, onReorderClick = {
                            viewModel.addToCart(
                                name = order.restaurantName,
                                priceDouble = order.totalPrice / order.itemCount,
                                quantity = order.itemCount,
                                size = "Standard",
                                addOns = emptyList(),
                                imageUrl = order.imageUrl
                            )
                            Toast.makeText(context, "${order.restaurantName} items added back to Basket!", Toast.LENGTH_SHORT).show()
                        })
                    }
                }

                // Extra footer details text
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Showing your last 6 months of cravings",
                        fontSize = 14.sp,
                        color = LightGreyText,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 36.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            AppBottomNavBar(
                selectedRoute = "orders",
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    }
}

@Composable
fun OrderCard(
    order: OrderHistory,
    onReorderClick: () -> Unit
) {
    val baseGlow = Modifier.shadow(
        elevation = 6.dp,
        shape = RoundedCornerShape(16.dp),
        clip = true
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(baseGlow),
        colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, OutlinedCoral.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Restaurant thumbnail
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    AsyncImage(
                        model = order.imageUrl,
                        contentDescription = order.restaurantName,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = order.restaurantName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )

                        // Status badge label
                        val badgeBg = if (order.statusText == "Cancelled") Color(0xFFFFDAD6) else SoftTealBg
                        val badgeColor = if (order.statusText == "Cancelled") Color(0xFFBA1A1A) else TealAccent

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(badgeBg)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = order.statusText,
                                color = badgeColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = order.dateText,
                        fontSize = 12.sp,
                        color = LightGreyText,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )

                    Text(
                        text = String.format("$%.2f", order.totalPrice),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryOrange,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Lower Action segment
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (order.statusText == "Cancelled") {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceContainerHigh),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.HelpOutline, contentDescription = "Help support icon", tint = DarkText)
                            Text(text = "Get Support", color = DarkText, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                } else {
                    Button(
                        onClick = onReorderClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Replay, contentDescription = "Replay reorder icons", tint = Color.White)
                            Text(text = "Reorder", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }

                    FilledIconButton(
                        onClick = { },
                        modifier = Modifier.size(48.dp),
                        colors = IconButtonDefaults.filledIconButtonColors(containerColor = SurfaceContainerLow),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = "More options", tint = SoftLightGrey)
                    }
                }
            }
        }
    }
}

// DATA SPECIFICATION HELPER OBJECTS
data class FeaturedItem(
    val name: String,
    val badgeText: String,
    val desc: String,
    val timeText: String,
    val imageUrl: String
)

data class PopularItem(
    val name: String,
    val rating: String,
    val tags: String,
    val delivery: String,
    val time: String,
    val imageUrl: String
)

data class ExploreCategory(
    val name: String,
    val isTrending: Boolean,
    val isLarge: Boolean,
    val imageUrl: String,
    val testTag: String
)
