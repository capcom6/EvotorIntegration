# Evotor Integration

Библиотека предназначена для интеграции сторонних приложений для смарт-терминала Эвотор с приложениями разработки компании СОФТ-Центр.

## Драйвер принтера чеков

Разработчики могут использовать драйвер принтера чеков в своих приложениях. Для этого приложение должно обладать разрешением ru.softc.permission.receiptprinter.PRINT.

```xml

<uses-permission android:name="ru.softc.permission.receiptprinter.PRINT" />

```

Подключите библиотеку интеграции: https://jitpack.io/#capcom6/evotor-integration к своему приложению. Библиотека содержит класс ReceiptPrinter, предназначенный для упрощения работы с драйвером. Класс предоставляет следующие статические методы:

* boolean isDriverInstalled(Context context) - проверяет наличие драйвера на устройстве, если возвращено false, то другие методы не должны использоваться;
* ReceiptPrinter[] getPrinters(Context context) - возвращает массив всех настроенных принтеров;
* ReceiptPrinter getPrinter(Context context, long id) - возвращает описание принтера с идентификатором id;
* void printDocument(Context context, long printerId, PrinterDocument document) - выполняет печать документа на принтере с указанным идентификатором, если указан идентификатор -1, то печать производится на встроенном принтере.

Экземпляры класса ReceiptPrinter также содержат методы:

* long getId() - возвращает идентификатор принтера;
* String getName() - возвращает заданное пользователем имя принтера;
* int getAllowableSymbolsLineLength() - возвращает ширину печати в символах стандартного шрифта, может быть изменена пользователем.

### Печать в асинхронном режиме

При печати в асинхронном режиме приложение не может узнать об ошибках печати, но в случае ошибки сообщение будет выведено пользователю. Также есть ограничение на максимальный размер документа, не рекомендуется использовать при печати графики.

Пример работы с драйвером, выполняет печать на всех доступных принтерах в асинхронном режиме:

```java
if (ReceiptPrinter.isDriverInstalled(getApplicationContext())) {
    for (ReceiptPrinter printer : ReceiptPrinter.getPrinters(getApplicationContext())) {
        ReceiptPrinter.printDocument(getApplicationContext(), item.getId(), new PrinterDocument(
            new PrintableText("Первая строка"),
            new PrintableText("<!s>Мелкая строка"),
            new PrintableText("<!e>Жирная строка"),
            new PrintableText("<!hw>Крупная строка"),
            new PrintableText("<!u>Подчеркнутая строка"),
            new PrintableText("Довольно длинный текст, помещающийся лишь на несколько строк"),
            new PrintableAlignedText(PrintableAlignedText.Align.CENTER, "По центру"),
            new PrintableAlignedText(PrintableAlignedText.Align.RIGHT, "Справа"),
            new PrintableImage(BitmapFactory.decodeResource(getResources(), R.drawable.img_evotor, options)),
            new PrintableBarcode("EVOTOR", PrintableBarcode.BarcodeType.CODE39)
        ));
    }
}

```

### Печать в синхронном режиме

Печать в синхронном режиме осуществвляется по аналогии с использованием встроенного в Эвотор принтера. Обращаем Ваше внимание, что работы в синхронном режиме должна выполняться в потоке, отличном от UI.

```java
// создаем объект-помощник
final ReceiptPrinterService receiptPrinterService = new ReceiptPrinterService();
// инициализируем подключение к сервису
receiptPrinterService.startInitConnection(context, false);
// ожидаем подключения
receiptPrinterService.waitInitService(context);
// выполняем печать
receiptPrinterService.printDocument(printerId, document);

```

### Форматирование текста

Драйвер поддерживает несколько команд форматирования текста, необходимый формат определяется тегом в начале строки вида *<!флаги>*. В качестве флагов могут использоваться:

* s - мелкий шрифт;
* e - жирный шрифт;
* h - двойная высота шрифта;
* w - двойная ширина шрифта;
* u - подчеркнутый шрифт.
