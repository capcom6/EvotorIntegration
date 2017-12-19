package ru.softc.evotor.integration.apps;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.util.ArrayList;

import ru.evotor.devices.commons.printer.PrinterDocument;
import ru.evotor.devices.commons.printer.printable.IPrintable;
import ru.evotor.framework.receipt.Position;

/**
 * Created by capcom on 19.12.2017.
 */

public class ServicePrint {
    public static final String PERMISSION = "ru.softc.permission.serviceprint.PRINT";

    private static final String PACKAGE_NAME = "ru.softc.evotor.serviceprint";
    private static final String SERVICE_NAME = "ru.softc.evotor.serviceprint.services.PrintDivideService";

    public static boolean isAppInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(PACKAGE_NAME, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    public static void printDocuments(Context context, ArrayList<IPrintable> header, ArrayList<Position> positions) {
        final ComponentName name = new ComponentName(PACKAGE_NAME, SERVICE_NAME);
        final Intent starter = new Intent();
        starter.putExtra("EXTRA_HEADER", header);
        starter.putExtra("EXTRA_POSITIONS", positions);
        starter.setComponent(name);
        context.startService(starter);
    }
}
