package stream.reconfig.kirinmaru.plugins

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import stream.reconfig.kirinmaru.core.Plugin
import stream.reconfig.kirinmaru.plugins.gravitytales.GRAVITYTALES_ORIGIN
import stream.reconfig.kirinmaru.plugins.gravitytales.GravityTalesPlugin
import stream.reconfig.kirinmaru.plugins.wuxiaworld.WUXIAWORLD_ORIGIN
import stream.reconfig.kirinmaru.plugins.wuxiaworld.WuxiaworldPlugin
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Plugin registry
 */
@Singleton
@Module
interface PluginModule {

  @Binds
  @IntoMap
  @StringKey(GRAVITYTALES_ORIGIN) //this will dictate the plugin name in the app
  fun bindGravityTales(gravityTalesPlugin: GravityTalesPlugin): Plugin

  @Binds
  @IntoMap
  @StringKey(WUXIAWORLD_ORIGIN)
  fun bindWuxiaWorld(wuxiaworldPlugin: WuxiaworldPlugin): Plugin

  @Binds
  fun bindPlugins(pluginsMap: Map<String, @JvmSuppressWildcards Provider<Plugin>>): PluginMap
}

typealias PluginMap = Map<String, @JvmSuppressWildcards Provider<Plugin>>

fun PluginMap.getPlugin(origin: String): Plugin = get(origin)!!.get()