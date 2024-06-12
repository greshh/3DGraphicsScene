import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class A3DGraphicsScene {

    /* Draw black lines at the centre of the image. Call this to verify the camera
    pointing direction
     */
    public static void putAxes(BufferedImage image) {
        int npixx = image.getWidth();
        int npixy = image.getHeight();
        int L = (int) (Math.floor(0.0625*npixx));
        for (int k = 0; k < L; ++k) {
            int rgb = 0;
            image.setRGB(k+(npixx-1)/2, (npixy-1)/2, rgb);
            image.setRGB((npixx-1)/2, k+(npixy-1)/2, rgb);
        }
    }

    public static ArrayList<ThreeDSurface> createCube(Material material, SurfaceColour[] surfaceColours, Point4 pD, Point4 pA, Point4 pS) 
    {
        ArrayList<ThreeDSurface> cube = new ArrayList<>();

        cube.add(new ThreeDSurface(
            new Square(),
            material, 
            surfaceColours[0],
            Placement.placeModel(Point4.createPoint(0, 0, 0.5), Point4.createPoint(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0)), Point4.createPoint(1,1,1))
        )); /* FRONT */
        cube.add(new ThreeDSurface(
            new Square(),
            material,
            surfaceColours[1],
            Placement.placeModel(Point4.createPoint(0, 0, -0.5), Point4.createPoint(Math.toRadians(0), Math.toRadians(180), Math.toRadians(0)), Point4.createPoint(1,1,1))
        )); /* BACK */
        cube.add(new ThreeDSurface(
            new Square(),
            material,
            surfaceColours[2],
            Placement.placeModel(Point4.createPoint(0, .5, 0), Point4.createPoint(Math.toRadians(-90), Math.toRadians(0), Math.toRadians(0)), Point4.createPoint(1,1,1))
        )); /* TOP */
        cube.add(new ThreeDSurface(
            new Square(),
            material,
            surfaceColours[3],
            Placement.placeModel(Point4.createPoint(0, -.5, 0), Point4.createPoint(Math.toRadians(90), Math.toRadians(0), 0), Point4.createPoint(1,1,1))
        )); /* BOTTOM */
        cube.add(new ThreeDSurface(
            new Square(),
            material,
            surfaceColours[4],
            Placement.placeModel(Point4.createPoint(.5, 0, 0), Point4.createPoint(0, Math.toRadians(90), 0), Point4.createPoint(1,1,1))
        )); /* RIGHT */
        cube.add(new ThreeDSurface(
            new Square(),
            material,
            surfaceColours[5],
            Placement.placeModel(Point4.createPoint(-.5, 0, 0), Point4.createPoint(0, Math.toRadians(-90), 0), Point4.createPoint(1,1,1))
        )); /* LEFT */

        Placement cubePlacement = Placement.placeModel(pD, pA, pS);

        for (ThreeDSurface c:cube) {
            c.placement.tLW = cubePlacement.tLW.times(c.placement.tLW);
            c.placement.tWL = c.placement.tWL.times(cubePlacement.tWL);
        }

        return cube;
    }

    public static void main(String[] args) {

        // Position of light source in world coordinates
        Point4 pLightW = Point4.createPoint(-20, 20, 20);

        // Get a camera with field of view 15 degrees in y
        double fovy = Math.toRadians(15);
        int npixx = 1000;
        int npixy = 1000;
        Camera camera = Camera.standardCamera(fovy, npixx, npixy);

        // Position and orientation of the camera in the world scene
        Point4 pCam  = Point4.createPoint(0, 20, 30); // point from viewer
        Point4 pTarg = Point4.createPoint(0, 1.9, 0); // point of object
        Point4 vUp   = Point4.createVector(0, -1, 0);
        camera.rePoint(pCam, pTarg, vUp);

        // Get a scene graph that manages the list of surfaces to be rendered
        SceneGraph scene = new SceneGraph();

        ThreeDSurface shinyBall = new ThreeDSurface(new Sphere(), 
            new Material(0.5, 0.5, 100), 
            new UniformColour(Color.YELLOW), 
            Placement.placeModel(Point4.createPoint(-2.5, 1.20, -0.5), 
                Point4.createPoint(Math.toRadians(0), Math.toRadians(0), Math.toRadians(0)), 
                Point4.createPoint(1, 1, 1)
            )
        );

        SurfaceColour[] uniformCubeSC = { new UniformColour(Color.PINK),
            new UniformColour(Color.PINK),
            new UniformColour(Color.PINK),
            new UniformColour(Color.PINK),
            new UniformColour(Color.PINK),
            new UniformColour(Color.PINK),
        };

        ArrayList<ThreeDSurface> uniformCube = createCube(new Material(0.5, 0.5, 100), 
            uniformCubeSC, 
            Point4.createPoint(2.50, 0.95, -0.50), 
            Point4.createPoint(Math.toRadians(0), 
                Math.toRadians(45), 
                Math.toRadians(0)
            ),
            Point4.createPoint(1.5, 1.5, 1.5)
        );

        SurfaceColour[] rubicksCubeSC = { new TextureColour(new TextureMap("Textures/white.jpg")), // front
            new TextureColour(new TextureMap("Textures/yellow.jpg")), // back
            new TextureColour(new TextureMap("Textures/green.jpg")), // top
            new TextureColour(new TextureMap("Textures/blue.jpg")), // bottom
            new TextureColour(new TextureMap("Textures/orange.jpg")), // right
            new TextureColour(new TextureMap("Textures/red.jpg")), // left
        };

        ArrayList<ThreeDSurface> rubicksCube = createCube(new Material(0.5, 0.5, 100), 
            rubicksCubeSC, 
            Point4.createPoint(2.50, 2.45, -0.50), 
            Point4.createPoint(Math.toRadians(0), 
                Math.toRadians(-20), 
                Math.toRadians(0)
            ),
            Point4.createPoint(1.5, 1.5, 1.5)
        );

        SurfaceColour[] woodBlockSC = { new TextureColour(new TextureMap("Textures/wood.jpg")),
            new TextureColour(new TextureMap("Textures/wood.jpg")),
            new TextureColour(new TextureMap("Textures/wood.jpg")),
            new TextureColour(new TextureMap("Textures/wood.jpg")),
            new TextureColour(new TextureMap("Textures/wood.jpg")),
            new TextureColour(new TextureMap("Textures/wood.jpg")), 
        };

        ArrayList<ThreeDSurface> woodBlock = createCube(new Material(0.5, 0.5, 10.0), 
            woodBlockSC, Point4.createPoint(0, 0.5, -0.2), 
            Point4.createPoint(Math.toRadians(0), 
                Math.toRadians(0), 
                Math.toRadians(0)
            ), 
            Point4.createPoint(2, 0.6, 1.5)
        );

        ThreeDSurface floor = new ThreeDSurface(new Square(), 
            new Material(0.6, 0.5, 1), 
            new TextureColour(new TextureMap("Textures/carpet.jpg")), 
            Placement.placeModel(Point4.createPoint(0, 0.2, 0), 
                Point4.createPoint(Math.toRadians(270), 
                    Math.toRadians(0), 
                    Math.toRadians(0)
                ), 
                Point4.createPoint(30, 30, 1)
            )
        );

        /* WALLS */

        // LEFT WALL
        ThreeDSurface wallL = new ThreeDSurface(new Square(), 
            new Material(0.6, 0.5, 10), 
            new UniformColour(new Color(254,251,234)), 
            Placement.placeModel(Point4.createPoint(-7.5, 7.7, 0.0), 
                Point4.createPoint(Math.toRadians(0), 
                    Math.toRadians(270), 
                    Math.toRadians(0)
                ), 
                Point4.createPoint(15, 15, 15)
            )
        );
        wallL.isWall = true;

        // RIGHT WALL
        ThreeDSurface wallR = new ThreeDSurface(new Square(), 
            new Material(0.6, 0.5, 10), 
            new UniformColour(new Color(254,251,234)), 
            Placement.placeModel(Point4.createPoint(7.5, 7.7, 0.0), 
                Point4.createPoint(Math.toRadians(0), 
                    Math.toRadians(90), 
                    Math.toRadians(0)
                ), 
                Point4.createPoint(15, 15, 15)
            )
        );
        wallR.isWall = true;

        // BACK WALL
        ThreeDSurface wallB = new ThreeDSurface(new Square(), 
            new Material(0.6, 0.5, 10), 
            new UniformColour(new Color(254,251,234)), 
            Placement.placeModel(Point4.createPoint(0.0, 7.7, -7.5), 
                Point4.createPoint(Math.toRadians(0), 
                    Math.toRadians(0), 
                    Math.toRadians(0)
                ), 
                Point4.createPoint(15, 15, 15)
            )
        );
        wallB.isWall = true;

        // FRONT WALL
        ThreeDSurface wallF = new ThreeDSurface(new Square(), 
            new Material(0.6, 0.5, 10), 
            new UniformColour(new Color(254,251,234)), 
            Placement.placeModel(Point4.createPoint(0.0, 7.7, 7.5), 
                Point4.createPoint(Math.toRadians(0), 
                    Math.toRadians(180), 
                    Math.toRadians(0)
                ), 
                Point4.createPoint(15, 15, 15)
            )
        );
        wallF.isWall = true;

        // the walls can be shown depending on the coordinates of pCam.
        if (pCam.x > -7.5) { scene.add(wallL); }
        if (pCam.x < 7.5) { scene.add(wallR); }
        if (pCam.z > -7.5) { scene.add(wallB); }
        if (pCam.z < 7.5) { scene.add(wallF); }

        scene.add(floor);
        scene.add(createTeddyBear());
        scene.add(shinyBall);
        scene.add(uniformCube);
        scene.add(rubicksCube);
        scene.add(woodBlock);

        // Render the scene at the given camera and light source
        scene.render(camera, pLightW);

        // Uncomment if you want to verify the camera target point in the scene
        //putAxes(camera.image);

        // Display image in a JPanel/JFrame
        Display.show(camera.image);

        // Uncomment if you want to save your scene in an image file
        Display.write(camera.image, "scene.png");

    }

    public static ArrayList<ThreeDSurface> createTeddyBear() 
    {
        ArrayList<ThreeDSurface> teddy = new ArrayList<>();
        
        Material furMaterial = new Material(0.85, 0.5, 1.0);
        SurfaceColour furTexture = new TextureColour(new TextureMap("Textures/fur.jpg"));

        Point4 noRotation = Point4.createPoint(0, 0, 0);
        
        /* --- HEAD --- */
        Point4 headDisplacement = Point4.createPoint(0.00, 3.65, 0.00);
        Point4 headScale = Point4.createPoint(0.75, 0.75, 0.75);
        ThreeDSurface head = new ThreeDSurface(new Sphere(), 
            furMaterial, 
            furTexture,
            Placement.placeModel(headDisplacement, noRotation, headScale
            )
        );
        teddy.add(head);

        /* --- EYES AND NOSE --- */
        Material ENMaterial = new Material(0.01, 0.01, 1000); // EN = eyes/nose
        SurfaceColour ENColour = new UniformColour(Color.BLACK);

        Point4 eyeScale = Point4.createPoint(0.07, 0.07, 0.07);

        // LEFT EYE
        Point4 eyeLDisplacement = Point4.createPoint(-0.30, 3.75, 0.65);

        ThreeDSurface eyeL = new ThreeDSurface(new Sphere(), 
            ENMaterial, 
            ENColour, 
            Placement.placeModel(eyeLDisplacement, 
                noRotation, 
                eyeScale
            )
        );
        teddy.add(eyeL);

        // RIGHT EYE
        Point4 eyeRDisplacement = Point4.createPoint(0.30, 3.75, 0.65);

        ThreeDSurface eyeR = new ThreeDSurface(new Sphere(), 
            ENMaterial, 
            ENColour, 
            Placement.placeModel(eyeRDisplacement, 
                noRotation, 
                eyeScale
            )
        );
        teddy.add(eyeR);

        // NOSE
        Point4 noseDisplacement = Point4.createPoint(0.00, 3.45, 1.00);
        Point4 noseScale = Point4.createPoint(0.2, 0.1, 0.1);

        ThreeDSurface nose = new ThreeDSurface(new Sphere(), 
            ENMaterial, 
            ENColour, 
            Placement.placeModel(noseDisplacement, 
                noRotation, 
                noseScale
            )
        );   
        teddy.add(nose);

        /* --- SNOUT --- */
        Point4 snoutDisplacement = Point4.createPoint(0.00, 3.45, 0.80);
        Point4 snoutScale = Point4.createPoint(0.35, 0.25, 0.25);

        ThreeDSurface snout = new ThreeDSurface(new Sphere(), 
            furMaterial, 
            furTexture, 
            Placement.placeModel(snoutDisplacement, 
                noRotation, 
                snoutScale
            )
        );
        teddy.add(snout);

        /* --- EARS --- */
        Point4 earScale = Point4.createPoint(0.4, 0.4, 0.4);

        // LEFT EAR
        Point4 earLDisplacement = Point4.createPoint(-0.50, 4.10, 0);
        Point4 earLAngle = Point4.createPoint(Math.toRadians(0), Math.toRadians(0), Math.toRadians(-30));

        ThreeDSurface earL = new ThreeDSurface(new Sphere(), 
            furMaterial, 
            furTexture, 
            Placement.placeModel(earLDisplacement, 
                earLAngle, 
                earScale
            )
        );
        teddy.add(earL);

        // RIGHT EAR
        Point4 earRDisplacement = Point4.createPoint(0.50, 4.10, 0);
        Point4 earRAngle = Point4.createPoint(Math.toRadians(0), Math.toRadians(0), Math.toRadians(30));

        ThreeDSurface earR = new ThreeDSurface(new Sphere(), 
            furMaterial, 
            furTexture, 
            Placement.placeModel(earRDisplacement, 
                earRAngle, 
                earScale
            )
        );
        teddy.add(earR);

        /* --- BODY --- */
        Point4 bodyDisplacement = Point4.createPoint(0.0, 1.9, 0.0);
        Point4 bodyScale = Point4.createPoint(1.00, 1.15, 1.00);
        
        ThreeDSurface body = new ThreeDSurface(new Sphere(), 
            furMaterial, 
            furTexture, 
            Placement.placeModel(bodyDisplacement, 
                noRotation, 
                bodyScale
            )
        );
        teddy.add(body);

        /* --- ARMS AND LEGS --- */
        Point4 ALScale = Point4.createPoint(0.7, 1.8, 0.7); // AL = arm/leg
        SurfaceColour[] ALColours = {
            new TextureColour(new TextureMap("Textures/fur.jpg")), // front
            new TextureColour(new TextureMap("Textures/fur.jpg")), // back
            new TextureColour(new TextureMap("Textures/fur.jpg")), // top
            new TextureColour(new TextureMap("Textures/paw.jpg")), // bottom
            new TextureColour(new TextureMap("Textures/fur.jpg")), // left
            new TextureColour(new TextureMap("Textures/fur.jpg")) // right
        };

        // LEFT ARM
        Point4 armLDisplacement = Point4.createPoint(-1.20, 2.10, 0.25);
        Point4 armLRotation = Point4.createPoint(Math.toRadians(0), Math.toRadians(30), Math.toRadians(-50));
        
        ArrayList<ThreeDSurface> armL = createCube(furMaterial, 
            ALColours, 
            armLDisplacement, 
            armLRotation, 
            ALScale
        );
        for (ThreeDSurface face:armL) { teddy.add(face); }

        // RIGHT ARM
        Point4 armRDisplacement = Point4.createPoint(1.20, 2.20, 0.50);
        Point4 armRRotation = Point4.createPoint(Math.toRadians(0), Math.toRadians(-40), Math.toRadians(60));
        
        ArrayList<ThreeDSurface> armR = createCube(furMaterial, 
            ALColours, 
            armRDisplacement, 
            armRRotation, 
            ALScale
        );
        for (ThreeDSurface face:armR) { teddy.add(face); }

        // LEFT LEG
        Point4 legLDisplacement = Point4.createPoint(-0.80, 0.80, 0.25);
        Point4 legLRotation = Point4.createPoint(Math.toRadians(-75), Math.toRadians(-4), Math.toRadians(-11));
        
        ArrayList<ThreeDSurface> legL = createCube(furMaterial, 
            ALColours, 
            legLDisplacement, 
            legLRotation, 
            ALScale
        );
        for (ThreeDSurface face:legL) { teddy.add(face); }

        // RIGHT LEG
        Point4 legRDisplacement = Point4.createPoint(0.80, 0.80, 0.25);
        Point4 legRRotation = Point4.createPoint(Math.toRadians(-75), Math.toRadians(7), Math.toRadians(12));
        
        ArrayList<ThreeDSurface> legR = createCube(furMaterial, 
            ALColours, 
            legRDisplacement, 
            legRRotation, 
            ALScale
        );
        for (ThreeDSurface face:legR) { teddy.add(face); }

        return teddy;
    }

}
