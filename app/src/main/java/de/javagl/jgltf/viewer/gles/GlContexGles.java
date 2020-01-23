package de.javagl.jgltf.viewer.gles;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.logging.Logger;

import de.javagl.jgltf.model.GltfConstants;
import de.javagl.jgltf.viewer.GlContext;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_INFO_LOG_LENGTH;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_TRUE;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBlendColor;
import static android.opengl.GLES20.glBlendEquationSeparate;
import static android.opengl.GLES20.glBlendFuncSeparate;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glBufferSubData;
import static android.opengl.GLES20.glColorMask;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glCullFace;
import static android.opengl.GLES20.glDeleteBuffers;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glDeleteTextures;
import static android.opengl.GLES20.glDepthFunc;
import static android.opengl.GLES20.glDepthMask;
import static android.opengl.GLES20.glDepthRangef;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glDrawElements;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glFrontFace;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glPolygonOffset;
import static android.opengl.GLES20.glScissor;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glTexImage2D;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glUniform1fv;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform1iv;
import static android.opengl.GLES20.glUniform2fv;
import static android.opengl.GLES20.glUniform2iv;
import static android.opengl.GLES20.glUniform3fv;
import static android.opengl.GLES20.glUniform3iv;
import static android.opengl.GLES20.glUniform4fv;
import static android.opengl.GLES20.glUniform4iv;
import static android.opengl.GLES20.glUniformMatrix2fv;
import static android.opengl.GLES20.glUniformMatrix3fv;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glValidateProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES30.GL_TEXTURE_BASE_LEVEL;
import static android.opengl.GLES30.GL_TEXTURE_MAX_LEVEL;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glDeleteVertexArrays;
import static android.opengl.GLES30.glGenVertexArrays;

/**
 * Implementation of a {@link GlContext} based on OpenGL ES
 */
class GlContexGles implements GlContext {

    /**
     * The logger used in this class
     */
    private static final Logger logger =
            Logger.getLogger(GlContexGles.class.getName());


    /**
     * Create an OpenGL ES program with a vertex- and fragment shader created
     * from the given sources. If it is not possible to create the GL ES
     * program (due to compile- or link errors, or because one of the
     * shader sources is <code>null</code>), then an error message
     * may be printed, and <code>null</code> will be returned.
     *
     * @param vertexShaderSource   The vertex shader source code
     * @param fragmentShaderSource The fragment shader source code
     * @return The GL program
     */
    @Override
    public Integer createGlProgram(String vertexShaderSource, String fragmentShaderSource) {

        if (vertexShaderSource == null)
        {
            logger.warning("The vertexShaderSource is null");
            return null;
        }
        if (fragmentShaderSource == null)
        {
            logger.warning("The fragmentShaderSource is null");
            return null;
        }

        logger.fine("Creating vertex shader...");

        Integer glVertexShader = createGlShader(
                GL_VERTEX_SHADER, vertexShaderSource);
        if (glVertexShader == null)
        {
            logger.warning("Creating vertex shader FAILED");
            return null;
        }

        logger.fine("Creating vertex shader DONE");

        logger.fine("Creating fragment shader...");
        Integer glFragmentShader = createGlShader(
                GL_FRAGMENT_SHADER, fragmentShaderSource);
        if (glFragmentShader == null)
        {
            logger.warning("Creating fragment shader FAILED");
            return null;
        }
        logger.fine("Creating fragment shader DONE");

        int glProgram  = glCreateProgram();

        glAttachShader(glProgram, glVertexShader);
        glDeleteShader(glVertexShader);

        glAttachShader(glProgram, glFragmentShader);
        glDeleteShader(glFragmentShader);

        glLinkProgram(glProgram);
        glValidateProgram(glProgram);

        int validateStatus[] = { 0 };
        glGetProgramiv(glProgram, GL_VALIDATE_STATUS, validateStatus, 0);
        if (validateStatus[0] != GL_TRUE)
        {
            printProgramLogInfo(glProgram);
            return null;
        }
        return glProgram;
    }


