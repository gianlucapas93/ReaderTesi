import java.io.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

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
    String chooseFields2 = "C:\\Users\\Gianluca\\Desktop\\Politecnico\\chooseFieldsCLR.txt";
    String discretizationFile = "C:\\Users\\Gianluca\\Desktop\\Politecnico\\discretization.txt";
    //BufferedReader br= null;
    String line="";
    String csvsplitby=",";
    int i=0;

    public void initializeOnOff(){
        try(BufferedReader br = new BufferedReader(new FileReader(csvFile))){
            BufferedWriter bw = new BufferedWriter(new FileWriter(chooseFields));


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
    public void initializeDiscretization(){
        try{
            BufferedReader br = new BufferedReader(new FileReader(chooseFields));
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(discretizationFile));

            //HEADER COLONNE
            while((line= br.readLine())!=null) {

                String[] field = line.split(" ");

                bw2.write(field[0] + ";0");
                bw2.newLine();

            }
            bw2.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void sortAndLower(){

        try{
            BufferedReader br = new BufferedReader(new FileReader(chooseFields));
            BufferedWriter bw = new BufferedWriter(new FileWriter(chooseFields2));
            //Map<String,String> map=new TreeMap<>();
            while((line= br.readLine())!=null){
                line=line.toUpperCase();
                bw.write(line);
                bw.newLine();

            }
//            for(Map.Entry<String,String> entry: map.entrySet()){
//
//            }
//            System.out.println(map);
            bw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public void createNewOnOff() {

        try {
            BufferedReader br = new BufferedReader(new FileReader(chooseFields));
            BufferedWriter bw = new BufferedWriter(new FileWriter(chooseFields2));
            String line="";
            while((line=br.readLine())!=null){
                String []split=line.split(" ");
                if(split[1].equals("1")){
                    bw.write(split[0]+" "+split[1]);
                    bw.newLine();
                }

            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
