package com.scorelights.scorelights.ui.home


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devbrackets.android.exomedia.ui.widget.VideoView
import com.scorelights.scorelights.ui.listener.InfiniteScrollListener
import com.scorelights.scorelights.R
import com.scorelights.scorelights.ui.adapter.ClipsAdapter
import com.scorelights.scorelights.common.Constants
import com.scorelights.scorelights.SLApplication
import com.scorelights.scorelights.data.domain.Clip
import kotlinx.android.synthetic.main.fragment_clips.*
import org.parceler.Parcels
import timber.log.Timber
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 */
class ClipsFragment : Fragment(), ClipsContract.View {

    @Inject lateinit var clipsPresenter: ClipsPresenter

    var sub = ""
    var sort = ""
    var time = ""
    var after: String? = null

    var snackbar: Snackbar? = null

    fun newInstance(subreddit: String, sort: String, time: String): ClipsFragment {
        val fragment = ClipsFragment()

        val bundle = Bundle()
        bundle.putString(Constants.EXTRA_SUBREDDIT, subreddit)
        bundle.putString(Constants.EXTRA_SORT, sort)
        bundle.putString(Constants.EXTRA_TIME, time)
        fragment.arguments = bundle

        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SLApplication.appComponent.inject(this)
    }

    override fun onResume() {
        super.onResume()
        clipsPresenter.takeView(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_clips, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        after = ""
        tvNoClips.visibility = View.INVISIBLE

        swipeRefreshLayout.setOnRefreshListener {
            if (snackbar != null) {
                snackbar!!.dismiss()
            }

            swipeRefreshLayout.isRefreshing = false
            tvNoClips.visibility = View.INVISIBLE
            (rvClips.adapter as ClipsAdapter).clear()
            after = ""
            clipsPresenter.requestClips("no-cache", after, sub, time, sort)
        }

        sub = arguments.getString(Constants.EXTRA_SUBREDDIT)
        time = arguments.getString(Constants.EXTRA_TIME)
        sort = arguments.getString(Constants.EXTRA_SORT)

        rvClips.setHasFixedSize(true)
        val linearLayoutManager = LinearLayoutManager(context)
        rvClips.layoutManager = linearLayoutManager
        rvClips.clearOnScrollListeners()
        rvClips.addOnScrollListener(InfiniteScrollListener({ clipsPresenter.requestClips("",
                after, sub, time, sort) }, linearLayoutManager))

        if (rvClips.adapter == null) {
            rvClips.adapter = ClipsAdapter(this)
        }

        clipsPresenter.requestClips("", after, sub, time, sort)
    }

    override fun setClips(after: String?, clips: List<Clip>) {
        if (rvClips != null) {
            this.after = after
            val adapter = (rvClips.adapter as ClipsAdapter)
            adapter.addClips(clips)

            if (adapter.itemCount == 0) {
                tvNoClips.visibility = View.VISIBLE
            }
        } else {
            Timber.d("rvClips is null")
            showErrorToast()
        }
    }

    override fun showError() {
        val adapter = (rvClips.adapter as ClipsAdapter)
        adapter.removeLoading()
        showErrorToast()
    }

    override fun onPause() {
        super.onPause()
//        clipsPresenter.dropView()
    }

    override fun onStop() {
        stopPlaybackOnVisible()
        super.onStop()
    }

    private fun stopPlaybackOnVisible() {
        if (rvClips != null && rvClips.layoutManager != null){
            var position = (rvClips.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            val lastPosition = (rvClips.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

            while (position <= lastPosition) {
                val vvClip = getVideoViewFromRecyclerView(position)
                if (vvClip != null && vvClip.isPlaying){
                    vvClip.pause()
                }
                position++
            }
        }
    }

    private fun getVideoViewFromRecyclerView(firstPosition: Int): VideoView? {
        val rlClip = rvClips.layoutManager.findViewByPosition(firstPosition)
        var vvClip: VideoView? = null
        if (rlClip != null){
            vvClip = rlClip.findViewById<VideoView>(resources.getInteger(R.integer.vvClip))
        }
        return vvClip
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == Constants.FULLSCREEN_REQUEST_CODE
                && data.extras != null) {
            val clip = Parcels.unwrap<Clip>(data.extras.getParcelable(Constants.EXTRA_CLIP))

            if (clip != null) {
                //TODO: Maybe use holder or adapter instead?
                val vvClip = getVideoViewFromRecyclerView(clip.clipIndex)

                if (vvClip != null) {
                    vvClip.seekTo(clip.seekPosition)
                    if (clip.playing) {
                        vvClip.start()
                    }
                }
            }
        }
    }

    private fun showErrorToast() {
        if (flClips != null) {
            showError(flClips, getString(R.string.error_occurred))
        }
    }

    private fun showError(view: View, message: String) {
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE)
        snackbar!!.setAction(getString(R.string.dismiss), {
            snackbar!!.dismiss()
        })
        snackbar!!.setActionTextColor(ContextCompat.getColor(context, R.color.white))
        val sbView = snackbar!!.view
        sbView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent))
        snackbar!!.show()
    }
}