    /**
     * Creates an OpenGL shader with the given type, from the given source
     * code, and returns the GL shader object. If the shader cannot be
     * compiled, then <code>null</code> will be returned.
     *
     * @param shaderType The shader type
     * @param shaderSource The shader source code
     * @return The GL shader
     */
    private Integer createGlShader(int shaderType, String shaderSource)
    {
        Integer glShader = createGlShaderImpl(shaderType, shaderSource);
        if (glShader != null)
        {
            return glShader;
        }

        // If the shader source code does not contain a #version number,
        // then, depending on the com.jogamp.opengl.GLProfile that was
        // chosen for the viewer, certain warnings may be treated as
        // errors. As a workaround, pragmatically insert a version
        // number and try again...
        // (Also see https://github.com/javagl/JglTF/issues/12)
        if (!shaderSource.contains("#version"))
        {
            String versionString = "#version 120";
            logger.warning("Inserting GLSL version specifier \"" +
                    versionString + "\" in shader code");
            String shaderSourceWithVersion =
                    versionString + "\n" + shaderSource;
            return createGlShaderImpl(shaderType, shaderSourceWithVersion);
        }
        return null;
    }

    /**
     * Implementation for {@link #createGlShader(int, String)}.
     *
     * @param shaderType The shader type
     * @param shaderSource The shader source code
     * @return The GL shader, or <code>null</code> if it cannot be compiled
     */
    private Integer createGlShaderImpl(int shaderType, String shaderSource)
    {

        // this is code from book
        int glShader = glCreateShader(shaderType);
        glShaderSource(glShader, shaderSource); //
        glCompileShader(glShader);

        int compileStatus[] = { 0 };
        glGetShaderiv(glShader, GL_COMPILE_STATUS, compileStatus, 0);
        if (compileStatus[0] != GL_TRUE)
        {
            printShaderLogInfo(glShader);
            return null;
        }
        return glShader;

    }

    /**
     * Instruct the underling GL implementation to use the given program
     *
     * @param glProgram The GL program
     */
    @Override
    public void useGlProgram(int glProgram) {
        glUseProgram(glProgram);
    }

    /**
     * Delete the given GL program
     *
     * @param glProgram The GL program
     */
    @Override
    public void deleteGlProgram(int glProgram) {
        glDeleteProgram(glProgram);
    }

    /**
     * Enable all the states that are found in the given list, by calling
     * <code>glEnable</code> for all of them.
     *
     * @param states The states
     */
    @Override
    public void enable(Iterable<? extends Number> states) {
        if (states != null)
        {
            for (Number state : states)
            {
                if (state != null)
                {
                    glEnable(state.intValue());
                }
            }
        }
    }

    /**
     * Disable all the states that are found in the given list, by calling
     * <code>glDisable</code> for all of them.
     *
     * @param states The states
     */
    @Override
    public void disable(Iterable<? extends Number> states) {
        if (states != null)
        {
            for (Number state : states)
            {
                if (state != null)
                {
                    glDisable(state.intValue());
                }
            }
        }
    }

    /**
     * Returns the location of the specified uniform in the given program
     *
     * @param glProgram   The GL program
     * @param uniformName The name of the uniform
     * @return The uniform location
     */
    @Override
    public int getUniformLocation(int glProgram, String uniformName) {
        glUseProgram(glProgram);
        return glGetUniformLocation(glProgram, uniformName);
    }

    /**
     * Returns the location of the specified attribute in the given program
     *
     * @param glProgram     The GL program
     * @param attributeName The name of the attribute
     * @return The attribute location
     */
    @Override
    public int getAttributeLocation(int glProgram, String attributeName) {
        glUseProgram(glProgram);
        return glGetAttribLocation(glProgram, attributeName);
    }

    /**
     * Set the value of the specified uniform
     *
     * @param type     The type of the uniform
     * @param location The uniform location
     * @param count    The number of elements to set
     * @param value    The value to set
     */
    @Override
    public void setUniformiv(int type, int location, int count, int[] value) {
        if (value == null)
        {
            logger.warning("Invalid uniform value: " + value);
            return;
        }
        switch (type)
        {
            case GltfConstants.GL_INT:
            case GltfConstants.GL_UNSIGNED_INT:
            {
                glUniform1iv(location, count, value, 0);
                break;
            }
            case GltfConstants.GL_INT_VEC2:
            {
                glUniform2iv(location, count, value, 0);
                break;
            }
            case GltfConstants.GL_INT_VEC3:
            {
                glUniform3iv(location, count, value, 0);
                break;
            }
            case GltfConstants.GL_INT_VEC4:
            {
                glUniform4iv(location, count, value, 0);
                break;
            }
            default:
                logger.warning("Invalid uniform type: " +
                        GltfConstants.stringFor(type));
        }
    }

