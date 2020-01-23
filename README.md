# AndroidJgltfViewer

Android port of jgltfViewer with native OpenGL ES bindings

This repo holds Android port of Java glTF files viewer based on jglTF library with native OpenGL ES 3.0 rendering bindings.

Original (desktop) implementation for jgltf-viewer is located here:
https://github.com/javagl/JglTF/tree/master/jgltf-viewer

There are viewer implementations based on different Java OpenGL bindings:

- jgltf-viewer-jogl - A glTF viewer based on JOGL
- jgltf-viewer-lwjgl - A glTF viewer based on LWJGL version 2
- jgltf-viewer-gles - A glTF viewer based Android OpenGL ES 3.0 (this repo)

Note: This library is still subject to change.

# Features

This Android port of jglTF library supports most of the features provided in original jgltf-viewer including the following:

- Run time glTF file loading
- Native Android OpenGL ES 3.0 rendering
- Almost full glTF 2.0 version specification support (animation, morphing, textures, etc ...)
- Custom vertex and fragment shaders (GLSL) with physically based rendering (PBR) support
- The viewer utilizes GLSurfaceView.Renderer class for easy application integration including threading
- Small codebase footprint, no external dependency, created APK is only 2.3MB large

# TODO
- Fix texture Alpha Blending (PixelDatas)
- Fix Light position
- Fix Mime Type detection
- More to come ...

# How to run the example 

Open Android Studio and checkout source from this repository.

```
File >> New >> Project from Version Control >> Git >> https://github.com/mikikg/AndroidJgltfViewer
```

Wait for project synchronization and then build/run on your Android device or X86 emulator.

Please note that the Android emulator supports host OS Graphics Acceleration so this APK may work faster and better on an emulator than on real Android devices!

Start application and double-tap a gray screen area to load the next sample model from the remote repository at https://github.com/KhronosGroup/glTF-Sample-Models/raw/master/2.0/
Please be patient and wait to finish loading as some sample glTF models with texture assets are very large, more than 100MB.

At a moment there is no (visible) user interface, just basic touch functions to zoom in/out, pan and rotate.

The invisible zoom control area is located 150 pixels from the top of the screen, vertical panning control is from 150px to 300px and the rest of the screen is for camera rotation control. 
