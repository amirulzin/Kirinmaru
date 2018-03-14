package stream.reconfig.kirinmaru.core

import io.reactivex.Single

/**
 * The core app will use this to determine order of behaviors called on any given plugin
 */
interface PluginContract {

  fun obtainChapters(plugin: Plugin, novelDetail: NovelDetail, nextPage: Int): Single<List<ChapterId>> {

    try {
      return plugin.obtainChapters(novelDetail)
    } catch (e: NotImplementedError) {

    }
    try {
      return plugin.obtainChapters(novelDetail, nextPage)
    } catch (e: NotImplementedError) {

    }

    return Single.error { NotImplementedError("Plugin can't obtain chapters list for ${novelDetail.novelTitle}") }
  }
}