    /**
     * Set the value of the specified uniform
     *
     * @param type     The type of the uniform
     * @param location The uniform location
     * @param count    The number of elements to set
     * @param value    The value to set
     */
    @Override
    public void setUniformfv(int type, int location, int count, float[] value) {
        if (value == null)
        {
            logger.warning("Invalid uniform value: " + value);
            return;
        }
        switch (type)
        {
            case GltfConstants.GL_FLOAT:
            {
                glUniform1fv(location, count, value, 0);
                break;
            }
            case GltfConstants.GL_FLOAT_VEC2:
            {
                glUniform2fv(location, count, value, 0);
                break;
            }
            case GltfConstants.GL_FLOAT_VEC3:
            {
                glUniform3fv(location, count, value, 0);
                break;
            }
            case GltfConstants.GL_FLOAT_VEC4:
            {
                glUniform4fv(location, count, value, 0);
                break;
            }
            default:
                logger.warning("Invalid uniform type: " +
                        GltfConstants.stringFor(type));
        }

    }

    /**
     * Set the value of the specified uniform
     *
     * @param type     The type of the uniform
     * @param location The uniform location
     * @param count    The number of elements to set
     * @param value    The value to set
     */
    @Override
    public void setUniformMatrixfv(int type, int location, int count, float[] value) {
        if (value == null)
        {
            logger.warning("Invalid uniform value: " + value);
            return;
        }
        switch (type)
        {
            case GltfConstants.GL_FLOAT_MAT2:
            {
                glUniformMatrix2fv(location, count, false, value, 0);
                break;
            }
            case GltfConstants.GL_FLOAT_MAT3:
            {
                glUniformMatrix3fv(location, count, false, value, 0);
                break;
            }
            case GltfConstants.GL_FLOAT_MAT4:
            {
                glUniformMatrix4fv(location, count, false, value, 0);
                break;
            }
            default:
                logger.warning("Invalid uniform type: " +
                        GltfConstants.stringFor(type));
        }
    }

    /**
     * Set the value of the specified uniform
     *
     * @param location     The uniform location
     * @param textureIndex The index of the texture unit
     * @param glTexture    The GL texture
     */
    @Override
    public void setUniformSampler(int location, int textureIndex, int glTexture) {
        glActiveTexture(GL_TEXTURE0+textureIndex);
        glBindTexture(GL_TEXTURE_2D, glTexture);
        glUniform1i(location, textureIndex);
    }

    /**
     * Create an OpenGL vertex array object
     *
     * @return The vertex array object
     */
    @Override
    public int createGlVertexArray() {
        int vertexArrayArray[] = {0};
        glGenVertexArrays(1, vertexArrayArray, 0);
        int glVertexArray = vertexArrayArray[0];
        return glVertexArray;
    }

    /**
     * Delete the given GL vertex array object
     *
     * @param glVertexArray The GL vertex array object
     */
    @Override
    public void deleteGlVertexArray(int glVertexArray) {
        glDeleteVertexArrays(1, new int[] { glVertexArray }, 0);
    }

    /**
     * Create an OpenGL buffer view (vertex buffer object) from the given data
     *
     * @param target         The target, GL_ARRAY_BUFFER or GL_ELEMENT_ARRAY_BUFFER
     * @param byteLength     The length of the buffer data, in bytes
     * @param bufferViewData The actual buffer data
     * @return The GL buffer
     */
    @Override
    public int createGlBufferView(int target, int byteLength, ByteBuffer bufferViewData) {
        int bufferViewArray[] = {0};
        glGenBuffers(1, bufferViewArray, 0);
        int glBufferView = bufferViewArray[0];
        // Upload vertex buffer to GPU
        glBindBuffer(target, glBufferView);
        glBufferData(target, byteLength, bufferViewData, GL_STATIC_DRAW);
        return glBufferView;
    }

    /**
     * Create a vertex attribute in the given GL vertex array
     *
     * @param glVertexArray     The GL vertex array object
     * @param target            The target, GL_ARRAY_BUFFER or GL_ELEMENT_ARRAY_BUFFER
     * @param glBufferView      The GL buffer view (vertex buffer object)
     * @param attributeLocation The attribute location to bind to
     * @param size              The size of the attribute data, in bytes
     * @param type              The type of the attribute
     * @param stride            The stride between elements of the attribute, in bytes
     * @param offset            The offset of the attribute data, in bytes
     */
    @Override
    public void createVertexAttribute(int glVertexArray, int target, int glBufferView, int attributeLocation, int size, int type, int stride, int offset) {
        glBindVertexArray(glVertexArray);
        glBindBuffer(target, glBufferView);
        glVertexAttribPointer(
                attributeLocation, size, type, false, stride, offset);
        glEnableVertexAttribArray(attributeLocation);
    }

