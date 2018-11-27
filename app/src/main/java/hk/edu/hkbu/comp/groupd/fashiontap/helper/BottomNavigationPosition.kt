package hk.edu.hkbu.comp.groupd.fashiontap

import android.support.v4.app.Fragment


enum class BottomNavigationPosition(val position: Int, val id: Int) {
    HOME(0, R.id.navigation_home),
    DASHBOARD(1, R.id.navigation_dashboard),
    NOTIFICATIONS(2, R.id.navigation_notifications),
}

fun findNavigationPositionById(id: Int): BottomNavigationPosition = when (id) {
    BottomNavigationPosition.HOME.id -> BottomNavigationPosition.HOME
    BottomNavigationPosition.DASHBOARD.id -> BottomNavigationPosition.DASHBOARD
    BottomNavigationPosition.NOTIFICATIONS.id -> BottomNavigationPosition.NOTIFICATIONS
    else -> BottomNavigationPosition.HOME
}

fun BottomNavigationPosition.createFragment(): Fragment = when (this) {
    BottomNavigationPosition.HOME -> HomeFragment.newInstance()
    BottomNavigationPosition.DASHBOARD -> FashionFragment.newInstance()
    BottomNavigationPosition.NOTIFICATIONS -> YoutubeFragment.newInstance()
}

fun BottomNavigationPosition.getTag(): String = when (this) {
    BottomNavigationPosition.HOME -> HomeFragment.TAG
    BottomNavigationPosition.DASHBOARD -> FashionFragment.TAG
    BottomNavigationPosition.NOTIFICATIONS -> YoutubeFragment.TAG
}

