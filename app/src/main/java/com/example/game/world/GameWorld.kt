package com.example.game.world

import com.example.game.enemy.EnemyType

data class Platform(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val isBouncy: Boolean = false
)

data class Hazard(
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val damage: Float = 15f
)

data class EnemyTemplate(
    val x: Float,
    val y: Float,
    val type: EnemyType,
    val maxHp: Float = 40f
)

data class GameRegion(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val bgHex: Long,
    val width: Float = 1600f,
    val height: Float = 800f,
    val platforms: List<Platform>,
    val hazards: List<Hazard>,
    val enemyTemplates: List<EnemyTemplate>,
    val leftNodeRegionId: String? = null,
    val rightNodeRegionId: String? = null,
    val spawnXLeft: Float = 120f,
    val spawnXRight: Float = 1450f
)

object WorldConfig {
    val REGION_ASHEN = "REGION_ASHEN"
    val REGION_ARCHIVES = "REGION_ARCHIVES"
    val REGION_ARCHIPELAGO = "REGION_ARCHIPELAGO"
    val REGION_GLASSFJORD = "REGION_GLASSFJORD"
    val REGION_CLOCKWORKS = "REGION_CLOCKWORKS"
    val REGION_MOORLANDS = "REGION_MOORLANDS"
    val REGION_CHASM = "REGION_CHASM"

