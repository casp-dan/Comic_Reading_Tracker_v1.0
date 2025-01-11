package app;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * SQL Connection
 * @author: Daniel Casper
 **/

public class DBConnection {

    private static String urlString;
    private static String passwordString;

    public static void setLogin(String url, String password){
        urlString="jdbc:mysql://"+url+"2";
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
    public static void closeDB(@SuppressWarnings("exports") Connection connection){
        try{
            connection.close();
        }catch(SQLException e){
            throw new RuntimeException("Cannot close database", e);
        }

    }

    /**
 * Get a list of all the issues in a series from the comic table
 * @param dateString string for the dateString column of the new row
 * @return an ArrayList containing strings of all the issueNames with
 * the given SeriesName in the comic table
 */
    public static ArrayList<ArrayList<String>> getIssuesByDate(String dateString){
        ArrayList<ArrayList<String>> issues=new ArrayList<ArrayList<String>>();
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT issueName,seriesName FROM Issue2 WHERE dateString like '"+dateString+"%' order by DateString;");

            rs.next();
            if (rs.getRow()==0){
                return null;
            }
            ArrayList<String> issue=new ArrayList<String>();
            issue.add(rs.getString("issueName"));
            issue.add(rs.getString("seriesName"));
            issues.add(issue);

            while(rs.next()){
                issue=new ArrayList<String>();
                issue.add(rs.getString("issueName"));
                issue.add(rs.getString("seriesName"));
                issues.add(issue);
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
     * Create a new series in a new row in the series table
     * @param order integer by which to order the table
     * @param SeriesName string for the SeriesName column of the new row
     * @param Publisher string for the Publisher column for the new row
     * @param xmen boolean for the xmen column for the new row
     * @return integer in the SeriesName column of the new row, -1 if the series already exists
     */
    public static String createSeries(String SeriesName, String Publisher, boolean xmen){
        Connection connection = connectDB();
        try{
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql = "INSERT INTO Series2(SeriesName, Publisher, xmen) VALUES(\'"+SeriesName+"\', 0, \'"+Publisher+"\', "+xmen+" );";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                // SeriesName = resultSet.getInt(1);
                System.out.println("Added New Series: " + SeriesName);
            }

            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        closeDB(connection);
        return SeriesName;
        }

    /**
     * Add a new issue to the comic table
     * @param SeriesName integer for the SeriesName column of the new row
     * @param issueName string for the issueName column of the new row
     * @param dateString string for the dateString column of the new row
     * @param month integer for the month column of the new row
     * @param day integer for the day column of the new row
     * @param year integer for the year column of the new row
     * @param xmenAdj boolean indicating whether or not to add this issue to the xmenadjcomic table
     */
    public static void addIssue(String issueName, String SeriesName, boolean xmenAdj, String DateString){
        // int issueID = -1;
        Connection connection = connectDB();
        try{
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql = "insert into Issue2(IssueName, SeriesName, DateString, XmenAdj) values(\'"+issueName+"\' , \'"+ SeriesName +"\' , \'"+DateString+"\' , "+xmenAdj+");";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                // issueID = resultSet.getInt(1);
                System.out.println("Added New Issue: " + SeriesName + "#" + issueName + " on " + DateString);
            }
            statement.close();
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        closeDB(connection);
    }

    /**
     * Get the number of issues read of a specific series
     * @param SeriesName string in the SeriesName column of a given row of the series table
     * @return integer in the issueID column of a given row of the series table
     */
    public static int getNumIssuesSeries(String SeriesName){
        Connection connection = connectDB();
        int sum = 0;
        try{
            assert connection != null;
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT count(*) FROM Issue2 WHERE SeriesName='"+SeriesName+"';");
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
     * Get a list of all the series in the series table
     * @return an ArrayList containing strings of all the SeriesNames in the series table
     */
    public static ArrayList<String> getSeries(){
        ArrayList<String> titles=new ArrayList<String>();
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT SeriesName FROM Series2;");

            rs.next();
            titles.add(rs.getString("SeriesName"));
            if (rs.getRow()==0){
                return null;
            }

            while(rs.next()){
                titles.add(rs.getString("SeriesName"));
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
     * @param SeriesName string in the SeriesName column of a given row of the series table
     * @return an ArrayList containing strings of all the issueNames with
     * the given SeriesName in the comic table
     */
    public static ArrayList<ArrayList<String>> getIssuesBySeriesName(String SeriesName){
        ArrayList<ArrayList<String>> issues=new ArrayList<ArrayList<String>>();
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT issueName,DateString FROM Issue2 WHERE SeriesName='"+SeriesName+"' order by DateString;");

            rs.next();
            if (rs.getRow()==0){
                return null;
            }

            ArrayList<String> issue=new ArrayList<String>();
            issue.add(rs.getString("issueName"));
            issue.add(rs.getString("DateString"));
            issues.add(issue);

            while(rs.next()){
                issue=new ArrayList<String>();
                issue.add(rs.getString("issueName"));
                issue.add(rs.getString("DateString"));
                issues.add(issue);
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
     * Get the total number of issues in the comic table
     * @return the number of rows in the comic table
     */
    public static int getTotalIssues(){
        Connection connection = connectDB();
        int sum = 0;
        try{
            assert connection != null;
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(*) FROM Issue2;");
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
     * Get the total number of issues in the comic table
     * @return the number of rows in the comic table
     */
    public static int getTotalSeries(){
        Connection connection = connectDB();
        int sum = 0;
        try{
            assert connection != null;
            Statement st = connection.createStatement();
            ResultSet res = st.executeQuery("SELECT COUNT(*) FROM Series2;");
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
                ResultSet res = st.executeQuery("select count(*) FROM Series2 s, Issue2 i where s.`SeriesName`=i.`SeriesName` and s.`Xmen`=1;");
                while (res.next()) {
                    int c = res.getInt(1);
                    sum = sum + c;
                }
                res.close();
                st.close();
            }catch(SQLException e){
                throw new RuntimeException("Problem querying database", e);
            }
            int sum2=0;
            try{
                assert connection != null;
                Statement st = connection.createStatement();
                ResultSet res = st.executeQuery("select count(*) FROM Issue2 where `XmenAdj`=1;");
                while (res.next()) {
                    int c = res.getInt(1);
                    sum2 = sum2 + c;
                }
                res.close();
                st.close();
            }catch(SQLException e){
                throw new RuntimeException("Problem querying database", e);
            }
            sum+=sum2;
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
            ResultSet res = st.executeQuery("select count(*) FROM Series2 s, Issue2 i where s.`SeriesName`=i.`SeriesName` and s.`Publisher`=\'"+Publisher+"\';");
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

    public static ArrayList<Integer> tempTableYear(String year){
        int totalSum=0;
        int xmenSum=0;
        int seriesSum=0;
        ArrayList<Integer> totals=new ArrayList<Integer>();
        Connection connection = connectDB();
        try{
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql = "CREATE TEMPORARY TABLE tempComic AS SELECT * FROM Issue2 WHERE DateString like \'"+year+"%\'';";
            statement.execute(sql);
            statement.close();

            statement = connection.createStatement();
            sql="SELECT DISTINCT SeriesName FROM tempComic;";
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<String> seriesList=new ArrayList<String>();
            rs.next();
            seriesList.add(rs.getString("SeriesName"));
            if (rs.getRow()!=0){
                while(rs.next()){
                    seriesList.add(rs.getString("SeriesID"));
                }
            }
            rs.close();
            statement.close();

            ArrayList<String> pub_list=getPublishers();
            ArrayList<Integer> sums=new ArrayList<Integer>();
            for (int i = 0; i < pub_list.size(); i++) {
                sums.add(0);
            }
            for (String series: seriesList){
                String publisher=getPublisher(series);
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
                    int ind=pub_list.indexOf(publisher);
                    sums.set(ind,sums.get(ind)+addTo);
                    if (seriesIsXmen(series)) {
                        xmenSum += addTo;
                    }
                }
                
            }
            seriesSum=seriesList.size();
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic where XmenAdj=1;");
            rs.next();
            if (rs.getRow()==0){
                xmenSum+=0;
            }
            xmenSum= rs.getInt("count");
            rs.close();

            // for (String series: seriesList){
            //     String publisher=getPublisher(series);
            //     int addTo;
            //     statement = connection.createStatement();
            //     rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic where SeriesName=\'"+series+"\';");
            //     rs.next();
            //     if (rs.getRow()==0){
            //         addTo=0;
            //     }
            //     addTo= rs.getInt("count");
            //     rs.close();
            //     statement.close();
            //     switch(publisher) {
            //         case "Marvel":
            //             marvelSum += addTo;
            //             if (seriesIsXmen(series)) {
            //                 xmenSum += addTo;
            //             }
            //             break;
            //         case "Image":
            //             imageSum += addTo;
            //             break;
            //         case "Dark Horse":
            //             darkHorseSum += addTo;
            //             break;
            //         case "Boom":
            //             boomSum += addTo;
            //             break;
            //         case "DC":
            //             dcSum += addTo;
            //             break;
            //         case "Other":
            //             otherSum += addTo;
            //             break;
            //     }
            // }
            // int addTo;
            
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic;");
            rs.next();
            totalSum=rs.getInt("count");
            rs.close();
            statement.close();
            totals.add(totalSum);
            for (int i=0;i<sums.size();i++){
                totals.add(sums.get(i));
            }
            totals.add(xmenSum);
            totals.add(seriesSum);
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        closeDB(connection);
        return totals;
    }

    public static ArrayList<Integer> tempTableSnap(String year, String month){
        int totalSum=0;
        int xmenSum=0;
        int seriesSum=0;
        ArrayList<Integer> totals=new ArrayList<Integer>();
        Connection connection = connectDB();
        try{
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql;
            
            if ((Integer.parseInt(month)+1)<12){
                sql = "CREATE TEMPORARY TABLE tempComic AS SELECT * FROM Issue2 WHERE DateString<\'"+year+"-"+(month+1)+"%\';";
            }
            else{
                sql = "CREATE TEMPORARY TABLE tempComic AS SELECT * FROM Issue2 WHERE DateString<\'"+(year+1)+"-01%\';";
            }
            statement.execute(sql);
            statement.close();

            statement = connection.createStatement();
            sql="SELECT DISTINCT SeriesName FROM tempComic;";
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<String> seriesList=new ArrayList<String>();
            rs.next();
            seriesList.add(rs.getString("SeriesName"));
            if (rs.getRow()!=0){
                while(rs.next()){
                    seriesList.add(rs.getString("SeriesName"));
                }
            }
            rs.close();
            statement.close();

            ArrayList<String> pub_list=getPublishers();
            ArrayList<Integer> sums=new ArrayList<Integer>();
            for (int i = 0; i < pub_list.size(); i++) {
                sums.add(0);
            }
            for (String series: seriesList){
                String publisher=getPublisher(series);
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
                    int ind=pub_list.indexOf(publisher);
                    sums.set(ind,sums.get(ind)+addTo);
                    if (seriesIsXmen(series)) {
                        xmenSum += addTo;
                    }
                }
                
            }
            seriesSum=seriesList.size();
            // for (String series: seriesList){
            //     String publisher=getPublisher(series);
            //     int addTo;
            //     statement = connection.createStatement();
            //     rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic where SeriesName=\'"+series+"\';");
            //     rs.next();
            //     if (rs.getRow()==0){
            //         addTo=0;
            //     }
            //     addTo= rs.getInt("count");
            //     rs.close();
            //     statement.close();
            //     switch(publisher) {
            //         case "Marvel":
            //             marvelSum += addTo;
            //             if (seriesIsXmen(series)) {
            //                 xmenSum += addTo;
            //             }
            //             break;
            //         case "Image":
            //             imageSum += addTo;
            //             break;
            //         case "Dark Horse":
            //             darkHorseSum += addTo;
            //             break;
            //         case "Boom":
            //             boomSum += addTo;
            //             break;
            //         case "DC":
            //             dcSum += addTo;
            //             break;
            //         case "Other":
            //             otherSum += addTo;
            //             break;
            //     }
            // }
            // int addTo;
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic where XmenAdj=1;");
            rs.next();
            if (rs.getRow()==0){
                xmenSum+=0;
            }
            xmenSum+= rs.getInt("count");
            rs.close();




            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic;");
            rs.next();
            totalSum= rs.getInt("count");
            rs.close();
            statement.close();
            
            totals.add(totalSum);
            for (int i=0;i<sums.size();i++){
                totals.add(sums.get(i));
            }
            totals.add(xmenSum);
            totals.add(seriesSum);
        }catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        closeDB(connection);
        return totals;
        }

    public static ArrayList<Integer> tempTableMonth(String year, String month){
        int totalSum=0;
        int xmenSum=0;
        int seriesSum=0;
        ArrayList<Integer> totals=new ArrayList<Integer>();
        Connection connection = connectDB();
        try{
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql = "CREATE TEMPORARY TABLE tempComic AS SELECT * FROM Issue2 WHERE DateString like \'"+year+"-"+month+"%\';";
            statement.execute(sql);
            statement.close();

            statement = connection.createStatement();
            sql="SELECT DISTINCT SeriesName FROM tempComic;";
            ResultSet rs = statement.executeQuery(sql);
            ArrayList<String> seriesList=new ArrayList<String>();
            rs.next();
            seriesList.add(rs.getString("SeriesName"));
            if (rs.getRow()!=0){
                while(rs.next()){
                    seriesList.add(rs.getString("SeriesName"));
                }
            }
            rs.close();
            statement.close();
            ArrayList<String> pub_list=getPublishers();
            ArrayList<Integer> sums=new ArrayList<Integer>();
            for (int i = 0; i < pub_list.size(); i++) {
                sums.add(0);
            }
            for (String series: seriesList){
                String publisher=getPublisher(series);
                int addTo;
                statement = connection.createStatement();
                rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic where SeriesID=\'"+series+"\'';");
                rs.next();
                if (rs.getRow()==0){
                    addTo=0;
                }
                addTo=rs.getInt("count");
                rs.close();
                statement.close();
                if (pub_list.contains(publisher)){
                    int ind=pub_list.indexOf(publisher);
                    sums.set(ind,sums.get(ind)+addTo);
                    if (seriesIsXmen(series)) {
                        xmenSum += addTo;
                    }
                }
                
            }
            seriesSum=seriesList.size();

            // for (String series: seriesList){
            //     String publisher=getPublisher(series);
            //     int addTo;
            //     statement = connection.createStatement();
            //     rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic where SeriesName=\'"+series+"\';");
            //     rs.next();
            //     if (rs.getRow()==0){
            //         addTo=0;
            //     }
            //     addTo= rs.getInt("count");
            //     rs.close();
            //     statement.close();
            //     switch(publisher) {
            //         case "Marvel":
            //             marvelSum += addTo;
            //             if (seriesIsXmen(series)) {
            //                 xmenSum += addTo;
            //             }
            //             break;
            //         case "Image":
            //             imageSum += addTo;
            //             break;
            //         case "Dark Horse":
            //             darkHorseSum += addTo;
            //             break;
            //         case "Boom":
            //             boomSum += addTo;
            //             break;
            //         case "DC":
            //             dcSum += addTo;
            //             break;
            //         case "Other":
            //             otherSum += addTo;
            //             break;
            //     }
            // }

            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic where XmenAdj=1;");
            rs.next();
            if (rs.getRow()==0){
                xmenSum+=0;
            }
            xmenSum+= rs.getInt("count");
            rs.close();
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT COUNT(*) AS count FROM tempComic;");
            rs.next();
            totalSum= rs.getInt("count");
            rs.close();
            statement.close();
            totals.add(totalSum);
            for (int i=0;i<sums.size();i++){
                totals.add(sums.get(i));
            }
            totals.add(xmenSum);
            totals.add(seriesSum);
        } catch(SQLException e){
            throw new RuntimeException("Problem with database", e);
        }
        closeDB(connection);
        return totals;
    }

    public static boolean seriesIsXmen(String series){
            boolean xmen;
            Connection connection = connectDB();
            try {
                assert connection != null;
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT xmen FROM Series2 WHERE SeriesName=\'"+series+"\';");

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

    /**
     * Get the publisher of a series from the series table
     * @param seriesID integer in the SeriesID column of a given row of the series table
     * @return string in the Publisher column of a given row of the series table
     */
    public static String getPublisher(String SeriesName){
        String Publisher;
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            String sql="SELECT Publisher FROM Series2 WHERE SeriesName=\'"+SeriesName+"\';";
            ResultSet rs = statement.executeQuery(sql);

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
    public static boolean entryExists(String SeriesName, String issueName, String dateString){
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT IssueName FROM Issue2 WHERE SeriesName=\'"+SeriesName+"\' AND issueName=\'"+issueName+"\' AND dateString like '"+dateString+"%';");


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
     * Checks whether or not an entry for a specific issue on a given date has already been made
     * @param SeriesID integer in the SeriesID column of a given row of the comic table
     * @param issueName string in the issueName column of a given row of the comic table
     * @param dateString string in the dateString column of a given row of the comic table
     * @param month integer in the month column of a given row of the comic table
     * @param day integer in the day column of a given row of the comic table
     * @param year integer in the year column of a given row of the comic table
     * @return true if an entry matching all the parameters is found and false if not
     */
    public static boolean seriesExists(String SeriesName){
        Connection connection = connectDB();
        try {
            assert connection != null;
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT SeriesName FROM Series2 WHERE SeriesName=\'"+SeriesName+"\';");


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


}