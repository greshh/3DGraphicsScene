/* Class for placing an object (surface or virtual camera) into the world scene

   Note: "local" can also mean camera coordinates

 */

public class Placement {
    public Matrix4 tLW;    // Transform local to world
    public Matrix4 tWL;    // Transform world to local

    Placement() {
        tLW = Matrix4.createIdentity();
        tWL = Matrix4.createIdentity();
    }

    // Transform point in local to point in world
    public Point4 toWorld(Point4 p) {
        return tLW.times(p);
    }

    // Transform point in world to point in local
    public Point4 toLocal(Point4 p) {
        return tWL.times(p);
    }

    public static Placement placeModel(Point4 pD, Point4 pA, Point4 pS) {
        Placement p = new Placement();

        // creating the transformation matrices.
        Matrix4 S = Matrix4.createScale(pS.x, pS.y, pS.z);
        Matrix4 Rx = Matrix4.createRotationX(pA.x);
        Matrix4 Ry = Matrix4.createRotationY(pA.y);
        Matrix4 Rz = Matrix4.createRotationZ(pA.z);
        Matrix4 D = Matrix4.createDisplacement(pD.x, pD.y, pD.z);

        p.tLW = p.tLW.times(D).times(Rx).times(Ry).times(Rz).times(S);

        // creating the inverse matrices.
        S = Matrix4.createScale(1/pS.x, 1/pS.y, 1/pS.z);
        Rx = Matrix4.createRotationX(-pA.x);
        Ry = Matrix4.createRotationY(-pA.y);
        Rz = Matrix4.createRotationZ(-pA.z);
        D = Matrix4.createDisplacement(-pD.x, -pD.y, -pD.z);

        p.tWL = p.tWL.times(S).times(Rz).times(Ry).times(Rx).times(D);

        return p;
    }

    private static Matrix4 lookAt(Point4 pCamera, Point4 xaxis, Point4 yaxis, Point4 zaxis) {
        Matrix4 R = new Matrix4(
                xaxis.x, xaxis.y, xaxis.z, 0,
                yaxis.x, yaxis.y, yaxis.z, 0,
                zaxis.x, zaxis.y, zaxis.z, 0,
                0, 0, 0, 1);
        Matrix4 T = Matrix4.createDisplacement(-pCamera.x, -pCamera.y, -pCamera.z);
        return R.times(T);
    }

    private static Matrix4 lookBack(Point4 pCamera, Point4 xaxis, Point4 yaxis, Point4 zaxis) {
        Matrix4 R = new Matrix4(
                xaxis.x, yaxis.x, zaxis.x, 0,
                xaxis.y, yaxis.y, zaxis.y, 0,
                xaxis.z, yaxis.z, zaxis.z, 0,
                0, 0, 0, 1);
        Matrix4 T = Matrix4.createDisplacement(pCamera.x, pCamera.y, pCamera.z);
        return T.times(R);
    }

    public static Placement placeCamera(Point4 pCamera, Point4 pTarget, Point4 vUp) {
        Point4 zaxis = pTarget.minus(pCamera);
        Point4 xaxis = Point4.cross(vUp, zaxis);
        Point4 yaxis = Point4.cross(zaxis, xaxis);
        xaxis.normalize();
        yaxis.normalize();
        zaxis.normalize();
        Placement p = new Placement();
        p.tLW = lookBack(pCamera, xaxis, yaxis, zaxis);
        p.tWL = lookAt(pCamera, xaxis, yaxis, zaxis);
        return p;
    }
}
