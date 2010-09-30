package org.openpit.ui

import org.openpit.ui._
import org.lwjgl.opengl.GL11._

object TerrainLayer extends Layer3d(100, false) {
    def update() {
        Render.updateDisplayList(displayList, Render.renderOpaqueBlock)
    }
}

object GlassLayer extends Layer3d(900, true) {
    def update() {
        Render.updateDisplayList(displayList, Render.renderTranslucentBlock)
    }
}