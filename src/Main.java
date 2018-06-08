import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //CAMPIONE
        String csvFileCamp = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\itarea_compl2016_telematics_sent_016.csv";
        String csvFileCampCLR = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\itarea_compl2016_telematics_sent_016_clr.csv";
        String csvFileCampDisc = "C:\\Users\\Gianluca\\Desktop\\Tesi\\ReaderData\\itarea_compl2016_telematics_sent_016_clr_disc.csv";
        //FILE COMPLETO
        String csvFile = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent.csv";
        String csvFileCleaned = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent_clr.csv";
        String csvFileCleanedDiscretized = "C:\\Users\\Gianluca\\Desktop\\Tesi\\itarea_compl2016_telematics_sent_clr_disc.csv";


        ArrayList<Integer> fieldsOnOff;
        Map<Integer,Discretization> map =new HashMap<>();


        OpenFileAndCreate op=new OpenFileAndCreate();
        //op.initialize();
        //op.createNewOnOff();
        //op.initializeDiscretization();


        ReadSettings rs=new ReadSettings();
        fieldsOnOff=rs.readOnOff();
        map=rs.getMap();


//        CleanFile cf=new CleanFile(csvFileCamp,fieldsOnOff,csvFileCampCLR);
//        cf.clean();
//
//        //TODO: La mappa continua a prendere come indice il 6. Controllare nella transformation e nel doDiscretization

        Transformation tr=new Transformation(csvFileCampCLR,csvFileCampDisc,fieldsOnOff,map);
        tr.doDiscretization();
//
//
//        System.out.println(fieldsOnOff);
//        System.out.println(map);






    }
}
