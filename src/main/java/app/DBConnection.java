package app;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * SQL Connection
 * @author: Daniel Casper
 **/

public class DBConnection {

    private static String urlString;
    private static String passwordString;

    public static void setLogin(String url, String password){
        urlString="jdbc:mysql://"+url;
        passwordString=password;
    }

    /***
     * This opens the connection to the database
     * @return connection
     */
    @SuppressWarnings("exports")
    public static Connection connectDB(){
        String url=urlString;
        String password=passwordString;
        //ArrayList<String> tasks = new ArrayList<String>();
        Connection connection;
        try{
            connection = DriverManager.getConnection(url, null, password);
        }catch (SQLException e) {
            return null;
        }
        return connection;
    }

    /**
     * This closes the connection
     * @param connection database connection
     */
    public static void closeDB(Connection connection){
        try{
            connection.close();
        }catch(SQLException e){
            throw new RuntimeException("Cannot close database", e);
        }

    }

    /**
     * Returns the seriesID of an entry in the series table
     * based on the given SeriesTitle
     * @param SeriesTitle string in the SeriesTitle column of a given row of the series table
     * @return integer in the SeriesID of the given row of the series table
     */
    public static int getSeriesIDByTitle(String SeriesTitle){
        int SeriesID;
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT SeriesID FROM series WHERE SeriesTitle=\""+SeriesTitle+"\";");


            rs.next();

