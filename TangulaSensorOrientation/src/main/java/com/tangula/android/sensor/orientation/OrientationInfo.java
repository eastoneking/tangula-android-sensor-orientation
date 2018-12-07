package com.tangula.android.sensor.orientation;

/**
 * 姿态信息.
 */
public class OrientationInfo {
    //方位角
    private float azimuthAngle;
    //俯仰角
    private float pitchingAngle;
    //翻滚角
    private float rollingAngle;

    public OrientationInfo(){
        this(0.0f, 0.0f, 0.0f);
    }

    /**
     *
     * @param azimuthAngle 方位角.
     * @param pitchingAngle 俯仰角.
     * @param rollingAngle 翻滚角.
     */
    public OrientationInfo(float azimuthAngle, float pitchingAngle, float rollingAngle) {
        this.azimuthAngle = azimuthAngle;
        this.pitchingAngle = pitchingAngle;
        this.rollingAngle = rollingAngle;
    }

    public float getAzimuthAngle() {
        return azimuthAngle;
    }

    public void setAzimuthAngle(float azimuthAngle) {
        this.azimuthAngle = azimuthAngle;
    }

    public float getPitchingAngle() {
        return pitchingAngle;
    }

    public void setPitchingAngle(float pitchingAngle) {
        this.pitchingAngle = pitchingAngle;
    }

    public float getRollingAngle() {
        return rollingAngle;
    }

    public void setRollingAngle(float rollingAngle) {
        this.rollingAngle = rollingAngle;
    }
}
