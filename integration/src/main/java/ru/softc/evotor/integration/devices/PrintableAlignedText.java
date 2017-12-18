package ru.softc.evotor.integration.devices;

import android.os.Parcel;

import ru.evotor.devices.commons.printer.printable.IPrintable;
import ru.evotor.devices.commons.printer.printable.PrintableText;

/**
 * Created by capcom on 15.12.2017.
 */

public class PrintableAlignedText extends PrintableText {
    public enum Align {
        LEFT,
        CENTER,
        RIGHT
    }

    private final Align align;

    protected PrintableAlignedText(Parcel in) {
        super(in.readString());
        align = Align.values()[in.readInt()];
    }

    public PrintableAlignedText(Align align, String text) {
        super(text);

        this.align = align;
    }

    public Align getAlign() {
        return align;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(align.ordinal());
    }

    public static final Creator<PrintableAlignedText> CREATOR = new Creator<PrintableAlignedText>() {

        public PrintableAlignedText createFromParcel(Parcel in) {
            return new PrintableAlignedText(in);
        }

        public PrintableAlignedText[] newArray(int size) {
            return new PrintableAlignedText[size];
        }
    };
}
