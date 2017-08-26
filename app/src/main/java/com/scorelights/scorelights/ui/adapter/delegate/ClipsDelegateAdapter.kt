package com.scorelights.scorelights.ui.adapter.delegate

import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.devbrackets.android.exomedia.listener.VideoControlsVisibilityListener
import com.devbrackets.android.exomedia.ui.widget.VideoControlsMobile
import com.devbrackets.android.exomedia.ui.widget.VideoView
import com.scorelights.scorelights.R
import com.scorelights.scorelights.ui.BaseActivity
import com.scorelights.scorelights.ui.fullscreen.FullscreenActivity
import com.scorelights.scorelights.ui.adapter.utils.ViewType
import com.scorelights.scorelights.ui.adapter.utils.inflate
import com.scorelights.scorelights.common.Constants
import com.scorelights.scorelights.data.domain.Clip
import kotlinx.android.synthetic.main.view_custom_video_controls.view.*
import kotlinx.android.synthetic.main.view_clip.view.*
import org.ocpsoft.prettytime.PrettyTime
import org.parceler.Parcels


/**
 * Created by korji on 5/16/17.
 */
class ClipsDelegateAdapter(var fragment: Fragment) : ViewTypeDelegateAdapter {
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder, item: ViewType, position: Int) {
        holder as ClipsDelegateAdapter.TurnsViewHolder
        val vvClip = holder.itemView.findViewById<VideoView>(fragment.resources.getInteger(com.scorelights.scorelights.R.integer.vvClip))
        if (vvClip != null && !vvClip.isPlaying){

            val videoControls = vvClip.videoControls
            if (videoControls == null) {
                (item as Clip).clipIndex = position
                holder.bind(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return ClipsDelegateAdapter.TurnsViewHolder(parent, fragment)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType, position: Int) {
        holder as ClipsDelegateAdapter.TurnsViewHolder
        (item as Clip).clipIndex = position
        holder.bind(item)
    }

    class TurnsViewHolder(parent: ViewGroup, var fragment: Fragment) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.view_clip)){

        fun bind(clip: Clip) = with(itemView) {

            var isMuted = true
            val p = PrettyTime()
            val date = java.util.Date(clip.created * 1000)
            val formattedDate = p.format(date)
            tvCreated.text = formattedDate
            tvAuthor.text = "by ${clip.author}"

            var vvClip = flVideo.findViewById<VideoView>(resources.getInteger(com.scorelights.scorelights.R.integer.vvClip))

            if (vvClip != null) {
                flVideo.removeView(vvClip)
            }

            vvClip = VideoView(context)
            val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT)
            vvClip.layoutParams = layoutParams
            vvClip.setControls(VideoControlsMobile(context))
            vvClip.id = resources.getInteger(com.scorelights.scorelights.R.integer.vvClip)
            vvClip.tag = clip.title

            flVideo.addView(vvClip)

            /*
             * TODO: Refactor similar logic with FullscreenActivity
             * TODO: Maybe subclass VideoView and create custom VideoView to house of the logic
             */
            val videoUri = android.net.Uri.parse(clip.url)
            vvClip.setVideoURI(videoUri)
            vvClip.videoControls?.setTitle(clip.title)

            vvClip.setOnPreparedListener {
                if (isMuted) {
                    (vvClip as VideoView).setVolume(0f)
                    ivVolume.setImageResource(R.drawable.ic_volume_off)
                } else {
                    (vvClip as VideoView).setVolume(1f)
                    ivVolume.setImageResource(R.drawable.ic_volume_on)
                }
            }

            vvClip.setOnCompletionListener {
                (vvClip as VideoView).restart()
            }

            (fragment.activity as BaseActivity).toggleControlsButtons(ivShare, ivVolume,
                    ivFullScreen, View.VISIBLE)
            vvClip.videoControls?.setVisibilityListener(object: VideoControlsVisibilityListener {
                override fun onControlsShown() {
                    (fragment.activity as BaseActivity).toggleControlsButtons(ivShare, ivVolume,
                            ivFullScreen, View.VISIBLE)
                }

                override fun onControlsHidden() {
                    (fragment.activity as BaseActivity).toggleControlsButtons(ivShare, ivVolume,
                            ivFullScreen, View.INVISIBLE)
                }
            })

            ivFullScreen.setOnClickListener {
                (vvClip as VideoView).pause()
                val intent = android.content.Intent(context, FullscreenActivity::class.java)
                clip.muted = isMuted
                clip.seekPosition = (vvClip as VideoView).currentPosition
                intent.putExtra(Constants.EXTRA_CLIP, Parcels.wrap(clip))
                fragment.startActivityForResult(intent, Constants.FULLSCREEN_REQUEST_CODE)
            }

            ivVolume.setOnClickListener {
                if (isMuted){
                    (vvClip as VideoView).setVolume(1f)
                    isMuted = false
                    ivVolume.setImageResource(R.drawable.ic_volume_on)
                } else {
                    (vvClip as VideoView).setVolume(0f)
                    isMuted = true
                    ivVolume.setImageResource(R.drawable.ic_volume_off)
                }
            }

            ivShare.setOnClickListener {
                (fragment.activity as BaseActivity).share(clip.shareUrl)
            }

            tvSource.setOnClickListener {
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(context, Uri.parse(clip.link))
            }

        }

    }


}