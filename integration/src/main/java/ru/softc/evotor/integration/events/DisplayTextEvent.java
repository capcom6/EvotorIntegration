package ru.softc.evotor.integration.events;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;

import ru.softc.evotor.integration.devices.PrintableAlignedText;

public class DisplayTextEvent {
    private static final String PACKAGE_NAME = "ru.softc.evotor.customerdisplay";
    private static final String SERVICE_NAME = "ru.softc.evotor.customerdisplay.services.CustomerDisplayService";
    private static final String ACTION_DISPLAY_TEXT_EVENT = "ru.softc.devices.action.DISPLAY_TEXT";

    private static final String EXTRA_LINE_1 = "EXTRA_LINE1";
    private static final String EXTRA_LINE_2 = "EXTRA_LINE2";
    private static final String EXTRA_CLEAR = "EXTRA_CLEAR";
    private static final String EXTRA_WORDWRAP = "EXTRA_WORDWRAP";

    private final PrintableAlignedText line1;
    private final PrintableAlignedText line2;
    private final boolean clear;
    private final boolean wordwrap;

    public static DisplayTextEvent fromIntent(Intent intent) {
        if (!ACTION_DISPLAY_TEXT_EVENT.equals(intent.getAction())) {
            return null;
        }

        return new DisplayTextEvent.Builder()
                .setLine1((PrintableAlignedText)intent.getParcelableExtra(EXTRA_LINE_1))
                .setLine2((PrintableAlignedText)intent.getParcelableExtra(EXTRA_LINE_2))
                .setClear(intent.getBooleanExtra(EXTRA_CLEAR, false))
                .setWordwrap(intent.getBooleanExtra(EXTRA_WORDWRAP, false))
                .build();
    }

    private DisplayTextEvent(PrintableAlignedText line1, PrintableAlignedText line2, boolean clear, boolean wordwrap) {
        this.line1 = line1;
        this.line2 = line2;
        this.clear = clear;
        this.wordwrap = wordwrap;
    }

    public Intent makeIntent() {
        final Intent intent = new Intent(ACTION_DISPLAY_TEXT_EVENT);
        intent.setComponent(new ComponentName(PACKAGE_NAME, SERVICE_NAME));

        if (line1 != null) {
            intent.putExtra(EXTRA_LINE_1, line1);
        }
        if (line2 != null) {
            intent.putExtra(EXTRA_LINE_2, line2);
        }
        intent.putExtra(EXTRA_CLEAR, clear);
        intent.putExtra(EXTRA_WORDWRAP, wordwrap);

        return intent;
    }

    public PrintableAlignedText getLine1() {
        return line1;
    }

    public PrintableAlignedText getLine2() {
        return line2;
    }

    public boolean isClear() {
        return clear;
    }

    public boolean isWordwrap() {
        return wordwrap;
    }

    public static class Builder {
        private PrintableAlignedText line1;
        private PrintableAlignedText line2;
        private boolean clear;
        private boolean wordwrap;

        public Builder setClear(boolean clear) {
            this.clear = clear;
            return this;
        }

        public Builder setLine1(PrintableAlignedText line1) {
            this.line1 = line1;
            return this;
        }

        public Builder setLine2(PrintableAlignedText line2) {
            this.line2 = line2;
            return this;
        }

        public Builder setWordwrap(boolean wordwrap) {
            this.wordwrap = wordwrap;
            return this;
        }

        public DisplayTextEvent build() {
            return new DisplayTextEvent(line1, line2, clear, wordwrap);
        }
    }
}