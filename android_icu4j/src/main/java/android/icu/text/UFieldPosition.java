/* GENERATED SOURCE. DO NOT MODIFY. */
/*
 *******************************************************************************
 * Copyright (C) 2013-2014, International Business Machines Corporation and    *
 * others. All Rights Reserved.                                                *
 *******************************************************************************
 */
package android.icu.text;

import java.text.FieldPosition;
import java.text.Format.Field;

/**
 * Adds the ability to get the decimal digits
 * {@literal @}internal
 * @deprecated This API is ICU internal only.
 * @hide All android.icu classes are currently hidden
 */
@Deprecated
public class UFieldPosition extends FieldPosition {
    private int countVisibleFractionDigits = -1;
    private long fractionDigits = 0;

    /**
     * {@literal @}internal
     * @deprecated This API is ICU internal only.
     */
    @Deprecated
    public UFieldPosition() {
        super(-1);
    }

    /**
     * {@literal @}internal
     * @deprecated This API is ICU internal only.
     */
    @Deprecated
    public UFieldPosition(int field) {
        super(field);
    }

    /**
     * {@literal @}internal
     * @deprecated This API is ICU internal only.
     */
    @Deprecated
    public UFieldPosition(Field attribute, int fieldID) {
        super(attribute, fieldID);
    }

    /**
     * {@literal @}internal
     * @deprecated This API is ICU internal only.
     */
    @Deprecated
    public UFieldPosition(Field attribute) {
        super(attribute);
    }

    /**
     * {@literal @}internal
     * @deprecated This API is ICU internal only.
     */
    @Deprecated
    public void setFractionDigits(int countVisibleFractionDigits, long fractionDigits ) {
        this.countVisibleFractionDigits = countVisibleFractionDigits;
        this.fractionDigits = fractionDigits;
    }

    /**
     * {@literal @}internal
     * @deprecated This API is ICU internal only.
     */
    @Deprecated
    public int getCountVisibleFractionDigits() {
        return countVisibleFractionDigits;
    }

    /**
     * {@literal @}internal
     * @deprecated This API is ICU internal only.
     */
    @Deprecated
    public long getFractionDigits() {
        return fractionDigits;
    }
}
