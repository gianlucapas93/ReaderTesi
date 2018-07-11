import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadSettings {

    String chooseFields = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\chooseFields.txt";
    String chooseFieldsCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\chooseFieldsCLR.txt";
    String discretizationFile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\discretization.txt";
    String discretizationFileCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\discretizationCLR.txt";
    String csvFile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent.csv";


    String line = "", line2 = "";
    String csvsplitby = ",";
    int i = 0;
    ArrayList<Integer> arrayList = new ArrayList<>();
    Map<Integer, Discretization> map = new HashMap<>();
    BufferedReader br, br2;

    public ArrayList<Integer> readChooseFile() {

        //legge il file degli attributi on/off, legge la prima riga del csv, confronta il numero di attributi
        ArrayList<Integer> array = new ArrayList<>();
        String line = "", line2 = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(chooseFields));
            BufferedReader br2 = new BufferedReader(new FileReader(csvFile));

            line2 = br2.readLine();
            String[] split2 = line2.split(",", -1);
            // System.out.println("Dimensione indice file originale: "+split2.length);
            br2.close();
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(" ");

                array.add(i, Integer.parseInt(split[1]));
                i++;
            }
            // System.out.println("Dimensione file delle scelte: "+array.size());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return array;
    }

    public void writeCleanFile() {

    }

    public ArrayList<Integer> readOnOff(String file1) {
        // leggo ogni riga del file di discretizzazione e la carico in una mappa
        try {
            Integer j = 0;
            br = new BufferedReader(new FileReader(chooseFieldsCLR));
            br2 = new BufferedReader(new FileReader(file1));

            while ((line = br.readLine()) != null && (line2 = br2.readLine()) != null) {

                String[] splitline = line.split(" ");
                if (splitline[1].equals("1")) {
                    String[] splitline2 = line2.split(";");
//                    System.out.println(splitline2[0]);
                    if (!splitline2[1].equals("0")) {
                        Integer n = Integer.parseInt(splitline2[2]);

                        String[] intervals = splitline2[3].split(",");
                        Integer[] valueOfIntervals = new Integer[n];
                        Integer i = 1;
                        //Limite inferiore 0
                        valueOfIntervals[0] = 0;
                        for (String s : intervals) {

                            valueOfIntervals[i++] = Integer.parseInt(s);
                        }
                        String[] nameIntervals = splitline2[4].split(",");
                        Discretization d = new Discretization(true, n, valueOfIntervals, nameIntervals);
                        map.put(j, d);
                    }
                    arrayList.add(j, Integer.parseInt(splitline[1]));
                    j++;

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    public ArrayList<Integer> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Integer> arrayList) {
        this.arrayList = arrayList;
    }

    public Map<Integer, Discretization> getMap() {
        return map;
    }

    public void setMap(Map<Integer, Discretization> map) {
        this.map = map;
    }

    public void tookOutZero(String file1, String file2, ArrayList<Integer> array) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file1));
            BufferedWriter bw = new BufferedWriter(new FileWriter(file2));
            String line1, line2;
            String[] split1;
            String[] split2;
            int i = 0, count = 0;
            while ((line1 = br.readLine()) != null) {
                if (array.get(i) != 0) {
                    count++;
                    bw.write(line1);
                    bw.newLine();
                }
                i++;
            }
            System.out.println("Attributi selezionati: " + count);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
