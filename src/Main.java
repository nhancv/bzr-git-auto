import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.List;

public class Main
{
    class Data
    {
        private String title;
        private Long id;
        private Boolean children;
        private List<Data> groups;

        public String getTitle()
        {
            return title;
        }

        public Long getId()
        {
            return id;
        }

        public Boolean getChildren()
        {
            return children;
        }

        public List<Data> getGroups()
        {
            return groups;
        }

        public void setTitle( String title )
        {
            this.title = title;
        }

        public void setId( Long id )
        {
            this.id = id;
        }

        public void setChildren( Boolean children )
        {
            this.children = children;
        }

        public void setGroups( List<Data> groups )
        {
            this.groups = groups;
        }

        public String toString()
        {
            return String.format( "title:%s,id:%d,children:%s,groups:%s", title, id, children, groups );
        }
    }

    public static void main( String[] args ) throws Exception
    {

        String json =
            "{"
                + "'title': 'Computing and Information systems',"
                + "'id' : 1,"
                + "'children' : 'true',"
                + "'groups' : [{"
                + "'title' : 'Level one CIS',"
                + "'id' : 2,"
                + "'children' : 'true',"
                + "'groups' : [{"
                + "'title' : 'Intro To Computing and Internet',"
                + "'id' : 3,"
                + "'children': 'false',"
                + "'groups':[]"
                + "}]"
                + "}]"
                + "}";
//        Data data = new Gson().fromJson( new InputStreamReader( new FileInputStream( "config.json" ) ), Data.class );
        Data data = new Gson().fromJson( json, Data.class );

        // Show it.
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
