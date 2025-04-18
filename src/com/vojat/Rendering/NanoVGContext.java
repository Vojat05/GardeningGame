package com.vojat.Rendering;

import static org.lwjgl.nanovg.NanoVGGL2.*;

public class NanoVGContext {
    public static long vg;

    public static void init() {
        vg = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
        if (vg == 0) throw new RuntimeException("Failed to create NanoVG context");
    }

    public static void destroy() {
        nvgDelete(vg);
    }
}
