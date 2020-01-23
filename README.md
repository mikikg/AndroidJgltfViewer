
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

This Android port of jglTF library supports most of features provided in original jgltf-viewer including following:

- Run time glTF file loading
- Native Android OpenGL ES 3.0 rendering
- Almost full glTF 2.0 version specification support (animation, morphing, textures, etc ...)
- Custom vertex and fragment shaders (GLSL) with physically based rendering (PBR) support
- Viewer utilize GLSurfaceView.Renderer class for easy application integration including threading

# TODO
- Fix texure Alpha Blending (PixelDatas)
- Fix Mime Type detection
- More to come ...

