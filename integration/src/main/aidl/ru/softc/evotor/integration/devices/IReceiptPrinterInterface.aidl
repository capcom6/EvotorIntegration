// IReceiptPrinterInterface.aidl
package ru.softc.evotor.integration.devices;

// Declare any non-default types here with import statements
import ru.evotor.devices.commons.printer.PrinterDocument;

interface IReceiptPrinterInterface {
    void printDocument(int deviceId, in PrinterDocument printerDocument);
    int getAllowableSymbolsLineLength(int deviceId);
}
