package com.fmk.monitoring.util

import android.util.Log
import com.fmk.monitoring.BuildConfig.APP_DEBUG

/**
 * 디버그
 */
object DebugUtil {
    private const val LOGTAG = "FMK"

    const val LOG_OUTPUT_LOGCAT = true

    val isLoggable: Boolean
        get() = APP_DEBUG

    private fun wrappedLog(level: Int, tag: String, text: String) {
        when (level) {
            Log.INFO -> {
                if (LOG_OUTPUT_LOGCAT) Log.i(tag, text)
            }
            Log.DEBUG -> {
                if (LOG_OUTPUT_LOGCAT) Log.d(tag, text)
            }
            Log.ERROR -> {
                if (LOG_OUTPUT_LOGCAT) Log.e(tag, text)
            }
            Log.WARN -> {
                if (LOG_OUTPUT_LOGCAT) Log.w(tag, text)
            }
        }
    }

    fun debug(tag: String?, text: String?) {
        var tag = tag
        var text = text
        if (tag == null) tag = ""
        if (text == null) text = ""
        if (isLoggable) wrappedLog(Log.DEBUG, tag, text)
    }

    fun error(tag: String?, text: String?) {
        var tag = tag
        var text = text
        if (tag == null) tag = ""
        if (text == null) text = ""
        if (isLoggable) wrappedLog(Log.ERROR, tag, text)
    }

    fun info(_s1: String?, _s2: String?) {
        var _s1 = _s1
        var _s2 = _s2
        if (isLoggable) {
            if (_s1 == null) _s1 = ""
            if (_s2 == null) _s2 = ""
            if (_s2.length > 2000) {
                val logChunkCount = _s2.length / 2000
                for (i in 0..logChunkCount) {
                    val max = 2000 * (i + 1)
                    if (max >= _s2.length) wrappedLog(
                        Log.INFO,
                        _s1,
                        _s2.substring(2000 * i)
                    ) else wrappedLog(Log.INFO, _s1, _s2.substring(2000 * i, max))
                }
            } else {
                wrappedLog(Log.INFO, _s1, _s2)
            }
        }
    }

    fun warning(tag: String?, text: String?) {
        var tag = tag
        var text = text
        if (tag == null) tag = ""
        if (text == null) text = ""
        if (isLoggable) wrappedLog(Log.WARN, tag, text)
    }
    //-------------------------------------------------
    // CS용 로그
    /**
     * getLoggingText : 로그 출력용 문자열 가져오는 함수
     *
     * @param text   : 로그 출력용으로 조합될 문자열 인수
     * @return      : tag가 포함된 로그 출력용 문자열
     */
    private fun getLoggingText(text: String?): String {
        var text = text
        if (text == null) text = ""
        return "[Logging] $text" //프로가드로 인한 메소드 못찾는 문제로 수정.
    }
    //--------------------------------------------------
    /**
     * printDebug : 고객 노출형 Debug 함수
     *
     * @param text : 디버그 출력용 문자열 인수
     * tag는 기본 태그인 LOGTAG 를 사용
     */
    fun printDebug(text: String?) {
        printDebug(LOGTAG, text)
    }

    /**
     * printDebug : 고객 노출형 Debug 함수
     * @param tagParam : 사용자 지정 tag
     * @param text : 로그 출력용 문자열
     *
     * 사용자가 지정한 tag로 debug 로그 출력
     */
    fun printDebug(tagParam: String?, text: String?) {
        var tagParam = tagParam
        var text = text
        if (isLoggable) {
            if (tagParam == null) tagParam = ""
            if (text == null) text = ""
            //				String tag = getCallStackTrace(new Throwable());
            wrappedLog(Log.DEBUG, tagParam, "[$tagParam] $text")
        }
    }
    //--------------------------------------------------
    //--------------------------------------------------
    /**
     * printInfo : 고객 노출형 info 함수
     * @param text : 로그 출력용 문자열
     * tag는 기본 tag인 LOGTAG 사용
     */
    fun printInfo(text: String?) {
        printInfo(LOGTAG, text)
    }

