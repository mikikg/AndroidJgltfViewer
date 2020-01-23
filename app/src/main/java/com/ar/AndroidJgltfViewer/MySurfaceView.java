package com.ar.AndroidJgltfViewer;

import android.content.Context;
import android.opengl.GLSurfaceView;

import android.view.MotionEvent;
import android.widget.Toast;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import de.javagl.jgltf.model.io.GltfModelReader;

class MySurfaceView extends GLSurfaceView {

    private MyRenderer mRenderer;

    private static final long DOUBLE_CLICK_TIME_DELTA = 300;//milliseconds
    long lastClickTime = 0;
    private int modelCNT = 0;

    MyGlobals glb;

    public MySurfaceView(Context context, MyGlobals myGlobals)
    {
        super(context);

        glb = myGlobals;

        setEGLContextClientVersion(3); // GLES 3.0
        setRenderer(new MyRenderer(myGlobals)); // <<< add Renderer!
    }

    // Hides superclass method.
    public void setRenderer(MyRenderer renderer)
    {
        mRenderer = renderer;
        super.setRenderer(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event != null) {

            float x = event.getX();
            float y = event.getY();

            if (event.getAction() == MotionEvent.ACTION_DOWN) {

                if (y < 150) {
                    float zoom = mRenderer.myTouchActionZoom(x, y);
                    return true;
                } else if (y>150 && y < 300) {
                    mRenderer.myTouchActionMove(x, y);
                    return true;
                } else if (mRenderer != null) {
                    long clickTime = System.currentTimeMillis();
                    if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA){
                        //--- DoubleClick ---
                        queueEvent(() -> {
                            loadNextGltf();
                        });
                    } else {
                        //--- SingleClick ---
                        queueEvent(() -> {

                        });
                    }

                    lastClickTime = clickTime;
                    return true; //must return value, ACTION_MOVE will not work without this!
                }
            }

            if (event.getAction() == MotionEvent.ACTION_MOVE) {
                if (mRenderer != null) {
                    mRenderer.myTouchActionRotate(event.getY(), event.getX());
                    return true;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    public void loadNextGltf() {

        String srvBase = "https://github.com/KhronosGroup/glTF-Sample-Models/raw/master/2.0/"; //remote

        String uriStr[]= {
                "Duck/glTF/Duck.gltf",
                //"van_gogh_-_bedroom_in_arles/scene.gltf",
                "AnimatedMorphCube/glTF/AnimatedMorphCube.gltf",
                "CesiumMan/glTF/CesiumMan.gltf",
                "BrainStem/glTF/BrainStem.gltf",
                "VC/glTF/VC.gltf",
                "Monster/glTF/Monster.gltf",
                "Sponza/glTF/Sponza.gltf",
                "DamagedHelmet/glTF/DamagedHelmet.gltf",
                "TextureCoordinateTest/glTF/TextureCoordinateTest.gltf",
                "SciFiHelmet/glTF/SciFiHelmet.gltf",
                "Avocado/glTF/Avocado.gltf",
                "VertexColorTest/glTF/VertexColorTest.gltf",
                "BoomBoxWithAxes/glTF/BoomBoxWithAxes.gltf",
                "TwoSidedPlane/glTF/TwoSidedPlane.gltf",
                "BoxVertexColors/glTF/BoxVertexColors.gltf",
                "Box/glTF/Box.gltf",
                "BoxTexturedNonPowerOfTwo/glTF/BoxTexturedNonPowerOfTwo.gltf",
                "BoxInterleaved/glTF/BoxInterleaved.gltf",
                "AnimatedTriangle/glTF/AnimatedTriangle.gltf",
                "BoomBox/glTF/BoomBox.gltf",
                "AnimatedCube/glTF/AnimatedCube.gltf",
                "Buggy/glTF/Buggy.gltf",
                "Sponza/glTF/Sponza.gltf",
                "CesiumMan/glTF/CesiumMan.gltf",
                "AlphaBlendModeTest/glTF/AlphaBlendModeTest.gltf",
                "Cube/glTF/Cube.gltf",
                "TriangleWithoutIndices/glTF/TriangleWithoutIndices.gltf",
                "Duck/glTF/Duck.gltf",
                "TextureTransformTest/glTF/TextureTransformTest.gltf",
                "MorphPrimitivesTest/glTF/MorphPrimitivesTest.gltf",
                "2CylinderEngine/glTF/2CylinderEngine.gltf",
                "SimpleMeshes/glTF/SimpleMeshes.gltf",
                "Lantern/glTF/Lantern.gltf",
                "MultiUVTest/glTF/MultiUVTest.gltf",
                "WaterBottle/glTF/WaterBottle.gltf",
                "BrainStem/glTF/BrainStem.gltf",
                "Triangle/glTF/Triangle.gltf",
                "BarramundiFish/glTF/BarramundiFish.gltf",
                "NormalTangentMirrorTest/glTF/NormalTangentMirrorTest.gltf",
                "ReciprocatingSaw/glTF/ReciprocatingSaw.gltf",
                "Cameras/glTF/Cameras.gltf",
                "MetalRoughSpheres/glTF/MetalRoughSpheres.gltf",
                "TextureSettingsTest/glTF/TextureSettingsTest.gltf",
                "BoxAnimated/glTF/BoxAnimated.gltf",
                "SimpleSparseAccessor/glTF/SimpleSparseAccessor.gltf",
                "SimpleMorph/glTF/SimpleMorph.gltf",
                "AntiqueCamera/glTF/AntiqueCamera.gltf",
                "OrientationTest/glTF/OrientationTest.gltf",
                "CesiumMilkTruck/glTF/CesiumMilkTruck.gltf",
                "AnimatedMorphSphere/glTF/AnimatedMorphSphere.gltf",
                "RiggedSimple/glTF/RiggedSimple.gltf",
                "Corset/glTF/Corset.gltf",
                "Suzanne/glTF/Suzanne.gltf",
                "RiggedFigure/glTF/RiggedFigure.gltf",
                "GearboxAssy/glTF/GearboxAssy.gltf",
                "NormalTangentTest/glTF/NormalTangentTest.gltf",
                "SpecGlossVsMetalRough/glTF/SpecGlossVsMetalRough.gltf",
                "Monster/glTF/Monster.gltf",
                "BoxTextured/glTF/BoxTextured.gltf",
                "FlightHelmet/glTF/FlightHelmet.gltf"
        };

        GltfModelReader r = new GltfModelReader();
        boolean loadOK = true;

        //Toast message before loading
        glb.actv.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(glb.actv, "Loading: " + uriStr[modelCNT] + " Please wait ...", Toast.LENGTH_LONG).show();
            }
        });

        try {
            //try to load gltf
            mRenderer.addGM(r.read(new URI(srvBase + uriStr[modelCNT])));
        } catch (IOException e1) {
            //Toast message on IOException
            glb.actv.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(glb.actv, e1.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            loadOK = false;
        } catch (URISyntaxException e2) {
            //Toast message on URISyntaxException
            glb.actv.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(glb.actv, e2.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            loadOK = false;
        }

        if (loadOK == true) {
            //Toast message after loading
            glb.actv.runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(glb.actv, uriStr[modelCNT - 1], Toast.LENGTH_LONG).show();
                }
            });
        }

        if (modelCNT++ > uriStr.length-1) modelCNT=0;
    }

}
