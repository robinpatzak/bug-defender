package de.mow2.towerdefense.controller.helper

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.view.Gravity
import de.mow2.towerdefense.R
import de.mow2.towerdefense.model.core.GameManager
import de.mow2.towerdefense.model.gameobjects.actors.Enemy.EnemyType
import de.mow2.towerdefense.model.gameobjects.actors.TowerTypes
import java.util.concurrent.ConcurrentHashMap

class BitmapPreloader(val resources: Resources) {
    private val defaultWidth = GameManager.playGround.squareSize
    private val defaultHeight = defaultWidth
    /**
     * Initialize all images and hold references for further use
     * Should improve performance compared to decoding bitmaps while drawing
     */
    fun preloadGraphics() {
        preloadGui()
        preloadTowers()
        preloadWeapons()
        preloadProjectiles()
        preloadEnemies()
    }

    private fun preloadGui() {
        //bottom gui
        bottomDrawable = BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.bottomgui_bg))
        bottomDrawable.tileModeX = Shader.TileMode.REPEAT
        //top gui
        topDrawable = BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.topgui_bg))
        topDrawable.tileModeX = Shader.TileMode.REPEAT
        topDrawable.gravity = Gravity.BOTTOM
    }

    private fun preloadTowers() {
        for(level in 0..GameManager.maxTowerLevel) { //for each tower level
            val towerImages = ConcurrentHashMap<TowerTypes, Bitmap>()
            TowerTypes.values().forEach { type ->
                val towerR: Int
                when(type) {
                    TowerTypes.BLOCK -> {
                        towerR = when(level) {
                            0 -> R.drawable.tower_block_1
                            1 -> R.drawable.tower_block_2
                            2 -> R.drawable.tower_block_3
                            else -> R.drawable.tower_block_1
                        }
                    }
                    TowerTypes.SLOW -> {
                        towerR = when(level) {
                            0 -> R.drawable.tower_slow_1
                            1 -> R.drawable.tower_slow_2
                            2 -> R.drawable.tower_slow_3
                            else -> R.drawable.tower_slow_1
                        }
                    }
                    TowerTypes.AOE -> {
                        towerR = when(level) {
                            0 -> R.drawable.tower_aoe_1
                            1 -> R.drawable.tower_aoe_2
                            2 -> R.drawable.tower_aoe_3
                            else -> R.drawable.tower_aoe_1
                        }
                    }
                    TowerTypes.MAGIC -> {
                        towerR = when(level) {
                            0 -> R.drawable.tower_magic_1
                            1 -> R.drawable.tower_magic_2
                            2 -> R.drawable.tower_magic_3
                            else -> R.drawable.tower_magic_1
                        }
                    }
                }
                towerImages[type] = ScaledImage(resources, defaultWidth, defaultHeight * 2, towerR).scaledImage
            }
            towerImagesArray = towerImagesArray.plus(towerImages)
        }
    }

    private fun preloadWeapons() {
        for(level in 0..GameManager.maxTowerLevel) { //for each tower level
            val weaponAnims = ConcurrentHashMap<TowerTypes, SpriteAnimation>()
            TowerTypes.values().forEach { type ->
                val weaponAnimR: Int
                val frameCountWeapon: Int
                val rowCountWeapon = 4
                //build images and animation maps
                when(type) {
                    TowerTypes.BLOCK -> {
                        weaponAnimR = when(level) {
                            //TODO: einigen, wie man das mit dem sprite und dem offset löst
                            0 -> R.drawable.tower_block_weapon_anim_1_alternative
                            1 -> R.drawable.tower_block_weapon_anim_2
                            2 -> R.drawable.tower_block_weapon_anim_3
                            else -> R.drawable.tower_block_weapon_anim_1
                        }
                        frameCountWeapon = 6
                    }
                    TowerTypes.SLOW -> {
                        weaponAnimR = when(level) {
                            0 -> R.drawable.tower_slow_weapon_anim_1
                            1 -> R.drawable.tower_slow_weapon_anim_2
                            2 -> R.drawable.tower_slow_weapon_anim_3
                            else -> R.drawable.tower_slow_weapon_anim_1
                        }
                        frameCountWeapon = 16
                    }
                    TowerTypes.AOE -> {
                        weaponAnimR = when(level) {
                            0 -> R.drawable.tower_aoe_weapon_anim_1
                            1 -> R.drawable.tower_aoe_weapon_anim_1
                            2 -> R.drawable.tower_aoe_weapon_anim_1
                            else -> R.drawable.tower_aoe_weapon_anim_1
                        }
                        frameCountWeapon = 8
                    }
                    TowerTypes.MAGIC -> {
                        weaponAnimR = when(level) {
                            0 -> R.drawable.tower_magic_weapon_anim_1
                            1 -> R.drawable.tower_magic_weapon_anim_1
                            2 -> R.drawable.tower_magic_weapon_anim_1
                            else -> R.drawable.tower_magic_weapon_anim_1
                        }
                        frameCountWeapon = 29
                    }
                }
                weaponAnims[type] = SpriteAnimation(BitmapFactory.decodeResource(resources, weaponAnimR), defaultWidth, defaultHeight, rowCountWeapon, frameCountWeapon, 100, true)
            }
            weaponAnimsArray = weaponAnimsArray.plus(weaponAnims)
        }
    }

    private fun preloadProjectiles() {
        for(level in 0..GameManager.maxTowerLevel) { //for each tower level
            val projectileAnims = ConcurrentHashMap<TowerTypes, SpriteAnimation>()
            TowerTypes.values().forEach { type ->
                val projectileAnimR: Int
                val frameCountProjectile: Int
                val rowCountProjectile = 4
                val widthProjectile: Int
                val heightProjectile: Int
                when(type) {
                    TowerTypes.BLOCK -> {
                        projectileAnimR = when(level) {
                            0 -> R.drawable.tower_block_projectile_1
                            1 -> R.drawable.tower_block_projectile_2
                            2 -> R.drawable.tower_block_projectile_3
                            else -> R.drawable.tower_block_projectile_1
                        }
                        frameCountProjectile = 3
                        widthProjectile = 20
                        heightProjectile = 80
                    }
                    TowerTypes.SLOW -> {
                        projectileAnimR = when(level) {
                            0 -> R.drawable.tower_slow_projectile_1
                            1 -> R.drawable.tower_slow_projectile_2
                            2 -> R.drawable.tower_slow_projectile_3
                            else -> R.drawable.tower_slow_projectile_1
                        }
                        frameCountProjectile = 5
                        widthProjectile = 64
                        heightProjectile = 64
                    }
                    TowerTypes.AOE -> {
                        projectileAnimR = when(level) {
                            0 -> R.drawable.tower_aoe_projectile_1
                            1 -> R.drawable.tower_aoe_projectile_2
                            2 -> R.drawable.tower_aoe_projectile_3
                            else -> R.drawable.tower_aoe_projectile_1
                        }
                        frameCountProjectile = 6
                        widthProjectile = 20
                        heightProjectile = 20
                    }
                    TowerTypes.MAGIC -> {
                        //tower
                        projectileAnimR = when(level) {
                            0 -> R.drawable.tower_magic_projectile_1
                            1 -> R.drawable.tower_magic_projectile_2
                            2 -> R.drawable.tower_magic_projectile_3
                            else -> R.drawable.tower_magic_projectile_1
                        }
                        frameCountProjectile = 12
                        widthProjectile = 128
                        heightProjectile = 128
                    }
                }
                projectileAnims[type] = SpriteAnimation(BitmapFactory.decodeResource(resources, projectileAnimR), widthProjectile, heightProjectile, rowCountProjectile, frameCountProjectile, 100, true)
            }
            projectileAnimsArray = projectileAnimsArray.plus(projectileAnims)
        }
    }
    private fun preloadEnemies() {
        EnemyType.values().forEach { key ->
            val enemyR: Int
            val frameCount: Int
            val rowCount = 4 // 4 rows = 4 animation types (0=down, 1=up, 2=right, 3= left), sprite sheet has to be in that order!
            val frameDuration: Int
            when(key) {
                EnemyType.LEAFBUG -> {
                    enemyR = R.drawable.enemy_leafbug_anim
                    frameCount = 7
                    frameDuration = 45
                }
                EnemyType.FIREBUG -> {
                    enemyR = R.drawable.enemy_firebug_anim
                    frameCount = 8
                    frameDuration = 60
                }
                EnemyType.MAGMACRAB -> {
                    enemyR = R.drawable.enemy_magmacrab_anim
                    frameCount = 8
                    frameDuration = 60
                }
                EnemyType.SCORPION -> {
                    enemyR = R.drawable.enemy_scorpion_anim
                    frameCount = 8
                    frameDuration = 120
                }
                EnemyType.CLAMPBEETLE -> {
                    enemyR = R.drawable.enemy_clampbeetle_anim
                    frameCount = 8
                    frameDuration = 60
                }
                EnemyType.FIREWASP -> {
                    enemyR = R.drawable.enemy_firewasp_anim
                    frameCount = 8
                    frameDuration = 60
                }
                EnemyType.LOCUST -> {
                    enemyR = R.drawable.enemy_locust_anim
                    frameCount = 8
                    frameDuration = 60
                }
                EnemyType.VOIDBUTTERFLY -> {
                    enemyR = R.drawable.enemy_voidbutterfly_anim
                    frameCount = 4
                    frameDuration = 60
                }
                EnemyType.SKELETONGRUNT -> {
                    enemyR = R.drawable.enemy_skeletongrunt_anim
                    frameCount = 6
                    frameDuration = 60
                }
                EnemyType.NECROMANCER -> {
                    enemyR = R.drawable.enemy_necromancer_anim
                    frameCount = 6
                    frameDuration = 105
                }
                EnemyType.SKELETONWARRIOR -> {
                    enemyR = R.drawable.enemy_skeletonwarrior_anim
                    frameCount = 8
                    frameDuration = 90
                }
                EnemyType.SKELETONKNIGHT -> {
                    enemyR = R.drawable.enemy_skeletonknight_anim
                    frameCount = 8
                    frameDuration = 60
                }
                EnemyType.SKELETONKING -> {
                    enemyR = R.drawable.enemy_skeletonking_anim
                    frameCount = 10
                    frameDuration = 120
                }
            }
            enemyAnims[key] = SpriteAnimation(BitmapFactory.decodeResource(resources, enemyR), defaultWidth, defaultHeight, rowCount, frameCount, frameDuration)
        }
    }

    companion object {
        //all various lists and maps for game objects and their respective bitmaps or animations
        var towerImagesArray = emptyArray<ConcurrentHashMap<TowerTypes, Bitmap>>()
        var weaponAnimsArray = emptyArray<ConcurrentHashMap<TowerTypes, SpriteAnimation>>()
        var projectileAnimsArray = emptyArray<ConcurrentHashMap<TowerTypes, SpriteAnimation>>()
        var enemyAnims = ConcurrentHashMap<EnemyType, SpriteAnimation>()
        lateinit var bottomDrawable: BitmapDrawable
        lateinit var topDrawable: BitmapDrawable
    }
}