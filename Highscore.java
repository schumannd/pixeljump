import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.rms.*;

public class Highscore{
    
    private RecordStore highscore;
    private int heigth;
    private int width;
    public int nameIndex = 0;
    public Vector data = new Vector();


    /** Konstruktor der Klasse Highscoere
    *   
    */
    public Highscore(){
        // We open the recordstore
        try{
            highscore = RecordStore.openRecordStore("High Score", true);
            //wenn noch keine einträge vorliegen werden standardwerte eingetragen
            if (highscore.getNumRecords() == 0)
                init();
            else
                loadHighscores();
            //ansonsten wird der vector aus dem rms befüllt
        }
        catch(Exception e){}
    }
    
    /** Methode saveScore 
     * überschreibt den lokalen vector ins rms
     */
    public void saveScore(){
        try {
            for(int j = nameIndex; j < data.size(); j++){
                String curr = (String) data.elementAt(j);
                highscore.setRecord(j, curr.getBytes(), 0, curr.length());
            }
        } catch (Exception e) {}
    }
    
    /**
     * fügt einen highscore an der richtigen stelle in den lokalen vector ein
     * @param s Der Score
     * @param name Der einzutragende Name
     */
    public void addScore(int s, String name){
        String score = Integer.toString(s);

        for (int i = 1; i < data.size(); i+=2) {
            int currScore = Integer.parseInt((String)data.elementAt(i));
            if (s > currScore) {
                data.insertElementAt(name, i - 1);
                nameIndex = i - 1;
                data.insertElementAt(score, i);
                data.setSize(10);
                return;
            }
        }
    }
    /**
     * Methode init
     * initialisiert die bildschirmwerte, die für die highscoreausgabe benötigt werden
     * @param height die höhe des bildschirms
     * @param width die breite des bildschirms
     */
    public void init(int heigth, int width){
        this.heigth = heigth;
        this.width = width;
    }
    
    /**
     * Methode init
     * initialisiert den Vector
     */
    private void init() {
        try {
            
            addElement("Malte", 10000);
            addElement("Malte", 9001);
            addElement("Malte", 8000);
            addElement("Malte", 500);
            addElement("Malte", 1);
            
        } catch (Exception e){}
        
    }
    
    /**
     * Methode loadHighscores
     * lädt die werte aus dem rms in den vector
     */
    private void loadHighscores(){
        try{
            // To read all hiscores saved on the RMS
            RecordEnumeration enu = null;
            
            enu = highscore.enumerateRecords(null, null, false);

            while (enu.hasNextElement( )) {
                byte[] record;
                String str;
                record = enu.nextRecord();
                str = new String(record);
                data.addElement(str);
            }
        }
        catch(Exception e){
            Debug.add("exception");
        }
    }
    
    /**
     * Methode addElement
     * fügt einen datensatz aus name und highscore in den vector ein
     * @param name der name
     * @param score der erreichte punktestand
     */
    private void addElement(String name, int score){
        data.addElement(name);
        data.addElement(Integer.toString(score)); 
    }
    
    /**
     * Methode paintHighscores
     * gibt die highscores als tabelle aus
     * @param g die zeichenklasse
     */
    public void paintHighscores(Graphics g) {
        //alle Namen ausgeben
        for (int i = 0; i < data.size(); i+=2){
            g.drawString((String) data.elementAt(i), width/3, i * 7 + heigth/5, 0);
        }
        //alle scores ausgeben
        for (int i = 1; i < data.size(); i+=2){
            g.drawString((String) data.elementAt(i), width/3 + 50, (i-1) * 7 + heigth/5, 0);
        }
    }
    
    /**
     * Methode isNewHighscore
     * überprüft, ob ein score besser als der schlechteste highscore ist
     * @param score der zu überprüfende score
     * @return true, wenn ein neuer highscoreeintrag erreicht wurde
     */
    public boolean isNewHighscore(int score){
        return score > Integer.parseInt((String) data.elementAt(data.size()-1));
    }
}