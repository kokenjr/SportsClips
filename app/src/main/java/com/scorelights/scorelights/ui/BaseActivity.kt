package com.scorelights.scorelights.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.scorelights.scorelights.R

/**
 * Created by korji on 6/28/17.
 */
open class BaseActivity : AppCompatActivity() {

    fun share(url: String){
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, url)
        sendIntent.type = "text/plain"
        val chooserIntent = Intent.createChooser(sendIntent, getString(R.string.share_url))
        startActivity(chooserIntent)
    }

    fun toggleControlsButtons(ivShare: View, ivVolume: View, ivFullScreen: View, visibility: Int) {
        ivShare.visibility = visibility
        ivVolume.visibility = visibility
        ivFullScreen.visibility = visibility
    }
}