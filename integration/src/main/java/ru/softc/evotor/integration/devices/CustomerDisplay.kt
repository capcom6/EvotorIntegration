package ru.softc.evotor.integration.devices

import android.content.Context
import ru.softc.evotor.integration.events.DisplayTextEvent

object CustomerDisplay {
    fun displayText(context: Context, line1: String, line2: String) {
        displayText(context,
                PrintableAlignedText(PrintableAlignedText.Align.LEFT, line1),
                PrintableAlignedText(PrintableAlignedText.Align.LEFT, line2))
    }

    fun displayText(context: Context, line1: PrintableAlignedText?, line2: PrintableAlignedText?) {
        val event = DisplayTextEvent.Builder()
                .setClear(true)
                .setLine1(line1)
                .setLine2(line2)
                .build()

        display(context, event)
    }

    fun display(context: Context, event: DisplayTextEvent) {
        val intent = event.makeIntent()

        context.startService(intent)
    }
}