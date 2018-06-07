import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //CAMPIONE
        String csvFileCamp = "C:\\Users\\Gianluca\\Desktop\\Politecnico\\itarea_compl2016_telematics_sent_016.csv";
        String csvFileCampDisc = "C:\\Users\\Gianluca\\Desktop\\Politecnico\\itarea_compl2016_telematics_sent_016_disc.csv";
        //FILE COMPLETO
        String csvFile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent.csv";
        String csvFileCleaned = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent_clr2.csv";

        ArrayList<Integer> fieldsOnOff;
        Map<Integer,Discretization> map =new HashMap<>();


        OpenFileAndCreate op=new OpenFileAndCreate();
        //op.initialize();
        //op.initializeDiscretization();
        //op.sortAndLower();

        ReadSettings rs=new ReadSettings();
        fieldsOnOff=rs.readOnOff();
        map=rs.getMap();
        Transformation tr=new Transformation(csvFileCamp,csvFileCampDisc,fieldsOnOff,map);
        tr.doDiscretization(csvFileCamp,csvFileCampDisc);


        System.out.println(fieldsOnOff);
        System.out.println(map);


//        CleanFile cf=new CleanFile(csvFile,fieldsOnOff,csvFileCleaned);
//        cf.clean();




    }
}
