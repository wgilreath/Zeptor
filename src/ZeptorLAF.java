/*
 * Copyright © 2024 by William F. Gilreath (will@zepton.xyz)
 * All rights reserved.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation.  
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details. The license is available at the following 
 * link:  https://www.gnu.org/licenses/gpl-3.0.txt.
 *
 */
package xyz.zepton.zeptor.editor;

import java.util.HashMap;
import java.util.Map;
import javax.swing.UIManager;

public enum ZeptorLAF {

    ACRYL("com.jtattoo.plaf.acryl.AcrylLookAndFeel", 0),
    AERO("com.jtattoo.plaf.aero.AeroLookAndFeel", 1),
    ALUMINUM("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel", 2),
    BERNSTEIN("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel", 3),
    DARCULA("com.formdev.flatlaf.FlatDarculaLaf", 4),
    FAST("com.jtattoo.plaf.fast.FastLookAndFeel", 5),
    GRAPHITE("com.jtattoo.plaf.graphite.GraphiteLookAndFeel", 6),
    HIFI("com.jtattoo.plaf.hifi.HiFiLookAndFeel", 7),
    INFONODE("net.infonode.gui.laf.InfoNodeLookAndFeel", 8),
    INTELLIJ("com.formdev.flatlaf.FlatIntelliJLaf", 9),
    LIGHT("com.formdev.flatlaf.FlatLightLaf", 10),
    LIPSTIK("com.lipstikLF.LipstikLookAndFeel", 11),
    LIQUID("com.birosoft.liquid.LiquidLookAndFeel", 12),
    LUNA("com.jtattoo.plaf.luna.LunaLookAndFeel", 13),
    MCWIN("com.jtattoo.plaf.mcwin.McWinLookAndFeel", 14),
    METAL("javax.swing.plaf.metal.MetalLookAndFeel", 15),
    MINT("com.jtattoo.plaf.mint.MintLookAndFeel", 16),
    MOTIF("com.sun.java.swing.plaf.motif.MotifLookAndFeel", 17),
    NIMBUS("javax.swing.plaf.nimbus.NimbusLookAndFeel", 18),
    NOIRE("com.jtattoo.plaf.noire.NoireLookAndFeel", 19),
    PGS("com.pagosoft.plaf.PgsLookAndFeel", 20),
    PLASTIC("com.jgoodies.looks.plastic.PlasticLookAndFeel", 21),
    PLATFORM(UIManager.getSystemLookAndFeelClassName(), 22),
    SMART("com.jtattoo.plaf.smart.SmartLookAndFeel", 23),
    TEXTURE("com.jtattoo.plaf.texture.TextureLookAndFeel", 25),
    TONIC("com.digitprop.tonic.TonicLookAndFeel", 25);

    private String lafImage = "";
    private Integer lafIndex = -1;

    private static final Map<Integer, ZeptorLAF> mapIZ = new HashMap<>();

    public static final void initMapEnum() {

        for (ZeptorLAF lafValue : ZeptorLAF.values()) {
            mapIZ.put(lafValue.getIndex(), lafValue);
        }//end for

    }//end initMapEnum

    private ZeptorLAF(final String laf, final int idx) {
        this.lafImage = laf;
        this.lafIndex = idx;
    }//end constructor

    public static final ZeptorLAF getZeptorLAF(final Integer idx) {
        ZeptorLAF result = mapIZ.get(idx);
        if (result == null) {
            result = ZeptorLAF.PLATFORM;
        }
        return result;
    }//end getZeptorLAF

    public Integer getIndex() {
        return this.lafIndex;
    }

    public String getImage() {
        return this.lafImage;
    }

}//end enum ZeptorLAF
