package cc.carm.lib.easysql.testrunner;

import cc.carm.lib.easysql.EasySQL;
import cc.carm.lib.easysql.api.SQLManager;
import cc.carm.lib.easysql.testrunner.tests.DeleteTest;
import cc.carm.lib.easysql.testrunner.tests.QueryCloseTest;
import cc.carm.lib.easysql.testrunner.tests.QueryFunctionTest;
import cc.carm.lib.easysql.testrunner.tests.TableCreateTest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Scanner;
import java.util.Set;

@TestOnly
@SuppressWarnings("all")
public class Main {

	public static void main(String[] args) {
		if (args.length < 4) {
			print("请提供以下参数：<数据库驱动> <数据库地址(JDBC)> <数据库用户> <数据库密码> [是否采用DEBUG(y/n)]");
			return;
		}

		SQLManager sqlManager;
		try {
			print("初始化 SQLManager...");
			print("数据库驱动 %s", args[0]);
			print("数据库地址 %s", args[1]);
			print("数据库用户 %s", args[2]);
			print("数据库密码 %s", args[3]);
			sqlManager = EasySQL.createManager(args[0], args[1], args[2], args[3]);
			sqlManager.setDebugMode(args.length > 4);
			print("SQLManager 初始化完成！");
		} catch (Exception exception) {
			print("SQLManager 初始化失败，请检查数据库配置。");
			exception.printStackTrace();
			return;
		}

		print("加载测试类...");
		Set<EasySQLTest> tests = new LinkedHashSet<>();
		tests.add(new TableCreateTest());
//		tests.add(new TableAlterTest());
//		tests.add(new TableRenameTest());
//		tests.add(new QueryNotCloseTest());
		tests.add(new QueryCloseTest());
//		tests.add(new SQLUpdateBatchTests());
//		tests.add(new SQLUpdateReturnKeysTest());
		tests.add(new QueryFunctionTest());
		tests.add(new DeleteTest());

		print("准备进行测试...");

		int index = 1;
		int success = 0;

		Iterator<EasySQLTest> testIterator = tests.iterator();

		print("准备完成，请按回车键开始执行测试。");

		while (testIterator.hasNext()) {
			Scanner scanner = new Scanner(System.in);

			if (scanner.nextLine() != null) {

				EasySQLTest currentTest = testIterator.next();

				if (currentTest.executeTest(index, sqlManager)) {
					success++;
				}

				index++;
			}

		}

		print(" ");
		print("全部测试执行完毕，成功 %s 个，失败 %s 个。",
				success, (tests.size() - success)
		);

		EasySQL.shutdownManager(sqlManager);

	}

	public static void print(@NotNull String format, Object... params) {
		System.out.printf((format) + "%n", params);
	}

}
