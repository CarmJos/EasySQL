package cc.carm.lib.easysql;

import cc.carm.lib.easysql.api.transaction.SQLSavepoint;
import cc.carm.lib.easysql.api.transaction.SQLTransaction;

public class TransactionTest {


    public void demo() {


        try (SQLTransaction transaction = createTransaction()) {

            transaction.updateInto("A")
                    .set("name", "CARM")
                    .addCondition("name", "CUMR")
                    .build().execute();

            SQLSavepoint pointA = transaction.savepoint("TEST");

            transaction.insertInto("A")
                    .columns("name")
                    .values("TEST")
                    .execute();

            try {
                transaction.commit(); // 提交
            } catch (Exception ex) {
                transaction.rollback(pointA); // 出错回滚到pointA
                transaction.commit(); // 提交快照前的代码
            }

            pointA.release(); // release savepoint (结束后也会被自动释放)

        } catch (Exception ex) {

        }


    }


    protected SQLTransaction createTransaction() {
        return null;
    }

}
