package ru.softc.evotor.integration.apps

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import ru.evotor.devices.commons.printer.printable.IPrintable
import ru.evotor.framework.receipt.Position
import java.util.*

/**
 * Created by capcom on 19.12.2017.
 */
object ServicePrint {
    const val PERMISSION = "ru.softc.permission.serviceprint.PRINT"
    private const val PACKAGE_NAME = "ru.softc.evotor.serviceprint"
    @Deprecated("Переходим на работу через BroadcastReceiver")
    private const val SERVICE_NAME = "ru.softc.evotor.serviceprint.services.PrintDivideService"
    private const val RECIEVER_NAME = "ru.softc.evotor.serviceprint.recievers.EventsReceiver"
    private const val ACTIVITY_NAME = "ru.softc.evotor.serviceprint.MainActivity"

    @JvmStatic
    fun createIntentForSettings(): Intent {
        val name = ComponentName(PACKAGE_NAME, ACTIVITY_NAME)
        val starter = Intent()
        starter.component = name
        starter.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        return starter
    }

    @JvmStatic
    fun isAppInstalled(context: Context): Boolean {
        val packageManager = context.packageManager
        try {
            packageManager.getPackageInfo(PACKAGE_NAME, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }

    @JvmStatic
    fun appVersion(context: Context): Int? {
        try {
            return context.packageManager.getPackageInfo(PACKAGE_NAME, 0).versionCode
        } catch (e: Exception) {
            return null
        }
    }

    @JvmStatic
    fun printDocuments(
        context: Context,
        header: ArrayList<IPrintable>,
        positions: ArrayList<Position>
    ) {
        val appVersion = appVersion(context) ?: return
        if (appVersion >= 10) {
            context.sendBroadcast(Intent("ru.softc.evotor.serviceprint.ACTION_PRINT_DIVIDE").apply {
                component = ComponentName(PACKAGE_NAME, RECIEVER_NAME)
                putExtra("EXTRA_HEADER", header)
                putExtra("EXTRA_POSITIONS", positions)
            })
            return
        }

        val name = ComponentName(PACKAGE_NAME, SERVICE_NAME)
        val starter = Intent()
        starter.putExtra("EXTRA_HEADER", header)
        starter.putExtra("EXTRA_POSITIONS", positions)
        starter.component = name
        context.startService(starter)
    }
}