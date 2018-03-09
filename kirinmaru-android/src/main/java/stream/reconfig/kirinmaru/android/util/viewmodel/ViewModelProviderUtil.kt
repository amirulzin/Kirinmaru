package stream.reconfig.kirinmaru.android.util.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * ViewModelProvider extensions for support library Fragment and FragmentActivity
 */
fun <T : ViewModel> Fragment.viewModel(factory: ViewModelProvider.Factory, clazz: Class<T>) =
    ViewModelProviders.of(this, factory).get(clazz)

fun <T : ViewModel> FragmentActivity.viewModel(factory: ViewModelProvider.Factory, clazz: Class<T>) =
    ViewModelProviders.of(this, factory).get(clazz)

fun <T : ViewModel> Fragment.viewModel(clazz: Class<T>) =
    ViewModelProviders.of(this).get(clazz)

fun <T : ViewModel> FragmentActivity.viewModel(clazz: Class<T>) =
    ViewModelProviders.of(this).get(clazz)

