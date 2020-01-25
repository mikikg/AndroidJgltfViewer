package com.ar.AndroidJgltfViewer;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.Log;

import androidx.core.view.KeyEventDispatcher;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import de.javagl.jgltf.model.GltfModel;
import de.javagl.jgltf.viewer.ExternalCamera;
import de.javagl.jgltf.viewer.GltfViewer;
import de.javagl.jgltf.viewer.gles.GlViewerGles;

import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glViewport;

class MyRenderer implements GLSurfaceView.Renderer {

    private GltfViewer<KeyEventDispatcher.Component> gltfViewerOBJ; //main viewer object
    private ExternalCamera extCam; //our external camera model with touch integration
    private int width; // width of the screen
    private int height; // height of the screen
    private boolean modelReady = false;

    float zoom = 0.1f;

    public MyRenderer(MyGlobals myGlobals) {

        //Create new external (virtual/software) Camera
        extCam = new MyExtCamera();
        ((MyExtCamera) extCam).init();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        Log.d("GLSurfaceViewTest", "surface created");
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Log.d("GLSurfaceViewTest", "surface changed: " + width + "x" + height);
        this.width = width;
        this.height = height;

        // Adjust the viewport based on geometry changes, such as screen rotation
        glViewport(0, 0, width, height);
        ((MyExtCamera) extCam).set_wh((float)width, (float)height); //
        ((MyExtCamera) extCam).setLoc(15, 1/(float)zoom); //set default zoom
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        if (modelReady) {
            //render 3D model now
            gltfViewerOBJ.triggerRendering(); //! :)
        } else {
            glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
            glClear(GLES20.GL_COLOR_BUFFER_BIT);
        }
    }

    public void myTouchActionRotate(float x, float y) {
        ((MyExtCamera) extCam).rotateMatrix(x,y); //
    }

    public void myTouchActionMove(float x, float y) {
        ((MyExtCamera) extCam).moveView(x, y);
    }

    public float myTouchActionZoom(float x, float y) {
        if (x > width/2f) {
            //zoom in
            zoom +=(x - width/2f)/1150;
            ((MyExtCamera) extCam).setLoc(15, 1/zoom);
        } else {
            //zoom out
            zoom -= (width/2f - x)/1150;
            if (zoom <= 0) {zoom = 0.005f;}
            ((MyExtCamera) extCam).setLoc(15, 1/zoom);
        }

        return zoom;
    }

    void addGM (GltfModel gm) {
        gltfViewerOBJ = new GlViewerGles( this.width, this.height );
        gltfViewerOBJ.setExternalCamera(this.extCam);
        gltfViewerOBJ.addGltfModel(gm);
        //gltfViewerOBJ.setAnimationsRunning(false);
        modelReady = true;
    }
}
