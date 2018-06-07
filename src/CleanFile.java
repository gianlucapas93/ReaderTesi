import java.io.*;
import java.util.ArrayList;

public class CleanFile {
    String csvFile;
    ArrayList<Integer> fieldsOnOff;
    String csvFileCleaned;

    public CleanFile(String csvFile, ArrayList<Integer> fieldsOnOff,String csvFileCleaned) {
        this.csvFile = csvFile;
        this.fieldsOnOff = fieldsOnOff;
        this.csvFileCleaned=csvFileCleaned;
    }

    public void clean(){
        String line="";

        try{

            BufferedReader br= new BufferedReader(new FileReader(csvFile));
            FileWriter fw=new FileWriter(csvFileCleaned);
            BufferedWriter bw = new BufferedWriter(fw);

            int y=0;
            long start = System.currentTimeMillis();

            while((line=br.readLine())!=null){
                String[] field=line.split(",");

                for(Integer i=0; i<fieldsOnOff.size() && i<field.length; i++){
                    if(fieldsOnOff.get(i)!=0) bw.write(field[i]+",");
                }

                bw.write("\n");
                y++;

                if(y%1000000==0) {


                    System.out.println("Righe completate: "+y/1000000+"M");}
            }
            fw.close();
            long end = System.currentTimeMillis();
            System.out.println((end - start) / 1000+ " seconds");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
