package com.showmiso.swipecontacts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_on_boarding.*

class OnBoardingActivity : AppCompatActivity() {
    private val pagerAdapter: ViewPagerAdapter by lazy { ViewPagerAdapter(baseContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)

        view_pager.adapter = pagerAdapter
        view_pager.addOnPageChangeListener(onPageChangeListener)
        tab_selector.setupWithViewPager(view_pager, true)
        btn_skip.setOnClickListener(onClickListener)
    }

    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            when (position) {
                (pagerAdapter.count - 1) -> {
                    // last item
                    btn_next.text = getString(R.string.on_boarding_start)
                }
            }
        }
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.btn_skip -> {
                skipToMain()
            }
            R.id.btn_next -> {
                if (view_pager.currentItem == pagerAdapter.count - 1) {
                    // skip
                    skipToMain()
                } else {
                    // next

                }
            }
        }
    }

    private fun skipToMain() {

    }

}