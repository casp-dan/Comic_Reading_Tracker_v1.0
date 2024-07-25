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

    /***
     * This opens the connection to the database
     * @return connection
     */
    @SuppressWarnings("exports")
    public static Connection connectDB(){
        String url = "urlGoesHere"; 
        String username = "username";
        String password = "paassword";
        //ArrayList<String> tasks = new ArrayList<String>();
        Connection connection;
        try{
            connection = DriverManager.getConnection(url, username, password);
        }catch (SQLException e) {
            throw new IllegalStateException("Cannot connect!", e);
        }
        return connection;
    }

    /**
     * This closes the connection
     * @param connection
     */
    public static void closeDB(@SuppressWarnings("exports") Connection connection){
        try{
            connection.close();
        }catch(SQLException e){
            throw new RuntimeException("Cannot close database", e);
        }

    }

    /**
     * Returns the seriesID of an entry in the Series table 
     * based on the given SeriesTitle
     * @param SeriesTitle string in the SeriesTitle column of a given row of the Series table
     * @return integer in the SeriesID of the given row of the Series table
     */
    public static int getSeriesIDByTitle(String SeriesTitle){
        int SeriesID;
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT SeriesID FROM Series WHERE SeriesTitle=\""+SeriesTitle+"\";");


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
     * Gets the SeriesID of the most recently added item in the Series table
     * @return the SeriesID of the most recently added item in the Series table
     */
    public static int getFinalSeriesID(){
        int SeriesID;
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT SeriesID FROM Series ORDER BY SeriesID DESC LIMIT 1;");


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
     * Returns the seriesID of an entry in the Series table 
     * based on the given SeriesTitle
     * @param SeriesID integer in the SeriesID of a given row of the Series table
     * @return string in the SeriesTitle column of the given row of the Series table
     */
    public static String getSeriesTitleByID(int SeriesID){
        String SeriesTitle;
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT SeriesTitle FROM Series WHERE SeriesID="+SeriesID+";");


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
     * Returns whether or not a given series has been added to the XmenAdjSeries yet
     * @param SeriesID integer in the SeriesID column of a given row of the Series/XmenAdjSeries tables
     * @return true if the series is found, otherwise false
     */
    public static boolean XSeriesExists(int SeriesID){
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT SeriesID FROM XmenAdjSeries WHERE SeriesID="+SeriesID+";");


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
     * Get the publisher of a series from the Series table
     * @param seriesID integer in the SeriesID column of a given row of the Series table
     * @return string in the Publisher column of a given row of the Series table
     */
    public static String getPublisherByID(int seriesID){
        String Publisher;
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT Publisher FROM Series WHERE seriesID="+seriesID+";");

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
     * @param SeriesID integer in the SeriesID column of a given row of the Comic table
     * @param issueName string in the issueName column of a given row of the Comic table
     * @param dateString string in the dateString column of a given row of the Comic table
     * @param month integer in the month column of a given row of the Comic table
     * @param day integer in the day column of a given row of the Comic table
     * @param year integer in the year column of a given row of the Comic table
     * @return true if an entry matching all the parameters is found and false if not
     */
    public static boolean entryExists(int SeriesID, String issueName, String dateString, int month, int day, int year){
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT issueID FROM Comic WHERE SeriesID="+SeriesID+" AND issueName='"+issueName+"' AND dateString='"+dateString+"' AND month="+month+" AND day="+day+" AND year="+year+";");


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
     * @return the number of rows in the Series table
     */    
    public static int getNumSeries(){
        int count;
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT COUNT(*) AS count FROM SERIES;");


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
     * @param SeriesTitle string in the SeriesTitle column of a given row of the Series table
     * @return integer in the issueID column of a given row of the Series table
     */
    public static int getNumIssuesSeries(String SeriesTitle){
        int count;
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT issueID FROM SERIES WHERE SeriesTitle='"+SeriesTitle+"';");


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
     * Get a list of all the series in the Series table
     * @return an ArrayList containing strings of all the SeriesTitles in the Series table
     */
    public static ArrayList<String> getSeries(){
        ArrayList<String> titles=new ArrayList<String>();
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT SeriesTitle FROM Series;");

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
     * Get a list of all the issues in a series from the Comic table
     * @param SeriesTitle string in the SeriesTitle column of a given row of the Series table
     * @return an ArrayList containing strings of all the issueNames with 
     * the given SeriesID in the Comic table
     */
    public static ArrayList<String> getIssuesBySeriesID(String SeriesTitle){
        int seriesID=getSeriesIDByTitle(SeriesTitle);
        ArrayList<String> issues=new ArrayList<String>();
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT issueName FROM COMIC WHERE seriesID="+seriesID+";");

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
     * Get a list of all the issues in a series from the Comic table
     * @param SeriesTitle string in the SeriesTitle column of a given row of the Series table
     * @return an ArrayList containing strings of all the issueNames with 
     * the given SeriesID in the Comic table
     */
    public static ArrayList<String> getIssuesByDate(String dateString){
        ArrayList<String> issues=new ArrayList<String>();
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT issueName FROM COMIC WHERE dateString='"+dateString+"';");

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
     * Get a list of all the issues in a series from the Comic table
     * @param SeriesTitle string in the SeriesTitle column of a given row of the Series table
     * @return an ArrayList containing strings of all the issueNames with 
     * the given SeriesID in the Comic table
     */
    public static ArrayList<Integer> getSeriesByDate(String dateString){
        ArrayList<Integer> series=new ArrayList<Integer>();
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT seriesID FROM COMIC WHERE dateString='"+dateString+"';");

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
     * Get a list of all the dates for each issue in a series from the Comic table
     * @param SeriesTitle string in the SeriesTitle column of a given row of the Series table
     * @return an ArrayList containing strings of all the dateStrings with 
     * the given SeriesID in the Comic table
     */
    public static ArrayList<String> getDatesBySeriesID(String SeriesTitle){
        int seriesID=getSeriesIDByTitle(SeriesTitle);
        ArrayList<String> issues=new ArrayList<String>();
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT dateString FROM COMIC WHERE seriesID="+seriesID+";");

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
     * Get a list of all the series in the Series table that 
     * are designated as X-Men series
     * @return an array list containing integers of all the 
     * SeriesIDs that have true in the xmen column of the Series table
     */
    public static ArrayList<Integer> getXmen(){
        ArrayList<Integer> titles=new ArrayList<Integer>();
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select SeriesID from Series where xmen=1;");

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
     * Get a list of all the series in the XmenAdjSeries table
     * @return an array list containing integers of all the 
     * SeriesIDs in the XmenAdjSeries table
     */
    public static ArrayList<Integer> getXmenAdj(){
        ArrayList<Integer> titles=new ArrayList<Integer>();
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("Select SeriesID from XmenAdjSeries;");

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
     * Increment the issueID column in the Series table for the given series by one
     * @param SeriesID integer in the SeriesID of a given row of the Series table
     */
    public static void updateIssueCount(int SeriesID){
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            String sql = "UPDATE Series SET issueID=issueID+1 WHERE SeriesID="+SeriesID+";";
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
     * Increment the issueID column in the XmenAdjSeries table for the given series by one
     * @param SeriesID integer in the SeriesID of a given row of the XmenAdjSeries table
     */
    public static int updateXIssueCount(int SeriesID){
        Connection connection = connectDB();
        try {
            Statement statement = connection.createStatement();
            String sql = "UPDATE XmenAdjSeries SET issueID=issueID+1 WHERE SeriesID="+SeriesID+";";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();
            


            rs.next();
            rs.close();
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem querying database", e);
        }
        closeDB(connection);
        return SeriesID;
    }
    
    /**
     * Create a new series in a new row in the Series table
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
                Statement statement = connection.createStatement();
                String sql = "INSERT INTO Series(SeriesTitle, issueID, Publisher, xmen) VALUES('"+SeriesTitle+"', 0, '"+Publisher+"', "+xmen+" );";
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
            return SeriesID;
        }
        else{
            return(-1);
        }
    }
    
    /**
     * @param SeriesID integer for the SeriesID column of the new row
     * @param SeriesTitle string for the SeriesTitle column of the new row
     * @return integer in the SeriesID column of the new row, -1 if the series already exists
     */
    public static int createXmenAdjSeries(int SeriesID, String SeriesTitle){
        Connection connection = connectDB();
        try{
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO XmenAdjSeries(SeriesID, SeriesTitle, issueID) VALUES("+SeriesID+", '"+SeriesTitle+"', 0);";
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
        return SeriesID;
    }

    /**
     * Get the total number of issues in the Comic table
     * @return the number of rows in the Comic table
     */
    public static int getTotal(){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(*) FROM COMIC;");
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
     * Get the total number of issues in the Comic table that were read in a specific month and year
     * @return the number of rows in the Comic table with the given month and year column values
     */
    public static int getTotalMonth(int month, int year){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(issueID) FROM COMIC WHERE month="+month+" AND year="+year+";");
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
     * Get the total number of issues in the Comic table that were read in a specific year
     * @return the number of rows in the Comic table with the given year column value
     */
    public static int getTotalYear(int year){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(issueID) FROM COMIC WHERE year="+year+";");
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
     * Get the total number of issues in the Comic table from a specific publisher
     * @return the sum of all the issueIDs in the Series table with the given Publisher column value
     */
    public static int getNumPublisher(String Publisher){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT SUM(issueID) FROM Series WHERE Publisher='"+Publisher+"';");
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
     * Get the total number of issues for a given series in the Comic table that were read in a specific month and year
     * @return the number of rows in the Comic table with the given month, seriesID, and year column values
     */
    public static int getNumByMonth(int seriesID, int month, int year){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(issueID) FROM COMIC WHERE month="+month+" AND year="+year+" and seriesID="+seriesID+";");
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
     * Get the total number of issues for a given series in the Comic table that were read in a specific year
     * @return the number of rows in the Comic table with the given seriesID and year column values
     */
    public static int getNumByYear(int seriesID, int year){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(issueID) FROM COMIC WHERE year="+year+" and seriesID="+seriesID+";");
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
     * Get the total number of issues for a given series in the XmenAdjComic table that were read in a specific month and year
     * @return the number of rows in the XmenAdjComic table with the given month, seriesID, and year column values
     */
    public static int getNumXmenByMonth(int seriesID, int month, int year){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(issueID) FROM XmenAdjComic WHERE month="+month+" AND year="+year+" and seriesID="+seriesID+";");
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
     * Get the total number of issues for a given series in the XmenAdjComic table that were read in a specific year
     * @return the number of rows in the XmenAdjComic table with the given seriesID and year column values
     */
    public static int getNumXmenByYear(int seriesID, int year){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(issueID) FROM XmenAdjComic WHERE year="+year+" and seriesID="+seriesID+";");
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
     * @return the sum of all the issueIDs in the Series table where 
     * the xmen column is true
     */
    public static int getNumXMen(){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT SUM(issueID) FROM Series WHERE xmen=1;");
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
     * Get the total number of issues read in the XmenAdjSeries table
     * @return the sum of all the issueIDs in the XmenAdjSeries table
     */
    public static int getNumXMenAdj(){
        Connection connection = connectDB();
        int sum = 0;
        try{
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT SUM(issueID) FROM XmenAdjSeries;");
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
     * Add a new issue to the Comic table
     * @param SeriesID integer for the SeriesID column of the new row
     * @param issueName string for the issueName column of the new row
     * @param dateString string for the dateString column of the new row
     * @param month integer for the month column of the new row
     * @param day integer for the day column of the new row
     * @param year integer for the year column of the new row
     * @param xmenAdj boolean indicating whether or not to add this issue to the XmenAdjComic table
     */
    public static void addIssue(int SeriesID, String issueName, String dateString, int month, int day, int year, boolean xmenAdj){
        int issueID = -1;
        Connection connection = connectDB();
        try{
            Statement statement = connection.createStatement();
            String sql = "insert into Comic(SeriesID, issueName, dateString, month, day, year) values("+SeriesID+", '"+issueName+"','"+dateString+"', "+month+", "+day+", "+year+");";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                issueID = resultSet.getInt(1);
                System.out.println("Generated primary key: " + issueID);
            }
            updateIssueCount(SeriesID);
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        if (xmenAdj){
            if (!XSeriesExists(SeriesID)){
                createXmenAdjSeries(SeriesID, getSeriesTitleByID(SeriesID));
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
            Statement statement = connection.createStatement();
            String sql = "insert into XmenAdjComic(SeriesID, issueName, dateString, month, day, year) values("+SeriesID+", '"+issueName+"','"+dateString+"', "+month+", "+day+", "+year+");";
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
}