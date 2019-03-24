package stream.reconfig.kirinmaru.plugins

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import stream.reconfig.kirinmaru.core.Plugin
import stream.reconfig.kirinmaru.plugins.novelfull.NOVELFULL_ORIGIN
import stream.reconfig.kirinmaru.plugins.novelfull.NovelFullPlugin
import stream.reconfig.kirinmaru.plugins.wuxiaworld.WUXIAWORLD_ORIGIN
import stream.reconfig.kirinmaru.plugins.wuxiaworld.WuxiaworldPlugin
import javax.inject.Provider

/**
 * Plugin registry
 */
@Module
interface PluginModule {

  @Binds
  @IntoMap
  @StringKey(WUXIAWORLD_ORIGIN)
  fun bindWuxiaWorld(plugin: WuxiaworldPlugin): Plugin

  @Binds
  @IntoMap
  @StringKey(NOVELFULL_ORIGIN)
  fun bindNovelFull(plugin: NovelFullPlugin): Plugin

  @Binds
  fun bindPlugins(pluginsMap: Map<String, @JvmSuppressWildcards Provider<Plugin>>): PluginMap
}

typealias PluginMap = Map<String, @JvmSuppressWildcards Provider<Plugin>>

fun PluginMap.getPlugin(origin: String): Plugin = get(origin)!!.get()