            if (rs.getRow()==0){
                return 0;
            }
            SeriesID= rs.getInt("SeriesID");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return SeriesID;
    }

    /**
     * Gets the SeriesID of the most recently added item in the series table
     * @return the SeriesID of the most recently added item in the series table
     */
    public static int getFinalSeriesID(){
        int SeriesID;
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT SeriesID FROM series ORDER BY SeriesID DESC LIMIT 1;");


            rs.next();

            if (rs.getRow()==0){
                return 0;
            }
            SeriesID= rs.getInt("SeriesID");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return SeriesID;
    }

    /**
     * Returns the seriesID of an entry in the series table
     * based on the given SeriesTitle
     * @param SeriesID integer in the SeriesID of a given row of the series table
     * @return string in the SeriesTitle column of the given row of the series table
     */
    public static String getSeriesTitleByID(int SeriesID){
        String SeriesTitle;
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT SeriesTitle FROM series WHERE SeriesID="+SeriesID+";");


            rs.next();

            if (rs.getRow()==0){
                return "";
            }
            SeriesTitle= rs.getString("SeriesTitle");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return SeriesTitle;
    }

    /**
     * Returns whether or not a given series has been added to the xmenadjseries yet
     * @param SeriesID integer in the SeriesID column of a given row of the series/xmenadjseries tables
     * @return true if the series is found, otherwise false
     */
    public static boolean XSeriesExists(int SeriesID){
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT SeriesID FROM xmenadjseries WHERE SeriesID="+SeriesID+";");


            rs.next();

            if (rs.getRow()==0){
                return false;
            }
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return true;
    }

    /**
     * Get the publisher of a series from the series table
     * @param seriesID integer in the SeriesID column of a given row of the series table
     * @return string in the Publisher column of a given row of the series table
     */
    public static String getPublisherByID(int seriesID){
        String Publisher;
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT Publisher FROM series WHERE seriesID="+seriesID+";");

            rs.next();
            if (rs.getRow()==0){
                return "";
            }
            Publisher= rs.getString("Publisher");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return Publisher;
    }

    /**
     * Checks whether or not an entry for a specific issue on a given date has already been made
     * @param SeriesID integer in the SeriesID column of a given row of the comic table
     * @param issueName string in the issueName column of a given row of the comic table
     * @param dateString string in the dateString column of a given row of the comic table
     * @param month integer in the month column of a given row of the comic table
     * @param day integer in the day column of a given row of the comic table
     * @param year integer in the year column of a given row of the comic table
     * @return true if an entry matching all the parameters is found and false if not
     */
    public static boolean entryExists(int SeriesID, String issueName, String dateString, int month, int day, int year){
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT issueID FROM comic WHERE SeriesID="+SeriesID+" AND issueName='"+issueName+"' AND dateString='"+dateString+"' AND month="+month+" AND day="+day+" AND year="+year+";");


            rs.next();

            if (rs.getRow()==0){
                return false;
            }
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return true;
    }

    /**
     * Get the number of series that have entries in the database
     * @return the number of rows in the series table
     */
    public static int getNumSeries(){
        int count;
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS count FROM series;");


            rs.next();

            if (rs.getRow()==0){
                return 0;
            }

            count= rs.getInt("count");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return count;
    }

    /**
     * Get the number of issues read of a specific series
     * @param SeriesTitle string in the SeriesTitle column of a given row of the series table
     * @return integer in the issueID column of a given row of the series table
     */
    public static int getNumIssuesSeries(String SeriesTitle){
        int count;
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT issueID FROM series WHERE SeriesTitle='"+SeriesTitle+"';");


            rs.next();

            if (rs.getRow()==0){
                return 0;
            }

            count= rs.getInt("issueID");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return count;
    }

    /**
     * Get a list of all the series in the series table
     * @return an ArrayList containing strings of all the SeriesTitles in the series table
     */
    public static ArrayList<String> getSeries(){
        ArrayList<String> titles=new ArrayList<String>();
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT SeriesTitle FROM series;");

            rs.next();
            titles.add(rs.getString("SeriesTitle"));
            if (rs.getRow()==0){
                return null;
            }

            while(rs.next()){
                titles.add(rs.getString("SeriesTitle"));
            }


            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return titles;
    }

    /**
     * Get a list of all the issues in a series from the comic table
     * @param SeriesTitle string in the SeriesTitle column of a given row of the series table
     * @return an ArrayList containing strings of all the issueNames with
     * the given SeriesID in the comic table
     */
    public static ArrayList<String> getIssuesBySeriesID(String SeriesTitle){
        int seriesID=getSeriesIDByTitle(SeriesTitle);
        ArrayList<String> issues=new ArrayList<String>();
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT issueName FROM comic WHERE seriesID="+seriesID+";");

            rs.next();
            issues.add(rs.getString("issueName"));
            if (rs.getRow()==0){
                return null;
            }

            while(rs.next()){
                issues.add(rs.getString("issueName"));
            }


            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return issues;
    }

    /**
     * Get a list of all the issues in a series from the comic table
     * @param dateString string for the dateString column of the new row
     * @return an ArrayList containing strings of all the issueNames with
     * the given SeriesID in the comic table
     */
    public static ArrayList<String> getIssuesByDate(String dateString){
        ArrayList<String> issues=new ArrayList<String>();
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT issueName FROM comic WHERE dateString='"+dateString+"';");

            rs.next();
            if (rs.getRow()==0){
                return null;
            }
            issues.add(rs.getString("issueName"));

            while(rs.next()){
                issues.add(rs.getString("issueName"));
            }


            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return issues;
    }

    /**
     * Get a list of all the issues in a series from the comic table
     * @param dateString string for the dateString column of the new row
     * @return an ArrayList containing strings of all the issueNames with
     * the given SeriesID in the comic table
     */
    public static ArrayList<Integer> getSeriesByDate(String dateString){
        ArrayList<Integer> series=new ArrayList<Integer>();
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT seriesID FROM comic WHERE dateString='"+dateString+"';");

            rs.next();
            if (rs.getRow()==0){
                return null;
            }
            series.add(rs.getInt("seriesID"));

            while(rs.next()){
                series.add(rs.getInt("seriesID"));
            }


            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return series;
    }

    /**
     * Get a list of all the dates for each issue in a series from the comic table
     * @param SeriesTitle string in the SeriesTitle column of a given row of the series table
     * @return an ArrayList containing strings of all the dateStrings with
     * the given SeriesID in the comic table
     */
    public static ArrayList<String> getDatesBySeriesID(String SeriesTitle){
        int seriesID=getSeriesIDByTitle(SeriesTitle);
        ArrayList<String> issues=new ArrayList<String>();
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT dateString FROM comic WHERE seriesID="+seriesID+";");

            rs.next();
            issues.add(rs.getString("dateString"));
            if (rs.getRow()==0){
                return null;
            }

            while(rs.next()){
                issues.add(rs.getString("dateString"));
            }


            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return issues;
    }

    /**
     * Get a list of all the series in the series table that
     * are designated as X-Men series
     * @return an array list containing integers of all the
     * SeriesIDs that have true in the xmen column of the series table
     */
    public static ArrayList<Integer> getXmen(){
        ArrayList<Integer> titles=new ArrayList<Integer>();
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select SeriesID from series where xmen=1;");

            rs.next();
            titles.add(rs.getInt("SeriesID"));
            if (rs.getRow()==0){
                return null;
            }

            while(rs.next()){
                titles.add(rs.getInt("SeriesID"));
            }


            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return titles;
    }

    /**
     * Get a list of all the series in the xmenadjseries table
     * @return an array list containing integers of all the
     * SeriesIDs in the xmenadjseries table
     */
    public static ArrayList<Integer> getXmenAdj(){
        ArrayList<Integer> titles=new ArrayList<Integer>();
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select SeriesID from xmenadjseries;");

            rs.next();
            titles.add(rs.getInt("SeriesID"));
            if (rs.getRow()==0){
                return null;
            }

            while(rs.next()){
                titles.add(rs.getInt("SeriesID"));
            }


            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return titles;
    }

    /**
     * Increment the issueID column in the series table for the given series by one
     * @param SeriesID integer in the SeriesID of a given row of the series table
     */
    public static void updateIssueCount(int SeriesID){
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql = "UPDATE series SET issueID=issueID+1 WHERE SeriesID="+SeriesID+";";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();



            rs.next();
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
    }

    /**
     * Increment the issueID column in the xmenadjseries table for the given series by one
     *
     * @param SeriesID integer in the SeriesID of a given row of the xmenadjseries table
     */
    public static void updateXIssueCount(int SeriesID){
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql = "UPDATE xmenadjseries SET issueID=issueID+1 WHERE SeriesID="+SeriesID+";";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();



            rs.next();
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
    }

    /**
     * Create a new series in a new row in the series table
     * @param SeriesTitle string for the SeriesTitle column of the new row
     * @param Publisher string for the Publisher column for the new row
     * @param xmen boolean for the xmen column for the new row
     * @return integer in the SeriesID column of the new row, -1 if the series already exists
     */
    public static int createSeries(String SeriesTitle, String Publisher, boolean xmen){
        if (getSeriesIDByTitle(SeriesTitle)==0){

            Connection connection = connectDB();
            int SeriesID = 0;
            try{
                assert connection != null;
                Statement statement = connection.createStatement();
                String sql = "INSERT INTO series(SeriesTitle, issueID, Publisher, xmen) VALUES('"+SeriesTitle+"', 0, '"+Publisher+"', "+xmen+" );";
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    SeriesID = resultSet.getInt(1);
                    System.out.println("Added New Series: " + SeriesTitle);
                }

                statement.close();
            }catch(SQLException e){
                throw new RuntimeException("Problem with database", e);
            }
            closeDB(connection);
            return SeriesID;
        }
        else{
            return(-1);
        }
    }

    /**
     * @param SeriesID integer for the SeriesID column of the new row
     * @param SeriesTitle string for the SeriesTitle column of the new row
     */
    public static void createxmenadjseries(int SeriesID, String SeriesTitle){
        Connection connection = connectDB();
        try{
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO xmenadjseries(SeriesID, SeriesTitle, issueID) VALUES("+SeriesID+", '"+SeriesTitle+"', 0);";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                SeriesID = resultSet.getInt(1);
                System.out.println("Generated primary key: " + SeriesID);
            }

            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        closeDB(connection);
    }

    /**
     * Get the total number of issues in the comic table
     * @return the number of rows in the comic table
     */
    public static int getTotal(){
        Connection connection = connectDB();
        int sum = 0;
        try{
            assert connection != null;
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(*) FROM comic;");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            }
            res.close();
            st.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return sum;
    }

    /**
     * Get the total number of issues in the comic table that were read in a specific month and year
     * @return the number of rows in the comic table with the given month and year column values
     */
    public static int getTotalSnapshot(int month, int year){
        Connection connection = connectDB();
        int sum = 0;
        try{
            assert connection != null;
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(issueID) FROM comic WHERE year<"+year+";");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            }
            res.close();
            st.close();
            st = connection.createStatement();
            res = st.executeQuery("SELECT COUNT(issueID) FROM comic WHERE month<="+month+" AND year="+year+";");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            }
            res.close();
            st.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return sum;
    }

    /**
     * Get the total number of issues in the comic table from a specific publisher
     * @return the sum of all the issueIDs in the series table with the given Publisher column value
     */
    public static int getNumPublisher(String Publisher){
        Connection connection = connectDB();
        int sum = 0;
        try{
            assert connection != null;
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT SUM(issueID) FROM series WHERE Publisher='"+Publisher+"';");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            }
            res.close();
            st.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return sum;
    }

    /**
     * Get the total number of issues for a given series in the comic table that were read in a specific month and year
     * @return the number of rows in the comic table with the given month, seriesID, and year column values
     */
    public static int getNumSnapshot(int seriesID, int month, int year){
        Connection connection = connectDB();
        int sum = 0;
        try{
            assert connection != null;
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(issueID) FROM comic WHERE year<"+year+" and seriesID="+seriesID+";");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            }
            res.close();
            st.close();
            st = connection.createStatement();
            res = st.executeQuery("SELECT COUNT(issueID) FROM comic WHERE month<="+month+" AND year="+year+" and seriesID="+seriesID+";");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            }
            res.close();
            st.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return sum;
    }

    /**
     * Get the total number of issues for a given series in the xmenadjcomic table that were read in a specific month and year
     * @return the number of rows in the xmenadjcomic table with the given month, seriesID, and year column values
     */
    public static int getNumXmenSnapshot(int seriesID, int month, int year){
        Connection connection = connectDB();
        int sum = 0;
        try{

            assert connection != null;
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(issueID) FROM xmenadjcomic WHERE year<"+year+" and seriesID="+seriesID+";");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            }
            res.close();
            st.close();
            st = connection.createStatement();
            res = st.executeQuery("SELECT COUNT(issueID) FROM xmenadjcomic WHERE month<="+month+" AND year="+year+" and seriesID="+seriesID+";");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            }
            res.close();
            st.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return sum;
    }

    /**
     * Get the total number of issues read that are from X-Men series
     * @return the sum of all the issueIDs in the series table where
     * the xmen column is true
     */
    public static int getNumXMen(){
        Connection connection = connectDB();
        int sum = 0;
        try{
            assert connection != null;
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT SUM(issueID) FROM series WHERE xmen=1;");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            }
            res.close();
            st.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        sum+=getNumXMenAdj();
        closeDB(connection);
        return sum;
    }

    /**
     * Get the total number of issues read in the xmenadjseries table
     * @return the sum of all the issueIDs in the xmenadjseries table
     */
    public static int getNumXMenAdj(){
        Connection connection = connectDB();
        int sum = 0;
        try{
            assert connection != null;
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT SUM(issueID) FROM xmenadjseries;");
            while (res.next()) {
                int c = res.getInt(1);
                sum = sum + c;
            }
            res.close();
            st.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return sum;
    }

    /**
     * Add a new issue to the comic table
     * @param SeriesID integer for the SeriesID column of the new row
     * @param issueName string for the issueName column of the new row
     * @param dateString string for the dateString column of the new row
     * @param month integer for the month column of the new row
     * @param day integer for the day column of the new row
     * @param year integer for the year column of the new row
     * @param xmenAdj boolean indicating whether or not to add this issue to the xmenadjcomic table
     */
    public static void addIssue(int SeriesID, String issueName, String dateString, int month, int day, int year, boolean xmenAdj){
        int issueID = -1;
        Connection connection = connectDB();
        try{
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql = "insert into comic(SeriesID, issueName, dateString, month, day, year) values("+SeriesID+", '"+issueName+"','"+dateString+"', "+month+", "+day+", "+year+");";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                issueID = resultSet.getInt(1);
                String SeriesName=getSeriesTitleByID(SeriesID);
                System.out.println("Added New Issue: " + SeriesName + " #" + issueName + " on " + dateString);
            }
            updateIssueCount(SeriesID);
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        if (xmenAdj){
            if (!XSeriesExists(SeriesID)){
                createxmenadjseries(SeriesID, getSeriesTitleByID(SeriesID));
            }
            addXAdjIssue(SeriesID,issueName,dateString,month,day,year);
        }
        closeDB(connection);
    }

    /**
     *
     * @param SeriesID integer for the SeriesID column of the new row
     * @param issueName string for the issueName column of the new row
     * @param dateString string for the dateString column of the new row
     * @param month integer for the month column of the new row
     * @param day integer for the day column of the new row
     * @param year integer for the year column of the new row
     */
    public static void addXAdjIssue(int SeriesID, String issueName, String dateString, int month, int day, int year){
        int issueID = -1;
        Connection connection = connectDB();
        try{
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql = "insert into xmenadjcomic(SeriesID, issueName, dateString, month, day, year) values("+SeriesID+", '"+issueName+"','"+dateString+"', "+month+", "+day+", "+year+");";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                issueID = resultSet.getInt(1);
                System.out.println("Generated primary key: " + issueID);
            }
            updateXIssueCount(SeriesID);
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        closeDB(connection);
    }
    
    public static void addPublisher(String publisher){
        int num;
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT MAX(list_order) as count FROM publisher;");
            rs.next();
            num = rs.getInt("count");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        try{
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql = "insert into publisher(publisher, list_order) values('"+publisher+"', "+(num+1)+");";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                String pubKey = resultSet.getString(1);
                System.out.println("Added Publisher: " + pubKey);
            }
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        closeDB(connection);
    }

    public static boolean seriesIsXmen(int seriesID){
        boolean xmen;
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT xmen FROM series WHERE seriesID="+seriesID+";");

            rs.next();
            if (rs.getRow()==0){
                return false;
            }
            xmen= rs.getBoolean("xmen");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return xmen;
    }

    public static ArrayList<Integer> tempTableMonth(int year, int month){
        //int dcSum=0;
        int totalSum=0;
        int xmenSum=0;
        //int marvelSum=0;
        //int imageSum=0;
        //int darkHorseSum=0;
        //int boomSum=0;
        //int otherSum=0;
        int seriesSum=0;
        ArrayList<Integer> totals=new ArrayList<Integer>();
        Connection connection = connectDB();
        try{
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql = "CREATE TEMPORARY TABLE tempComic AS SELECT * FROM comic WHERE year="+year+" and month="+month+";";
            statement.execute(sql);
            statement.close();

            statement = connection.createStatement();
            sql="SELECT DISTINCT SeriesID FROM tempComic;";
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<Integer> ids=new ArrayList<Integer>();
            rs.next();
            ids.add(rs.getInt("SeriesID"));
            if (rs.getRow()==0){
                int x=2;
            }
            while(rs.next()){
                ids.add(rs.getInt("SeriesID"));
            }
            rs.close();
            statement.close();

            ArrayList<String> pub_list=getPublishers();
            Integer[] sums=new Integer[pub_list.size()];
            for (int series: ids){
                String publisher=getPublisherByID(series);
                int addTo;
                statement = connection.createStatement();
                rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic where SeriesID="+series+";");
                rs.next();
                if (rs.getRow()==0){
                    addTo=0;
                }
                addTo= rs.getInt("count");
                rs.close();
                statement.close();
                if (pub_list.contains(publisher)){
                    if (publisher.equals("Marvel")){
                        sums[pub_list.indexOf(publisher)]+=addTo;
                        //marvelSum += addTo;
                        if (seriesIsXmen(series)) {
                            xmenSum += addTo;
                        }
                    }
                    else{
                        sums[pub_list.indexOf(publisher)]+=addTo;
                    }
                }
                // switch(publisher) {
                //     case "Marvel":
                //         marvelSum += addTo;
                //         if (seriesIsXmen(series)) {
                //             xmenSum += addTo;
                //         }
                //         break;
                //     case "Image":
                //         imageSum += addTo;
                //         break;
                //     case "Dark Horse":
                //         darkHorseSum += addTo;
                //         break;
                //     case "Boom":
                //         boomSum += addTo;
                //         break;
                //     case "DC":
                //         dcSum += addTo;
                //         break;
                //     case "Other":
                //         otherSum += addTo;
                //         break;
                // }
            }
            xmenSum+=getXmenAdjMonth(month,year);
            seriesSum=ids.size();
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic;");
            rs.next();
            totalSum= rs.getInt("count");
            rs.close();
            statement.close();
            totals.add(totalSum);
            for (int i=0;i<sums.length;i++){
                totals.add(sums[i]);
            }
            totals.add(xmenSum);
            totals.add(seriesSum);
            //totals=new ArrayList<Integer>(Arrays.asList(totalSum, dcSum, marvelSum, imageSum, darkHorseSum, boomSum, otherSum, xmenSum, seriesSum));
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        closeDB(connection);
        return totals;
    }

    public static int getXmenAdjMonth(int month, int year){
        int count;
        getNumXMenAdj();
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS count FROM xmenadjcomic WHERE month="+month+" AND year="+year+";");
            rs.next();
            if (rs.getRow()==0){
                return 0;
            }
            count = rs.getInt("count");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return count;
    }

    public static ArrayList<Integer> tempTableYear(int year){
        //int dcSum=0;
        int totalSum=0;
        int xmenSum=0;
        //int marvelSum=0;
        //int imageSum=0;
        //int darkHorseSum=0;
        //int boomSum=0;
        //int otherSum=0;
        int seriesSum=0;
        ArrayList<Integer> totals=new ArrayList<Integer>();
        Connection connection = connectDB();
        try{
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql = "CREATE TEMPORARY TABLE tempComic AS SELECT * FROM comic WHERE year="+year+";";
            statement.execute(sql);
            statement.close();

            statement = connection.createStatement();
            sql="SELECT DISTINCT SeriesID FROM tempComic;";
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<Integer> ids=new ArrayList<Integer>();
            rs.next();
            ids.add(rs.getInt("SeriesID"));
            if (rs.getRow()==0){
                int x=2;
            }
            while(rs.next()){
                ids.add(rs.getInt("SeriesID"));
            }
            rs.close();
            statement.close();

            ArrayList<String> pub_list=getPublishers();
            Integer[] sums=new Integer[pub_list.size()];
            for (int series: ids){
                String publisher=getPublisherByID(series);
                int addTo;
                statement = connection.createStatement();
                rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic where SeriesID="+series+";");
                rs.next();
                if (rs.getRow()==0){
                    addTo=0;
                }
                addTo= rs.getInt("count");
                rs.close();
                statement.close();
                if (pub_list.contains(publisher)){
                    if (publisher.equals("Marvel")){
                        sums[pub_list.indexOf(publisher)]+=addTo;
                        //marvelSum += addTo;
                        if (seriesIsXmen(series)) {
                            xmenSum += addTo;
                        }
                    }
                    else{
                        sums[pub_list.indexOf(publisher)]+=addTo;
                    }
                }
                // switch(publisher) {
                //     case "Marvel":
                //         marvelSum += addTo;
                //         if (seriesIsXmen(series)) {
                //             xmenSum += addTo;
                //         }
                //         break;
                //     case "Image":
                //         imageSum += addTo;
                //         break;
                //     case "Dark Horse":
                //         darkHorseSum += addTo;
                //         break;
                //     case "Boom":
                //         boomSum += addTo;
                //         break;
                //     case "DC":
                //         dcSum += addTo;
                //         break;
                //     case "Other":
                //         otherSum += addTo;
                //         break;
                // }
            }
            xmenSum+=getXmenAdjYear(year);
            seriesSum=ids.size();
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic;");
            rs.next();
            totalSum= rs.getInt("count");
            rs.close();
            statement.close();
            totals.add(totalSum);
            for (int i=0;i<sums.length;i++){
                totals.add(sums[i]);
            }
            totals.add(xmenSum);
            totals.add(seriesSum);
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        closeDB(connection);
        return totals;
    }

    public static int getXmenAdjYear(int year){
        int count;
        getNumXMenAdj();
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS count FROM xmenadjcomic WHERE year="+year+";");
            rs.next();
            if (rs.getRow()==0){
                return 0;
            }
            count = rs.getInt("count");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return count;
    }

    public static ArrayList<Integer> tempTableSnap(int year, int month){
        //int dcSum=0;
        int totalSum=0;
        int xmenSum=0;
        //int marvelSum=0;
        //int imageSum=0;
        //int darkHorseSum=0;
        //int boomSum=0;
        //int otherSum=0;
        int seriesSum=0;
        ArrayList<Integer> totals=new ArrayList<Integer>();
        Connection connection = connectDB();
        try{
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql = "CREATE TEMPORARY TABLE tempComic AS SELECT * FROM comic WHERE year="+year+" and month<="+month+";";
            statement.execute(sql);
            statement.close();

            statement = connection.createStatement();
            sql="SELECT DISTINCT SeriesID FROM tempComic;";
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<Integer> ids=new ArrayList<Integer>();
            rs.next();
            ids.add(rs.getInt("SeriesID"));
            if (rs.getRow()==0){
                int x=2;
            }
            while(rs.next()){
                ids.add(rs.getInt("SeriesID"));
            }
            rs.close();
            statement.close();

            ArrayList<String> pub_list=getPublishers();
            Integer[] sums=new Integer[pub_list.size()];
            for (int series: ids){
                String publisher=getPublisherByID(series);
                int addTo;
                statement = connection.createStatement();
                rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic where SeriesID="+series+";");
                rs.next();
                if (rs.getRow()==0){
                    addTo=0;
                }
                addTo= rs.getInt("count");
                rs.close();
                statement.close();
                if (pub_list.contains(publisher)){
                    if (publisher.equals("Marvel")){
                        sums[pub_list.indexOf(publisher)]+=addTo;
                        //marvelSum += addTo;
                        if (seriesIsXmen(series)) {
                            xmenSum += addTo;
                        }
                    }
                    else{
                        sums[pub_list.indexOf(publisher)]+=addTo;
                    }
                }
                // switch(publisher) {
                //     case "Marvel":
                //         marvelSum += addTo;
                //         if (seriesIsXmen(series)) {
                //             xmenSum += addTo;
                //         }
                //         break;
                //     case "Image":
                //         imageSum += addTo;
                //         break;
                //     case "Dark Horse":
                //         darkHorseSum += addTo;
                //         break;
                //     case "Boom":
                //         boomSum += addTo;
                //         break;
                //     case "DC":
                //         dcSum += addTo;
                //         break;
                //     case "Other":
                //         otherSum += addTo;
                //         break;
                // }
            }
            xmenSum+=getXmenAdjSnap(month,year);
            seriesSum=ids.size();
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic;");
            rs.next();
            totalSum= rs.getInt("count");
            rs.close();
            statement.close();
            totals.add(totalSum);
            for (int i=0;i<sums.length;i++){
                totals.add(sums[i]);
            }
            totals.add(xmenSum);
            totals.add(seriesSum);
            //totals=new ArrayList<Integer>(Arrays.asList(totalSum, dcSum, marvelSum, imageSum, darkHorseSum, boomSum, otherSum, xmenSum, seriesSum));
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        closeDB(connection);
        return totals;
    }

    public static int getXmenAdjSnap(int month, int year){
        int count;
        getNumXMenAdj();
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS count FROM xmenadjcomic WHERE month<="+month+" AND year="+year+";");
            rs.next();
            if (rs.getRow()==0){
                return 0;
            }
            count = rs.getInt("count");
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return count;
    }

    public static ArrayList<String> getPublishers(){
        ArrayList<String> publishers=new ArrayList<String>();
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT publisher from publisher order by list_order;");

            rs.next();
            if (rs.getRow()==0){
                return null;
            }
            publishers.add(rs.getString("publisher"));

            while(rs.next()){
                publishers.add(rs.getString("publisher"));
            }


            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return publishers;
    }


}
