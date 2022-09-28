package com.webkul.mobikul.odoo.domain.usecase.home

import android.widget.AbsListView
import androidx.viewpager.widget.ViewPager
import com.webkul.mobikul.odoo.core.utils.Resource
import com.webkul.mobikul.odoo.domain.enums.HomeRefreshState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HandleRefreshStateUseCase @Inject constructor() {

    operator fun invoke(homeRefreshState: HomeRefreshState): Flow<Resource<Boolean>> = flow {
        when (homeRefreshState) {
            HomeRefreshState.SCROLL_STATE -> {
                if (homeRefreshState.state1 == (AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
                            or AbsListView.OnScrollListener.SCROLL_STATE_FLING
                            or AbsListView.OnScrollListener.SCROLL_STATE_IDLE)
                ) {
                    emit(Resource.Success(false))
                }
            }
            HomeRefreshState.PAGE_SCROLL_STATE -> if (homeRefreshState.state1 == 0
                && homeRefreshState.state2 == ViewPager.SCROLL_STATE_IDLE
            ) emit(
                Resource.Success(true)
            )
            HomeRefreshState.OFF_SET_CHANGED_STATE -> if (homeRefreshState.state1 == 0) emit(
                Resource.Success(
                    true
                )
            )
        }
    }
}