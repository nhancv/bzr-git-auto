import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args){
        try
        {
            runCommand( "cd /Volumes/MACData/1ENGLISH; pwd" );
        } catch (Exception t)
        {
            t.printStackTrace();
        }

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
