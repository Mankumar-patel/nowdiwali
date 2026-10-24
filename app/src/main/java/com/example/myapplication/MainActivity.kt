package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var animationView: TextView
    private lateinit var titleView: TextView

    private var effectPlayer: MediaPlayer? = null
    private var musicPlayer: MediaPlayer? = null
    private var animator: ValueAnimator? = null

    private val frames = listOf(
        "✨       🎆\n   🎇       ✨\n🪔  🪔  🪔",
        "🎆   ✨    🎇\n✨     🎆\n🪔  🪔  🪔",
        "💥    🎇   ✨\n   🎆      💥\n🪔  🪔  🪔",
        "✨    💥   🎆\n🎇      ✨\n🪔  🪔  🪔"
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        animationView = findViewById(R.id.tvAnimation)
        titleView = findViewById(R.id.tvTitle)

        findViewById<Button>(R.id.btnFireworks)
            .setOnClickListener {
                playEffect(R.raw.firework_sound)
                startAnimation()
            }

        findViewById<Button>(R.id.btnDiyas)
            .setOnClickListener {
                playEffect(R.raw.diya_sound)
                animationView.text = "🪔  🪔  🪔\n✨  🪔  ✨"
                pulseTitle()
            }

        findViewById<Button>(R.id.btnLanterns)
            .setOnClickListener {
                playEffect(R.raw.lantern_sound)
                startLanternAnimation()
            }

        findViewById<Button>(R.id.btnMusic)
            .setOnClickListener {
                toggleMusic()
            }

        findViewById<Button>(R.id.btnCelebrate)
            .setOnClickListener {
                playEffect(R.raw.firework_sound)
                startAnimation()
                startMusic()
            }

        findViewById<Button>(R.id.btnStop)
            .setOnClickListener {
                stopEverything()
            }
    }

    private fun playEffect(soundId: Int) {
        effectPlayer?.release()
        effectPlayer = MediaPlayer.create(this, soundId)
        effectPlayer?.setOnCompletionListener {
            it.release()
            if (effectPlayer === it) effectPlayer = null
        }
        effectPlayer?.start()
    }

    private fun startAnimation() {
        animator?.cancel()

        animator = ValueAnimator.ofInt(0, frames.size - 1).apply {
            duration = 2400
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()

            addUpdateListener {
                val index = it.animatedValue as Int
                animationView.text = frames[index]
            }

            start()
        }

        pulseTitle()
    }

    private fun pulseTitle() {
        titleView.animate()
            .scaleX(1.12f)
            .scaleY(1.12f)
            .setDuration(400)
            .withEndAction {
                titleView.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(400)
                    .start()
            }
            .start()
    }

    private fun startLanternAnimation() {
        animator?.cancel()

        val lanternFrames = listOf(
            "🏮             🏮\n\n       🏮",
            "\n🏮           🏮\n\n   🏮",
            "\n\n🏮          🏮\n🏮",
            "🏮               🏮\n\n       🏮"
        )

        animator = ValueAnimator.ofInt(0, lanternFrames.size - 1).apply {
            duration = 3000
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()

            addUpdateListener {
                animationView.text =
                    lanternFrames[it.animatedValue as Int]
            }

            start()
        }
    }

    private fun startMusic() {
        if (musicPlayer == null) {
            musicPlayer = MediaPlayer.create(
                this,
                R.raw.background_music
            )?.apply {
                isLooping = true
            }
        }

        if (musicPlayer?.isPlaying != true) {
            musicPlayer?.start()
        }
    }

    private fun toggleMusic() {
        if (musicPlayer?.isPlaying == true) {
            musicPlayer?.pause()
        } else {
            startMusic()
        }
    }

    private fun stopEverything() {
        animator?.cancel()
        animator = null

        effectPlayer?.release()
        effectPlayer = null

        musicPlayer?.stop()
        musicPlayer?.release()
        musicPlayer = null

        animationView.animate().cancel()
        titleView.animate().cancel()

        titleView.scaleX = 1f
        titleView.scaleY = 1f

        animationView.text = "🎆\n🪔 🪔 🪔\n✨ 🎇 ✨"
    }

    override fun onDestroy() {
        animator?.cancel()
        effectPlayer?.release()
        musicPlayer?.release()
        super.onDestroy()
    }
}
