package de.javagl.jgltf.viewer.gles;

import android.opengl.GLSurfaceView;

//import android.support.v4.view.KeyEventDispatcher;
import androidx.core.view.KeyEventDispatcher;

import android.view.SurfaceView;

import java.util.logging.Logger;

import de.javagl.jgltf.viewer.AbstractGltfViewer;
import de.javagl.jgltf.viewer.GlContext;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glColorMask;
import static android.opengl.GLES20.glDepthMask;

/**
 * Implementation of a glTF viewer based on OpenGL ES
 */
public class GlViewerGles extends AbstractGltfViewer<KeyEventDispatcher.Component>
//public class GlViewerGles extends GLSurfaceView
{

    private int width, height;

    /**
     * The logger used in this class
     */
    private static final Logger logger =
            Logger.getLogger(GlViewerGles.class.getName());


    /**
     * The GLSurfaceView, i.e. the rendering component of this renderer
     */
    private GLSurfaceView glComponent;

    /**
     * The {@link GlContext}
     */
    private final GlContexGles glContext ; //?null

    /**
     * Whether the component was resized, and glViewport has to be called
     */
    private boolean viewportNeedsUpdate = true;


    public void setGlComponent( GLSurfaceView glC)
    {
        this.glComponent = glC;
    }

    /*
     ***********************************************
     * Creates a new GltfViewerGles
     ***********************************************
     */
    public GlViewerGles(int width, int height)
    {
        this.glContext = new GlContexGles();
       // this.width = width;
       // this.height = height;
    }

    @Override
    public SurfaceView getRenderComponent()
    {
        return this.glComponent;
    }

    /**
     * Returns the width of the render component
     *
     * @return The width of the render component
     */
    @Override
    public int getWidth()
    {
        return this.width;
        //return glComponent.getWidth();
    }

    /**
     * Returns the height of the render component
     *
     * @return The height of the render component
     */
    @Override
    public int getHeight()
    {
        return this.height;
        //return glComponent.getHeight();
    }

    /**
     * Trigger a rendering pass
     */
    @Override
    public void triggerRendering()
    {
        //if (getRenderComponent() != null)
        //{
            //getRenderComponent().repaint();
            //TODO
            render();
        //}
    }

    /**
     * Returns the {@link GlContext} of this viewer
     *
     * @return The {@link GlContext} of this viewer
     */
    @Override
    public GlContext getGlContext()
    {
        return glContext;
    }

    /**
     * Will be called at the beginning of each rendering pass. May be
     * used to do basic setup, e.g. to make the required GL context
     * current.
     */
    @Override
    protected void prepareRender() {
        // Nothing to do here
    }

    /**
     * The actual rendering method. Subclasses implementing this method
     * will usually call {@link #renderGltfModels()}.
     */
    @Override
    protected void render()
    {
        // Enable the color and depth mask explicitly before calling glClear.
        // When they are not enabled, they will not be cleared!

        //glColorMask(true, true, true, true);
        //glDepthMask(true);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //
        renderGltfModels();
    }
}