    /**
     * Update the vertex attribute data in the given GL vertex array
     * with the given data
     *
     * @param glVertexArray The GL vertex array object
     * @param target        The target, GL_ARRAY_BUFFER or GL_ELEMENT_ARRAY_BUFFER
     * @param glBufferView  The GL buffer view (vertex buffer object)
     * @param offset        The offset of the attribute data, in bytes
     * @param size          The size of the attribute data, in bytes
     * @param data          The actual buffer data
     */
    @Override
    public void updateVertexAttribute(int glVertexArray, int target, int glBufferView, int offset, int size, ByteBuffer data) {
        glBindVertexArray(glVertexArray);
        glBindBuffer(target, glBufferView);
        glBufferSubData(target, offset, size, data);
    }

    /**
     * Delete the given GL buffer
     *
     * @param glBufferView The GL buffer
     */
    @Override
    public void deleteGlBufferView(int glBufferView) {
        glDeleteBuffers(1, new int[] { glBufferView }, 0);
    }

    /**
     * Create an OpenGL texture from the given texture parameters
     *
     * @param pixelData      The pixel data,
     * @param internalFormat The internal format
     * @param width          The width
     * @param height         The height
     * @param format         The format
     * @param type           The type
     * @return The GL texture
     */
    @Override
    public int createGlTexture(Bitmap pixelData, int internalFormat, int width, int height, int format, int type) {



        int textureArray[] = {0};
        glGenTextures(1, textureArray, 0);
        int glTexture = textureArray[0];

        glBindTexture(GL_TEXTURE_2D, glTexture);

        //glTexImage2D(
        //        GL_TEXTURE_2D, 0, internalFormat, width, height,
        //        0, format, type, pixelData);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, pixelData, 0);

        return glTexture;
    }

