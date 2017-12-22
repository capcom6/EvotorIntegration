package ru.softc.evotor.integration.devices;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import ru.evotor.devices.commons.exception.DeviceServiceOperationOnMainThreadException;
import ru.evotor.devices.commons.printer.PrinterDocument;
import ru.evotor.devices.commons.services.AbstractService;

/**
 * Created by capcom on 21.12.2017.
 */

public class ReceiptPrinterService extends AbstractService {
    private static final String ACTION_PRINTER_SERVICE = "ru.softc.devices.action.PRINTER_SERVICE";
    private static final String TARGET_PACKAGE = "ru.softc.evotorserviceprinter";
    private static final String TARGET_CLASS_NAME = "ru.softc.evotorserviceprinter.PrinterManagerService";

    protected volatile Boolean serviceConnected = null;
    protected IReceiptPrinterInterface service;
    protected final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = IReceiptPrinterInterface.Stub.asInterface(binder);
            serviceConnected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
            serviceConnected = false;
            startInitConnection(context, false);
        }
    };

    public ReceiptPrinterService() {
    }

    @Override
    public Boolean getServiceConnected() {
        return serviceConnected;
    }

    @Override
    public void startInitConnection(final Context appContext, final boolean force) {
        new Thread() {
            @Override
            public void run() {
                if (appContext == null) {
                    return;
                }

                context = appContext;
                if (service == null || force) {
                    Intent pr = new Intent(ACTION_PRINTER_SERVICE);
                    pr.setPackage(TARGET_PACKAGE);
                    pr.setClassName(TARGET_PACKAGE, TARGET_CLASS_NAME);
                    serviceConnected = null;
                    boolean serviceBound = context.bindService(pr, serviceConnection, Service.BIND_AUTO_CREATE);
                    if (!serviceBound) {
                        serviceConnected = false;
                    }
                }
            }
        }.start();
    }

    @Override
    public void startDeinitConnection() {
        new Thread() {
            @Override
            public void run() {
                if (context == null) {
                    return;
                }
                context.unbindService(serviceConnection);
                service = null;
            }
        }.start();
    }

    public int getAllowableSymbolsLineLength(int deviceId) throws RemoteException {
//        DeviceServiceOperationOnMainThreadException.throwIfMainThread();

        return service.getAllowableSymbolsLineLength(deviceId);
    }
//
//    public int getAllowablePixelLineLength(int deviceId) throws DeviceServiceException {
//        DeviceServiceOperationOnMainThreadException.throwIfMainThread();
//
//        try {
//            ResultInt result = service.getAllowablePixelLineLength(deviceId);
//            return result.getResult();
//        } catch (RemoteException | RuntimeException exc) {
//            throw new UnknownException(UNKNOWN_EXCEPTION_TEXT);
//        }
//    }
//
    public void printDocument(int deviceId, PrinterDocument printerDocument) throws RemoteException {
        DeviceServiceOperationOnMainThreadException.throwIfMainThread();

        service.printDocument(deviceId, printerDocument);
    }
}
