package com.example.planetapp.domain.model

data class Category(
    val id: Int,
    val title: String,
    val subtitle: String,
    val icon: String,
    val image: Int,
    val itemCount: String,
    val gradientColors: Pair<String, String>,
    val isEnabled: Boolean = true
)
