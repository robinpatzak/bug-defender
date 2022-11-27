package de.mow2.towerdefense.controller

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.*
import androidx.preference.PreferenceManager
import com.shashank.sony.fancytoastlib.FancyToast
import de.mow2.towerdefense.MainActivity
import de.mow2.towerdefense.R
import de.mow2.towerdefense.controller.SoundManager.musicSetting
import de.mow2.towerdefense.controller.SoundManager.soundPool
import de.mow2.towerdefense.controller.helper.BuildButton
import de.mow2.towerdefense.controller.helper.GameState
import de.mow2.towerdefense.databinding.ActivityGameBinding
import de.mow2.towerdefense.model.core.BuildUpgradeMenu
import de.mow2.towerdefense.model.core.GameController
import de.mow2.towerdefense.model.core.GameManager
import de.mow2.towerdefense.model.gameobjects.actors.TowerTypes


/**
 * This Activity starts the game
 */
class GameActivity : AppCompatActivity(), GameController {
    private val gameState = GameState()

     //game content and gui
    private val gameManager = GameManager(this)
    private lateinit var gameLayout: LinearLayout
    private lateinit var gameView: GameView
    private lateinit var chrono: Chronometer
    private lateinit var coinsTxt: TextView
    private lateinit var healthBar: ProgressBar
    private lateinit var healthText: TextView
    private lateinit var waveBar: ProgressBar
    private lateinit var waveDisplay: TextView
    private val menuPopup = PopupFragment()
    private val tutPopup = TutorialFragment()
    private val fm = supportFragmentManager
    private lateinit var prefManager: SharedPreferences

    //buildmenu
    private lateinit var buildMenuScrollView: HorizontalScrollView
    private lateinit var buildMenuLayout: LinearLayout
    private lateinit var buildButton: ImageButton
    private var buildMenuExists = false