    /**
     * Set the parameters for the given GL texture
     *
     * @param glTexture The GL texture
     * @param minFilter The minimization filter method
     * @param magFilter The magnification filter method
     * @param wrapS     The wrapping method along the S-axis
     * @param wrapT     The wrapping method along the T-axis
     */
    @Override
    public void setGlTextureParameters(int glTexture, int minFilter, int magFilter, int wrapS, int wrapT) {
        glBindTexture(GL_TEXTURE_2D, glTexture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, 0);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, magFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapS);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapT);
    }

    /**
     * Delete the given GL texture
     *
     * @param glTexture The GL texture
     */
    @Override
    public void deleteGlTexture(int glTexture) {
        glDeleteTextures(1, new int[] { glTexture }, 0);
    }

    /**
     * Render an indexed object, described by the given parameters
     *
     * @param glVertexArray   The GL vertex array
     * @param mode            The rendering mode (GL_TRIANGLES, for example)
     * @param glIndicesBuffer The indices buffer object
     * @param numIndices      The number of indices
     * @param indicesType     The type of the indices
     * @param offset          The offset in the indices buffer, in bytes
     */
    @Override
    public void renderIndexed(int glVertexArray, int mode, int glIndicesBuffer, int numIndices, int indicesType, int offset) {
        glBindVertexArray(glVertexArray);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, glIndicesBuffer);
        glDrawElements(mode, numIndices, indicesType, offset);
    }

    /**
     * Render a non-indexed object, described by the given vertex array
     *
     * @param glVertexArray The GL vertex array
     * @param mode          The rendering mode (GL_TRIANGLES, for example)
     * @param numVertices   The number of vertices of the object
     */
    @Override
    public void renderNonIndexed(int glVertexArray, int mode, int numVertices) {
        glBindVertexArray(glVertexArray);
        glDrawArrays(mode, 0, numVertices);
    }

    /**
     * Set the blend color<br>
     * <br>
     * (See <code>glBlendColor</code>)
     *
     * @param r red
     * @param g green
     * @param b blue
     * @param a alpha
     */
    @Override
    public void setBlendColor(float r, float g, float b, float a) {
        glBlendColor(r, g, b, a);
    }

    /**
     * Set the blend equation mode<br>
     * <br>
     * (See <code>glBlendEquationSeparate</code>)
     *
     * @param modeRgb   The mode for the RBG part
     * @param modeAlpha The mode for alpha
     */
    @Override
    public void setBlendEquationSeparate(int modeRgb, int modeAlpha) {
        glBlendEquationSeparate(modeRgb, modeAlpha);
    }

    /**
     * Set the blend function<br>
     * <br>
     * (See <code>glBlendFuncSeparate</code>)
     *
     * @param srcRgb   The source RGB function
     * @param dstRgb   The destination RGB function
     * @param srcAlpha The source alpha function
     * @param dstAlpha The destination alpha function
     */
    @Override
    public void setBlendFuncSeparate(int srcRgb, int dstRgb, int srcAlpha, int dstAlpha) {
        glBlendFuncSeparate(srcRgb, dstRgb, srcAlpha, dstAlpha);
    }

    /**
     * Set the color mask<br>
     * <br>
     * (See <code>glColorMask</code>)
     *
     * @param r red
     * @param g green
     * @param b blue
     * @param a alpha
     */
    @Override
    public void setColorMask(boolean r, boolean g, boolean b, boolean a) {
        glColorMask(r, g, b, a);
    }

    /**
     * Set the face culling<br>
     * <br>
     * (See <code>glCullFace</code>)
     *
     * @param mode The culling mode
     */
    @Override
    public void setCullFace(int mode) {
        glCullFace(mode);
    }

    /**
     * Set the depth test function<br>
     * <br>
     * (See <code>glDepthFunc</code>)
     *
     * @param func The depth test function
     */
    @Override
    public void setDepthFunc(int func) {
        glDepthFunc(func);
    }

    /**
     * Set the depth test mask<br>
     * <br>
     * (See <code>glDepthMask</code>)
     *
     * @param mask The depth test mask
     */
    @Override
    public void setDepthMask(boolean mask) {
        glDepthMask(mask);
    }

    /**
     * Set the depth range<br>
     * <br>
     * (See <code>glDepthRange</code>)
     *
     * @param zNear The near range
     * @param zFar  The far range
     */
    @Override
    public void setDepthRange(float zNear, float zFar) {
        glDepthRangef(zNear, zFar);
    }

    /**
     * Set the front face mode<br>
     * <br>
     * (See <code>glFrontFace</code>)
     *
     * @param mode The front face mode
     */
    @Override
    public void setFrontFace(int mode) {
        glFrontFace(mode);
    }

    /**
     * Set the line width<br>
     * <br>
     * (See <code>glLineWidth</code>)
     *
     * @param width The line width
     */
    @Override
    public void setLineWidth(float width) {
        glLineWidth(width);
    }

    /**
     * Set the polygon offset<br>
     * <br>
     * (See <code>glPolygonOffset</code>)
     *
     * @param factor The factor
     * @param units  The units
     */
    @Override
    public void setPolygonOffset(float factor, float units) {
        glPolygonOffset(factor, units);
    }

    /**
     * Set the scissor<br>
     * <br>
     * (See <code>glScissor</code>)
     *
     * @param x      The x-coordinate
     * @param y      The y-coordinate
     * @param width  The width
     * @param height The height
     */
    @Override
    public void setScissor(int x, int y, int width, int height) {
        glScissor(x, y, width, height);
    }


    /**
     * For debugging: Print shader log info
     *
     * @param id shader ID
     */
    private void printShaderLogInfo(int id)
    {
        IntBuffer infoLogLength = ByteBuffer
                .allocateDirect(4)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();
        glGetShaderiv(id, GL_INFO_LOG_LENGTH, infoLogLength);
        if (infoLogLength.get(0) > 0)
        {
            infoLogLength.put(0, infoLogLength.get(0) - 1);
        }

        String infoLogString = glGetShaderInfoLog(id);
        if (infoLogString.trim().length() > 0)
        {
            logger.warning("shader log:\n"+infoLogString);
        }
    }

    /**
     * For debugging: Print program log info
     *
     * @param id program ID
     */
    private void printProgramLogInfo(int id)
    {
        IntBuffer infoLogLength = ByteBuffer
                .allocateDirect(4)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();
        glGetProgramiv(id, GL_INFO_LOG_LENGTH, infoLogLength);
        if (infoLogLength.get(0) > 0)
        {
            infoLogLength.put(0, infoLogLength.get(0) - 1);
        }

        String infoLogString = glGetProgramInfoLog(id);
        if (infoLogString.trim().length() > 0)
        {
            logger.warning("program log:\n"+infoLogString);
        }
    }

}
