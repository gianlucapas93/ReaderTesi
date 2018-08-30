import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Check {
    public void checkAccsxAndCost(String file1,String csvsplit){
        try {
            BufferedReader br=new BufferedReader(new FileReader(file1));
            String line,indice;
            String[] split2;
            int c=0,i_acc=-1,i_cost=-1,ok=0,not=0;

            indice=br.readLine().replaceAll("\"","");
            String [] split1=indice.split(csvsplit,-1);
            for(String s: split1){
                if(s.toLowerCase().equals("acc_sx")){
                    i_acc=c;
                }
                if(s.toLowerCase().contains("cost_caused")){
                    i_cost=c;
                }
                c++;
            }
            if(i_acc==-1 || i_cost==-1){
                throw new IOException("Indici non trovati");
            }

            while((line=br.readLine())!=null){
                split2=line.split(csvsplit,-1);
                if(split2[i_acc]!=null && !split2[i_acc].equals("0")){
                    if(split2[i_cost]!=null && !split2[i_cost].equals("0")){
                        ok++;
                    }else{
                        not++;
                    }
                }
            }

            System.out.println("Fine analisi Accsx e Cost: ok "+ok+" not "+not);

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }
}
