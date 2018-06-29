import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Transformation {
    String csvFile;
    String discretizedFile;
    ArrayList<Integer> fieldsOnOff;
    Map<Integer,Discretization> map;

    public Transformation(String csvFile, String discretizedFile, ArrayList<Integer> fieldsOnOff, Map<Integer, Discretization> map) {
        this.csvFile = csvFile;
        this.discretizedFile = discretizedFile;
        this.fieldsOnOff = fieldsOnOff;
        this.map = map;
    }

    public void doDiscretization(String csvFile, String discretizedFile){

        try {
            BufferedReader br=new BufferedReader(new FileReader(csvFile));
            BufferedWriter bw=new BufferedWriter(new FileWriter(discretizedFile));
            String labelDiscrete="";

            String line=br.readLine();
            bw.write(line);
            bw.newLine();
            while((line=br.readLine())!=null){
                String[] splitted=line.split(",");
                for(Map.Entry<Integer,Discretization> entry :map.entrySet()){
                    Integer val;
                    if(splitted[entry.getKey()].isEmpty())val=0;
                    else val=Integer.parseInt(splitted[entry.getKey()]);

                    Discretization d=entry.getValue();
                    String[] label=d.getName_interval();
                    Integer[] intervals=d.getInterval();

                    //if(label.length!=(intervals.length+1)) throw new Exception("Vettore delle label e degli intervalli non coerenti");

                    int start=0,flag=-1,i=0;


                    for(i=1; i<intervals.length && flag==-1; i++){
                        if(val>=start && val<intervals[i]) flag=i-1;
                        else start=intervals[i];
                    }
                    if(flag==-1)labelDiscrete=label[label.length-1];
                    else{labelDiscrete=label[flag];}

                    splitted[entry.getKey()]=labelDiscrete;
                    System.out.println("Valore "+val +" disc:"+labelDiscrete);

                }

                for(String s:splitted){
                    bw.write(s+",");
                }
                bw.newLine();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

    }
    void propagateAccSx(String file1, String path){
        String file2=path+"_acc_sx.csv";
        Map<String,ArrayList<String>> map=new HashMap<>();
        long start = System.currentTimeMillis();
        try {
            BufferedReader br= new BufferedReader(new FileReader(file1));
            BufferedWriter bw = new BufferedWriter(new FileWriter(file2));

            String line1=br.readLine();
            String [] split1=line1.split(",",-1);
            int index_accsx=-1,i=0,index_nplza=-1,index_settimana=-1;

            for(String s: split1){
                if(s.toLowerCase().equals("nplza")){
                    index_nplza=i;
                }
                if(s.toLowerCase().equals("settima")){
                    index_settimana=i;
                }
                if(s.toLowerCase().equals("acc_sx")){
                    index_accsx=i;
                }
                i++;
            }

            if (index_accsx==-1 || index_nplza==-1 || index_settimana==-1) {
                System.out.println("ERRORE! INDEX MANCANTE");
                throw new Exception("ERRORE di indice");}
//            bw.write(line1);
//            bw.newLine();
            while((line1=br.readLine())!=null){
                String [] split=line1.split(",",-1);
                if(!split[index_accsx].isEmpty()){                                          //se la riga ha l'attributo acc_sx non nullo

                    if(map.containsKey(split[index_nplza])){                                //se la mappa contiene gia quel nplza (piu di un incidente in un anno)
                        map.get(split[index_nplza]).add(split[index_settimana]);               //aggiungo la nuova settimana alla lista
                    }
                    else{
                        ArrayList<String> list=new ArrayList<>();                           //sennò creo una nuova lista e la aggiungo in corrispondenza della nuova chiave
                        list.add(split[index_settimana]);
                        map.put(split[index_nplza],list);
                    }
                }

            }
            br.close();
            br=new BufferedReader(new FileReader(file1));

            line1=br.readLine();
            bw.write(line1);
            bw.newLine();
            while((line1=br.readLine())!=null){
                split1=line1.split(",",-1);

                if(map.containsKey(split1[index_nplza])){                                       //se la polizza è presente nella map degli incidenti
                    ArrayList<String> list2=new ArrayList<>();                                  //scarico la lista delle settimane di incidente
                    list2=map.get(split1[index_nplza]);
                    for(String s: list2){                                                       //se quella riga fa parte di una settimana incidente, metto a 1
                        if(split1[index_settimana].equals(s))split1[index_accsx]="1";
                    }

                }
                for(String s: split1){
                    bw.write(s+",");
                }
                bw.newLine();
            }
            bw.close();
            long end = System.currentTimeMillis();
            System.out.println("Propagate ACCSX: "+(end - start) / 1000+ " seconds");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCsvFile() {
        return csvFile;
    }

    public void setCsvFile(String csvFile) {
        this.csvFile = csvFile;
    }

    public String getDiscretizedFile() {
        return discretizedFile;
    }

    public void setDiscretizedFile(String discretizedFile) {
        this.discretizedFile = discretizedFile;
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

    public void splitOldNewAnnSem(String csvFileCampDiscAfterACCSX, String VApath, String NApath, String VSpath, String NSpath) {
        long start = System.currentTimeMillis();
        try {
            BufferedReader br= new BufferedReader(new FileReader(csvFileCampDiscAfterACCSX));
            String VA=VApath+"\\2016-VA.csv";
            String VS=VSpath+"\\2016-VS.csv";
            String NA=NApath+"\\2016-NA.csv";
            String NS=NSpath+"\\2016-NS.csv";
            BufferedWriter bwVA = new BufferedWriter(new FileWriter(VA));
            BufferedWriter bwNA = new BufferedWriter(new FileWriter(NA));
            BufferedWriter bwVS = new BufferedWriter(new FileWriter(VS));
            BufferedWriter bwNS = new BufferedWriter(new FileWriter(NS));

            String line=br.readLine();

            bwNA.write(line);
            bwVA.write(line);
            bwNS.write(line);
            bwVS.write(line);
            bwNA.newLine();
            bwVA.newLine();
            bwNS.newLine();
            bwVS.newLine();
            String [] split=line.split(",",-1);
            int index_affarenuovo=-1,index_fraz=-2,i=0;

            for(String s: split){
                if(s.toLowerCase().equals("affare_nuovo"))index_affarenuovo=i;
                if(s.toLowerCase().equals("fraz"))index_fraz=i;
                i++;
            }
            line="";

            while ((line=br.readLine())!=null){
                split=line.split(",",-1);
                if(split[index_affarenuovo].toLowerCase().startsWith("s")) { //nuovo N
                    split[index_affarenuovo]="Si";
                    if(split[index_fraz].toLowerCase().startsWith("2")){//  2-semestrale   NS

                        for(String s:split){
                        bwNS.write(s+",");
                        }
                        bwNS.newLine();
                    }else{
                        for(String s:split){
                        bwNA.write(s+",");
                        }
                        bwNA.newLine();
                    }
                }

                else {                                                  //VECCHIO V
                    if(split[index_fraz].toLowerCase().startsWith("2")){//  2-semestrale   VS
                        bwVS.write(line);
                        bwVS.newLine();
                    }else{
                        bwVA.write(line);
                        bwVA.newLine();
                    }}
            }
            bwNA.close();
            bwVA.close();
            bwNS.close();
            bwVS.close();
            br.close();
            long end = System.currentTimeMillis();
            System.out.println("Split old/new/ann/sem: "+(end - start) / 1000+ " seconds");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void splitForNplza(String file1, String path) {
        long start = System.currentTimeMillis();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file1));

            String indice=br.readLine();
            String line;
            String now="-1",past="-2";
            int index_nplza=-1,i=0;


            String [] split=indice.split(",",-1);
            for(String s: split){
                if(s.toLowerCase().equals("nplza")){
                    index_nplza=i;
                }
                i++;}

            line=br.readLine();
            split=line.split(",",-1);
            now=split[index_nplza];
            past=now;
            String newpath=path+now+"-2016.csv";
            BufferedWriter bw= new BufferedWriter(new FileWriter(newpath));
            bw.write(indice);
            bw.newLine();
            bw.write(line);
            bw.newLine();

            while((line=br.readLine())!=null){
                split=line.split(",",-1);
                now=split[index_nplza];
                if(!now.equals(past)){
                    bw.close();
                    newpath=path+now+"-2016.csv";
                    bw=new BufferedWriter(new FileWriter(newpath));
                    bw.write(indice);
                    bw.newLine();
                    bw.write(line);
                    bw.newLine();
                    past=now;
                }else{
                    bw.write(line);
                    bw.newLine();
                    past=now;

                }
            }
            bw.close();
            br.close();
            long end = System.currentTimeMillis();
            System.out.println("Split plza: "+(end - start) / 1000+ " seconds");
        } catch (IOException e) {

            e.printStackTrace();
        }

    }

    public void groupByWeek(String fileinput, String fileoutput) {
        try {
            BufferedReader br= new BufferedReader(new FileReader(fileinput));
            BufferedWriter bw=new BufferedWriter(new FileWriter(fileoutput));

            int i_nplza=-1,i_sett=-1,i_events=-1,i_costs=-1,i=0;

            String indice=br.readLine(),line;

            String [] split=indice.split(",",-1);
            String[] indicesplit=split;
            bw.write(indice);
            bw.newLine();

            for (String s: split){
                if(s.toLowerCase().equals("nplza")){
                    i_nplza=i;
                }
                if(s.toLowerCase().equals("settima")){
                    i_sett=i;
                }
                if(s.toLowerCase().equals("ex_velocita_urbana_d")){
                    i_events=i;
                }
                if(s.toLowerCase().equals("nnc")){
                    i_costs=i;
                }
                i++;
            }
            if(i_nplza==-1||i_sett==-1||i_events==-1||i_costs==-1){
                System.out.println("Errore nel retrieve degli indici di settimana, costo, o eventi.");
                //throw new IOException();
            }
            String old_nplza="1",now_nplza="1",old_sett="-1",now_sett="-1",old_line;
            int numero_eventi=30,numero_costi=5;
            ArrayList<Integer> eventi=new ArrayList<>(numero_eventi);
            ArrayList<Double> costi=new ArrayList<>(numero_costi);
            line=br.readLine();
            String[] oldsplit=line.split(",",-1);
            old_nplza=oldsplit[i_nplza];
            old_sett=oldsplit[i_sett];
            old_line=line;
            int j;

            for(i=i_events,j=0; j<numero_eventi;j++,i++){
                if(!oldsplit[i].isEmpty()){
                    eventi.add(j,Integer.parseInt(oldsplit[i]));
                }else eventi.add(j,0);
            }
            for(i=i_costs,j=0; j<numero_costi; j++,i++){
                if(!oldsplit[i].isEmpty()){
                    costi.add(j,Double.parseDouble(oldsplit[i]));}
                else costi.add(j,0.0);
            }
            int countEmpty=0,countFull=0;
            while ((line=br.readLine())!=null){
                split=line.split(",",-1);
                old_nplza=oldsplit[i_nplza];
                old_sett=oldsplit[i_sett];
                now_nplza=split[i_nplza];
                now_sett=split[i_sett];

                if(now_nplza.isEmpty())countEmpty++;
                else countFull++;

                Integer sommaeventi;
                Double sommacosti;

//                if(old_sett.equals("43")){
//                    System.out.println("Mannaggia la marina");
//                }

                if(now_sett.equals(old_sett) && now_nplza.equals(old_nplza)){


                    //System.out.println("now_plza: "+now_nplza+" old_plza: "+old_nplza+" now_sett: "+now_sett+" old_sett: "+old_sett);
                    //sono sulla stessa polizza e sulla stessa settimana

                    for(i=i_events,j=0,sommaeventi=0; j<numero_eventi;i++, j++){

                        if(!split[i].isEmpty()){
                            if(oldsplit[i].isEmpty())
                            sommaeventi=Integer.parseInt(split[i])+Integer.parseInt(oldsplit[i]);
                            //System.out.println("split: "+split[i]+" oldsplit: "+oldsplit[i]+" somma: " +somma);
                            split[i]=String.valueOf(sommaeventi);
                        }
                        else{
                            split[i]=oldsplit[i];
                        }

                    }
                    for(i=i_costs,j=0,sommacosti=0.0;j<numero_costi;j++,i++){
                        if(!split[i].isEmpty()){
                            sommacosti=Double.parseDouble(split[i])+Double.parseDouble(oldsplit[i]);
                            split[i]=String.valueOf(sommacosti);
                        }
                        else{
                            split[i]=oldsplit[i];
                        }
                    }

                    oldsplit=split;
                }
                else{     //sono sulla stessa polizza ma su una settimana diversa oppure ho cambiato polizza e settimana
                    //devo stampare tutto e azzerare i contatori

                    for(String s: oldsplit){
                        bw.write(s+",");
                    }
                    bw.newLine();
                    eventi.clear();
                    costi.clear();


                    for(i=i_events,j=0; j<numero_eventi;j++,i++){
                        if(!split[i].isEmpty()){

                            eventi.add(j,Integer.parseInt(split[i]));
                        }else {
                            split[i]="0";
                            eventi.add(j,0);}
                    }

                    for(i=i_costs,j=0; j<numero_costi; j++,i++){
                        if(!split[i].isEmpty()){
                            costi.add(j,Double.parseDouble(split[i]));}
                        else {
                            split[i]="0";
                            costi.add(j,0.0);}
                    }
                    oldsplit=split;




                }

            }
            bw.close();
            System.out.println("Count nplza vuote:"+countEmpty+" Count piene: "+countFull);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
