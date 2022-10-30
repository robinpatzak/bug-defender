package de.mow2.towerdefense.controller

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat.getColor
import de.mow2.towerdefense.R
import de.mow2.towerdefense.model.gameobjects.actors.TowerTypes

@SuppressLint("ViewConstructor")
/**
 * Custom ImageButton - This button is part of the in-game build menu
 */
class BuildButton(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int, type: TowerTypes, level: Int) : AppCompatImageButton(context, attributeSet, defStyleAttr) {

    init {
        this.setBackgroundColor(getColor(context, R.color.green_overlay))
        this.setPadding(0, 0, 0, 30)
        when(type) {
            TowerTypes.BLOCK -> {
                when(level) {
                    0 -> {this.setImageResource(R.drawable.tower_block)}
                    1 -> {this.setImageResource(R.drawable.tower_block1)}
                    else -> {}
                }
            }
            TowerTypes.SLOW -> {
                when(level) {
                    0 -> {this.setImageResource(R.drawable.tower_slow)}
                    1 -> {this.setImageResource(R.drawable.tower_slow1)}
                    else -> {}
                }
            }
            TowerTypes.AOE -> {
                when(level) {
                    0 -> {this.setImageResource(R.drawable.tower_aoe)}
                    1 -> {this.setImageResource(R.drawable.tower_aoe1)}
                    else -> {}
                }
            }
        }
    }
}