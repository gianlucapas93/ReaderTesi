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

    private String chooseFields,csvFile,chooseFieldsCLR,discretizationFile,discretizationFileCLR,csvsplitby;

    public OpenFileAndCreate() {
    }

    public OpenFileAndCreate(Integer anno) {
        if (anno == 2016) {
            this.csvFile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent.csv";
            this.chooseFields = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\chooseFields.txt";
            this.chooseFieldsCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\chooseFieldsCLR.txt";
            this.discretizationFile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\discretization.txt";
            this.discretizationFileCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\discretizationCLR.txt";
            this.csvsplitby = ",";
        } else {
            this.csvFile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\itarea_compl2015_telematics_sent.csv";
            this.chooseFields = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\ReaderData\\chooseFields.txt";
            this.chooseFieldsCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\ReaderData\\chooseFieldsCLR.txt";
            this.discretizationFile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\ReaderData\\discretization.txt";
            this.discretizationFileCLR= "C:\\Users\\Gianluca\\Desktop\\Tesi\\2015\\ReaderData\\discretizationCLR.txt";
            this.csvsplitby = ";";
        }

        //BufferedReader br= null;
        String line = "";
        int i = 0;

    }


    public void initializeOnOff() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.csvFile))) {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.chooseFields));

            String line,line2;
            //HEADER COLONNE
            line2 = br.readLine();
            line=line2.replaceAll("\"","");
            String[] field = line.split(csvsplitby);
            for (String s : field) {
                bw.write(s + " 1");

                bw.newLine();

            }

            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeDiscretization() {
        try {
            BufferedReader br;
            BufferedWriter bw;

            //HEADER COLONNE

            String line;
            br = new BufferedReader(new FileReader(chooseFieldsCLR));
            bw = new BufferedWriter(new FileWriter(discretizationFileCLR));

            while ((line = br.readLine()) != null) {
                String[] split2 = line.split(" ");
                bw.write(split2[0] + ";0");
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void createNewOnOff() {

        try {
            BufferedReader br = new BufferedReader(new FileReader(chooseFields));
            BufferedWriter bw = new BufferedWriter(new FileWriter(chooseFieldsCLR));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] split = line.split(" ");
                if (split[1].equals("1")) {
                    bw.write(split[0] + " " + split[1]);
                    bw.newLine();
                }

            }
            bw.flush();
            bw.close();




        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
