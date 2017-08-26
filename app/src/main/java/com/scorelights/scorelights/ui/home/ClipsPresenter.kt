package com.scorelights.scorelights.ui.home


import com.scorelights.scorelights.data.rest.ClipsManager
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by korji on 6/26/17.
 */

class ClipsPresenter @Inject constructor() : ClipsContract.Presenter{

    @Inject lateinit var clipsManager: ClipsManager
    lateinit var clipsView: ClipsContract.View
    var subscriptions = CompositeSubscription()


    override fun takeView(clipsView: ClipsContract.View) {
        this.clipsView = clipsView
    }

    override fun requestClips(cacheControl: String, after: String?, sub: String, time: String, sort: String) {
        if (after != null) {
            val subscript = clipsManager.getClips(sub, after, time, sort, cacheControl)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { response ->
                                clipsView.setClips(response.after, response.clips)
                            },
                            { e ->
                                Timber.e("Error: " + e.message)
                                clipsView.showError()
                            }
                    )
            subscriptions.add(subscript)
        } else {
            Timber.d("No more clips to load.")
        }
    }

    override fun dropView() {
//        this.clipsView = null
    }
}