    /**
     * printInfo : 고객 노출형 info 함수
     *
     * @param tagParam : 사용자 지정 tag
     * @param text : 로그 출력용 문자열
     *
     * 사용자가 지정한 tag로 info 로그를 출력
     */
    fun printInfo(tagParam: String?, text: String?) {
        var tagParam = tagParam
        var text = text
        if (isLoggable) {
            if (tagParam == null) tagParam = ""
            if (text == null) text = ""
            //				String tag = getCallStackTrace(new Throwable());
            wrappedLog(Log.INFO, tagParam, "[$tagParam] $text")
        }
    }
    //--------------------------------------------------
    //--------------------------------------------------
    /**
     * printInfo : 고객 노출형 info 함수
     * @param text : 로그 출력용 문자열
     * tag는 기본 tag인 LOGTAG 사용
     */
    fun printWarning(text: String?) {
        printWarning(LOGTAG, text)
    }

    /**
     * printWarning : 고객 노출형 warning 함수
     *
     * @param tagParam : 사용자 지정 tag
     * @param text : 로그 출력용 문자열
     *
     * 사용자가 지정한 tag로 warning 로그를 출력
     */
    fun printWarning(tagParam: String?, text: String?) {
        var tagParam = tagParam
        var text = text

        if (isLoggable) {
            if (tagParam == null) tagParam = ""
            if (text == null) text = ""
            //				String tag = getCallStackTrace(new Throwable());
            wrappedLog(Log.WARN, tagParam, "[$tagParam] $text")
        }
    }
    //--------------------------------------------------
    //--------------------------------------------------
    /**
     * printError : 고객 노출형 error 함수
     *
     * @param text : 로그 출력용 문자열
     * tag는 기본 tag인 LOGTAG 사용
     */
    fun printError(text: String?) {
        printError(LOGTAG, text)
    }

    /**
     * printError : 고객 노출형 error 함수
     *
     * @param text : 로그 출력용 문자열
     * @param throwable : 예외 throwable 인자
     * tag는 기본 tag인 LOGTAG 사용
     */
    fun printError(text: String?, throwable: Throwable?) {
        printError(LOGTAG, text, throwable)
    }

    /**
     * printError : 고객 노출형 error 함수
     *
     * @param tagParam : 사용자 지정 tag
     * @param text : 로그 출력용 문자열
     *
     * 사용자가 지정한 tag로 error 로그를 출력
     */
    fun printError(tagParam: String?, text: String?) {
        var tagParam = tagParam
        var text = text

        if (isLoggable) {
            if (tagParam == null) tagParam = ""
            if (text == null) text = ""
            //				String tag = getCallStackTrace(new Throwable());
            wrappedLog(Log.ERROR, tagParam, "[$tagParam] $text")
        }

    }

    /**
     * printError : 고객 노출형 error 함수
     *
     * @param tagParam : 사용자 지정 tag
     * @param text : 로그 출력용 문자열
     * @param throwable :예외 throwable 인자
     *
     * 사용자가 지정한 tag로 error 로그를 출력
     */
    fun printError(tagParam: String?, text: String?, throwable: Throwable?) {
        var tagParam = tagParam
        var text = text
        if (isLoggable) {
            if (tagParam == null) tagParam = ""
            if (text == null) text = ""
            //				String tag = getCallStackTrace(new Throwable());
            wrappedLog(
                Log.ERROR, tagParam, """
     [$tagParam] $text
     ${Log.getStackTraceString(throwable)}
     """.trimIndent()
            )
        }
    }
    //--------------------------------------------------
    /** 호출한 클래스-메소드 반환  */
    private fun getCallStackTrace(throwable: Throwable?): String {
        return try {
            if (throwable == null) {
                return ""
            }
            val elements = throwable.stackTrace
            if (elements != null && elements.size > 1) {
//				String stackClassName = elements[1].getClassName();
                val stackMethodName = elements[1].methodName
                var stackFileName = elements[1].fileName
                val stackLineNumber = elements[1].lineNumber
                if (stackFileName.contains(".")) stackFileName =
                    stackFileName.substring(0, stackFileName.lastIndexOf("."))
                "$stackFileName:$stackMethodName:$stackLineNumber"
            } else {
                ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}