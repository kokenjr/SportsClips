package com.scorelights.scorelights.ui.home

import android.content.pm.ActivityInfo
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.scorelights.scorelights.R
import com.scorelights.scorelights.ui.BaseActivity
import com.scorelights.scorelights.ui.adapter.PagerAdapter
import com.scorelights.scorelights.common.Constants
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivity() {

    var prevFilterMenuItem: MenuItem? = null
    var prevSortMenuItem: MenuItem? = null
    var sort = Constants.SORT_NEW
    var time = Constants.FILTER_ALL
    var menu: Menu? = null

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        toolbar.title = getString(R.string.app_name)
        setSupportActionBar(toolbar)

        //Lock rotation
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        var pagerAdapter = PagerAdapter(supportFragmentManager)
        addPagerFragments(pagerAdapter)
        vpClips.adapter = pagerAdapter
        tlClips.setupWithViewPager(vpClips)

        prevSortMenuItem = nvFilters.menu.findItem(R.id.sortNew)
        prevSortMenuItem!!.isChecked = true

        prevFilterMenuItem = nvFilters.menu.findItem(R.id.filterAllTime)
        prevFilterMenuItem!!.isChecked = true

        nvFilters.setNavigationItemSelectedListener({ item ->
            val groupId = item.groupId
            if (groupId == R.id.filterGroup) {
                prevFilterMenuItem?.let {menuItem ->
                    menuItem.isChecked = false
                }
                prevFilterMenuItem = item
            } else if (groupId == R.id.sortGroup) {
                prevSortMenuItem?.let {menuItem ->
                    menuItem.isChecked = false
                }
                prevSortMenuItem = item
            }

            item.isChecked = true
            when (item.itemId) {
                R.id.sortNew -> {
                    setSortType(Constants.SORT_NEW, pagerAdapter)
                    false
                }
                R.id.sortPopular -> {
                    setSortType(Constants.SORT_TOP, pagerAdapter)
                    false
                }
//                R.id.filterHour -> {
//                    setTimeFilter(Constants.FILTER_HOUR, pagerAdapter)
//                    false
//                }
                R.id.filterDay -> {
                    setTimeFilter(Constants.FILTER_DAY, pagerAdapter)
                    false
                }
                R.id.filterWeek -> {
                    setTimeFilter(Constants.FILTER_WEEK, pagerAdapter)
                    false
                }
                R.id.filterMonth -> {
                    setTimeFilter(Constants.FILTER_MONTH, pagerAdapter)
                    false
                }
                R.id.filterYear -> {
                    setTimeFilter(Constants.FILTER_YEAR, pagerAdapter)
                    false
                }
                R.id.filterAllTime -> {
                    setTimeFilter(Constants.FILTER_ALL, pagerAdapter)
                    false
                }
                else -> false

            }
        })
    }

    private fun setTimeFilter(filterType: String, pagerAdapter: PagerAdapter) {
        if (time != filterType) {
            time = filterType
            updatePagerAdapter(pagerAdapter)
        }
    }

    private fun setSortType(sortType: String, pagerAdapter: PagerAdapter) {
        if (sort != sortType) {
            sort = sortType
            updatePagerAdapter(pagerAdapter)
        }
    }

    private fun addPagerFragments(pagerAdapter: PagerAdapter) {
        pagerAdapter.addFragment(ClipsFragment().newInstance(getString(R.string.soccer),
                sort, time),
                getString(R.string.soccer))
        pagerAdapter.addFragment(ClipsFragment().newInstance(getString(R.string.nba),
                sort, time),
                getString(R.string.nba))
        pagerAdapter.addFragment(ClipsFragment().newInstance(getString(R.string.nfl),
                sort, time),
                getString(R.string.nfl))
        pagerAdapter.addFragment(ClipsFragment().newInstance(getString(R.string.nhl),
                sort, time),
                getString(R.string.nhl))
    }

    private fun updatePagerAdapter(pagerAdapter: PagerAdapter) {
        if (sort == Constants.SORT_NEW && time == Constants.FILTER_ALL) {
           menu!!.findItem(R.id.filter).setIcon(R.drawable.ic_filter)
        } else {
            menu!!.findItem(R.id.filter).setIcon(R.drawable.ic_filter_enabled)
        }
        pagerAdapter.clearFragments()
        addPagerFragments(pagerAdapter)
        pagerAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        this.menu = menu
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.filter) {
            drawerLayout.openDrawer(Gravity.END)
        }
        return super.onOptionsItemSelected(item)
    }

}
