import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadSettings {

    String chooseFields = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\chooseFields.txt";
    String chooseFieldsCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\chooseFieldsCLR.txt";
    String discretizationFile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\discretization.txt";
    String discretizationFileCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\discretizationCLR.txt";



    String line="",line2="";
    String csvsplitby=",";
    int i=0;
    ArrayList<Integer> arrayList=new ArrayList<>();
    Map<Integer,Discretization> map =new HashMap<>();
    BufferedReader br,br2;

    public ArrayList<Integer> readOnOff(){

        try{
            Integer j=0;
            br= new BufferedReader(new FileReader(chooseFieldsCLR));
            br2= new BufferedReader(new FileReader(discretizationFileCLR));

            while((line=br.readLine())!=null && (line2=br2.readLine())!=null){

                String[] splitline=line.split(" ");
                if(splitline[1].equals("1")){
                    String[] splitline2= line2.split(";");

                    if(splitline2[1].equals("1")){
                        Integer n=Integer.parseInt(splitline2[2]);

                        String[] intervals =splitline2[3].split(",");
                        Integer[] valueOfIntervals=new Integer[n];
                        Integer i =0;
                        for(String s: intervals){

                            valueOfIntervals[i++]=Integer.parseInt(s);
                        }
                        String[] nameIntervals=splitline2[4].split(",");
                        Discretization d=new Discretization(true,n,valueOfIntervals,nameIntervals);
                        map.put(j,d);
                    }
                    arrayList.add(j,Integer.parseInt(splitline[1]));
                    j++;

                }

            }}
        catch(IOException e){
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
}
