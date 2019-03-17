package commons.android.core.menu

import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu


inline fun Menu.subMenu(groupId: Int, itemId: Int, order: Int, title: String, crossinline build: (SubMenu) -> Unit = {}) {
  build(addSubMenu(groupId, itemId, order, title))
}

inline fun Menu.menuItem(groupId: Int, itemId: Int, order: Int, title: String, crossinline build: (MenuItem) -> Unit = {}) {
  build(add(groupId, itemId, order, title))
}

inline fun SubMenu.subMenu(groupId: Int, itemId: Int, order: Int, title: String, crossinline build: (SubMenu) -> Unit = {}) {
  build(addSubMenu(groupId, itemId, order, title))
}

inline fun SubMenu.menuItem(groupId: Int, itemId: Int, order: Int, title: String, crossinline build: (MenuItem) -> Unit = {}) {
  build(add(groupId, itemId, order, title))
}