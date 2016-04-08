import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main
{
    public static String CONFIG_FILE = "config.json";
    public static String COMMIT_FILE = "bzr-2_20_brief";
    public static String OUTPUT_FILE = "output.txt";

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
        private String path_bzr;
        private String path_git;

        public String getPath_bzr()
        {
            return path_bzr;
        }

        public void setPath_bzr( String path_bzr )
        {
            this.path_bzr = path_bzr;
        }

        public String getPath_git()
        {
            return path_git;
        }

        public void setPath_git( String path_git )
        {
            this.path_git = path_git;
        }
    }

    public static void main( String[] args ) throws Exception
    {
        List<BzrItem> bzrItems = readBzrLog();

        Config config = new Gson().fromJson( new InputStreamReader( new FileInputStream( CONFIG_FILE ) ), Config.class );
        String cdBzr = "cd " + config.getPath_bzr() + "; pwd; ";
        String cdGit = "cd " + config.getPath_git() + "; pwd; ";

        Collections.sort( bzrItems, ( o1, o2 ) -> {
            return o1.getRevnoInt() - o2.getRevnoInt();
        } );
        FileOutputStream out = new FileOutputStream( OUTPUT_FILE );
        BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( out ) );
        Date start = new Date();
        writeLog( bw, "Start: " + start );
        for ( BzrItem item : bzrItems )
        {
            String bzrCommand = cdBzr + "bzr up -r " + item.getRevnoInt();
            String gitCommand = bzrCommand + "; " + cdGit + "git add *; git commit -m \"" + item.toString() + "\"";
            try
            {
                runCommand( bw, gitCommand );
            }
            catch ( Exception t )
            {
                t.printStackTrace();
            }
        }
        Date end = new Date();
        writeLog( bw, "Finished: " + end );
        writeLog( bw, "Total: " + getTimeDiff( start, end ) );
        bw.close();
        out.close();
    }

    public static String getTimeDiff( Date dateOne, Date dateTwo )
    {
        String diff = "";
        long timeDiff = Math.abs( dateOne.getTime() - dateTwo.getTime() );
        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds( timeDiff );
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes( timeDiff );
        long diffInHours = TimeUnit.MILLISECONDS.toHours( timeDiff );
        diff = String.format( "%d hour(s) %d min(s) %d sec(s)", diffInHours, diffInMinutes, diffInSeconds );
        return diff;
    }

    public static void runCommand( BufferedWriter bw, String command ) throws Exception
    {
        int exit_value = -1;
        System.out.println( "Running command: " + command );
        try
        {
            ProcessBuilder processBuilder = new ProcessBuilder(
                "bash",
                "-c",
                command );
            processBuilder.redirectErrorStream( true );
            Process proc = processBuilder.start();
//            BufferedReader stdInput = new BufferedReader( new
//                InputStreamReader( proc.getInputStream() ) );
//
            BufferedReader stdError = new BufferedReader( new
                InputStreamReader( proc.getErrorStream() ) );
//
            String s = null;
//            while ( (s = stdInput.readLine()) != null )
//            {
//                System.out.println( s );
//            }
//
            while ( (s = stdError.readLine()) != null )
            {
                System.out.println( s );
            }

            exit_value = proc.waitFor();
            System.out.println( "Process exitValue: " + exit_value );
        }
        finally
        {
            writeLog( bw, "\"Running command: \"" + command + " \n" + "Process exitValue: " + exit_value );
        }
    }

    public static void writeLog( BufferedWriter bw, String msg ) throws Exception
    {
        try
        {
            bw.write( msg );
            bw.newLine();
        }
        finally
        {
            bw.flush();
        }
    }

    public static List<BzrItem> readBzrLog() throws Exception
    {
        List<BzrItem> bzrItems = new ArrayList<>();
        FileInputStream in = null;
        try
        {
            in = new FileInputStream( COMMIT_FILE );
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
        }
        return bzrItems;
    }
}
