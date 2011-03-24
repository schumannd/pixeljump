import java.util.Vector;

import javax.microedition.rms.*;

public class Highscore{
    
    RecordStore highscore;
    
    Vector data = new Vector();
    
    public Highscore(){
        // We open the recordstore
        try{
            highscore = RecordStore.openRecordStore("High Score", true);
            if (highscore.getNumRecords() == 0)
                init();
            else
                loadHighscores();
        }
        catch(Exception e){}
    }
    
    public void addScore(int s){
        // To add a new HiScore we use a quick string comma-separated
        String name = "Pixel, ";
        String score = Integer.toString(s);
        
        for (int i = 1; i < data.size(); i+=2) {
            int currScore = Integer.parseInt((String)data.elementAt(i));
            if (s > currScore) {
                data.insertElementAt(name, i - 1);
                data.insertElementAt(score, i);
                data.setSize(10);
                try {
                    RecordStore.openRecordStore("High Score", true);
                    for(int j = i - 1; j < data.size(); j++){
                        String curr = (String) data.elementAt(j);
                        highscore.setRecord(j, curr.getBytes(), 0, curr.length());
                    }
                } catch (Exception e) {}
            }
        }
    }
    
    private void init() {
        try {
            highscore = RecordStore.openRecordStore("High Score", true);
            addElement("Malte", 10000);
            addElement("Malte", 9001);
            addElement("Malte", 8000);
            addElement("Malte", 500);
            addElement("Malte", 1);
            
        } catch (Exception e){}
        
    }
    
    private void loadHighscores(){
        // To read all hiscores saved on the RMS
        RecordEnumeration enu = null;
        try{
            RecordStore.openRecordStore("High Score", true);
            enu = highscore.enumerateRecords(null, null, false);
        }
        catch(Exception e){Debug.add("exception");};
        byte[] record;
        String str;
        while (enu.hasNextElement( )) {
            try{
                record = enu.nextRecord();
                str = new String(record);
                data.addElement(str);
            }
            catch(Exception e){}
        }
    }
    
    private void addElement(String name, int score){
        data.addElement(name);
        data.addElement(Integer.toString(score));
        byte [] nameb = name.getBytes();
        byte [] scoreb = Integer.toString(score).getBytes();
        
        try {
            RecordStore.openRecordStore("High Score", true);
            highscore.addRecord(nameb, 0, nameb.length);
            highscore.addRecord(scoreb, 0, scoreb.length);
        } catch (Exception e) {}
    }

}