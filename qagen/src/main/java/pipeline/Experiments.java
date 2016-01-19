package pipeline;

import config.DBConfig;

public class Experiments {
	public static void main(String[] args) {
		DBConfig dbconf = DBConfig.getInstance();
		
		System.out.println(dbconf.host);
		System.out.println(dbconf.database);
		System.out.println(dbconf.user);
		System.out.println(dbconf.password);
	}
}
