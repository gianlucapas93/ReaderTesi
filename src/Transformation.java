import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Transformation {

    ArrayList<Integer> fieldsOnOff;
    Map<Integer, Discretization> map;

    public Transformation(ArrayList<Integer> fieldsOnOff, Map<Integer, Discretization> map) {
        this.fieldsOnOff = fieldsOnOff;
        this.map = map;
    }

    public void doDiscretization(String csvFile, String discretizedFile) {

        try {
            System.out.println("Inizio Discretization");
            BufferedReader br = new BufferedReader(new FileReader(csvFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(discretizedFile));
            String labelDiscrete = "";
            long starttime = System.currentTimeMillis();

            String indice = br.readLine();
            String[] splitindice = indice.split(",", -1);
            bw.write(indice);
            bw.newLine();
            String line;
            Double valtmp;

            while ((line = br.readLine()) != null) {
                String[] splitted = line.split(",");
                for (Map.Entry<Integer, Discretization> entry : map.entrySet()) {
                    Integer val;
                    if (!splitted[entry.getKey()].isEmpty()) {
//                        if(splitted[entry.getKey()].contains("PR")){
//                            System.out.println("NPLZA:"+splitted[50]+splitindice[entry.getKey()]+" "+splitted[entry.getKey()] +" disc:"+labelDiscrete);
//
//                        }

                        valtmp = Double.parseDouble(splitted[entry.getKey()]);
                        val = valtmp.intValue();


                        Discretization d = entry.getValue();
                        String[] label = d.getName_interval();
                        Integer[] intervals = d.getInterval();

                        //if(label.length!=(intervals.length+1)) throw new Exception("Vettore delle label e degli intervalli non coerenti");

                        int start = 0, flag = -1, i = 0;

                        for (i = 1, start = 0; i < intervals.length && flag == -1; i++) {
                            if (val >= start && val < intervals[i]) flag = i - 1;
                            else start = intervals[i];
                        }
                        if (flag == -1) {
                            labelDiscrete = label[label.length - 1];
                        } else {
                            labelDiscrete = label[flag];
                        }

                        splitted[entry.getKey()] = labelDiscrete;

                        //System.out.println("NPLZA:"+splitted[50]+splitindice[entry.getKey()]+" "+val +" disc:"+labelDiscrete);


                    }

                }

                for (String s : splitted) {
                    bw.write(s + ",");
                }
                bw.newLine();
            }
            bw.close();
            long end = System.currentTimeMillis();
            System.out.println("Discretization: " + (end - starttime) / 1000 + " seconds");


        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void propagateAccSx(String file1, String file2) {
        //String file2=path+"_acc_sx.csv";
        System.out.println("Inzio ACCSX");
        Map<String, ArrayList<String>> map = new HashMap<>();
        long start = System.currentTimeMillis();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file1));
            BufferedWriter bw = new BufferedWriter(new FileWriter(file2));

            String indice = br.readLine();
            String[] splitindice = indice.split(",", -1);
            int index_accsx = -1, i = 0, index_nplza = -1, index_settimana = -1;

            for (String s : splitindice) {
                if (s.toLowerCase().equals("nplza")) {
                    index_nplza = i;
                }
                if (s.toLowerCase().equals("settima")) {
                    index_settimana = i;
                }
                if (s.toLowerCase().equals("acc_sx")) {
                    index_accsx = i;
                }
                i++;
            }

            if (index_accsx == -1 || index_nplza == -1 || index_settimana == -1) {
                System.out.println("ERRORE! INDEX MANCANTE");
                throw new Exception("ERRORE di indice");
            }
//            bw.write(line1);
//            bw.newLine();
            String line1 = "";
            String[] split1;
            String[] split;
            System.out.println("Inizio la lettura incidenti");
            while ((line1 = br.readLine()) != null) {
                split = line1.split(",", -1);

                if (!split[index_accsx].isEmpty() && !split[index_accsx].equals("0")) {                                          //se la riga ha l'attributo acc_sx non nullo

                    if (map.containsKey(split[index_nplza])) {                                //se la mappa contiene gia quel nplza (piu di un incidente in un anno)
                        map.get(split[index_nplza]).add(split[index_settimana]);               //aggiungo la nuova settimana alla lista
                    } else {
                        ArrayList<String> list = new ArrayList<>();                           //sennò creo una nuova lista e la aggiungo in corrispondenza della nuova chiave
                        list.add(split[index_settimana]);
                        map.put(split[index_nplza], list);
                    }
                }

            }
            br.close();
            System.out.println("Fine Lettura, inizio aggiornamento accsx");
            br = new BufferedReader(new FileReader(file1));
            int mese = 0, totgiorni = 0, sett = 0;
            indice = br.readLine();
            splitindice = indice.split(",");

            bw.write(indice);
            bw.newLine();

            while ((line1 = br.readLine()) != null) {
                split1 = line1.split(",", -1);

                if (map.containsKey(split1[index_nplza])) {                                       //se la polizza è presente nella map degli incidenti
                    ArrayList<String> list2 = new ArrayList<>();                                  //scarico la lista delle settimane di incidente
                    list2 = map.get(split1[index_nplza]);
                    for (String s : list2) {                                                       //se quella riga fa parte di una settimana incidente, metto a 1
                        if (split1[index_settimana].equals(s)) split1[index_accsx] = "1";
                    }

                }
                // sett = Integer.parseInt(split1[index_settimana]);

                for (String s : split1) {

                    if (s.isEmpty()) s = "0";

                    bw.write(s + ",");


                }

                //bw.write(String.valueOf(mese) + ",");
                bw.newLine();
            }
            bw.close();
            long end = System.currentTimeMillis();
            System.out.println("Propagate ACCSX: " + (end - start) / 1000 + " seconds");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void splitOldNewAnnSem(String csvFileCampDiscAfterACCSX, String VApath, String NApath, String VSpath, String NSpath, Integer anno) {
        System.out.println("Inizio Split Old New");
        long start = System.currentTimeMillis();
        try {
            BufferedReader br = new BufferedReader(new FileReader(csvFileCampDiscAfterACCSX));
            String VA = VApath + "\\" + anno + "-VA.csv";
            String VS = VSpath + "\\" + anno + "-VS.csv";
            String NA = NApath + "\\" + anno + "-NA.csv";
            String NS = NSpath + "\\" + anno + "-NS.csv";
            BufferedWriter bwVA = new BufferedWriter(new FileWriter(VA));
            BufferedWriter bwNA = new BufferedWriter(new FileWriter(NA));
            BufferedWriter bwVS = new BufferedWriter(new FileWriter(VS));
            BufferedWriter bwNS = new BufferedWriter(new FileWriter(NS));

            String line = br.readLine();

            bwNA.write(line);
            bwVA.write(line);
            bwNS.write(line);
            bwVS.write(line);
            bwNA.newLine();
            bwVA.newLine();
            bwNS.newLine();
            bwVS.newLine();
            String[] split = line.split(",", -1);
            int index_affarenuovo = -1, index_fraz = -2, i = 0;

            for (String s : split) {
                if (s.toLowerCase().equals("affare_nuovo")) index_affarenuovo = i;
                if (s.toLowerCase().equals("fraz")) index_fraz = i;
                i++;
            }
            line = "";

            while ((line = br.readLine()) != null) {
                split = line.split(",", -1);
                if (split[index_affarenuovo].toLowerCase().startsWith("s")) { //nuovo N
                    split[index_affarenuovo] = "Si";
                    if (split[index_fraz].toLowerCase().startsWith("2")) {//  2-semestrale   NS

                        for (String s : split) {
                            bwNS.write(s + ",");
                        }
                        bwNS.newLine();
                    } else {
                        for (String s : split) {
                            bwNA.write(s + ",");
                        }
                        bwNA.newLine();
                    }
                } else {                                                  //VECCHIO V
                    if (split[index_fraz].toLowerCase().startsWith("2")) {//  2-semestrale   VS
                        bwVS.write(line);
                        bwVS.newLine();
                    } else {
                        bwVA.write(line);
                        bwVA.newLine();
                    }
                }
            }
            bwNA.close();
            bwVA.close();
            bwNS.close();
            bwVS.close();
            br.close();
            long end = System.currentTimeMillis();
            System.out.println("Split old/new/ann/sem: " + (end - start) / 1000 + " seconds");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void splitForNplza(String file1, String path, Integer anno) {
        long start = System.currentTimeMillis();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file1));

            String indice = br.readLine();
            String line;
            String now = "-1", past = "-2";
            int index_nplza = -1, i = 0;


            String[] split = indice.split(",", -1);
            for (String s : split) {
                if (s.toLowerCase().equals("nplza")) {
                    index_nplza = i;
                }
                i++;
            }

            line = br.readLine();
            split = line.split(",", -1);
            now = split[index_nplza];
            past = now;
            String newpath = path + now + "-" + anno + ".csv";
            BufferedWriter bw = new BufferedWriter(new FileWriter(newpath));
            bw.write(indice);
            bw.newLine();
            bw.write(line);
            bw.newLine();

            while ((line = br.readLine()) != null) {
                split = line.split(",", -1);
                now = split[index_nplza];
                if (!now.equals(past)) {
                    bw.close();
                    newpath = path + now + "-" + anno + ".csv";
                    bw = new BufferedWriter(new FileWriter(newpath));
                    bw.write(indice);
                    bw.newLine();
                    bw.write(line);
                    bw.newLine();
                    past = now;
                } else {
                    bw.write(line);
                    bw.newLine();
                    past = now;

                }
            }
            bw.close();
            br.close();
            long end = System.currentTimeMillis();
            System.out.println("Split plza: " + (end - start) / 1000 + " seconds");
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public void groupByWeek(String fileinput, String fileoutput,String fileoutput2) {
        try {
            System.out.println("Group by Week");
            BufferedReader br = new BufferedReader(new FileReader(fileinput));
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileoutput));
            long start = System.currentTimeMillis();
            int i_nplza = -1, i_sett = -1, i_events = -1, i_costs = -1, i = 0;

            String indice = br.readLine(), line;
            if (indice.endsWith(",")) indice.substring(0, indice.length() - 2);

            String[] split = indice.split(",", -1);
            String[] indicesplit = split;
            bw.write(indice);
            bw.newLine();

            for (String s : split) {
                if (s.toLowerCase().equals("nplza")) {
                    i_nplza = i;
                }
                if (s.toLowerCase().equals("settima")) {
                    i_sett = i;
                }
                if (s.toLowerCase().equals("ex_velocita_urbana_d")) {
                    i_events = i;
                }
                if (s.toLowerCase().equals("nnc")) {
                    i_costs = i;
                }
                i++;
            }
            if (i_nplza == -1 || i_sett == -1 || i_events == -1 || i_costs == -1) {
                System.out.println("Errore nel retrieve degli indici di settimana, costo, o eventi.");
                //throw new IOException();
            }
            String old_nplza = "1", now_nplza = "1", old_sett = "-1", now_sett = "-1", old_line;
            int numero_eventi = 30, numero_costi = 4;
            ArrayList<Integer> eventi = new ArrayList<>(numero_eventi);
            ArrayList<Integer> costi = new ArrayList<>(numero_costi);
            line = br.readLine();
            String[] oldsplit = line.split(",", -1);
            old_nplza = oldsplit[i_nplza];
            old_sett = oldsplit[i_sett];
            old_line = line;
            int j;

            for (i = i_events, j = 0; j < numero_eventi; j++, i++) {
                if (!oldsplit[i].isEmpty()) {
                    eventi.add(j, Integer.parseInt(oldsplit[i]));
                } else {
                    oldsplit[i] = "0";
                    eventi.add(j, 0);
                }
            }
            for (i = i_costs, j = 0; j < numero_costi; j++, i++) {
                if (!oldsplit[i].isEmpty()) {
                    costi.add(j, Integer.parseInt(oldsplit[i]));
                } else {
                    costi.add(j, 0);
                    oldsplit[i] = "0";
                }
            }
            int countEmpty = 0, countFull = 0;
            while ((line = br.readLine()) != null) {
                if (line.endsWith(",")) line.substring(0, line.length() - 2);

                split = line.split(",", -1);
                old_nplza = oldsplit[i_nplza];
                old_sett = oldsplit[i_sett];
                now_nplza = split[i_nplza];
                now_sett = split[i_sett];

                if (!now_nplza.isEmpty()) {
                    countFull++;


                    Integer sommaeventi;
                    Integer sommacosti;
                    Double sommacostiTMP;
                    Double doubletmp;


                    if (now_sett.equals(old_sett) && now_nplza.equals(old_nplza)) {


                        //System.out.println("now_plza: "+now_nplza+" old_plza: "+old_nplza+" now_sett: "+now_sett+" old_sett: "+old_sett);
                        //sono sulla stessa polizza e sulla stessa settimana

                        for (i = i_events, j = 0, sommaeventi = 0; j < numero_eventi; i++, j++) {

                            if (!split[i].isEmpty()) {
                                if (!oldsplit[i].isEmpty())
                                    sommaeventi = Integer.parseInt(split[i]) + Integer.parseInt(oldsplit[i]);
                                //System.out.println("split: "+split[i]+" oldsplit: "+oldsplit[i]+" somma: " +somma);
                                split[i] = String.valueOf(sommaeventi);
                            } else {
                                split[i] = oldsplit[i];
                            }

                        }
                        for (i = i_costs, j = 0, sommacosti = 0, sommacostiTMP = 0.0; j < numero_costi; j++, i++) {
                            if (!split[i].isEmpty()) {

                                sommacostiTMP = Double.parseDouble(split[i]); /*+Double.parseDouble(oldsplit[i]);*/
                                sommacosti = sommacostiTMP.intValue();
                                split[i] = String.valueOf(sommacosti);
                            } else {
                                split[i] = oldsplit[i];
                            }
                        }

                        oldsplit = split;
                    } else {     //sono sulla stessa polizza ma su una settimana diversa oppure ho cambiato polizza e settimana
                        //devo stampare tutto e azzerare i contatori
                        String s1 = "";
                        for (String s : oldsplit) {
                            s1 = s1 + s + ",";


                            //bw.write(s + ",");
                        }
                        s1 = s1.substring(0, s1.length() - 2);
                        bw.write(s1);
                        bw.newLine();
                        eventi.clear();
                        costi.clear();


                        for (i = i_events, j = 0; j < numero_eventi; j++, i++) {
                            if (!split[i].isEmpty()) {

                                eventi.add(j, Integer.parseInt(split[i]));
                            } else {
                                split[i] = "0";
                                eventi.add(j, 0);
                            }
                        }

                        for (i = i_costs, j = 0; j < numero_costi; j++, i++) {
                            if (!split[i].isEmpty()) {
//                            if(split[i].equals("4178.5")){
//
//                                System.out.println("---------------------------");}
                                doubletmp = Double.parseDouble(split[i]);
                                costi.add(j, doubletmp.intValue());
                            } else {
                                split[i] = "0";
                                costi.add(j, 0);
                            }
                        }
                        oldsplit = split;


                    }

                } else {
                    countEmpty++;
                }
            }
            br.close();
            bw.close();
            System.out.println("GroupByWeek terminata, inizio addMonth");
            this.addMonth(fileoutput,fileoutput2);
            File f=new File(fileoutput);
            f.delete();
            long end = System.currentTimeMillis();
            System.out.println("Fine addmonth");
            System.out.println("Group by week & addMonth: " + (end - start) / 1000 + " seconds");
            System.out.println("Count nplza vuote:" + countEmpty + " Count piene: " + countFull);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void groupByMonth(String fileinput, String fileoutput) {
        try {
            System.out.println("GroupByMonth");
            BufferedReader br = new BufferedReader(new FileReader(fileinput));
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileoutput));
            long start = System.currentTimeMillis();
            int i_nplza = -1, i_sett = -1, i_events = -1, i_costs = -1, i_mese = -1, i_metri = 0, i_accsx = 0, i = 0;

            String indice = br.readLine(), line;

            String[] split = indice.split(",", -1);
            String[] indicesplit = split;
            bw.write(indice);
            bw.newLine();
            for (String s : split) {
                if (s.toLowerCase().equals("nplza")) {
                    i_nplza = i;
                }
                if (s.toLowerCase().equals("settima")) {
                    i_sett = i;
                }
                if (s.toLowerCase().equals("ex_velocita_urbana_d")) {
                    i_events = i;
                }
                if (s.toLowerCase().equals("nnc")) {
                    i_costs = i;
                }
                if (s.toLowerCase().equals("mese")) {
                    i_mese = i;
                }
                if (s.toLowerCase().equals("numero_metri_percorsi")) {
                    i_metri = i;
                }
                if (s.toLowerCase().equals("acc_sx")) {
                    i_accsx = i;
                }
                i++;
            }

            System.out.println("Indice del mese: " + i_mese);

            if (i_nplza == -1 || i_sett == -1 || i_events == -1 || i_costs == -1 || i_mese == -1) {
                System.out.println("Errore nel retrieve degli indici di settimana, costo, o eventi o mese.");
            }
            String old_nplza = "1", now_nplza = "1", old_mese = "-1", now_mese = "-1", old_line;
            int numero_eventi = 30, numero_costi = 4, numero_metri = 6, numero_accsx = 1;
            ;
            ArrayList<Integer> eventi = new ArrayList<>(numero_eventi);
            ArrayList<Integer> costi = new ArrayList<>(numero_costi);
            ArrayList<Integer> metri = new ArrayList<>(numero_metri);
            ArrayList<Integer> accsx = new ArrayList<>(numero_accsx);
            line = br.readLine();
            String[] oldsplit = line.split(",", -1);
            old_nplza = oldsplit[i_nplza];
            old_mese = oldsplit[i_mese];
            old_line = line;
            int j;

            for (i = i_events, j = 0; j < numero_eventi; j++, i++) {
                if (!oldsplit[i].isEmpty()) {
                    eventi.add(j, Integer.parseInt(oldsplit[i]));
                } else {
                    oldsplit[i] = "0";
                    eventi.add(j, 0);
                }
            }
            for (i = i_costs, j = 0; j < numero_costi; j++, i++) {
                if (!oldsplit[i].isEmpty()) {
                    costi.add(j, Integer.parseInt(oldsplit[i]));
                } else {
                    costi.add(j, 0);
                    oldsplit[i] = "0";
                }
            }
            int countEmpty = 0, countFull = 0;
            int c = 0;

            while ((line = br.readLine()) != null) {
                c++;
//                System.out.println(c);

                split = line.split(",", -1);
                old_nplza = oldsplit[i_nplza];
                old_mese = oldsplit[i_mese];
                now_nplza = split[i_nplza];
                now_mese = split[i_mese];

                if (!now_nplza.isEmpty()) {
                    countFull++;


                    Integer sommaeventi;
                    Integer sommacosti;
                    Double sommacostiTMP;
                    Double doubletmp;


                    if (now_mese.equals(old_mese) && now_nplza.equals(old_nplza)) {


                        //System.out.println("now_plza: "+now_nplza+" old_plza: "+old_nplza+" now_sett: "+now_sett+" old_sett: "+old_sett);
                        //sono sulla stessa polizza e sullo stesso mese

                        for (i = i_events, j = 0, sommaeventi = 0; j < numero_eventi; i++, j++) {

                            if (!split[i].isEmpty()) {
                                if (!oldsplit[i].isEmpty())
                                    sommaeventi = Integer.parseInt(split[i]) + Integer.parseInt(oldsplit[i]);
                                //System.out.println("split: "+split[i]+" oldsplit: "+oldsplit[i]+" somma: " +somma);
                                split[i] = String.valueOf(sommaeventi);
                            } else {
                                split[i] = oldsplit[i];
                            }

                        }
                        for (i = i_costs, j = 0, sommacosti = 0, sommacostiTMP = 0.0; j < numero_costi; j++, i++) {
                            if (!split[i].isEmpty()) {

                                sommacostiTMP = Double.parseDouble(split[i]); /*+Double.parseDouble(oldsplit[i]);*/
                                sommacosti = sommacostiTMP.intValue();
                                split[i] = String.valueOf(sommacosti);
                            } else {
                                split[i] = oldsplit[i];
                            }
                        }
                        int sommametri;
                        for (i = i_metri, j = 0, sommametri = 0; j < numero_metri; i++, j++) {

                            if (!split[i].isEmpty()) {
                                sommametri = Integer.parseInt(split[i]) + Integer.parseInt(oldsplit[i]);
                                split[i] = String.valueOf(sommametri);
                            } else {
                                split[i] = oldsplit[i];
                            }

                        }
                        int sommaccsx;
                        for (i = i_accsx, j = 0, sommaccsx = 0; j < numero_accsx; i++, j++) {

                            if (!split[i].isEmpty()) {
                                sommaccsx = Integer.parseInt(split[i]) + Integer.parseInt(oldsplit[i]);
                                split[i] = String.valueOf(sommaccsx);
                            } else {

                                split[i] = oldsplit[i];
                            }

                        }

                        oldsplit = split;
                    } else {     //sono sulla stessa polizza ma su un mese diverso oppure ho cambiato polizza e mese
                        //devo COMUNQUE stampare tutto e azzerare i contatori

                        for (String s : oldsplit) {
                            bw.write(s + ",");
                        }
                        bw.newLine();
                        eventi.clear();
                        costi.clear();


                        for (i = i_events, j = 0; j < numero_eventi; j++, i++) {
                            if (!split[i].isEmpty()) {

                                eventi.add(j, Integer.parseInt(split[i]));
                            } else {
                                split[i] = "0";
                                eventi.add(j, 0);
                            }
                        }

                        for (i = i_costs, j = 0; j < numero_costi; j++, i++) {
                            if (!split[i].isEmpty()) {
                                doubletmp = Double.parseDouble(split[i]);
                                costi.add(j, doubletmp.intValue());
                            } else {
                                split[i] = "0";
                                costi.add(j, 0);
                            }
                        }
                        for (i = i_metri, j = 0; j < numero_metri; j++, i++) {
                            if (!split[i].isEmpty()) {

                                metri.add(j, Integer.parseInt(split[i]));
                            } else {
                                split[i] = "0";
                                metri.add(j, 0);
                            }
                        }

                        for (i = i_accsx, j = 0; j < numero_accsx; j++, i++) {
                            if (!split[i].isEmpty()) {
                                accsx.add(j, Integer.parseInt(split[i]));
                            } else {
                                split[i] = "0";
                                accsx.add(j, 0);
                            }
                        }
                        oldsplit = split;


                    }

                } else {
                    countEmpty++;
                }

            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sampleFile(String fileinput, String fileoutput, int start,int max) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileinput));
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileoutput));

            String indice = br.readLine();
            String line;

            String[] split = indice.split(",", -1);
            String[] indicesplit = split;
            bw.write(indice);
            bw.newLine();
            int c = 0,flag=0;

            while ((line = br.readLine()) != null && flag==0) {
                if(c < max && c>=start){
                bw.write(line);
                bw.newLine();}
                c++;
                if(c==max)flag=1;
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> getFieldsOnOff() {
        return fieldsOnOff;
    }

    public void setFieldsOnOff(ArrayList<Integer> fieldsOnOff) {
        this.fieldsOnOff = fieldsOnOff;
    }

    public Map<Integer, Discretization> getMap() {
        return map;
    }

    public void setMap(Map<Integer, Discretization> map) {
        this.map = map;
    }


    public void addMonth(String csvFileWeek, String csvFilePlusMonth) {
        try {
            System.out.println("addMonth");
            BufferedReader br = new BufferedReader(new FileReader(csvFileWeek));
            BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePlusMonth));

            String line = br.readLine();
            String[] split = line.split(",");
            int i_sett = 0, i_ccd = 0, i = 0;
            for (String s : split) {
                if (s.toLowerCase().equals("settima")) {
                    i_sett = i;
                }
                if (s.toLowerCase().equals("cost_caused_claim")) {
                    i_ccd = i;
                }

                i++;
            }

            for (String s : split) {
                bw.write(s + ",");
            }
            bw.newLine();

            int sett = 0, totgiorni = 0, mese = 0;
            while ((line = br.readLine()) != null) {
                split = line.split(",");

                sett = Integer.parseInt(split[i_sett]);
                totgiorni = sett * 7;
                mese = (totgiorni / 30);
                if (mese != 12) {
                    mese++;
                }
                split[i_ccd + 1] = String.valueOf(mese);
                for (String s : split) {
                    bw.write(s + ",");
                }
                bw.newLine();

            }
            br.close();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void groupByYear(String fileinput, String fileoutput) {
        try {
            System.out.println("Group by Year");
            BufferedReader br = new BufferedReader(new FileReader(fileinput));
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileoutput));

            int i_nplza = -1, i_events = -1, i_costs = -1, i_metri = -1, i_accsx = -1, i = 0;
            //int i_sett = -1;

            String indice = br.readLine(), line;       //leggo la tupla per selezionare gli indici corrispondenti ad ogni attributo
            if (indice.endsWith(",")) indice.substring(0, indice.length() - 2);

            String[] split = indice.split(",", -1);
            String[] indicesplit = split;
            bw.write(indice);
            bw.newLine();
            //fino ad ora ho riscritto un file con le intestazioni del file origine selezionate
            //ricavo gli indici degli attributi elencati di seguito, cioè le posizioni degli attributi nel file
            for (String s : split) {
                if (s.toLowerCase().equals("nplza")) {
                    i_nplza = i;
                }
                if (s.toLowerCase().equals("ex_velocita_urbana_d")) {
                    i_events = i;
                }
                if (s.toLowerCase().equals("nnc")) {
                    i_costs = i;
                }
                if (s.toLowerCase().equals("numero_metri_percorsi")) {
                    i_metri = i;
                }
                if (s.toLowerCase().equals("acc_sx")) {
                    i_accsx = i;
                }
                i++;
            }


            if (i_nplza == -1 || i_events == -1 || i_costs == -1 || i_metri == -1 || i_accsx == -1) {
                System.out.println("Errore nel retrieve degli indici eventi,costi, metri o accsx.");
                //throw new IOException();
            }
            String old_nplza = "1", now_nplza = "1";
            int numero_eventi = 30, numero_costi = 4, numero_metri = 6, numero_accsx = 1;
            ArrayList<Integer> eventi = new ArrayList<>(numero_eventi);
            ArrayList<Integer> costi = new ArrayList<>(numero_costi);
            ArrayList<Integer> metri = new ArrayList<>(numero_metri);
            ArrayList<Integer> accsx = new ArrayList<>(numero_accsx);

            line = br.readLine(); //leggo la prima riga dopo le intestazioni(seconda riga del file)
            String[] oldsplit = line.split(",", -1);
            old_nplza = oldsplit[i_nplza];
            int j;

            //inizializzo gli array
            for (i = i_events, j = 0; j < numero_eventi; j++, i++) {
                if (!oldsplit[i].isEmpty()) {
                    eventi.add(j, Integer.parseInt(oldsplit[i]));
                } else {
                    eventi.add(j, 0);
                    oldsplit[i] = "0";
                }
            }
            for (i = i_costs, j = 0; j < numero_costi; j++, i++) {
                if (!oldsplit[i].isEmpty()) {
                    costi.add(j, Integer.parseInt(oldsplit[i]));
                } else {
                    costi.add(j, 0);
                    oldsplit[i] = "0";
                }
            }
            for (i = i_metri, j = 0; j < numero_metri; j++, i++) {
                if (!oldsplit[i].isEmpty()) {
                    metri.add(j, Integer.parseInt(oldsplit[i]));
                } else {
                    metri.add(j, 0);
                    oldsplit[i] = "0";
                }
            }
            for (i = i_accsx, j = 0; j < numero_accsx; j++, i++) {
                if (!oldsplit[i].isEmpty()) {
                    accsx.add(j, Integer.parseInt(oldsplit[i]));
                } else {
                    accsx.add(j, 0);
                    oldsplit[i] = "0";
                }
            }


            int countEmpty = 0, countFull = 0;


            while ((line = br.readLine()) != null) { //sono nella seconda riga dopo le intestazioni
                split = line.split(",", -1);
                old_nplza = oldsplit[i_nplza];
                now_nplza = split[i_nplza];

                if (!now_nplza.isEmpty()) {
                    countFull++;

                    Integer sommaeventi;
                    Integer sommametri;
                    Integer sommacosti;
                    Integer sommaccsx;
                    Double sommacostiTMP;
                    Double doubletmp;


                    //sono sempre sulla stessa polizza
                    if (now_nplza.equals(old_nplza)) {

                        for (i = i_events, j = 0, sommaeventi = 0; j < numero_eventi; i++, j++) {

                            if (!split[i].isEmpty()) {

                                sommaeventi = Integer.parseInt(split[i]) + Integer.parseInt(oldsplit[i]);
                                split[i] = String.valueOf(sommaeventi);
                            } else {
                                split[i] = oldsplit[i];   //metto nella riga nuova il valore di prima
                            }

                        }
                        for (i = i_costs, j = 0, sommacosti = 0, sommacostiTMP = 0.0; j < numero_costi; j++, i++) {
                            if (!split[i].isEmpty()) {
                                sommacostiTMP = Double.parseDouble(split[i]); /*+Double.parseDouble(oldsplit[i]);*/
                                sommacosti = sommacostiTMP.intValue();
                                split[i] = String.valueOf(sommacosti);
                            } else {
                                split[i] = oldsplit[i];
                            }
                        }
                        for (i = i_metri, j = 0, sommametri = 0; j < numero_metri; i++, j++) {

                            if (!split[i].isEmpty()) {
                                sommametri = Integer.parseInt(split[i]) + Integer.parseInt(oldsplit[i]);
                                split[i] = String.valueOf(sommametri);
                            } else {
                                split[i] = oldsplit[i];
                            }

                        }
                        for (i = i_accsx, j = 0, sommaccsx = 0; j < numero_accsx; i++, j++) {

                            if (!split[i].isEmpty()) {
                                sommaccsx = Integer.parseInt(split[i]) + Integer.parseInt(oldsplit[i]);
                                split[i] = String.valueOf(sommaccsx);
                            } else {

                                split[i] = oldsplit[i];
                            }

                        }
                        oldsplit = split; //in questo modo, nel ciclo successivo avrò in oldspit, la riga precedente
                    }
                    //ho cambiato polizza
                    else {
                        //devo stampare tutto e azzerare i contatori
                        String s1 = "";
                        for (String s : oldsplit) {
                            s1 = s1 + s + ",";
                        }
                        s1 = s1.substring(0, s1.length() - 2);
                        bw.write(s1);
                        bw.newLine();
                        eventi.clear();
                        costi.clear();
                        metri.clear();
                        accsx.clear();


                        for (i = i_events, j = 0; j < numero_eventi; j++, i++) {
                            if (!split[i].isEmpty()) {

                                eventi.add(j, Integer.parseInt(split[i]));
                            } else {
                                split[i] = "0";
                                eventi.add(j, 0);
                            }
                        }

                        for (i = i_costs, j = 0; j < numero_costi; j++, i++) {
                            if (!split[i].isEmpty()) {
                                doubletmp = Double.parseDouble(split[i]);
                                costi.add(j, doubletmp.intValue());
                            } else {
                                split[i] = "0";
                                costi.add(j, 0);
                            }
                        }

                        for (i = i_metri, j = 0; j < numero_metri; j++, i++) {
                            if (!split[i].isEmpty()) {

                                metri.add(j, Integer.parseInt(split[i]));
                            } else {
                                split[i] = "0";
                                metri.add(j, 0);
                            }
                        }

                        for (i = i_accsx, j = 0; j < numero_accsx; j++, i++) {
                            if (!split[i].isEmpty()) {

                                accsx.add(j, Integer.parseInt(split[i]));
                            } else {
                                split[i] = "0";
                                accsx.add(j, 0);
                            }
                        }

                        oldsplit = split;

                    }
                } else
                    countEmpty++;
            }
            bw.close();
            System.out.println("Count nplza vuote:" + countEmpty + " Count piene: " + countFull);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void propagateAccSx2015(String file1, String file2) {
        System.out.println("Inzio ACCSX");
        Map<String, ArrayList<String>> map = new HashMap<>();
        long start = System.currentTimeMillis();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file1));
            BufferedWriter bw = new BufferedWriter(new FileWriter(file2));

            String indice = br.readLine();
            String[] splitindice = indice.split(",", -1);
            int index_accsx = -1, i = 0, index_nplza = -1, index_settimana = -1;

            for (String s : splitindice) {
                if (s.toLowerCase().equals("nplza")) {
                    index_nplza = i;
                }
                if (s.toLowerCase().equals("settima")) {
                    index_settimana = i;
                }
                if (s.toLowerCase().equals("acc_sx")) {
                    index_accsx = i;
                }
                i++;
            }

            if (index_accsx == -1 || index_nplza == -1 || index_settimana == -1) {
                System.out.println("ERRORE! INDEX MANCANTE");
                throw new Exception("ERRORE di indice");
            }
//            bw.write(line1);
//            bw.newLine();
            String line1 = "";
            String[] split1;
            String[] split;
            System.out.println("Inizio la lettura incidenti");
            while ((line1 = br.readLine()) != null) {
                split = line1.split(",", -1);

                if (!split[index_accsx].isEmpty() && !split[index_accsx].equals("0")) {                                          //se la riga ha l'attributo acc_sx non nullo

                    if (map.containsKey(split[index_nplza])) {                                //se la mappa contiene gia quel nplza (piu di un incidente in un anno)
                        map.get(split[index_nplza]).add(split[index_settimana]);               //aggiungo la nuova settimana alla lista
                    } else {
                        ArrayList<String> list = new ArrayList<>();                           //sennò creo una nuova lista e la aggiungo in corrispondenza della nuova chiave
                        list.add(split[index_settimana]);
                        map.put(split[index_nplza], list);
                    }
                }

            }
            br.close();
            System.out.println("Fine Lettura, inizio aggiornamento accsx");
            br = new BufferedReader(new FileReader(file1));
            int mese = 0, totgiorni = 0, sett = 0;
            indice = br.readLine();
            splitindice = indice.split(",");

            bw.write(indice);
            bw.newLine();

            while ((line1 = br.readLine()) != null) {
                split1 = line1.split(",", -1);

                if (map.containsKey(split1[index_nplza])) {                                       //se la polizza è presente nella map degli incidenti
                    ArrayList<String> list2 = new ArrayList<>();                                  //scarico la lista delle settimane di incidente
                    list2 = map.get(split1[index_nplza]);
                    for (String s : list2) {                                                       //se quella riga fa parte di una settimana incidente, metto a 1
                        if (split1[index_settimana].equals(s)) split1[index_accsx] = "1";
                    }

                }
                // sett = Integer.parseInt(split1[index_settimana]);

                for (String s : split1) {

                    if (s.isEmpty()) s = "0";

                    bw.write(s + ",");


                }

                //bw.write(String.valueOf(mese) + ",");
                bw.newLine();
            }
            bw.close();
            long end = System.currentTimeMillis();
            System.out.println("Propagate ACCSX: " + (end - start) / 1000 + " seconds");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int countRows(String file1) {
        int c = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file1));
            String line;
            c = 0;
            while ((line = br.readLine()) != null) {
                c++;
            }
            br.close();
            System.out.println("Numero righe file :" + c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return c;
    }
}
