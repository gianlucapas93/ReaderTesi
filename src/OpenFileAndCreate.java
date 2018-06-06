import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/*
*   CREA IL FILE CHE MAPPA IL CAMPO E L'INTERRUTTORE ON/OFF e inizializza tutto a 1 (cioè tutti i campi presenti)
*   Es.
*   field1 1
*   field2 1
*
* */

public class OpenFileAndCreate {
    String csvFile = "C:\\Users\\Gianluca\\Desktop\\Politecnico\\itarea_compl2016_telematics_sent_016.csv";
    String chooseFields = "C:\\Users\\Gianluca\\Desktop\\Politecnico\\chooseFields.txt";
    //BufferedReader br= null;
    String line="";
    String csvsplitby=",";
    int i=0;

    public void initialize(){
        try(BufferedReader br = new BufferedReader(new FileReader(csvFile))){
            BufferedWriter bw=null;
            FileWriter fw=null;
            fw = new FileWriter(chooseFields);
            bw = new BufferedWriter(fw);

            //HEADER COLONNE
            line= br.readLine();
            String[] field=line.split(csvsplitby);
            for(String s :field){
                bw.write(s+" 1");
                bw.newLine();
            }

            bw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}
