package com.gutierre.mylists.framework.ui.main

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData

object ActivityState {
    val isResumed = MutableLiveData<Boolean>()
    private val activity = MutableLiveData<FragmentActivity>()

    fun activityResumed() {
        isResumed.postValue(true)
    }

    fun activityPaused() {
        isResumed.postValue(false)
    }

    fun setActivity(activityFrag: FragmentActivity) {
        activity.postValue(activityFrag)
    }

    fun getActivity(): FragmentActivity? {
        return activity.value
    }
}