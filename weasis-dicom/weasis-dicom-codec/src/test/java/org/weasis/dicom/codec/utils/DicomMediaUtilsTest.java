/*
 * Copyright (c) 2009-2020 Weasis Team and other contributors.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0, or the Apache
 * License, Version 2.0 which is available at https://www.apache.org/licenses/LICENSE-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
 */
package org.weasis.dicom.codec.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.data.Tag;
import org.dcm4che3.data.VR;
import org.junit.Before;
import org.junit.Test;
import org.weasis.dicom.codec.TagD;

public class DicomMediaUtilsTest {
  public static final String[] STRING_ARRAY = {"RECTANGULAR", "CIRCULAR", "POLYGONAL"}; // NON-NLS

  private Attributes attributes = new Attributes();

  @Before
  public void setup() {
    attributes.setString(Tag.ShutterShape, VR.CS, STRING_ARRAY);
  }

  @Test
  public void testGetPeriod() throws Exception {
    assertEquals(
        "050Y",
        DicomMediaUtils.getPeriod(TagD.getDicomDate("19610625"), TagD.getDicomDate("20120624")));
    assertEquals(
        "051Y",
        DicomMediaUtils.getPeriod(TagD.getDicomDate("19610625"), TagD.getDicomDate("20120625")));
    assertEquals(
        "050Y",
        DicomMediaUtils.getPeriod(TagD.getDicomDate("19610714"), TagD.getDicomDate("20120625")));

    assertEquals(
        "005M",
        DicomMediaUtils.getPeriod(TagD.getDicomDate("20120103"), TagD.getDicomDate("20120625")));
    assertEquals(
        "031D",
        DicomMediaUtils.getPeriod(TagD.getDicomDate("20120525"), TagD.getDicomDate("20120625")));
    assertEquals(
        "003D",
        DicomMediaUtils.getPeriod(TagD.getDicomDate("20120622"), TagD.getDicomDate("20120625")));

    assertEquals(
        "011Y",
        DicomMediaUtils.getPeriod(TagD.getDicomDate("20000229"), TagD.getDicomDate("20110301")));
    assertEquals(
        "010Y",
        DicomMediaUtils.getPeriod(TagD.getDicomDate("20000229"), TagD.getDicomDate("20110228")));
    assertEquals(
        "011Y",
        DicomMediaUtils.getPeriod(TagD.getDicomDate("20000229"), TagD.getDicomDate("20120228")));
    assertEquals(
        "012Y",
        DicomMediaUtils.getPeriod(TagD.getDicomDate("20000229"), TagD.getDicomDate("20120229")));
    assertEquals(
        "012Y",
        DicomMediaUtils.getPeriod(TagD.getDicomDate("20000229"), TagD.getDicomDate("20120301")));
    assertEquals(
        "012Y",
        DicomMediaUtils.getPeriod(TagD.getDicomDate("20000228"), TagD.getDicomDate("20120228")));
    assertEquals(
        "012Y",
        DicomMediaUtils.getPeriod(TagD.getDicomDate("20000228"), TagD.getDicomDate("20120229")));

    try {
      assertEquals(
          "050Y",
          DicomMediaUtils.getPeriod(TagD.getDicomDate("19612506"), TagD.getDicomDate("20122406")));
    } catch (Exception e) {
      assertThat(e).isInstanceOf(NullPointerException.class);
    }
  }

  @Test
  public void testGetStringFromDicomElement() throws Exception {

    assertEquals(
        "RECTANGULAR\\CIRCULAR\\POLYGONAL",
        DicomMediaUtils.getStringFromDicomElement(attributes, Tag.ShutterShape));
    assertEquals(
        null, DicomMediaUtils.getStringFromDicomElement(attributes, Tag.ShutterPresentationValue));
  }

  @Test
  public void testGetStringArrayFromDicomElementAttributesInt() throws Exception {
    assertArrayEquals(
        STRING_ARRAY, DicomMediaUtils.getStringArrayFromDicomElement(attributes, Tag.ShutterShape));
    assertArrayEquals(
        null,
        DicomMediaUtils.getStringArrayFromDicomElement(attributes, Tag.ShutterPresentationValue));
  }
}
