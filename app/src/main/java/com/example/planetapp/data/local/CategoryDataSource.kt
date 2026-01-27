package com.example.planetapp.data.local

import com.example.planetapp.R
import com.example.planetapp.domain.model.Category

object CategoryDataSource {
    
    fun getAllCategories(): List<Category> {
        return listOf(
            Category(
                id = 1,
                title = "Planets",
                subtitle = "Explore our Solar System",
                icon = "",
                image = R.drawable.planet,
                itemCount = "8 Planets + Dwarf",
                gradientColors = Pair("#667eea", "#764ba2"),
                isEnabled = true
            ),
            Category(
                id = 2,
                title = "Moons",
                subtitle = "Natural Satellites",
                icon = "",
                image = R.drawable.moon,
                itemCount = "200+ Moons",
                gradientColors = Pair("#11998e", "#38ef7d"),
                isEnabled = true
            ),
            Category(
                id = 3,
                title = "Space Missions",
                subtitle = "Human Exploration",
                icon = "",
                image = R.drawable.mission,
                itemCount = "50+ Missions",
                gradientColors = Pair("#eb3349", "#f45c43"),
                isEnabled = true
            ),
            Category(
                id = 4,
                title = "Stars",
                subtitle = "Constellations & Beyond",
                icon = "",
                image = R.drawable.star,
                itemCount = "88 Constellations",
                gradientColors = Pair("#f093fb", "#f5576c"),
                isEnabled = true
            ),
            Category(
                id = 5,
                title = "Asteroids & Comets",
                subtitle = "Space Rocks & Ice",
                icon = "",
                image = R.drawable.meteorite,
                itemCount = "Famous Objects",
                gradientColors = Pair("#4facfe", "#00f2fe"),
                isEnabled = true
            ),
            Category(
                id = 6,
                title = "Galaxies",
                subtitle = "Islands of Stars",
                icon = "",
                image = R.drawable.galaxy,
                itemCount = "Galaxy Types",
                gradientColors = Pair("#a18cd1", "#fbc2eb"),
                isEnabled = true
            ),
            Category(
                id = 7,
                title = "Space Quiz",
                subtitle = "Test Your Knowledge",
                icon = "",
                image = R.drawable.knwoladge,
                itemCount = "100+ Questions",
                gradientColors = Pair("#ff9a9e", "#fecfef"),
                isEnabled = true
            )
        )
    }
}
