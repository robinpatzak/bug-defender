package de.mow2.towerdefense.controller

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.mow2.towerdefense.model.pathfinding.Astar


class LevelGenerator(): ViewModel() {
    //variables
    val coinAmnt: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val livesAmnt: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    fun initLevel(level: Int) {
        when(level) {
            0 -> {
                /* Endless mode */
                livesAmnt.value = 3
                coinAmnt.value = 1000
            }
            1 -> {/* Level 1 */}
            2 -> {/* Level 2 */}
            else -> {
                /* Endless mode */
                livesAmnt.value = 3
                coinAmnt.value = 200000
            }
        }
    }

    //TODO: Trigger function when enemy is defeated / wave is completed / default income etc.
    /**
     * Method to call when increasing coins (e.g. defeating an enemy creature or destroying a tower)
     * @param increaseValue the value to be added to the total coin amount
     */
    fun increaseCoins(increaseValue: Int){
        val oldVal = coinAmnt.value!!
        coinAmnt.value = oldVal + increaseValue
    }

    //TODO: Different values for different TowerTypes
    /**
     * Method to call when decreasing coins (e.g. Building or upgrading a tower)
     * @param decreaseValue the value to subtract from the total amount
     */
    fun decreaseCoins(decreaseValue: Int) : Boolean {
        val oldVal = coinAmnt.value!!
        return if(coinAmnt.value!! >= (0 + decreaseValue)) {
            coinAmnt.value = oldVal - decreaseValue
            true
        } else {
            false
        }
    }

    fun increaseLives(newValue: Int){
        val oldVal = livesAmnt.value!!
        livesAmnt.value = oldVal + newValue
    }
    //TODO: Trigger function when enemy reaches finish line
    fun decreaseLives(newValue: Int) : Boolean {
        val oldVal = livesAmnt.value!!
        return if(livesAmnt.value!! >= (0 + newValue)) {
            livesAmnt.value = oldVal - newValue
            true
        } else {
            false
        }
    }

    private fun startWave() {
/*        //define creeps
        //define timer
        //find path
        val target = Astar.Node(8, 17)
        //target needs to change dynamically along the path
        val creep = Creep(CreepTypes.LEAFBUG)
        val posX = creep.squareField.mapPos["x"]!!
        val posY = creep.squareField.mapPos["y"]!!
        val creepNode = Astar.Node(posX, posY)
        val path = alg.findPath(creepNode, Astar.Node(8, 17), GameManager.squaresX, GameManager.squaresY)
        if(path != null) {
            val sortedPath = path.reversed()
            creep.path = sortedPath
            Log.i("Sorted path first: ", "${sortedPath.first()}")
            GameManager.creepList[creep] = sortedPath.first()
            //weg gefunden!
            Log.i("Path: ", "$path")
        } else {
            //weg blockiert!
            Log.i("Path: ", "Pfad blockiert")
        }
        //start wave*/
    }
}