    // View Binding
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        //create new game view
        gameLayout = binding.gameViewContainer
        gameView = GameView(this, this, gameManager)
        gameLayout.addView(gameView)
        setContentView(binding.root)
        //load settings and GUI
        loadPrefs()
        initGUI()
        hideSystemBars()
        //init game manager
        gameManager.initLevel(GameManager.gameLevel) //TODO: Load saved game
        //start level timer
        chrono.start()
        // shows tutorial
        if(GameManager.tutorialsActive) {
           displayTutorial(false)
        }
    }

    /**
     * pauses Game and goes back to main menu
     */
    fun pauseGame(view: View) {
        //TODO: save game state and return to main menu
        gameState.saveGameState(this)
        startActivity(Intent(this, MainActivity::class.java))
    }

    /**
     * Triggered if liveAmt = 0, sets game over screen
     */
    override fun onGameOver() {
        setContentView(R.layout.gameover_view)
        SoundManager.mediaPlayer.release()
        soundPool.play(Sounds.GAMEOVER.id, 1F, 1F, 1, 0, 1F)
        val timeValue = findViewById<TextView>(R.id.timeValue)
        val levelValue = findViewById<TextView>(R.id.levelValue)
        val enemyValue = findViewById<TextView>(R.id.enemyValue)
        //"${this.resources.getString(R.string.timeMade)} ${chrono.text}".also { timeValue.text = it }
        timeValue.text = "${chrono.text}"
        levelValue.text = "${GameManager.gameLevel}"
        enemyValue.text = "${GameManager.killCounter}"
        GameManager.reset()
    }

    /**
     * Button-triggered reset (return to main menu)
     */
    fun leaveGame(view: View) {
        startActivity(Intent(this, MainActivity::class.java))
        GameManager.reset()
    }

    /**
     * Load all saved user preferences
     */
    private fun loadPrefs() {
        prefManager = PreferenceManager.getDefaultSharedPreferences(this)
        GameManager.tutorialsActive = prefManager.getBoolean("tutorial_pref", true)
        SoundManager.loadPreferences(this)
    }

    /**
     * opens menu as pop up window if menu button is clicked
     */
    fun popUpMenu(view: View) {
        menuPopup.show(fm, "menuDialog")
    }

    /**
     * Initialize all game GUI references and contents
     */
    private fun initGUI() {
        //reference game gui
        chrono = binding.timeView
        coinsTxt = binding.coinsText
        healthBar = binding.healthProgressBar
        healthText = binding.healthText
        waveBar = binding.waveProgressBar
        waveDisplay = binding.waveText
        //reference build menu container
        buildMenuScrollView = binding.buildMenuWrapper
        buildMenuLayout = binding.buildMenuContainer

        // detect which button is currently selected
        buildButton = binding.buildButton

        //create bottom gui contents
        binding.bottomGUI.children.forEach { view ->
            view.setOnClickListener { button ->
                if (GameManager.selectedTool == button.id){
                    GameManager.selectedTool = null
                    binding.bottomGUI.children.forEach { it.setBackgroundResource(R.drawable.defaultbtn_states) }
                } else {
                    GameManager.selectedTool = button.id
                    binding.bottomGUI.children.forEach { it.setBackgroundResource(R.drawable.defaultbtn_states) }
                    button.setBackgroundResource(R.drawable.button_border_active)
                }
            }
            if(view == buildButton) {
                view.setOnLongClickListener {
                    toggleBuildMenu()
                    return@setOnLongClickListener true
                }
            }
        }

        //"choose a tower" menu displayed on long click
        val buildMenu = BuildUpgradeMenu(gameManager, this)
        TowerTypes.values().forEachIndexed { i, type ->
            val towerBtn = BuildButton(this, null, R.style.MenuButton_Button, type)
            val towerBtnText = TextView(this)
            towerBtnText.text = "${buildMenu.getTowerCost(type)}"
            towerBtnText.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            towerBtnText.setTextColor(Color.YELLOW)
            towerBtn.id = i
            towerBtn.setOnClickListener {
                GameManager.selectedTool = buildButton.id
                GameManager.selectedTower = type
                binding.bottomGUI.children.forEach { it.setBackgroundResource(R.drawable.defaultbtn_states) }
                binding.buildButton.setBackgroundResource(R.drawable.button_border_active)
                toggleBuildMenu()
                when(type) {
                    TowerTypes.BLOCK -> buildButton.setImageResource(R.drawable.tower_block_imagebtn)
                    TowerTypes.SLOW -> buildButton.setImageResource(R.drawable.tower_slow_imagebtn)
                    TowerTypes.AOE -> buildButton.setImageResource(R.drawable.tower_aoe_imagebtn)
                    TowerTypes.MAGIC -> buildButton.setImageResource(R.drawable.tower_magic_imagebtn)
                }
            }
            val buttonContainer = LinearLayout(this)
            buttonContainer.orientation = LinearLayout.VERTICAL
            buttonContainer.addView(towerBtnText)
            buttonContainer.addView(towerBtn)
            buildMenuLayout.addView(buttonContainer)
        }
        GameManager.selectedTool = null //deselect any tool at beginning
    }

    /**
     * Method to write all GUI-related data into their respective layout element
     */
    override fun updateGUI() {
        runOnUiThread {
            coinsTxt.text = GameManager.coinAmnt.toString()
            healthBar.progress = GameManager.livesAmnt
            waveBar.progress = GameManager.killCounter
            val livesText = "${GameManager.livesAmnt} / ${healthBar.max}"
            healthText.text = livesText
            val waveText = "${GameManager.killCounter} / ${waveBar.max}"
            waveDisplay.text = waveText
        }
    }

    override fun updateHealthBarMax(newMax: Int) {
        healthBar.max = newMax
    }

    override fun updateProgressBarMax(newMax: Int) {
        waveBar.max = newMax
        waveBar.progress = 0
    }

    override fun showToastMessage(message: String, type: Int) {
        runOnUiThread {
            val toast = FancyToast.makeText(this, message, FancyToast.LENGTH_SHORT, type, false )
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    override fun onResume(){
        super.onResume()
        // (re-)initialize MediaPlayer with correct settings
        SoundManager.initMediaPlayer(this, R.raw.exploration)
        SoundManager.playSounds()
        SoundManager.loadSounds(this)
        if(!musicSetting) {
            SoundManager.pauseMusic()
        }
        if(!SoundManager.soundSetting){
            soundPool.release()
        }
    }

    override fun onPause() {
        super.onPause()
        // stops MediaPlayer while not being in activity
        SoundManager.mediaPlayer.release()
    }

    /**
     * immersive mode (hide system bars)
     */
    private fun hideSystemBars() {
        // Hide both the status bar and the navigation bar
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun toggleBuildMenu() {
        if (!buildMenuExists){
            buildMenuScrollView.visibility = View.VISIBLE
        } else {
            buildMenuScrollView.visibility = View.GONE
        }
        buildMenuExists = !buildMenuExists
    }

    fun displayTutorial(active: Boolean) {
        if(active) {
            tutPopup.show(fm, "tutorialDialog")
            gameView.toggleGameLoop(false)
        } else {
            prefManager.edit {
                putBoolean("tutorial_pref", false)
            }
            gameView.toggleGameLoop(true)
        }
    }

    /**
     * sets a highlight for the in the tutorial mentioned element
     * @param item the element which should be highlighted
     */
    fun highlight(item: String) {
        binding.bottomGUI.children.forEach { it.alpha = 0.2F }
        binding.topGUI.children.forEach { it.alpha = 0.2F }
        binding.progressBarContainer.children.forEach {it.alpha = 0.2F}
        when(item) {
            "bottomGui" -> {
                binding.bottomGUI.children.forEach { it.alpha = 1F }
            }
            "bottomLeft" -> {
                binding.deleteButton.alpha = 1F
            }
            "bottomRight" -> {
                binding.upgradeButton.alpha = 1F
            }
            "bottomMiddle" -> {
                binding.buildButton.alpha = 1F
            }
            "topGui" -> {
                binding.topGUI.children.forEach { it.alpha = 1F }
            }
            "topGuiLeft" -> {
                binding.timeView.alpha = 1F
                binding.clockImage.alpha = 1F
            }
            "topGuiRight" -> {
                binding.coinsText.alpha = 1F
                binding.coinImg.alpha = 1F
            }
            "topGuiLeftBar" -> {
                binding.healthProgressBar.alpha = 1F
                binding.healthText.alpha = 1F
                binding.healthBallImg.alpha = 1F
            }
            "topGuiRightBar" -> {
                binding.waveProgressBar.alpha = 1F
                binding.waveText.alpha = 1F
                binding.progressBallImg.alpha = 1F
            }
            "topGuiMenu" -> {
                binding.menuBtn.alpha = 1F
            }
            "endTutorial" -> {
                binding.bottomGUI.children.forEach { it.alpha = 1F }
                binding.topGUI.children.forEach { it.alpha = 1F }
                binding.progressBarContainer.children.forEach {it.alpha = 1F}
            }
        }
    }
}

