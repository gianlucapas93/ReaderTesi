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
            while((line=br.readLine())!=null){
                String[] splitted=line.split(",");
                for(Map.Entry<Integer,Discretization> entry :map.entrySet()){
                    Integer val=Integer.parseInt(splitted[entry.getKey()]);
                    Discretization d=entry.getValue();
                    String[] label=d.getName_interval();
                    Integer[] intervals=d.getInterval();

                    int start=0,flag=0;
                    for(Integer i=0; i<intervals.length-1 && flag==0; i++){
                        if(val>=start && val<intervals[i]) flag=i;
                        else start=intervals[i];
                    }
                    if(flag==0)labelDiscrete=label[label.length-1];
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
}
