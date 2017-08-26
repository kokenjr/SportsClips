package com.scorelights.scorelights.ui.fullscreen

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.devbrackets.android.exomedia.listener.VideoControlsVisibilityListener
import com.scorelights.scorelights.data.domain.Clip
import org.parceler.Parcels
import android.view.WindowManager
import android.os.Build
import com.scorelights.scorelights.R
import com.scorelights.scorelights.ui.BaseActivity
import com.scorelights.scorelights.common.Constants
import kotlinx.android.synthetic.main.activity_fullscreen.*
import kotlinx.android.synthetic.main.view_custom_video_controls.*


class FullscreenActivity : BaseActivity() {
    private val fullScreenListener = FullScreenListener()
    private var pausedInOnStop = false
    private var clip: Clip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        val extras = intent.extras
        clip = Parcels.unwrap<Clip>(extras.getParcelable(Constants.EXTRA_CLIP))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        }

        val fullScreenParams = ivFullScreen.layoutParams as FrameLayout.LayoutParams
        fullScreenParams.marginEnd = resources.getDimension(R.dimen.fullscreen_exit_end_margin).toInt()
        ivFullScreen.layoutParams = fullScreenParams
        ivFullScreen.setImageResource(R.drawable.ic_fullscreen_exit)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        initUiFlags()

        /*
         * TODO: Refactor similar logic with ClipsDelegateAdapter
         * TODO: Maybe Sub class VideoView and create custom VideoView
         */

        vvClip.setVideoURI(Uri.parse(clip!!.url))
        vvClip.videoControls?.setTitle(clip!!.title)

        vvClip.setOnPreparedListener {
            if (clip!!.muted){
                vvClip.setVolume(0f)
            } else {
                ivVolume.setImageResource(R.drawable.ic_volume_on)
            }
            vvClip.seekTo(clip!!.seekPosition)
            vvClip.start()
        }

        vvClip.setOnCompletionListener { vvClip.restart() }

        vvClip.videoControls?.setVisibilityListener(object: VideoControlsVisibilityListener {
            override fun onControlsShown() {
                toggleControlsButtons(ivShare, ivVolume, ivFullScreen, View.VISIBLE)
            }

            override fun onControlsHidden() {
                toggleControlsButtons(ivShare, ivVolume, ivFullScreen, View.INVISIBLE)
                setUiFlags(true)
            }
        })

        ivVolume.setOnClickListener {
            if (clip!!.muted){
                vvClip.setVolume(1f)
                clip!!.muted = false
                ivVolume.setImageResource(R.drawable.ic_volume_on)
            } else {
                vvClip.setVolume(0f)
                clip!!.muted = true
                ivVolume.setImageResource(R.drawable.ic_volume_off)
            }
        }

        ivShare.setOnClickListener { share(clip!!.shareUrl) }

        ivFullScreen.setOnClickListener { onBackPressed() }
    }

    override fun onStop() {
        super.onStop()
        if (vvClip.isPlaying) {
            pausedInOnStop = true
            vvClip.pause()
        }
    }

    override fun onStart() {
        super.onStart()

        if (pausedInOnStop) {
            vvClip.start()
            pausedInOnStop = false
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        if (clip != null) {
            clip!!.seekPosition = vvClip.currentPosition
            clip!!.playing = vvClip.isPlaying
            intent.putExtra(Constants.EXTRA_CLIP, Parcels.wrap(clip))
        }
        setResult(RESULT_OK, intent)
        super.onBackPressed()
    }

    private fun initUiFlags() {
        var flags = View.SYSTEM_UI_FLAG_VISIBLE or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        val decorView = window.decorView
        if (decorView != null) {
            decorView.systemUiVisibility = flags
            decorView.setOnSystemUiVisibilityChangeListener(fullScreenListener)
        }
    }

    private fun setUiFlags(fullscreen: Boolean) {
        val decorView = window.decorView
        if (decorView != null) {
            decorView.systemUiVisibility = if (fullscreen) getFullscreenFlags() else View.SYSTEM_UI_FLAG_VISIBLE
        }
    }

    private fun getFullscreenFlags(): Int {

        var flags = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        return flags
    }

    private inner class FullScreenListener : View.OnSystemUiVisibilityChangeListener {
        override fun onSystemUiVisibilityChange(visibility: Int) {
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                vvClip.showControls()
            }
        }
    }
}
