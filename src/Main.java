import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main
{
    static class BzrItem
    {
        private String revno;
        private String committer;
        private String branch_nick;
        private String timestamp;
        private String message;

        public String getRevno()
        {
            return revno;
        }

        public void setRevno( String revno )
        {
            this.revno = revno;
        }

        public String getCommitter()
        {
            return committer;
        }

        public void setCommitter( String committer )
        {
            this.committer = committer;
        }

        public String getBranch_nick()
        {
            return branch_nick;
        }

        public void setBranch_nick( String branch_nick )
        {
            this.branch_nick = branch_nick;
        }

        public String getTimestamp()
        {
            return timestamp;
        }

        public void setTimestamp( String timestamp )
        {
            this.timestamp = timestamp;
        }

        public String getMessage()
        {
            return message;
        }

        public void setMessage( String message )
        {
            this.message = message;
        }

        @Override public String toString()
        {
            return "BzrItem{" +
                "revno='" + revno + '\'' +
                ", committer='" + committer + '\'' +
                ", branch_nick='" + branch_nick + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", message='" + message + '\'' +
                '}';
        }
    }

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
//        Data data = new Gson().fromJson( new InputStreamReader( new FileInputStream( "config.json" ) ), Data.class );
//        String command = "cd ";
//        command+=data.getPath();
//        command+="; pwd";
//        try
//        {
//            runCommand( command );
//        } catch (Exception t)
//        {
//            t.printStackTrace();
//        }

        readWriteFile();

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
            in = new FileInputStream( "bzr-2_20_brief" );
            out = new FileOutputStream( "output.txt" );
            BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( out ) );
            Scanner scanner = new Scanner( in );
            List<BzrItem> bzrItems = new ArrayList<>();
            String msg = "";
            BzrItem item = new BzrItem();
            while ( scanner.hasNext() )
            {

                String line = scanner.nextLine();
                if ( line.startsWith( "------------------------------------------------------------" ) )
                {
                    item.setMessage( msg );

                    System.out.println(item);
                    msg="";
                    item=new BzrItem();
                }
                else if ( line.startsWith( "revno" ) )
                {
                    item.setRevno( line );
                }
                else if ( line.startsWith( "committer" ) )
                {
                    item.setCommitter( line );
                }
                else if ( line.startsWith( "branch nick" ) )
                {
                    item.setBranch_nick( line );
                }
                else if ( line.startsWith( "timestamp" ) )
                {
                    item.setTimestamp( line );
                }
                else
                {
                    msg+="\n" + line;
                }
            }
            bw.write( "lsakdf" );
            bw.newLine();

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
