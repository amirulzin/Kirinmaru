package commons.android.rx

import commons.android.core.mediahelper.PendingFile
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

inline fun PendingFile?.clearPendingFile(errorHandler: Consumer<Throwable>, crossinline onComplete: () -> Unit) =
  Completable.fromCallable { this?.file?.delete() }
    .observeOn(AndroidSchedulers.mainThread())
    .doFinally { onComplete() }
    .subscribeOn(Schedulers.io())
    .subscribeBy(errorHandler::accept)

