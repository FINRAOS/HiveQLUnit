# Writing HiveQLUnit Tests #

This user guide explains how to write a unit test using the tools provided by HiveQLUnit. HiveQLUnit unit tests are built just like any other JUnit unit tests, with a dedicated testing class containing test methods annotated with @Test. HiveQLUnit provides a number of TestRules which integrate with the JUnit framework along with other utilities for writing JUnit tests focused around Hive.

## Resources ##

HiveQLUnit provides utilities to access scripts and data needed during testing.

### TextResource ###

The TextResource interface abstracts access to resources like scripts and testing data needed in a unit test. Resources can be pulled from the src/test/resources folder bundled into the jar, from the local file system, or passed as raw text programatically.

    public interface TextResource {
    
        public String resourceText();
    }

    new TextLiteralResource("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)")
    new LocalFileResource("C:/cygwin64/home/K00001/testdata.txt")
    new ResourceFolderResource("/testdata.txt")

Many other tools provided by HiveQLUnit take a TextResource as an input. Because of this, theses tools can work with many different data sources.

### HqlScript ###

The HqlScript interface abstracts how to parse and run an hql script on a hive server. All implimentations of HqlScript take in a TextResource object containing the script to execute.

    new MultiExpressionScript(
        new TextLiteralResource("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)")
    )

    new SingleExpressionScript(
        new TextLiteralResource("DROP TABLE IF EXISTS src")
    )

## JUnit Integration ##

    @ClassRule
    public static TestHiveServer hiveServer = new TestHiveServer();

    @Rule
    public static TestDataLoader loader = new TestDataLoader(hiveServer);

    @Rule
    public static SetUpHql prepSrc =
            new SetUpHql(
                    hiveServer,
                    new MultiExpressionScript(
                            new TextLiteralResource("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)")
                    )
            );

    @Rule
    public static TearDownHql cleanSrc =
            new TearDownHql(
                    hiveServer,
                    new SingleExpressionScript(
                            new TextLiteralResource("DROP TABLE IF EXISTS src")
                    )
            );

TestRules are java objects which integrate with the JUnit framework and alter how unit tests are run. HiveQLUnit provides a number of TestRules which support unit tests focused around Hive.

### TestHiveServer ###

    @ClassRule
    public static TestHiveServer hiveServer = new TestHiveServer();

The TestHiveServer constructs an instance of HiveContext, which provides access to a disposable Hive cluster created just for testing by means of Spark. TestHiveServer needs to be the first TestRule used in a given test class, and must be annotated with the @ClassRule annotation.

### TestDataLoader ###

    @Rule
    public static TestDataLoader loader = new TestDataLoader(hiveServer);

The TestDataLoader provides utility methods accesible from within test methods for loading data out of a TextResource and into a Hive table.

    @Test
    public void test() {
        loader.loadDataIntoTable("src", new LocalFileResource("C:/cygwin64/home/K00001/testdata.txt"), "");

        HiveContext sqlContext = hiveServer.getHiveContext();
        Row[] results = sqlContext.sql("SELECT key FROM src WHERE key = 5").collect();
        Assert.assertEquals(3, results.length);
    }

### SetUpHql ###

A test class can have multiple SetUpHql test rules. Each one takes in an HqlScript with a TextResource and runs the given script on the Hive server before every test method. All SetUpHqls are run before a @Test method, but the execution order of the different SetUpHqls is non deterministic. Be sure each SetUpHql can run independently of the others.

### TearDownHql ###

TearDownHql is exactly like SetUpHql, except it runs after a @Test method executes. Though the Hive server created by HiveQLUnit is disposable, things like the metastore often persist and need to be cleaned between tests. Be sure to drop tables and such after after every test to keep Hive clean.