package hydra.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

/**
 *
 * @author EKiselyov
 */
public class dbcache  {
  static private PoolDataSource pds = null;
  static private String server_url = "jdbc:oracle:thin:@//10.10.10.10:1521/idltd";
  static private String server_user = "armvz";
  static private String server_password = "armvz";

  static private String min_poolsize = "1";
  static private String max_poolsize = "50";
  static private String initial_poolsize = "1";
  static private String inactiveconnectiontimeout = "60";

  
  public static Connection getConnection()
    throws SQLException, IOException {
      
    Connection conn;
    if (pds == null || pds.getValidateConnectionOnBorrow()) {
           // Читаем конфигурационный файл
           Properties props = new Properties();
           props.load(new FileInputStream(new File(System.getProperty("user.home")+"/armvz.ini")));
           server_url = String.valueOf(props.getProperty("hydra.pool.url", "jdbc:oracle:thin:@//11.10.10.10:1521/idltd"));
           server_user = String.valueOf(props.getProperty("hydra.pool.user", "whiteant_guest"));
           server_password = String.valueOf(props.getProperty("hydra.pool.password", "chupacabra"));
           min_poolsize = String.valueOf(props.getProperty("hydra.pool.minpoolsize", "1"));
           max_poolsize = String.valueOf(props.getProperty("hydra.pool.maxpoolsize", "50"));
           initial_poolsize = String.valueOf(props.getProperty("hydra.pool.initialpoolsize", "1"));
           inactiveconnectiontimeout = String.valueOf(props.getProperty("hydra.pool.inactiveconnectiontimeout", "60"));
           
           //Creating a pool-enabled data source
           pds = PoolDataSourceFactory.getPoolDataSource();
           //Setting connection properties of the data source
           pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
           pds.setURL(server_url);
           pds.setUser(server_user);
           pds.setPassword(server_password);

           //Setting pool properties
           pds.setInitialPoolSize(Integer.parseInt(initial_poolsize));
           pds.setMinPoolSize(Integer.parseInt(min_poolsize));
           pds.setMaxPoolSize(Integer.parseInt(max_poolsize));
           pds.setInactiveConnectionTimeout(Integer.parseInt(inactiveconnectiontimeout));

           //Borrowing a connection from the pool
           conn = pds.getConnection();
           
    }
    else conn = pds.getConnection();
    
    return conn;
  }

}