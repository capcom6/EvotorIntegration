package ru.softc.evotor.integrationlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import ru.evotor.devices.commons.printer.PrinterDocument;
import ru.evotor.devices.commons.printer.printable.PrintableText;
import ru.softc.evotor.intergation.devices.ReceiptPrinter;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final StringBuilder builder = new StringBuilder();

        builder.append("Драйвер установлен: " + ReceiptPrinter.isDriverInstalled(getApplicationContext()) + "\r\n");
        if (ReceiptPrinter.isDriverInstalled(getApplicationContext())) {
            for (ReceiptPrinter printer :
                    ReceiptPrinter.getPrinters(this)) {
                builder.append(printer.toString() + "\r\n");

                ReceiptPrinter.print(this, printer.getId(), new PrinterDocument(
                        new PrintableText("Первая строка"),
                        new PrintableText("Довольно длинный текст, помещающийся лишь на несколько строк")
                ));
            }
        }

        textView.setText(builder.toString());
    }
}
