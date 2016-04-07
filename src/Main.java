import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

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

        System.out.println( data );

//        try
//        {
//            runCommand( "cd /Volumes/MACData/1ENGLISH; pwd" );
//        } catch (Exception t)
//        {
//            t.printStackTrace();
//        }

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
            in = new FileInputStream( "config.json" );
            out = new FileOutputStream( "output.txt" );


            int c;
            while ( (c = in.read()) != -1 )
            {
                out.write( c );
            }
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
