package stream.reconfig.kirinmaru.android.db

import android.app.Application
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import stream.reconfig.kirinmaru.android.di.DaggerAndroidTestAppComponent
import stream.reconfig.kirinmaru.android.logd
import stream.reconfig.kirinmaru.android.ui.favorites.FavoriteNovel
import stream.reconfig.kirinmaru.android.ui.favorites.FavoritePref
import stream.reconfig.kirinmaru.android.vo.Chapter
import stream.reconfig.kirinmaru.android.vo.Novel
import stream.reconfig.kirinmaru.plugins.PluginMap
import stream.reconfig.kirinmaru.plugins.getPlugin
import javax.inject.Inject

/**
 * Used when testing a weird bug where a favorited novel from GravityTales
 * cannot find its local chapters but upon remote insert, they appear just fine.
 * Althourgh the test passes, it is currently not resolved and it probably something
 * have to do with either wrong url encoding or just another unknown Room bug.
 */
@Ignore
class DatabaseBugTest {

  private lateinit var db: Database

  @Inject
  lateinit var pluginMap: PluginMap

  @Inject
  lateinit var favoritePref: FavoritePref

  @Before
  fun setUp() {
    val context = InstrumentationRegistry.getTargetContext()

    db = Room.databaseBuilder(context, Database::class.java, "SAMPLE_DB.DB")
        .allowMainThreadQueries()
        .build()
    //db.clearAllTables()
    InstrumentationRegistry.getInstrumentation().waitForIdleSync()
    DaggerAndroidTestAppComponent.builder().application(context.applicationContext as Application).build()
        .injectTest(this)
  }

  @After
  fun tearDown() {
    db.close()
  }

  // Run this first
  @Test
  fun testInsertAndReadBack() {
    favoritePref.persist(mutableSetOf())

    val plugin = pluginMap.getPlugin("GravityTales")
    val novels = plugin.obtainNovels().blockingGet()
    val novel = novels.find { it.url.contains("chaotic") }!!

    val storeResultN = db.novelDao().insert(Novel(novel))
    logd("Stored: $storeResultN")

    val novelFound = db.novelDao().novels(setOf(novel.origin), setOf(novel.url)).first()
    val chapters = plugin.obtainChapters(novelFound).blockingGet()
    assertTrue(chapters.isNotEmpty())

    val storeResultC = db.chapterDao().insert(chapters.map { Chapter(novelFound, it) })
    logd("Stored: $storeResultC.size")

    val chaptersStored = db.chapterDao().chapters(novelFound.origin, novelFound.url)
    assertTrue(chaptersStored.isNotEmpty())

    assertTrue(novel.url.contentEquals(novelFound.url))

    val set = favoritePref.load()
    set.add(FavoriteNovel(novelFound.origin, novelFound.url))
    favoritePref.persist(set)
  }

  // Then this. We need the test to fully exits the app and
  // cleared from memory to verify nothing funny going on.
  @Test
  fun testLoadFavoriteAndLoadChapters() {
    val set = favoritePref.load()
    assertTrue("Found favorites: ${set.size} with values: $set", set.size == 1)

    val novel = set.first()
    val novelFound = db.novelDao().novels(setOf(novel.origin), setOf(novel.url)).first()
    logd("favorite [${novel.url}]: db [${novelFound.url}]")
    assertTrue(
        "Novel url not equal favorite [${novel.url}]: db [${novelFound.url}]",
        novel.url.contentEquals(novelFound.url)
    )

    val chaptersStored = db.chapterDao().chapters(novelFound.origin, novelFound.url)
    logd("last chapter: ${chaptersStored.last()}")
    assertTrue("Chapter stored is empty", chaptersStored.isNotEmpty())
  }
}