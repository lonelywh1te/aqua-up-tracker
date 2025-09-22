package utils

import timber.log.Timber

class TestTimberTree: Timber.Tree() {
    val logs = mutableListOf<Throwable?>()

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        logs.add(t)
    }

}