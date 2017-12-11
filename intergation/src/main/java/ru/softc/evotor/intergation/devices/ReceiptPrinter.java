package ru.softc.evotor.intergation.devices;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.ArrayList;

import ru.evotor.devices.commons.printer.PrinterDocument;
import ru.evotor.devices.commons.printer.printable.PrintableText;

/**
 * Created by capcom on 11.12.2017.
 */

public final class ReceiptPrinter {

    private static final String ACTION_PRINT_DOCUMENT = "ru.softc.devices.action.PRINT_DOCUMENT";
    /**
     * Идентификатор принтера
     */
    private final long id;
    /**
     * Наименование принтера
     */
    private final String name;
    /**
     * Ширина печати в символах стандартного шрифта
     */
    private final int printWidth;

    private static final String PACKAGE_NAME = "ru.softc.evotorserviceprinter";
    private static final String SERVICE_NAME = "ru.softc.evotorserviceprinter.PrinterDriverService";

    public ReceiptPrinter(long id, String name, int printWidth) {
        this.id = id;
        this.name = name;
        this.printWidth = printWidth;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrintWidth() {
        return printWidth;
    }

    public static boolean isDriverInstalled(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(PACKAGE_NAME, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
        return true;
    }

    public static ReceiptPrinter[] getPrinters(Context context) {
        final ArrayList<ReceiptPrinter> printers = new ArrayList<>();
        final Cursor cursor = context.getContentResolver().query(Uri.parse("content://ru.softc.receiptprinter.Printers"), null, null, null, null);

        final int idIndex = cursor.getColumnIndex(BaseColumns._ID);
        final int nameIndex = cursor.getColumnIndex("name");
        final int widthIndex = cursor.getColumnIndex("symbol_width");

        while (cursor.moveToNext()) {
            printers.add(new ReceiptPrinter(cursor.getLong(idIndex), cursor.getString(nameIndex), cursor.getInt(widthIndex)));
        }

        return printers.toArray(new ReceiptPrinter[0]);
    }

    public static void print(Context context, long printerId, PrinterDocument document) {
        final ComponentName name = new ComponentName(PACKAGE_NAME, SERVICE_NAME);
        final Intent starter = new Intent(ACTION_PRINT_DOCUMENT);
        starter.putExtra("EXTRA_PRINTER_ID", printerId);
        starter.putExtra("EXTRA_DOCUMENT", document);
        starter.setComponent(name);
        context.startService(starter);
    }


    @Override
    public String toString() {
        return "Id: " + this.id + "; Name: " + this.name + "; Width: " + this.printWidth;
    }
}
