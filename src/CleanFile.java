import java.io.*;
import java.util.ArrayList;

public class CleanFile {
    String csvFile;
    ArrayList<Integer> fieldsOnOff;
    String csvFileCleaned;

    public CleanFile(ArrayList<Integer> fieldsOnOff) {

        this.fieldsOnOff = fieldsOnOff;

    }

    public void clean(String file1,String file2) {


        try {

            BufferedReader br = new BufferedReader(new FileReader(file1));

            BufferedWriter bw = new BufferedWriter(new FileWriter(file2));

            int y = 0;
            long start = System.currentTimeMillis();
            System.out.println("Inizio clean");
            String line=br.readLine();
            String[] indice = line.split(",", -1);

            for (Integer i = 0; i < fieldsOnOff.size() && i < indice.length; i++) {
                if (fieldsOnOff.get(i) != 0) bw.write(indice[i] + ",");
            }
            bw.write("Mese");

            //y++;
            bw.newLine();
            int i_nplza = -1, c = 0;
            for (String s : indice) {
                if (s.toLowerCase().equals("nplza")) i_nplza = c;
                c++;
            }
            int i = 0, j = 0;
//            System.out.println("Numero campi indice: " + indice.length);

            while ((line = br.readLine()) != null) {
                String[] field = line.split(",", -1);
//                System.out.println("Numero campi linea: " + field.length);
                if (!field[i_nplza].isEmpty()) {
                    for (i = 0, j = 0; i < fieldsOnOff.size() && i < field.length; i++, j++) {

                        if (fieldsOnOff.get(i) != 0) {
                            if (field[j].toLowerCase().contains("manager")) {
                                bw.write("TOP MANAGER" + ",");
                                j = j + 2;
                            } else {
                                if (!field[j].isEmpty()) {
                                    bw.write(field[j] + ",");
                                } else {
                                    bw.write("0,");
                                }
                            }
                        }
                    }
                    bw.write("0");
                    bw.newLine();
                }

            }
            bw.close();

            long end = System.currentTimeMillis();
            System.out.println("Clean file: " + (end - start) / 1000 + " seconds");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void clean2015(String file1,String file2) {


        try {

            BufferedReader br = new BufferedReader(new FileReader(file1));

            BufferedWriter bw = new BufferedWriter(new FileWriter(file2));

            int y = 0;
            long start = System.currentTimeMillis();
            System.out.println("Inizio clean");
            String line2=br.readLine();
            String line=line2.replaceAll("\"","");
            String[] indice = line.split(";", -1);

            for (Integer i = 0; i < fieldsOnOff.size() && i < indice.length; i++) {
                if (fieldsOnOff.get(i) != 0) bw.write(indice[i] + ",");
            }
            bw.write("Mese");

            //y++;
            bw.newLine();
            int i_nplza = -1, c = 0;
            for (String s : indice) {
                if (s.toLowerCase().equals("nplza")) i_nplza = c;
                c++;
            }
            int i = 0, j = 0;
//            System.out.println("Numero campi indice: " + indice.length);

            while ((line2 = br.readLine()) != null) {
                line=line2.replaceAll("\"","");
                String[] field = line.split(";", -1);
//                System.out.println("Numero campi linea: " + field.length);
                if (!field[i_nplza].isEmpty()) {
                    for (i = 0, j = 0; i < fieldsOnOff.size() && i < field.length; i++, j++) {

                        if (fieldsOnOff.get(i) != 0) {
                            if (field[j].toLowerCase().contains("manager")) {
                                bw.write("TOP MANAGER" + ",");
//                                j = j + 2;
                            } else {
                                if (!field[j].isEmpty() && !field[j].equals(".")) {
                                    bw.write(field[j] + ",");
                                } else {
                                    bw.write("0,");
                                }
                            }
                        }
                    }
                    bw.write("0");
                    bw.newLine();
                }

            }
            bw.close();

            long end = System.currentTimeMillis();
            System.out.println("Clean file: " + (end - start) / 1000 + " seconds");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

