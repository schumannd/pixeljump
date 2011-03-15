/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/*
 * Sample code for M3G article on IBM developerWorks.
 * http://www.ibm.com/developerworks/
 */


import javax.microedition.lcdui.*;
import javax.microedition.m3g.*;

/**
 * @author Claus Hoefele
 */
public class Background3D
{
    private final static byte xr = 5;//breite nach rechts
    private final static byte xl = 0;
    private final static byte yu = 0;
    private final static byte yd = -1;//hoehe nach unten
    private final static byte zb = -10;//laenge nach hinten
    private final static byte zf = 0;

    /** The cube's vertex positions (x, y, z). */
    private static final byte[] VERTEX_POSITIONS = {
        xl, yd, zf,   xr, yd, zf,   xl, yu, zf,   xr, yu, zf, // front
        xr, yd, zb,   xl, yd, zb,   xr, yu, zb,   xl, yu, zb, // back
        xr, yd, zf,   xr, yd, zb,   xr, yu, zf,   xr, yu, zb, // right
        xl, yd, zb,   xl, yd, zf,   xl, yu, zb,   xl, yu, zf, // left
        xl, yu, zf,   xr, yu, zf,   xl, yu, zb,   xr, yu, zb, // top
        xl, yd, zb,   xr, yd, zb,   xl, yd, zf,   xr, yd, zf  // bottom
    };

    /** The cube's normals. */
    private static final byte[] VERTEX_NORMALS = {
        0, 0,  127,   0, 0,  127,   0, 0,  127,   0, 0,  127,   // front
        0, 0, -127,   0, 0, -127,   0, 0, -127,   0, 0, -127,   // back
        127, 0, 0,    127, 0, 0,    127, 0, 0,    127, 0, 0,   // right
        -127, 0, 0,   -127, 0, 0,   -127, 0, 0,   -127, 0, 0,   // left
        0,  127, 0,   0,  127, 0,   0,  127, 0,   0,  127, 0,   // top
        0, -127, 0,   0, -127, 0,   0, -127, 0,   0, -127, 0,   // bottom
    };

    /** Indices that define how to connect the vertices to build
     * triangles. */
    private static final int[] TRIANGLE_INDICES = {
        0,  1,  2,  3,   // front
        4,  5,  6,  7,   // back
        8,  9, 10, 11,   // right
        12, 13, 14, 15,   // left
        16, 17, 18, 19,   // top
        20, 21, 22, 23    // bottom
    };
    
    
    private World _world;
    private Graphics3D _graphics3d;
    
    private Mesh platformMesh;
    private Group allPlatforms;

    private float height;
    private float width;
    
    public void init(float width, float height)
    {
        this.height = height;
        this.width = width;
        _graphics3d = Graphics3D.getInstance();
        _world = new World();

        //weisser hintergrund
        Background background = new Background();
        background.setColor(0x00FFFFFF);
        _world.setBackground(background);

        //die kamera.
        Camera camera = new Camera();
        float aspect = width / height;
        camera.setPerspective(30.0f, aspect, 1.0f, 1000.0f);
        camera.setTranslation(0.0f, 0.0f, 10.0f);
        _world.addChild(camera);
        _world.setActiveCamera(camera);

        // punktlicht (wie ne gluehbirne) in der mitte des bildschirmes.
        Light light = new Light();
        light.setMode(Light.OMNI);
        light.setIntensity(1.5f);
        _world.addChild(light);
        //letzten parameter auf was positives setzen, um schwarze balken zu vermeiden.
        light.setTranslation(0.0f, 0.0f, 0.5f);

        //direktionales Licht fuer die vorderseiten der plattformen
        Light light2 = new Light();
        light2.setMode(Light.DIRECTIONAL);
        light2.setIntensity(1.0f);
//        _world.addChild(light2);
        
        
        //Ab hier wird die Form der Plattform initialisiert.
        VertexBuffer vertexData = new VertexBuffer();
        vertexData.setDefaultColor(0x0000D0FF); // tuerkis

        VertexArray vertexPositions = new VertexArray(VERTEX_POSITIONS.length/3, 3, 1);
        vertexPositions.set(0, VERTEX_POSITIONS.length/3, VERTEX_POSITIONS);
        vertexData.setPositions(vertexPositions, 1.0f, null);

        VertexArray vertexNormals = new VertexArray(VERTEX_NORMALS.length/3, 3, 1);
        vertexNormals.set(0, VERTEX_NORMALS.length/3, VERTEX_NORMALS);
        vertexData.setNormals(vertexNormals);
        
        TriangleStripArray cubeTriangles = new TriangleStripArray(
                TRIANGLE_INDICES, new int[] {TRIANGLE_INDICES.length});

        // Das Material. Wird fuer Beleuchtung gebraucht."
        Material material = new Material();
        material.setVertexColorTrackingEnable(true);
        Appearance appearance = new Appearance();
        appearance.setMaterial(material);

        // Gruppe, die alle Plattformen enthaelt. Damit kann man alle Plattformen gleichzeitig bewegen.
        allPlatforms = new Group();
        _world.addChild(allPlatforms);

        platformMesh = new Mesh(vertexData, cubeTriangles, appearance);
        platformMesh.setScale(0.1f, 0.1f, 0.1f);
    }
    
    
    /** Eine Plattform an der Pixelkoordinate x/y hinzufuegen. **/
    public void addPlatform(double x, double y) {
        //den ursprung an den mittelpunkt des bildschirmes verschieben
        x -= width/2f;
        y -= height/2f;
        //massstab anpassen
        x = (x / (width/2))*(2.68/(height/width));
        y = (-y / (height/2))*2.68;
        //erstellen, verkleinern, an die richtige stelle packen
        Mesh platform = (Mesh) platformMesh.duplicate();
        platform.translate((float)x, (float)y, 0);
        allPlatforms.addChild(platform);
    }
    
    
    /** Alle Plattformen um dist pixel verschieben. **/
    public void move (double dist) {
        allPlatforms.translate(0, (float)(-dist / (height/2))*2.68f, 0);
    }
    
    
    /** Alle Plattformen entfernen. **/
    public void removeAll() {
        _world.removeChild(allPlatforms);
        allPlatforms = new Group();
        _world.addChild(allPlatforms);
    }
    
    
    /** Malt alles was es zu malen gibt in g rein. **/
    public void paint(Graphics g)
    {
        _graphics3d.bindTarget(g);
        _graphics3d.render(_world);
        _graphics3d.releaseTarget();
    }
}