    val regions = mapOf(
        REGION_ASHEN to GameRegion(
            id = REGION_ASHEN,
            nameAr = "السهل الرمادي",
            nameEn = "Ashen Sprawl",
            bgHex = 0xFF2B2F33,
            width = 1600f,
            height = 800f,
            platforms = listOf(
                Platform(0f, 750f, 1600f, 50f),
                Platform(250f, 580f, 200f, 30f),
                Platform(550f, 480f, 200f, 30f),
                Platform(850f, 580f, 250f, 30f),
                Platform(1150f, 450f, 200f, 30f)
            ),
            hazards = listOf(
                Hazard(600f, 730f, 150f, 20f, damage = 10f)
            ),
            enemyTemplates = listOf(
                EnemyTemplate(350f, 540f, EnemyType.SCRAB_SCAVENGER),
                EnemyTemplate(950f, 540f, EnemyType.ASHWARDEN)
            ),
            rightNodeRegionId = REGION_ARCHIVES
        ),
        REGION_ARCHIVES to GameRegion(
            id = REGION_ARCHIVES,
            nameAr = "الأرشيف المظلل",
            nameEn = "Veiled Archives",
            bgHex = 0xFF24314A,
            width = 1600f,
            height = 800f,
            platforms = listOf(
                Platform(0f, 750f, 600f, 50f),
                Platform(1000f, 750f, 600f, 50f),
                Platform(300f, 550f, 180f, 30f),
                Platform(600f, 420f, 400f, 30f, isBouncy = true),
                Platform(1100f, 550f, 180f, 30f)
            ),
            hazards = listOf(Hazard(600f, 740f, 400f, 15f, damage = 20f)),
            enemyTemplates = listOf(
                EnemyTemplate(200f, 710f, EnemyType.PAGE_SCRAPER),
                EnemyTemplate(1300f, 710f, EnemyType.ECHO_SHADE)
            ),
            leftNodeRegionId = REGION_ASHEN,
            rightNodeRegionId = REGION_ARCHIPELAGO
        ),
        REGION_ARCHIPELAGO to GameRegion(
            id = REGION_ARCHIPELAGO,
            nameAr = "الأرخبيل المجوف",
            nameEn = "Hollowed Archipelago",
            bgHex = 0xFF4B6B6D,
            width = 1600f,
            height = 800f,
            platforms = listOf(
                Platform(0f, 750f, 1600f, 50f),
                Platform(200f, 600f, 150f, 30f),
                Platform(450f, 480f, 150f, 30f),
                Platform(700f, 380f, 200f, 30f),
                Platform(1000f, 480f, 150f, 30f),
                Platform(1250f, 600f, 150f, 30f)
            ),
            hazards = listOf(),
            enemyTemplates = listOf(
                EnemyTemplate(500f, 440f, EnemyType.ROPE_CROAKER),
                EnemyTemplate(1100f, 440f, EnemyType.DRIFT_KNIGHT)
            ),
            leftNodeRegionId = REGION_ARCHIVES,
            rightNodeRegionId = REGION_GLASSFJORD
        ),
        REGION_GLASSFJORD to GameRegion(
            id = REGION_GLASSFJORD,
            nameAr = "منحدرات الزجاج",
            nameEn = "Glassfjord Cliffs",
            bgHex = 0xFFD7EEF9,
            width = 1600f,
            height = 800f,
            platforms = listOf(
                Platform(0f, 750f, 400f, 50f),
                Platform(1200f, 750f, 400f, 50f),
                Platform(400f, 620f, 200f, 30f),
                Platform(1000f, 620f, 200f, 30f),
                Platform(650f, 480f, 300f, 30f)
            ),
            hazards = listOf(Hazard(400f, 740f, 800f, 15f, damage = 25f)),
            enemyTemplates = listOf(
                EnemyTemplate(750f, 420f, EnemyType.SHARDLING),
                EnemyTemplate(500f, 570f, EnemyType.SHARDLING)
            ),
            leftNodeRegionId = REGION_ARCHIPELAGO,
            rightNodeRegionId = REGION_CLOCKWORKS
        ),
        REGION_CLOCKWORKS to GameRegion(
            id = REGION_CLOCKWORKS,
            nameAr = "آليات الغمر",
            nameEn = "Sunken Clockworks",
            bgHex = 0xFF244E48,
            width = 1600f,
            height = 800f,
            platforms = listOf(
                Platform(0f, 750f, 1600f, 50f),
                Platform(300f, 550f, 300f, 30f),
                Platform(1000f, 550f, 300f, 30f)
            ),
            hazards = listOf(),
            enemyTemplates = listOf(
                EnemyTemplate(450f, 520f, EnemyType.GEARFOLK),
                EnemyTemplate(1150f, 520f, EnemyType.GEARFOLK)
            ),
            leftNodeRegionId = REGION_GLASSFJORD,
            rightNodeRegionId = REGION_MOORLANDS
        ),
        REGION_MOORLANDS to GameRegion(
            id = REGION_MOORLANDS,
            nameAr = "موائد الجذور السوداء",
            nameEn = "Blackroot Moorlands",
            bgHex = 0xFF2A1F1A,
            width = 1600f,
            height = 800f,
            platforms = listOf(
                Platform(0f, 750f, 600f, 50f),
                Platform(1000f, 750f, 600f, 50f),
                Platform(650f, 600f, 300f, 30f)
            ),
            hazards = listOf(Hazard(600f, 740f, 400f, 15f, damage = 15f)),
            enemyTemplates = listOf(
                EnemyTemplate(800f, 560f, EnemyType.ROOTCRAWLER)
            ),
            leftNodeRegionId = REGION_CLOCKWORKS,
            rightNodeRegionId = REGION_CHASM
        ),
        REGION_CHASM to GameRegion(
            id = REGION_CHASM,
            nameAr = "الشق المضيء",
            nameEn = "Luminous Chasm",
            bgHex = 0xFFA6F0E6,
            width = 1600f,
            height = 800f,
            platforms = listOf(
                Platform(0f, 750f, 1600f, 50f),
                Platform(400f, 500f, 200f, 30f),
                Platform(1000f, 500f, 200f, 30f)
            ),
            hazards = listOf(),
            enemyTemplates = listOf(
                EnemyTemplate(500f, 460f, EnemyType.GLOW_WISP),
                EnemyTemplate(1100f, 460f, EnemyType.GLOW_WISP)
            ),
            leftNodeRegionId = REGION_MOORLANDS
        )
    )
}
