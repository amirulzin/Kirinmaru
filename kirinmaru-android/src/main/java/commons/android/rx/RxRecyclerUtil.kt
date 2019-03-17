package commons.android.rx

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object RxRecyclerUtil {

  @JvmStatic
  fun calcAndDispatchDiff(adapter: RecyclerView.Adapter<*>, callback: () -> DiffUtil.Callback) =
    Single.fromCallable { DiffUtil.calculateDiff(callback()) }
      .subscribeOn(Schedulers.computation())
      .observeOn(AndroidSchedulers.mainThread())
      .map { diff -> diff.dispatchUpdatesTo(adapter) }
}
