import org.fostash.atomic.oracle.Factory;
import org.fostash.atomic.oracle.Select;
import org.fostash.atomic.storer.Storer;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.function.Function;

import static org.fostash.atomic.oracle.Factory.select;
import static org.fostash.atomic.oracle.Factory.update;
import static org.fostash.atomic.oracle.Select.Column.column;
import static org.fostash.atomic.oracle.Table.table;
import static org.fostash.atomic.oracle.Update.Set.set;

/**
 *
 * Created by Fausto on 23/02/16.
 */
public class TestAtomic {

    public void selectAll() {

    }


    public static void main(String args[]) throws SQLException {

        /*DBFactoryPool.setupDriver(() -> new HashMap<DBFactoryPool.SetupInformation.Keys, String>() {
            { put(DBFactoryPool.SetupInformation.Keys.DRIVER, "oracle.jdbc.driver.OracleDriver"); }
            { put(DBFactoryPool.SetupInformation.Keys.URL, "fost.cqergzfholaz.us-east-1.rds.amazonaws.com:1521:ORCL"); }
            { put(DBFactoryPool.SetupInformation.Keys.USER, "fost"); }
            { put(DBFactoryPool.SetupInformation.Keys.PASSWORD, "fost"); }
            { put(DBFactoryPool.SetupInformation.Keys.POOLNAME, "fost"); }
        });*/

        Select s = new Select();

        System.out.println(s.select()
                .from(
                        table("table", "t1").innerJoin("table2", "t2").on("t1.col1", "t2.col1")
                )
                .where()
                    .eq("t1.a", "2")
                    .and().in("b", new Object[] { "4", "asd" }));

        /*JsonParser jp = new JsonParser();
        try {
            JsonObject jsonObject = (JsonObject) jp.parse(new FileReader("src/main/java/org/fostash/querytest.json"));
            JsonObject testJson = jsonObject.get("testJson").getAsJsonObject();
            System.out.println(testJson.get("select"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/

        //final ResultSetIterator rsi = new ResultSetIterator(c, "SELECT * FROM JSON_TEST");

        //final List<JsonElement> r = SQL.stream("SELECT t.*, rownum as rn FROM JSON_TEST t ORDER BY id").collect(toList());
        long start = System.currentTimeMillis();
        System.out.println("start at:" + start);

        System.out.println(
                select(column("username"), column("email"))
                        .from(table("user"))
                        .where().gt("id", 0)
        );

        System.out.println("Join test " +
                select(column("col1"))
                    .from(
                        table("table", "tAlias").innerJoin("iTable", "itAlias").on("tAlias.col1", "itAlias.col1")
                            .innerJoin("table2", "t2").on("tAlias.col2", "t2.col2")
                    )
                    .where().eq("id", 1).and().gt("age", "32").orderBy(column("col1"), column("col2"))
        );

        System.out.println(
                Factory.update(table("table"))
                        .set(set("c1", "v1"), set("c2", "v2"))
                        .where().eq("id", 1)
        );

        System.out.println(
                Factory.insert().into(table("table"), column("column1"), column("column2"), column("column3"))
                        .values(1, "due", 4, System.currentTimeMillis())
        );

        System.out.println(
                Storer.read(select().from(table("employee")))
        );

        long end = System.currentTimeMillis();
        System.out.println("end at:" + end);
        System.out.println("time spent:" + (end - start));

        System.out.println(
                update(table("table")).set(set("col1", 3), set("col2", 4), set("col4", "asd"))
                        .where().eq("colN", "qwe")
        );

        Function<JSONObject, Test> converter = jo -> Test.factory(jo.getString("field1"), jo.getString("field2"));

        Storer.read(select().from(table("table")), converter);

        /*try {
            DBFactoryPool.printDriverStats();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            DBFactoryPool.shutdownDriver();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public static final class Test {
        private final String field1;
        private final String field2;

        private Test(String field1, String field2) {
            this.field1 = field1;
            this.field2 = field2;
        }

        public static Test factory(final String field1, final String field2) {
            return new Test(field1, field2);
        }
    }
}
