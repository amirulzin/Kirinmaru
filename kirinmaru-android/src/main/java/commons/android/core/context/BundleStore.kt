package commons.android.core.context

import android.os.Bundle

fun intoState(savedInstanceState: Bundle?): Bundle = savedInstanceState ?: Bundle()