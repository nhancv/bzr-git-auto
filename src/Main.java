import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception{

        FileInputStream in = null;
        FileOutputStream out = null;

        try {
            in = new FileInputStream("config.json");
            out = new FileOutputStream("output.txt");


            int c;
            while ((c = in.read()) != -1) {
                out.write(c);
            }
        }finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }

//        try
//        {
//            runCommand( "cd /Volumes/MACData/1ENGLISH; pwd" );
//        } catch (Exception t)
//        {
//            t.printStackTrace();
//        }

    }
    public static void runCommand(String command) throws Exception{
        System.out.println("Running command: " + command);

        ProcessBuilder processBuilder = new ProcessBuilder(
            "bash",
            "-c",
            command);
        processBuilder.redirectErrorStream(true);
        Process proc = processBuilder.start();
        BufferedReader stdInput = new BufferedReader(new
            InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
            InputStreamReader(proc.getErrorStream()));

        String s = null;
        while ((s = stdInput.readLine()) != null) {
            System.out.println(s);
        }

        while ((s = stdError.readLine()) != null) {
            System.out.println(s);
        }

        int exitVal = proc.waitFor();
        System.out.println("Process exitValue: " + exitVal);
    }

}
