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
    String discretizationFile = "C:\\Users\\Gianluca\\Desktop\\Politecnico\\discretization.txt";
    //BufferedReader br= null;
    String line="";
    String csvsplitby=",";
    int i=0;

    public void initializeOnOff(){
        try(BufferedReader br = new BufferedReader(new FileReader(csvFile))){
            BufferedWriter bw=null,bw2=null;
            FileWriter fw=null;

            bw = new BufferedWriter(new FileWriter(chooseFields));
            bw2 = new BufferedWriter(new FileWriter(discretizationFile));

            //HEADER COLONNE
            line= br.readLine();
            String[] field=line.split(csvsplitby);
            for(String s :field){
                bw.write(s+" 1");
                bw2.write(s +";0");
                bw.newLine();
                bw2.newLine();
            }

            bw.close();
            bw2.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    public void initializeDiscretization(){
        try{
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            BufferedWriter bw2=null;
            FileWriter fw=null;


            bw2 = new BufferedWriter(new FileWriter(discretizationFile));

            //HEADER COLONNE
            line= br.readLine();
            String[] field=line.split(csvsplitby);
            for(String s :field){
                bw2.write(s +";0");
                bw2.newLine();
            }

            bw2.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


}
