package org.whut.utils;

public class UrlStrings {

	/** IP地址 */
	public static final String BASE_URL="http://10.138.68.79:8080/iccard/";

//	public static final String BASE_URL="http://192.168.1.102:8080/iccard/";

	/** 提交用户名密码,获取ticket */
	public static final String TICKET_URL = BASE_URL + "rest/account/getTicket";

	/** 根据ticket获取SESSIONID */
	public static final String SESSIONID_URL = BASE_URL + "rest/account/getSessionId";

	/** 当前登录用户信息 */
	public static final String CURUSER_URL = BASE_URL + "rest/account/getCurrentUser";

	/** 获取当前用户维修任务列表 */
	public static final String REPAIRS_URL = BASE_URL + "rest/repair/getRepairTasks";

	/** 提交维修任务 */
	public static final String POST_REPAIR_URL = BASE_URL + "rest/repair/postRepairTasks";

	/** 获取当前用户安装列表 */
	public static final String INSTALLATIONS_URL = BASE_URL + "rest/installation/getInstallationTasks";
}
