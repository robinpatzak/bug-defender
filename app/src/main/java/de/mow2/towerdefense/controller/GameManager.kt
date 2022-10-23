package de.mow2.towerdefense.controller

import android.content.res.Resources
import android.graphics.*
import android.util.Log
import de.mow2.towerdefense.R
import de.mow2.towerdefense.model.core.SquareField
import de.mow2.towerdefense.model.gameobjects.actors.*
import de.mow2.towerdefense.model.pathfinding.Astar
import java.util.concurrent.ConcurrentHashMap


object GameManager {
    //playground variables
    const val squaresX = 9
    const val squaresY = 18

    //currently as array, should be a matrix (map or list)
    private var towerList = emptyArray<Tower>()
    private var creepList: ConcurrentHashMap<Creep, Astar.Node> = ConcurrentHashMap()
    lateinit var spriteSheet: SpriteSheet

    //nodes test
    var compoundPath: MutableList<SquareField> = mutableListOf()
    private var target: Astar.Node = Astar.Node(5, 5)
    //build and upgrade menu
    var buildMenuButtons = emptyArray<Bitmap>()
    var buildMenuButtonRanges = emptyArray<ClosedFloatingPointRange<Float>>()
    private val TAG = javaClass.name

    fun comparePathCoords(path: MutableSet<Astar.Node>) {
        val pathList = path.reversed()
        compoundPath.clear()
        pathList.forEach {
            compoundPath.add(GameView.playGround.squareArray[it.x][it.y])
            Log.i("Infos:", "$it, ${it.f}")
        }
    }

    fun buildTower(selectedField: SquareField, towerType: TowerTypes) {
        val tower = when(towerType) {
            TowerTypes.BLOCK -> {
                Tower(selectedField, TowerTypes.BLOCK)
            }
            TowerTypes.SLOW -> {
                Tower(selectedField, TowerTypes.SLOW)
            }
            TowerTypes.AOE -> {
                Tower(selectedField, TowerTypes.AOE)
            }
        }
        towerList = towerList.plus(tower)
        towerList.sort() //sorting array to avoid overlapped drawing
    }

    fun initBuildMenu(resources: Resources) {
        val dimensionX = 100 //TODO: BuildUpgradeMenu.width divided by no. of menu options??
        val dimensionY = BuildUpgradeMenu.height.toInt()
        var drawable: Int
        enumValues<TowerTypes>().forEach {
            drawable = when(it) {
                TowerTypes.BLOCK -> {
                    R.drawable.tower_block
                }
                TowerTypes.SLOW -> {
                    R.drawable.tower_slow
                }
                TowerTypes.AOE -> {
                    R.drawable.tower_aoe
                }
            }
            buildMenuButtons = buildMenuButtons.plus(resizeImage(BitmapFactory.decodeResource(resources, drawable), dimensionX, dimensionY))
        }
    }

    fun drawBuildMenu(canvas: Canvas, x: Float, y: Float) {
        buildMenuButtonRanges = emptyArray()
        var offsetX = 0
        var offsetY = BuildUpgradeMenu.height
        val paint = Paint()
        paint.color = Color.parseColor("#43240f")
        //TODO: beautify
        canvas.drawRect(0f, y - offsetY, GameView.gameWidth.toFloat(), y, paint)
        buildMenuButtons.forEach {
            draw(canvas, it, x + offsetX, y - offsetY)
            val range = ((x+offsetX)..(x+offsetX+it.width))
            buildMenuButtonRanges = buildMenuButtonRanges.plus(range)
            offsetX += 120
        }
    }

    /**
     * decides which objects to draw
     */
    fun drawObjects(canvas: Canvas, resources: Resources) {
        var sprite = SpriteSheet(resources, BitmapFactory.decodeResource(resources, R.drawable.leafbug)).cutSprite()
        //draw towers
        towerList.forEach {
            when (it.type) {
                TowerTypes.BLOCK -> {
                    draw(canvas, resizeImage(BitmapFactory.decodeResource(resources, R.drawable.tower_block), it.w, it.h), it.x, it.y)
                }
                TowerTypes.SLOW -> {
                    draw(canvas, resizeImage(BitmapFactory.decodeResource(resources, R.drawable.tower_slow), it.w, it.h), it.x, it.y)
                }
                TowerTypes.AOE -> {
                    draw(canvas, resizeImage(BitmapFactory.decodeResource(resources, R.drawable.tower_aoe), it.w, it.h), it.x, it.y)
                }
            }
        }

        creepList.forEach{ (enemy) ->
            draw(canvas, resizeImage(BitmapFactory.decodeResource(resources, R.drawable.leafbug_down), enemy.w, enemy.h), enemy.positionX(), enemy.positionY())

        }


/*        //astar visualization
        compoundPath.forEach {
            draw(canvas, resizeImage(BitmapFactory.decodeResource(resources, R.drawable.path), 50, 50), it.coordX, it.coordY)
        }*/
    }

    /**
     * updates to game logic related values
     */
    fun updateLogic() {
        //add enemies to the spawn
        if (Creep.canSpawn()) { //wait for update timer
            //add creeps and their individual target to concurrentHashMap
            creepList[Creep(target, CreepTypes.LEAFBUG)] = target

            //Log.i(TAG, "${creepList.size} enemies spawned")
        }

        /**
         * update movement, update target or remove enemy
         */
        creepList.forEach{ (enemy) ->
            if(enemy.positionY().toInt() >= GameView.playGround.squareArray[0][squaresY-1].coordY.toInt()){
                creepList.remove(enemy)
                //Log.i("enemyUpdater", "enemy removed")
            }else{
                //update movement
                enemy.update()
            }
            //TODO: update enemy target
        }
    }

    /**
     * actually draws objects
     */
    @Synchronized private fun draw(canvas: Canvas, bitmap: Bitmap, posX: Float, posY: Float) {
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