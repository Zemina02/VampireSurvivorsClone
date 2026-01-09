package com.example.game.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.game.R

class Game(
    context: Context,
    width: Int,
    height: Int,
    private val viewModel: GameViewModel
) : SurfaceView(context), Runnable {

    private var gameThread: Thread? = null
    private var isPlaying = false
    lateinit var surfaceHolder: SurfaceHolder
    lateinit var canvas: Canvas
    private lateinit var background: Bitmap
    lateinit var player: Player
    lateinit var paint: Paint

    private val enemies = mutableListOf<Enemy>()
    private val projectiles = mutableListOf<Projectile>()
    private val collisionManager = CollisionManager()

    private var lastSpawnTime = System.currentTimeMillis()
    private val spawnInterval = 500L
    private var lastTimeUpdate = System.currentTimeMillis()

    private var cameraX = 0f
    private var cameraY = 0f
    private val cameraZoom = 1.3f

    private val weapons = listOf(Weapon(WeaponType.FIREBALL, context))
    private var currentWeapon = weapons[0]


    private var screenWidth = width
    private var screenHeight = height

    init {
        surfaceHolder = holder
        paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        val backgroundBitmap =
            BitmapFactory.decodeResource(context.resources, R.drawable.fundo)
        background = Bitmap.createScaledBitmap(backgroundBitmap, width, height, false)

        player = Player(context, width.toFloat(), height.toFloat())

    }

    override fun run() {
        while (isPlaying) {
            update()
            draw()
            control()
        }
    }

    fun resume() {
        isPlaying = true
        gameThread = Thread(this)
        gameThread!!.start()
    }

    fun pause() {
        isPlaying = false
        try {
            gameThread?.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun control() {
        Thread.sleep(17)
    }

    fun update() {
        val currentTime = System.currentTimeMillis()
        val deltaTime = currentTime - lastTimeUpdate
        lastTimeUpdate = currentTime

        if (isJoystickActive) {
            val dx = joystickKnobX - joystickBaseX
            val dy = joystickKnobY - joystickBaseY
            val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()

            if (distance > 0) {
                val directionX = dx / distance
                val directionY = dy / distance
                player.move(directionX, directionY)
            } else {
                player.stopMoving()
            }
        } else {
            player.stopMoving()
        }

        player.update()

        cameraX = player.x + player.frameWidth / 2 - screenWidth / (2f * cameraZoom)
        cameraY = player.y + player.frameHeight / 2 - screenHeight / (2f * cameraZoom)
        val maxCameraX = background.width - width / cameraZoom
        val maxCameraY = background.height - height / cameraZoom
        cameraX = cameraX.coerceIn(0f, maxCameraX)
        cameraY = cameraY.coerceIn(0f, maxCameraY)

        viewModel.updateTime(deltaTime)

        if (currentTime - lastSpawnTime > spawnInterval) {
            spawnEnemy()
            lastSpawnTime = currentTime
        }

        for (enemy in enemies) {
            enemy.moveTowardsPlayer(player.x, player.y)
        }
        viewModel.updateEnemyCount(enemies.size)

        currentWeapon.update()

        val (targetX, targetY) = when (player.currentDirection) {
            Player.Direction.DOWN -> Pair(player.x + 50, player.y + 100)
            Player.Direction.UP -> Pair(player.x + 50, player.y - 100)
            Player.Direction.LEFT -> Pair(player.x - 100, player.y + 50)
            Player.Direction.RIGHT -> Pair(player.x + 100, player.y + 50)
        }

        val newProjectile = currentWeapon.shoot(
            player.x + player.frameWidth / 2,
            player.y + player.frameHeight / 2,
            targetX,
            targetY
        )
        if (newProjectile != null) {
            projectiles.add(newProjectile)
        }

        for (projectile in projectiles) {
            projectile.update()
        }

        val collisions = collisionManager.checkProjectileEnemyCollisions(projectiles, enemies)
        for ((projectile, enemy) in collisions) {
            enemy.takeDamage(projectile.damage)
            projectiles.remove(projectile)
            if (!enemy.isAlive()) {
                enemies.remove(enemy)
                viewModel.addScore(10)
            }
        }

        projectiles.removeAll { it.isOutOfBounds(screenWidth, screenHeight) }

        val collidedEnemies = collisionManager.checkPlayerEnemyCollisions(player, enemies)
        if (collidedEnemies.isNotEmpty()) {
            viewModel.damagePlayer(collidedEnemies.size * 5)
        }

        if (viewModel.isGameOver.value) {
            isPlaying = false
        }
    }


    private fun spawnEnemy() {
        val side = (0..3).random()
        val (spawnX, spawnY) = when (side) {
            0 -> Pair((50..screenWidth - 50).random().toFloat(), -50f)
            1 -> Pair((screenWidth + 50).toFloat(), (50..screenHeight - 50).random().toFloat())
            2 -> Pair((50..screenWidth - 50).random().toFloat(), (screenHeight + 50).toFloat())
            else -> Pair(-50f, (50..screenHeight - 50).random().toFloat())
        }

        val score = viewModel.score.value
        val enemyType = when {
            score < 100 -> Enemy.EnemyType.GOBLIN
            score < 300 -> {
                if (Math.random() > 0.7) Enemy.EnemyType.ORC else Enemy.EnemyType.GOBLIN
            }
            else -> {
                when ((Math.random() * 3).toInt()) {
                    0 -> Enemy.EnemyType.GOBLIN
                    1 -> Enemy.EnemyType.ORC
                    else -> Enemy.EnemyType.DEMON
                }
            }
        }

        val enemy = Enemy(context!!, spawnX, spawnY, enemyType)
        enemies.add(enemy)
    }

    fun draw() {
        if (surfaceHolder.surface.isValid) {
            canvas = surfaceHolder.lockCanvas()
            canvas.save()

            canvas.scale(cameraZoom, cameraZoom)
            canvas.translate(-cameraX, -cameraY)
            canvas.drawBitmap(background, 0f, 0f, null)

            for (enemy in enemies) {
                enemy.draw(canvas)
            }

            for (projectile in projectiles) {
                projectile.draw(canvas)
            }


            player.draw(canvas)

            canvas.restore()

            if (isJoystickActive) {
                paint.color = Color.argb(100, 50, 50, 50)
                canvas.drawCircle(joystickBaseX, joystickBaseY, joystickRadius, paint)

                paint.color = Color.argb(200, 80, 80, 80)

                val dx = joystickKnobX - joystickBaseX
                val dy = joystickKnobY - joystickBaseY
                val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()

                if (distance > joystickRadius) {
                    val knobX = joystickBaseX + (dx / distance) * joystickRadius
                    val knobY = joystickBaseY + (dy / distance) * joystickRadius
                    canvas.drawCircle(knobX, knobY, 60f, paint)
                } else {
                    canvas.drawCircle(joystickKnobX, joystickKnobY, 60f, paint)
                }
            }

            drawGameUI(canvas)

            surfaceHolder.unlockCanvasAndPost(canvas)
        }
    }

    private fun drawGameUI(canvas: Canvas) {
        val paint = Paint().apply {
            color = Color.WHITE
            textSize = 50f
            isFakeBoldText = true
        }

        canvas.drawText("Score: ${viewModel.score.value}", 30f, 80f, paint)

        val healthPaint = Paint().apply {
            color = if (viewModel.health.value > 50) Color.GREEN else Color.RED
            textSize = 40f
            isFakeBoldText = true
        }
        canvas.drawText("Health: ${viewModel.health.value}", 30f, 140f, healthPaint)

        val enemyPaint = Paint().apply {
            color = Color.CYAN
            textSize = 40f
            isFakeBoldText = true
        }
        canvas.drawText("Enemies: ${enemies.size}", 30f, 200f, enemyPaint)

        val timePaint = Paint().apply {
            color = Color.YELLOW
            textSize = 40f
            isFakeBoldText = true
        }
        val timeSeconds = viewModel.timeElapsed.value / 1000
        canvas.drawText("Time: ${timeSeconds}s", 30f, 260f, timePaint)
    }

    private var joystickBaseX = 0f
    private var joystickBaseY = 0f
    private var joystickKnobX = 0f
    private var joystickKnobY = 0f
    private var isJoystickActive = false
    private val joystickRadius = 150f

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isJoystickActive = true
                joystickBaseX = event.x
                joystickBaseY = event.y
                joystickKnobX = event.x
                joystickKnobY = event.y
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                if (isJoystickActive) {
                    joystickKnobX = event.x
                    joystickKnobY = event.y
                    return true
                }
            }

            MotionEvent.ACTION_UP -> {
                isJoystickActive = false
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}
