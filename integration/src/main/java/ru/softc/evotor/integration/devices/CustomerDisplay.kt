package ru.softc.evotor.integration.devices

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import ru.softc.evotor.integration.events.DisplayTextEvent

object CustomerDisplay {
    private const val PACKAGE_NAME = "ru.softc.evotor.customerdisplay"
    @Deprecated("Переходим на работу через BroadcastReceiver")
    private const val SERVICE_NAME = "ru.softc.evotor.customerdisplay.services.CustomerDisplayService"
    private const val RECEIVER_NAME = "ru.softc.evotor.customerdisplay.recievers.EventsReceiver"

    fun driverVersion(context: Context): Int? {
        try {
            return context.packageManager.getPackageInfo(PACKAGE_NAME, 0).versionCode
        } catch (e: Exception) {
            return null
        }
    }

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
        val version = requireNotNull(driverVersion(context)) { "Драйвер не установлен." }
        if (version >= 21) {
            context.sendBroadcast(Intent(DisplayTextEvent.ACTION_DISPLAY_TEXT_EVENT).apply {
                component = ComponentName(PACKAGE_NAME, RECEIVER_NAME)
                putExtras(event.makeExtras())
            })
            return
        }

//        val intent = event.makeIntent()

        context.startService(Intent(DisplayTextEvent.ACTION_DISPLAY_TEXT_EVENT).apply {
            component = ComponentName(PACKAGE_NAME, SERVICE_NAME)
            putExtras(event.makeExtras())
        })
    }
}