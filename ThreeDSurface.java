import java.awt.*;
import java.util.ArrayList;

/*
    Encapsulate information that is not when shading a point on the surface
 */
class ShadeRecord {
    Point4 pSurface;  // Surface position - need this for z-buffering
    Color colour;     // Final colour at that surface point  after applying shading
    boolean isShaded;
}

class Shadows {

    public static double feel(ArrayList<ThreeDSurface> surfaces, Point4 pSurfaceW, Point4 pLightW) {
        HitRecord hit = new HitRecord();

        for (ThreeDSurface s:surfaces) {
            Ray ray = Ray.transform(new Ray(pSurfaceW, pLightW), s.placement.tWL);
            if (!s.isWall) {
                if (s.surfaceGeometry.shoot(ray, hit)) {
                    // calculating length of ray as a vector from light source to hit surface.
                    double vx = ray.pDest.x - ray.pOrigin.x;
                    double vy = ray.pDest.y - ray.pOrigin.y;
                    double vz = ray.pDest.z - ray.pOrigin.z;
                    double distance = Math.sqrt((vx*vx)+(vy*vy)+(vz*vz));
                    
                    // comparing the distance from the hit ray on the obstruction (s) and the distance from pSurfaceW.
                    // using a small value (0.0000001) to avoid noise.
                    if (hit.tHit>=0.0000001 && hit.tHit<distance) { return 0.0; }
                }
            }
        }
        return 1.0;
    }
}

/*
    Class to encapsulate the properties of a surface that is placed in a 3D scene
 */
public class ThreeDSurface {

    SurfaceGeometry surfaceGeometry; // The type of geometry
    Material material; // The material reflective properties of the surface    
    SurfaceColour surfaceColour; // The surface colour model: either uniform or from a texture map
    Placement placement; // The placement of the surface within the scene

    boolean isWall = false; // used to omit obstruction when calculating shadowing.

    public ThreeDSurface() {}

    public ThreeDSurface(SurfaceGeometry sg, Material m, SurfaceColour sc, Placement p) {
        this.surfaceGeometry = sg;
        this.surfaceColour = sc;
        this.material = m;
        this.placement = p;
    }

    /*
    Given a Ray in world coordinates, calculate final shaded colour
     */
    public static int clip01(int c, double f) {
        return (int)Math.round(f*c);
    }

    public static Color rescaleColour(Color c, double f) {
        int r = clip01(c.getRed(), f);
        int g = clip01(c.getGreen(), f);
        int b = clip01(c.getBlue(), f);
        return new Color(r, g, b);
    }

    public RasterMap getRasterMap(Matrix4 pmx, int npixx, int npixy) {
        BoundingBox bb = surfaceGeometry.getBB().transform(pmx);
        RasterMap rm = RasterMap.fromBB(bb, npixx, npixy);
        if (bb.anyNegW) {
            rm.x1 = 0;
            rm.x2 = npixx;
            rm.y1 = 0;
            rm.y2 = npixy;
        }
        return rm;
    }

    public ShadeRecord shadeIt(Ray rayW, Point4 pLightW, ArrayList<ThreeDSurface> surfaces) {

        ShadeRecord sr = new ShadeRecord();

        // Ray in local coordinates of the surface
        Ray ray = Ray.transform(rayW, placement.tWL);

        HitRecord hit = new HitRecord();

        surfaceGeometry.shoot(ray, hit);
        sr.isShaded = hit.isHit;

        if (hit.isHit) {
            Point4 pSurfaceW = placement.toWorld(hit.pSurface);
            Point4 vNormalW  = placement.toWorld(hit.vNormal); // N
            Point4 vLightW   = pLightW.minus(pSurfaceW); // L
            Point4 vViewW    = rayW.pOrigin.minus(pSurfaceW); // V

            double ff = Shadows.feel(surfaces, pSurfaceW, pLightW);
            double fShade = material.calculate(vNormalW, vLightW, vViewW, ff);
            Color c1 = surfaceColour.pickColour(hit.u, hit.v);
            sr.colour = rescaleColour(c1, fShade);
            sr.pSurface = placement.toWorld(hit.pSurface);
        }
        return sr;
    }

}
