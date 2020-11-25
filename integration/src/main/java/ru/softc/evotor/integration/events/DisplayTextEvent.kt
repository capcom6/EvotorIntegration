package ru.softc.evotor.integration.events

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import ru.softc.evotor.integration.devices.PrintableAlignedText

class DisplayTextEvent private constructor(
    val line1: PrintableAlignedText?,
    val line2: PrintableAlignedText?,
    val isClear: Boolean,
    val isWordwrap: Boolean
) {
    fun makeExtras(): Bundle {
        return Bundle().apply {
            line1?.let { putParcelable(EXTRA_LINE_1, it) }
            line2?.let { putParcelable(EXTRA_LINE_2, it) }
            putBoolean(EXTRA_CLEAR, isClear)
            putBoolean(EXTRA_WORDWRAP, isWordwrap)
        }
//        if (line1 != null) {
//            intent.putExtra(EXTRA_LINE_1, line1)
//        }
//        if (line2 != null) {
//            intent.putExtra(EXTRA_LINE_2, line2)
//        }
//        intent.putExtra(EXTRA_CLEAR, isClear)
//        intent.putExtra(EXTRA_WORDWRAP, isWordwrap)
//        return intent
    }

    class Builder {
        private var line1: PrintableAlignedText? = null
        private var line2: PrintableAlignedText? = null
        private var clear = false
        private var wordwrap = false
        fun setClear(clear: Boolean): Builder {
            this.clear = clear
            return this
        }

        fun setLine1(line1: PrintableAlignedText?): Builder {
            this.line1 = line1
            return this
        }

        fun setLine2(line2: PrintableAlignedText?): Builder {
            this.line2 = line2
            return this
        }

        fun setWordwrap(wordwrap: Boolean): Builder {
            this.wordwrap = wordwrap
            return this
        }

        fun build(): DisplayTextEvent {
            return DisplayTextEvent(line1, line2, clear, wordwrap)
        }
    }

    companion object {
        const val ACTION_DISPLAY_TEXT_EVENT = "ru.softc.devices.action.DISPLAY_TEXT"

        private const val EXTRA_LINE_1 = "EXTRA_LINE1"
        private const val EXTRA_LINE_2 = "EXTRA_LINE2"
        private const val EXTRA_CLEAR = "EXTRA_CLEAR"
        private const val EXTRA_WORDWRAP = "EXTRA_WORDWRAP"

        @JvmStatic
        fun fromIntent(intent: Intent): DisplayTextEvent? {
            return if (ACTION_DISPLAY_TEXT_EVENT != intent.action) {
                null
            } else Builder()
                .setLine1(intent.getParcelableExtra<Parcelable>(EXTRA_LINE_1) as PrintableAlignedText)
                .setLine2(intent.getParcelableExtra<Parcelable>(EXTRA_LINE_2) as PrintableAlignedText)
                .setClear(intent.getBooleanExtra(EXTRA_CLEAR, false))
                .setWordwrap(intent.getBooleanExtra(EXTRA_WORDWRAP, false))
                .build()
        }
    }
}