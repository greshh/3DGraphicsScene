/*
    This class handles the reflective properties of the surface.

    Makes sense to do it this way. How the shading is done, does depend on the material property
 */


public class Material {

    // Parameters of the Phong reflection model as described in the lectures
    private double alpha, beta, nShiny;

    Material(double a, double b, double n) {
        alpha = a;
        beta = b;
        nShiny = n;
    }

    public double calculate(Point4 N, Point4 L, Point4 V, double ff ) 
    {
        double freflect, f;

        N.normalize();
        L.normalize();
        V.normalize();
        
        /* initialising mirror reflectance unit vector, R.
        *   R = (2(N.L)*N)-L 
        */ 
        Point4 R = (Point4.createPoint(2*Point4.dot(N, L)*N.x, 2*Point4.dot(N, L)*N.y, 2*Point4.dot(N, L)*N.z)).minus(L);

        double dotVR = Point4.dot(V, R);
        double dotNL = Point4.dot(N, L);

        if (dotVR < 0) { dotVR = 0; }
        if (dotNL < 0) { dotNL = 0; }
        
        /* implementing the reflection shading factor - freflect
        *   freflect = (1−β)(N·L) + β(V·R)^n
        */
        freflect = ff*(((1-beta)*dotNL)+(beta*(Math.pow(dotVR, nShiny))));

        f = freflect*(1-alpha)+alpha; // calculating final shading factor.
        return f;
    }
}
