/* GENERATED SOURCE. DO NOT MODIFY. */
/*
 ********************************************************************************
 * Copyright (C) 2009-2010, Google, International Business Machines Corporation *
 * and others. All Rights Reserved.                                             *
 ********************************************************************************
 */
package android.icu.text;

/**
 * Provide an interface for Transforms that focuses just on the transformation of the text.
 * APIs that take Transliterator or StringTransform, but only depend on the transformation should use this interface in the API instead.
 *
 * @author markdavis
 * @stable ICU 4.4

 * @hide Only a subset of ICU is exposed in Android
 * @hide All android.icu classes are currently hidden
 */

public interface Transform<S,D> {
    /**
     * Transform the input in some way, to be determined by the subclass.
     * @param source to be transformed (eg lowercased)
     * @return result
     * @stable ICU 4.4
     */
    public D transform(S source);
}
