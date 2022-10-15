package de.mow2.towerdefense.controller

import android.content.res.Resources
import android.graphics.*
import de.mow2.towerdefense.R
import de.mow2.towerdefense.model.actors.Creep
import de.mow2.towerdefense.model.actors.CreepTypes
import de.mow2.towerdefense.model.actors.Tower
import de.mow2.towerdefense.model.actors.TowerTypes
import de.mow2.towerdefense.model.core.SquareField
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

object GameManager {
    //playground variables
    val squaresX = 9
    val squaresY = 18
    //game variables
    var lives: Int = 3
    var coins: Int = 100
    //currently as array, should be a matrix (map or list)
    var towerList = emptyArray<Tower>()
    var creepList = emptyArray<Creep>()
    lateinit var sprite: Sprite
    lateinit var spriteSheet: SpriteSheet



    init {
        //TODO: get actual lives and coins
    }

    fun buildTower(selectedField: SquareField) {
        val tower = Tower(selectedField, TowerTypes.BLOCK)
        towerList = towerList.plus(tower)
        towerList.sort() //sorting array to avoid overlapped drawing
    }

    fun createCreep(spawnField: SquareField){
        val creep = Creep(spawnField, CreepTypes.LEAFBUG)
        creepList = creepList.plus(creep)
    }

    fun drawBuildMenu(canvas: Canvas, resources: Resources, x: Float, y: Float) {
        draw(canvas, resizeImage(BitmapFactory.decodeResource(resources, R.drawable.tower_block), 500, 200), x, y)
    }
    /**
     * decides which objects to draw
     */
    fun drawObjects(canvas: Canvas, resources: Resources) {
        runBlocking {
            launch {
                //draw towers
                towerList.forEach {
                    when(it.type) {
                        TowerTypes.BLOCK -> {
                            draw(canvas, resizeImage(BitmapFactory.decodeResource(resources, R.drawable.tower_block), it.w, it.h), it.x, it.y)
                        }
                    }
                }
            }
        }

        //draw creeps
    }

    /**
     * actually draws objects
     */
    private fun draw(canvas: Canvas, bitmap: Bitmap, posX: Float, posY: Float) {
        canvas.drawBitmap(bitmap, posX, posY, null)
    }

    /**
     * placeholder for the time being.
     * could be expanded to perform various action such as change color, alpha etc.
     */
    private fun resizeImage(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }


    }
