import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
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

        public int getRevnoInt()
        {
            try
            {
                String revno = getRevno().replace( "revno: ", "" ).trim();
                int blank = revno.indexOf( " " );
                if ( blank != -1 )
                {
                    revno = revno.substring( 0, blank );
                }
                return Integer.parseInt( revno );
            }
            catch ( NumberFormatException e )
            {
                e.printStackTrace();
                return 1;
            }
        }

        public boolean isNull()
        {
            return getRevno() == null;
        }

        @Override public String toString()
        {
            return
                revno + "\n" +
                committer + "\n" +
                branch_nick + "\n" +
                timestamp + "\n" +
                message;
        }
    }

    class Config
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
        List<BzrItem> bzrItems = readBzrLog();

        Config config = new Gson().fromJson( new InputStreamReader( new FileInputStream( "config.json" ) ), Config.class );
        String command = "cd ";
        command += config.getPath();
        command += "; pwd; ";

        Collections.sort( bzrItems, ( o1, o2 ) -> {
            return o1.getRevnoInt() - o2.getRevnoInt();
        } );

        for ( int i = 0; i < bzrItems.size(); i++ )
        {
            BzrItem item = bzrItems.get( i );
            String bzrCommand = command + "bzr up -r " + item.getRevnoInt();
            String gitCommand = bzrCommand + "; git add *; git commit -m \"" + item.toString() + "\"";
            try
            {
                runCommand( gitCommand );
            }
            catch ( Exception t )
            {
                t.printStackTrace();
            }
        }
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

    public static List<BzrItem> readBzrLog() throws Exception
    {
        List<BzrItem> bzrItems = new ArrayList<>();
        FileInputStream in = null;
        try
        {
            in = new FileInputStream( "bzr-2_20_brief" );
            Scanner scanner = new Scanner( in );
            String msg = "";
            BzrItem item = new BzrItem();
            while ( scanner.hasNext() )
            {
                String line = scanner.nextLine();
                if ( line.startsWith( "------------------------------------------------------------" ) )
                {
                    item.setMessage( msg );

                    if ( !item.isNull() )
                    {
                        bzrItems.add( item );
                    }
                    msg = "";
                    item = new BzrItem();
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
                    msg += line + "\n";
                }
            }
        }
        finally
        {
            if ( in != null )
            {
                in.close();
            }
            return bzrItems;
        }
    }
}
