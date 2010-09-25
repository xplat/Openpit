package org.openpit.ui

import org.lwjgl.Sys
import org.lwjgl.input.{Keyboard, Mouse}
import org.lwjgl.opengl.{Display, DisplayMode}
import org.lwjgl.opengl.GL11._
import org.lwjgl.util.glu._

import org.openpit.ui.render._
import org.openpit.ui.console.FPS
import org.openpit.world.World
import org.openpit.world.blocks._

object Main {
    var finished = false
    def framerate = 60
    def width = 800
    def height = 450

    def main(args: Array[String]) {
	World.plain
	World.put(10,10,11, Stone())
	World.put(12,10,11, Stone())
	World.put(11,10,15, Stone())

	for (y <- 20 until 30; z <- 11 until 15) {
	    World.put(15, y, z, Glass())
	}

	init()
	while (!finished) {
	    input()
	    render()
	}
	cleanup()
	exit(0)
    }

    def init() {
        println("starting!")
        Display.setDisplayMode(new DisplayMode(width, height))
        Display.setTitle("Openpit")
        //Display.setVSyncEnabled(true)
        Display.setFullscreen(false)
        Display.create()
        println("GL_RENDERER: " + glGetString(GL_RENDERER))
	glEnable(GL_DEPTH_TEST)
	glEnable(GL_CULL_FACE)
	glEnable(GL_TEXTURE_2D)
	//glShadeModel(GL_FLAT)
	glShadeModel(GL_SMOOTH)
	//glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL)
        glClearColor(135f/255f, 205f/255f, 222f/255f, 1.0f)

	Keyboard.create()
	Mouse.create()
	Mouse.setGrabbed(true)

	FPS.start()
    }

    def cleanup() {
	Mouse.setGrabbed(false)
    }

    def input() {
	if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
	    finished = true
	    println("esc hit")
	} else if (Display.isCloseRequested()) {
	    finished = true
	    println("exit clicked")
	}

	Camera.update(-Mouse.getDX * 0.3f, Mouse.getDY * 0.3f)

	if (Keyboard.isKeyDown(Keyboard.KEY_A)) Camera.strafe(-0.3)
	if (Keyboard.isKeyDown(Keyboard.KEY_S)) Camera.walk(-0.3)
	if (Keyboard.isKeyDown(Keyboard.KEY_D)) Camera.strafe(0.3)
	if (Keyboard.isKeyDown(Keyboard.KEY_W)) Camera.walk(0.3)

	if (Keyboard.isKeyDown(Keyboard.KEY_Q)) Camera.update(10, 0)
	if (Keyboard.isKeyDown(Keyboard.KEY_E)) Camera.update(-10, 0)
    }

    def render() {
	if (Display.isVisible()) {
	    Display.sync(framerate)
	    glClear(GL_COLOR_BUFFER_BIT |
			 GL_STENCIL_BUFFER_BIT |
			 GL_DEPTH_BUFFER_BIT)

	    glMatrixMode(GL_PROJECTION)
	    glLoadIdentity()
	    GLU.gluPerspective(40, (width toFloat) / height, 0.5f, 50)

	    Camera.look()
	    Render.render()
	    Display.update()
	    FPS ! FPS.Frame
	} else {
	    Thread.sleep(100)
	}
    }
}