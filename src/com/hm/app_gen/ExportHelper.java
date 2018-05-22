package app_gen;

import com.hm.app_gen.config.Config;
import com.hm.app_gen.config.Configurations;
import com.hm.app_gen.config.Table;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExportHelper
{

	Configurations configurations;

	public ExportHelper(Configurations configurations)
	{
		this.configurations = configurations;
	}

	public void export()
	{
		//Loop for all configurations
		for (Config config : configurations.getConfigs())
		{
			//Create dump from actual database
			createDump(configurations.getMysqlBinPath(),
					config.getDatabase().getName(),
					config.getDatabase().getUsername(),
					config.getDatabase().getPassword(), GV.TEMP_DIR + "/temp");

			//Create new database
			try
			{
				createNewDatabase(config.getDatabase().getUrl(),
						config.getDatabase().getUsername(),
						config.getDatabase().getPassword(),
						"for_dump");
			}
			catch (ClassNotFoundException ex)
			{
				Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, null, ex);
			}
			catch (SQLException ex)
			{
				Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, null, ex);
			}

			//Import dump
			importDump(configurations.getMysqlBinPath(), "for_dump", config.getDatabase().getUsername(), config.getDatabase().getPassword(), GV.TEMP_DIR + "/temp");

			StringBuilder sql = new StringBuilder();
			sql.append("SET FOREIGN_KEY_CHECKS=0;");

			//Truncate all mentioned tables
			if (config.getTruncate().getTable() != null && config.getTruncate().getTable().size() != 0)
			{
				for (Table table : config.getTruncate().getTable())
				{
					sql.append(getTruncateSql(table.getName()));
				}
			}

			//Clear
			if (config.getClear().getTable() != null && config.getClear().getTable().size() != 0)
			{
				for (Table table : config.getClear().getTable())
				{
					if (table.getIds() == null)
					{
						sql.append(getClearSql(table.getName(), table.getColumn()));
					}
					else
					{
						for (int i : table.getIds())
						{
							sql.append(getClearSql(table.getName(), table.getColumn(), i));
						}
					}
				}
			}
			sql.append("SET FOREIGN_KEY_CHECKS=1;");

			try
			{
				Connection connection = new MysqlHelper().connect(
						config.getDatabase().getUrl(),
						config.getDatabase().getUsername(),
						config.getDatabase().getPassword(),
						config.getDatabase().getName());

				Statement statement = connection.createStatement();
				String[] sqls = sql.toString().split(";");
				for (String sql1 : sqls)
				{
					statement.execute(sql1);
				}

			}
			catch (ClassNotFoundException ex)
			{
				Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, null, ex);
			}
			catch (SQLException ex)
			{
				Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, null, ex);
			}

			//Create dump
			createDump(configurations.getMysqlBinPath(),
					config.getDatabase().getName(),
					config.getDatabase().getUsername(),
					config.getDatabase().getPassword(),
					config.getOutputFileName());

			try
			{
				dropDatabase(config.getDatabase().getUrl(),
						config.getDatabase().getUsername(),
						config.getDatabase().getPassword(),
						"for_dump");
			}
			catch (ClassNotFoundException ex)
			{
				Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, null, ex);
			}
			catch (SQLException ex)
			{
				Logger.getLogger(ExportHelper.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private void createDump(String mysqlBinDir, String dbName, String username, String password, String outputFilePath)
	{

		try
		{
			String[] vExecuteCmd = new String[]
			{
				"cmd.exe",
				"/c",
				mysqlBinDir + "/mysqldump.exe -u " + username + " -p" + password + " " + dbName + " > " + outputFilePath
			};

			Process runtimeProcess = Runtime.getRuntime().exec(vExecuteCmd);

			InputStream is;
			if (runtimeProcess.waitFor() == 0)
			{
				//normally terminated, a way to read the output
				is = runtimeProcess.getInputStream();
				System.out.println("blank to sql success");
			}
			else
			{
				// abnormally terminated, there was some problem
				//a way to read the error during the execution of the command
				is = runtimeProcess.getErrorStream();
				System.out.println("blank to sql error");

			}
			byte[] buffer = new byte[is.available()];
			is.read(buffer);

			String str = new String(buffer);
			System.out.println(str);

		}
		catch (Exception ex)
		{
			System.out.println("Error at Backuprestore" + ex.getMessage());
		}

	}

	private void importDump(String mysqlBinDir, String dbName, String username, String password, String inputFilePath)
	{

		try
		{
			String[] executeCmd = new String[]
			{
				"cmd.exe",
				"/c",
				mysqlBinDir + "/mysql.exe", dbName, "-u" + username, "-p" + password, "-e", " source " + inputFilePath
			};

			/*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
			Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
			int processComplete = runtimeProcess.waitFor();

			//Wait for the command to complete, and check if the exit value was 0 (success)
			InputStream is;
			if (processComplete == 0)
			{
				//normally terminated, a way to read the output
				is = runtimeProcess.getInputStream();
				System.out.println("sql to db success");
			}
			else
			{
				// abnormally terminated, there was some problem
				//a way to read the error during the execution of the command
				is = runtimeProcess.getErrorStream();
				System.out.println("sql to db error");

			}
			byte[] buffer = new byte[is.available()];
			is.read(buffer);

			String str = new String(buffer);
			System.out.println(str);

		}
		catch (Exception ex)
		{
			System.out.println("Error at Restoredbfromsql" + ex.getMessage());
		}

	}

	private void createNewDatabase(String url, String username, String password, String database) throws ClassNotFoundException, SQLException
	{
		Connection connection = new MysqlHelper().connect(url, username, password);

		Statement s = connection.createStatement();
		s.execute("CREATE DATABASE `" + database + "`");

		s.close();
		connection.close();

	}

	private void dropDatabase(String url, String username, String password, String database) throws ClassNotFoundException, SQLException
	{
		Connection connection = new MysqlHelper().connect(url, username, password);

		Statement s = connection.createStatement();
		s.execute("DROP DATABASE `" + database + "`");

		s.close();
		connection.close();

	}

	private String getTruncateSql(String tableName)
	{
		return "TRUNCATE `" + tableName + "`;";
	}

	private String getClearSql(String tableName, String column)
	{
		return "UPDATE `" + tableName + "` SET `" + column + "`='';";
	}

	private String getClearSql(String tableName, String column, int id)
	{
		return "UPDATE `" + tableName + "` SET `" + column + "`='' WHERE `id`=" + String.valueOf(id) + ";";
	}
}
