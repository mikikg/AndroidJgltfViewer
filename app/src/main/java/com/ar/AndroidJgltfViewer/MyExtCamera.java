package com.ar.AndroidJgltfViewer;

import de.javagl.jgltf.viewer.ExternalCamera;

import android.opengl.Matrix;

/**
 * jglTF viewer external camera integration with GLSurfaceView.Renderer
 */

class MyExtCamera implements ExternalCamera {

    float matrix[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    //projection parameters
    float pp_l = -0.5f; //left, -0.5 default, -0.33 for aspect ratio correction
    float pp_r = 0.5f; //right, 0.5 default, 0.33 for aspect ratio correction
    float pp_b = -0.5f; //bottom
    float pp_t = 0.5f; //top
    float pp_n = 1f; //near
    float pp_f = 10.0f; //far

    //view parameters
    float vp_x = 0.0f; //position x
    float vp_y = 0.0f; //position y
    float vp_z = 10.0f; //position z
    float vp_p = 0.0f; //pitch
    float vp_h = 0.0f; //heading
    float vp_r = 0.0f; //roll

    private float height;
    private float width;

    void init() {

        //view matrix - default - face camera

        float right[] = {1f, 0, 0, 0};
        float up[] = {0, 1f, 0, 0};
        float forward[] = {0, 0, 1f, 0};
        float position[] = {0, 0, 0, 1};

        this.matrix[0] = right[0];
        this.matrix[1] = right[1];
        this.matrix[2] = right[2];
        this.matrix[3] = right[3];
        this.matrix[4] = up[0];
        this.matrix[5] = up[1];
        this.matrix[6] = up[2];
        this.matrix[7] = up[3];
        this.matrix[8] = forward[0];
        this.matrix[9] = forward[1];
        this.matrix[10] = forward[2];
        this.matrix[11] = forward[3];
        this.matrix[12] = position[0];
        this.matrix[13] = position[1];
        this.matrix[14] = position[2];
        this.matrix[15] = position[3];

        //Matrix.orthoM(this.matrix,0,pp_l,pp_r,pp_b,pp_t,pp_n,pp_f);

    }

    /**
     * The view matrix of this camera, as a float array with 16 elements,
     * representing the 4x4 matrix in column-major order.<br>
     * <br>
     * The returned matrix will not be stored or modified. So the supplier
     * may always return the same matrix instance.
     *
     * @return The view matrix
     */
    @Override
    public float[] getViewMatrix() {
        return matrix;
    }

    /**
     * The projection matrix of this camera, as a float array with 16 elements,
     * representing the 4x4 matrix in column-major order.<br>
     * <br>
     * The returned matrix will not be stored or modified. So the supplier
     * may always return the same matrix instance.
     *
     * @return The projection matrix
     */
    @Override
    public float[] getProjectionMatrix() {
        return new float[]{
                2 / (pp_r - pp_l), 0, 0, -(pp_r + pp_l) / (pp_r - pp_l),
                0, 2 / (pp_t - pp_b), 0, -(pp_t + pp_b) / (pp_t - pp_b),
                0, 0, -2 / (pp_f - pp_n), -(pp_f + pp_n) / (pp_f - pp_n),
                0, 0, 0, 1
        };
    }

    public float getLoc(int loc) {
        return this.matrix[loc];
    }

    public void setLoc(int loc, float val) {
        this.matrix[loc] = val;
    }

    public void updateLoc(int loc, float val) {
        this.matrix[loc] += val;
    }

    public void rotateMatrix(float xx, float yy) {
        float x, y;
        x = (xx - this.height / 2) / this.height;
        y = (yy - this.width / 2) / this.width;
        Matrix.rotateM(this.matrix, 0, x, 1f, 0f, 0f);
        Matrix.rotateM(this.matrix, 0, y, 0, 1f, 0f);
    }

    // set left and right projection parameter (view port), define aspect ratio
    public void set_wh(float width, float height) {
        this.width = width;
        this.height = height;
        this.pp_l = width / height / -2f;
        this.pp_r = this.pp_l * -1;
    }

    public void moveView(float x, float y) {
        Matrix.translateM(this.matrix, 0, 0, (x - this.height / 2) / this.height, 0);
    }

}