/*
 * Sample code for M3G article on IBM developerWorks.
 * http://www.ibm.com/developerworks/
 */


import javax.microedition.lcdui.*;
import javax.microedition.m3g.*;

/**
 * Uses a scene graph tree to structure a hierarchy of 3D objects.
 *
 * @author Claus Hoefele
 */
public class Background3D
{
    final static byte xr = 6;//breite nach rechts
    final static byte xl = 0;
    final static byte yu = 0;
    final static byte yd = -1;//hoehe nach unten
    final static byte zb = -10;//laenge nach hinten
    final static byte zf = 5;
    
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
    0, 0, -128,   0, 0, -128,   0, 0, -128,   0, 0, -128,   // back
     127, 0, 0,    127, 0, 0,    127, 0, 0,    127, 0, 0,   // right
    -128, 0, 0,   -128, 0, 0,   -128, 0, 0,   -128, 0, 0,   // left
    0,  127, 0,   0,  127, 0,   0,  127, 0,   0,  127, 0,   // top
    0, -128, 0,   0, -128, 0,   0, -128, 0,   0, -128, 0,   // bottom
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

  /** Lengths of triangle strips in TRIANGLE_INDICES. */
  private static int[] TRIANGLE_LENGTHS = {
    4, 4, 4, 4, 4, 4
  };
  
  /** User ID for all meshes (1).*/
  private static final int USER_ID_ALL_MESHES  = 1;
  /** Object that represents the 3D world. */
  private World _world;
  
  /** Graphics singleton used for rendering. */
  private Graphics3D _graphics3d;
  
  Mesh cubeMesh;
  VertexBuffer blueCubeVertexData;
  TriangleStripArray cubeTriangles;
  Appearance appearance;
  Group allMeshes;

  float height;
  float width;
  
  protected void init(float width, float height)
  {
      this.height = height;
      this.width = width;
    // Get the singleton for 3D rendering and a World.
    _graphics3d = Graphics3D.getInstance();
    _graphics3d.setViewport(0, 0, 10, 10);
    _world = new World();
    
    //weisser hintergrund
    Background background = new Background();
    background.setColor(0x00FFFFFF);
    background.setColorClearEnable(true);
    _world.setBackground(background);
    
    // kamera.
    Camera camera = new Camera();
    float aspect = width / height;
    camera.setPerspective(30.0f, aspect, 1.0f, 1000.0f);
    camera.setTranslation(0.0f, 0.0f, 10.0f);
    _world.addChild(camera);
    _world.setActiveCamera(camera);
    
    // licht in der mitte des bildschirmes.
    Light light = new Light();
    light.setMode(Light.OMNI);
    light.setIntensity(2.0f);
    _world.addChild(light);
    //letzten parameter auf was positives setzen, um schwarze balken zu vermeiden.
    light.setTranslation(0.0f, 0.0f, 0.0f);
    
    //ambientes licht (auf alles von ueberall) fuer ein bisschen grund-licht
    Light light2 = new Light();
    light2.setMode(Light.DIRECTIONAL);
    light2.setIntensity(1.0f);
//    light2.setColor(0x00FF00FF);
    _world.addChild(light2);
    
    
    
    blueCubeVertexData = new VertexBuffer();
    blueCubeVertexData.setDefaultColor(0x0000D0FF); // blue

    VertexArray vertexPositions = 
        new VertexArray(VERTEX_POSITIONS.length/3, 3, 1);
    vertexPositions.set(0, VERTEX_POSITIONS.length/3, VERTEX_POSITIONS);
    blueCubeVertexData.setPositions(vertexPositions, 1.0f, null);
    
    VertexArray vertexNormals = 
        new VertexArray(VERTEX_NORMALS.length/3, 3, 1);
    vertexNormals.set(0, VERTEX_NORMALS.length/3, VERTEX_NORMALS);
    blueCubeVertexData.setNormals(vertexNormals);

    // Create the triangles that define the cube; the indices point to
    // vertices in VERTEX_POSITIONS.
    cubeTriangles = new TriangleStripArray(
        TRIANGLE_INDICES, TRIANGLE_LENGTHS);
    
    // Create material.
    Material material = new Material();
    material.setVertexColorTrackingEnable(true);
    appearance = new Appearance();
    appearance.setMaterial(material);

    // Create groups to organize the cubes.
    allMeshes = new Group();
    allMeshes.setUserID(USER_ID_ALL_MESHES);
    _world.addChild(allMeshes);
  }
  
  public void addPlatform(double x, double y) {
      //den ursprung an den mittelpunkt des bildschirmes verschieben
      x -= width/2f + 0;
      y -= height/2f;
      //massstab anpassen
      x /= 54.3f;
      y /= -54.3f;
      //erstellen, verkleinern, an die richtige stelle packen
      cubeMesh = new Mesh(blueCubeVertexData, cubeTriangles, appearance);
      cubeMesh.setScale(0.1f, 0.1f, 0.1f);
      cubeMesh.translate((float)x, (float)y, 0);
      allMeshes.addChild(cubeMesh);
  }
  
  /** Alle Plattformen um dist pixel verschieben. **/
  public void move (double dist) {
      allMeshes.translate(0, -(float)dist/54.3f, 0);
  }
  
  
  public void removeAll() {
      _world.removeChild(allMeshes);
      allMeshes = new Group();
      _world.addChild(allMeshes);
  }
  
  /**
   * Renders the sample on the screen.
   *
   * @param graphics the graphics object to draw on.
   */
  protected void paint(Graphics graphics)
  {
    _graphics3d.bindTarget(graphics);
    _graphics3d.render(_world);
    _graphics3d.releaseTarget();
  }
}
