import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Scanner;

public class Main
{
    class Data
    {
        private String path;

        public String getPath()
        {
            return path;
        }

        public void setPath( String path )
        {
            this.path = path;
        }

        @Override public String toString()
        {
            return "Data{" +
                "path='" + path + '\'' +
                '}';
        }
    }

    public static void main( String[] args ) throws Exception
    {
        Data data = new Gson().fromJson( new InputStreamReader( new FileInputStream( "config.json" ) ), Data.class );
        String command = "cd ";
        command+=data.getPath();
        command+="; pwd; bzr up -r 3";
        try
        {
            runCommand( command );
        } catch (Exception t)
        {
            t.printStackTrace();
        }

//        readWriteFile();

    }

    public static void runCommand( String command ) throws Exception
    {
        System.out.println( "Running command: " + command );

        ProcessBuilder processBuilder = new ProcessBuilder(
            "bash",
            "-c",
            command );
        processBuilder.redirectErrorStream( true );
        Process proc = processBuilder.start();
        BufferedReader stdInput = new BufferedReader( new
            InputStreamReader( proc.getInputStream() ) );

        BufferedReader stdError = new BufferedReader( new
            InputStreamReader( proc.getErrorStream() ) );

        String s = null;
        while ( (s = stdInput.readLine()) != null )
        {
            System.out.println( s );
        }

        while ( (s = stdError.readLine()) != null )
        {
            System.out.println( s );
        }

        int exitVal = proc.waitFor();
        System.out.println( "Process exitValue: " + exitVal );
    }

    public static void readWriteFile() throws Exception
    {
        FileInputStream in = null;
        FileOutputStream out = null;

        try
        {
            in = new FileInputStream( "bzr-2_20" );
            out = new FileOutputStream( "output.txt" );

            Scanner scanner = new Scanner( in );


            BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( out ) );
            bw.write( "lsakdf" );
            bw.newLine();
            bw.write( "laskdfj" );

            bw.close();
        }
        finally
        {
            if ( in != null )
            {
                in.close();
            }
            if ( out != null )
            {
                out.close();
            }
        }
    }
}
