package com.example.planetapp.data.local

import com.example.planetapp.R
import com.example.planetapp.domain.model.Planet

object PlanetDataSource {
    
    fun getAllPlanets(): List<Planet> {
        return listOf(
            Planet(
                id = 1,
                name = "Sun",
                type = "Star",
                image = R.drawable.sun,
                galaxy = "Milky Way Galaxy",
                distanceFromSun = "0",
                gravity = "274",
                diameter = "1,392,700",
                orbitalPeriod = "N/A",
                moons = 0,
                temperature = "5,500°C (surface)",
                dayLength = "25-35 days",
                yearLength = "N/A",
                atmosphere = "Hydrogen, Helium",
                overview = "The Sun is the star at the center of the Solar System. It is a nearly perfect sphere of hot plasma, heated to incandescence by nuclear fusion reactions in its core, radiating the energy mainly as visible light and infrared radiation. It is by far the most important source of energy for life on Earth.\n\nIts diameter is about 1.39 million kilometres (864,000 miles), or 109 times that of Earth. Its mass is about 330,000 times that of Earth; it accounts for about 99.86% of the total mass of the Solar System.",
                funFacts = listOf(
                    "The Sun contains 99.86% of the mass in the Solar System",
                    "Over one million Earths could fit inside the Sun",
                    "The Sun's core reaches 15 million degrees Celsius",
                    "Light from the Sun takes 8 minutes to reach Earth"
                )
            ),
            Planet(
                id = 2,
                name = "Mercury",
                type = "Terrestrial",
                image = R.drawable.mercury,
                galaxy = "Milky Way Galaxy",
                distanceFromSun = "57.9",
                gravity = "3.7",
                diameter = "4,879",
                orbitalPeriod = "88 days",
                moons = 0,
                temperature = "167°C (average)",
                dayLength = "59 Earth days",
                yearLength = "88 Earth days",
                atmosphere = "Minimal (Oxygen, Sodium, Hydrogen)",
                overview = "Mercury is the smallest planet in the Solar System and the closest to the Sun. Its orbit around the Sun takes 87.97 Earth days, the shortest of all the Sun's planets.\n\nLike Venus, Mercury orbits the Sun within Earth's orbit as an inferior planet, and its apparent distance from the Sun as viewed from Earth never exceeds 28°. This proximity to the Sun means the planet can only be seen near the western horizon after sunset or the eastern horizon before sunrise.",
                funFacts = listOf(
                    "Mercury has no moons or rings",
                    "A year on Mercury is just 88 Earth days",
                    "Mercury has a molten core",
                    "Despite being closest to the Sun, Venus is hotter"
                )
            ),
            Planet(
                id = 3,
                name = "Venus",
                type = "Terrestrial",
                image = R.drawable.venus,
                galaxy = "Milky Way Galaxy",
                distanceFromSun = "108.2",
                gravity = "8.87",
                diameter = "12,104",
                orbitalPeriod = "225 days",
                moons = 0,
                temperature = "464°C (average)",
                dayLength = "243 Earth days",
                yearLength = "225 Earth days",
                atmosphere = "Carbon Dioxide, Nitrogen",
                overview = "Venus is the second planet from the Sun. It is named after the Roman goddess of love and beauty. As the brightest natural object in Earth's night sky after the Moon, Venus can cast shadows and can be visible to the naked eye in broad daylight.\n\nVenus lies within Earth's orbit, and so never appears to venture far from the Sun. Venus orbits the Sun every 224.7 Earth days. With a rotation period of 243 Earth days, it takes longer to rotate about its axis than any other planet in the Solar System.",
                funFacts = listOf(
                    "Venus rotates backwards compared to other planets",
                    "A day on Venus is longer than its year",
                    "Venus is the hottest planet in our Solar System",
                    "Venus has no moons or rings"
                )
            ),
            Planet(
                id = 4,
                name = "Earth",
                type = "Terrestrial",
                image = R.drawable.planet_earth,
                galaxy = "Milky Way Galaxy",
                distanceFromSun = "149.6",
                gravity = "9.807",
                diameter = "12,742",
                orbitalPeriod = "365.25 days",
                moons = 1,
                temperature = "15°C (average)",
                dayLength = "24 hours",
                yearLength = "365.25 days",
                atmosphere = "Nitrogen, Oxygen",
                overview = "Earth is the third planet from the Sun and the only astronomical object known to harbor and support life. About 29.2% of Earth's surface is land consisting of continents and islands. The remaining 70.8% is covered with water, mostly by oceans, seas, gulfs, and other salt-water bodies.\n\nMuch of Earth's polar regions are covered in ice. Earth's outer layer is divided into several rigid tectonic plates that migrate across the surface over many millions of years.",
                funFacts = listOf(
                    "Earth is the only planet not named after a god",
                    "Earth's core is as hot as the Sun's surface",
                    "70% of Earth's surface is covered by water",
                    "Earth is the densest planet in the Solar System"
                )
            ),
            Planet(
                id = 5,
                name = "Moon",
                type = "Natural Satellite",
                image = R.drawable.moon,
                galaxy = "Milky Way Galaxy",
                distanceFromSun = "149.6 (with Earth)",
                gravity = "1.62",
                diameter = "3,474",
                orbitalPeriod = "27.3 days",
                moons = 0,
                temperature = "-20°C (average)",
                dayLength = "27.3 Earth days",
                yearLength = "27.3 days (around Earth)",
                atmosphere = "Minimal (Helium, Neon, Argon)",
                overview = "The Moon is Earth's only natural satellite. At about one-quarter the diameter of Earth, it is the largest natural satellite in the Solar System relative to the size of its planet, and the fifth largest satellite overall.\n\nOrbiting Earth at an average distance of 384,400 km, its gravitational influence is the main driver of Earth's tides. The Moon is classified as a planetary-mass object and a differentiated rocky body.",
                funFacts = listOf(
                    "The Moon is slowly drifting away from Earth",
                    "The Moon has moonquakes",
                    "There is water ice on the Moon",
                    "Only 12 people have walked on the Moon"
                )
            ),
            Planet(
                id = 6,
                name = "Mars",
                type = "Terrestrial",
                image = R.drawable.planet_mars,
                galaxy = "Milky Way Galaxy",
                distanceFromSun = "227.9",
                gravity = "3.721",
                diameter = "6,779",
                orbitalPeriod = "687 days",
                moons = 2,
                temperature = "-65°C (average)",
                dayLength = "24.6 hours",
                yearLength = "687 Earth days",
                atmosphere = "Carbon Dioxide, Nitrogen, Argon",
                overview = "Mars is the fourth planet from the Sun and the second-smallest planet in the Solar System, being larger than only Mercury. In English, Mars carries the name of the Roman god of war and is often referred to as the 'Red Planet'.\n\nThe reddish color is due to iron oxide (rust) prevalent on Mars's surface. Mars is a terrestrial planet with a thin atmosphere, with surface features reminiscent of the impact craters of the Moon and the valleys, deserts and polar ice caps of Earth.",
                funFacts = listOf(
                    "Mars has the largest volcano in the Solar System (Olympus Mons)",
                    "Mars has two moons: Phobos and Deimos",
                    "A day on Mars is almost the same length as Earth",
                    "Mars has seasons like Earth"
                )
            ),
            Planet(
                id = 7,
                name = "Jupiter",
                type = "Gas Giant",
                image = R.drawable.planet_jupiter,
                galaxy = "Milky Way Galaxy",
                distanceFromSun = "778.3",
                gravity = "24.79",
                diameter = "139,820",
                orbitalPeriod = "12 years",
                moons = 95,
                temperature = "-110°C (average)",
                dayLength = "10 hours",
                yearLength = "12 Earth years",
                atmosphere = "Hydrogen, Helium",
                overview = "Jupiter is the fifth planet from the Sun and the largest in the Solar System. It is a gas giant with a mass more than two and a half times that of all the other planets in the Solar System combined, but slightly less than one-thousandth the mass of the Sun.\n\nJupiter is the third-brightest natural object in the Earth's night sky after the Moon and Venus. It has been observed since pre-historic times and is named after the Roman god Jupiter, the king of the gods.",
                funFacts = listOf(
                    "Jupiter has 95 known moons",
                    "The Great Red Spot is a storm that has lasted over 400 years",
                    "Jupiter has the shortest day in the Solar System",
                    "Jupiter's moon Ganymede is larger than Mercury"
                )
            ),
            Planet(
                id = 8,
                name = "Saturn",
                type = "Gas Giant",
                image = R.drawable.planet_saturn,
                galaxy = "Milky Way Galaxy",
                distanceFromSun = "1,427",
                gravity = "10.44",
                diameter = "116,460",
                orbitalPeriod = "29 years",
                moons = 146,
                temperature = "-140°C (average)",
                dayLength = "10.7 hours",
                yearLength = "29 Earth years",
                atmosphere = "Hydrogen, Helium",
                overview = "Saturn is the sixth planet from the Sun and the second-largest in the Solar System, after Jupiter. It is a gas giant with an average radius of about nine and a half times that of Earth.\n\nSaturn is named after the Roman god of wealth and agriculture; its astronomical symbol (♄) represents the god's sickle. Saturn's most famous feature is its prominent ring system, which is composed mainly of ice particles, rocky debris, and dust.",
                funFacts = listOf(
                    "Saturn has 146 known moons - the most of any planet",
                    "Saturn's rings are mostly made of ice",
                    "Saturn could float in water (if there was a bathtub big enough)",
                    "Saturn's moon Titan has a thick atmosphere"
                )
            ),
            Planet(
                id = 9,
                name = "Uranus",
                type = "Ice Giant",
                image = R.drawable.planet_uranus,
                galaxy = "Milky Way Galaxy",
                distanceFromSun = "2,871",
                gravity = "8.87",
                diameter = "50,724",
                orbitalPeriod = "84 years",
                moons = 28,
                temperature = "-195°C (average)",
                dayLength = "17.2 hours",
                yearLength = "84 Earth years",
                atmosphere = "Hydrogen, Helium, Methane",
                overview = "Uranus is the seventh planet from the Sun. Its name is a reference to the Greek god of the sky, Uranus. It has the third-largest planetary radius and fourth-largest planetary mass in the Solar System.\n\nUranus is similar in composition to Neptune, and both have bulk chemical compositions which differ from that of the larger gas giants Jupiter and Saturn. For this reason, scientists often classify Uranus and Neptune as 'ice giants' to distinguish them from the other gas giants.",
                funFacts = listOf(
                    "Uranus rotates on its side",
                    "Uranus was the first planet discovered with a telescope",
                    "Uranus has 13 known rings",
                    "Uranus has the coldest atmosphere in the Solar System"
                )
            ),
            Planet(
                id = 10,
                name = "Neptune",
                type = "Ice Giant",
                image = R.drawable.planet_neptune,
                galaxy = "Milky Way Galaxy",
                distanceFromSun = "4,497",
                gravity = "11.15",
                diameter = "49,244",
                orbitalPeriod = "165 years",
                moons = 16,
                temperature = "-200°C (average)",
                dayLength = "16 hours",
                yearLength = "165 Earth years",
                atmosphere = "Hydrogen, Helium, Methane",
                overview = "Neptune is the eighth and farthest-known Solar planet from the Sun. In the Solar System, it is the fourth-largest planet by diameter, the third-most-massive planet, and the densest giant planet.\n\nIt is 17 times the mass of Earth, slightly more massive than its near-twin Uranus. Neptune is denser and physically smaller than Uranus because its greater mass causes more gravitational compression of its atmosphere. Neptune is named after the Roman god of the sea.",
                funFacts = listOf(
                    "Neptune was discovered through mathematical predictions",
                    "Neptune has the strongest winds in the Solar System",
                    "Neptune has only been visited once by a spacecraft",
                    "Neptune's moon Triton orbits backwards"
                )
            )
        )
    }
    
    fun searchPlanets(query: String): List<Planet> {
        return getAllPlanets().filter { 
            it.name.contains(query, ignoreCase = true) ||
            it.type.contains(query, ignoreCase = true)
        }
    }
}
