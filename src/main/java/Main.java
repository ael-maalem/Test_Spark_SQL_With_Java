import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.*;


import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class Main {

    public static void main(String[] args) {

        SparkSession spark = SparkSession
                .builder()
                .appName("Spring Boot App with Spark SQL")
                .master("local[*]")
                .getOrCreate();

        Encoder<Orders> orderEncoder = Encoders.bean(Orders.class);

        /*SparkConf conf = new SparkConf()
                .setAppName("DataFrame")
                .set("spark.driver.allowMultipleContexts", "true")
                .setMaster("local[*]");

        JavaSparkContext sc = new JavaSparkContext(conf);

        SQLContext sqlContext = new SQLContext(sc);*/


        StructType customSchema = new StructType(new StructField[] {
                new StructField("orderId", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("date", DataTypes.DateType, true, Metadata.empty()),
                new StructField("quantity", DataTypes.IntegerType, true, Metadata.empty()),
                new StructField("sales", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("mode", DataTypes.StringType, true, Metadata.empty()),
                new StructField("profit", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("unitPrice", DataTypes.DoubleType, true, Metadata.empty()),
                new StructField("customerName", DataTypes.StringType, true, Metadata.empty()),
                new StructField("customerSegment", DataTypes.StringType, true, Metadata.empty()),
                new StructField("productCategory", DataTypes.StringType, true, Metadata.empty())
        });

        Dataset<Orders> ordersDataset = spark.read()
                .option("header","true")
                .option("treatEmptyValuesAsNulls", "true")
                //.option("inferSchema", "true") this schema used by spark
                .schema(customSchema) // this schema used by user ref"https://github.com/databricks/spark-csv"
                .option("mode","DROPMALFORMED")
                .option("dateFormat", "MM-dd-yyyy") // it will be display in spark like this: yyyy-MM-dd
                //.option("timestampFormat", "MM-dd-yyyy") don't need this option when we use our schema
                .option("delimiter",",")
                .csv("src/main/resources/Orders.csv")
                .as(orderEncoder);

        ordersDataset.show();

        ordersDataset.printSchema();
        /*
        root
                |-- orderId: integer (nullable = true)
                |-- date: date (nullable = true)
                |-- quantity: integer (nullable = true)
        */

        //TODO: Done
        // 1- SELECT * FROM ORDERS;
        //ordersDataset.show((int) ordersDataset.count());
        /*
        +-------+----------+--------+---------+--------------+--------+---------+------------------+---------------+---------------+
        |orderId|      date|quantity|    sales|          mode|  profit|unitPrice|      customerName|customerSegment|productCategory|
        +-------+----------+--------+---------+--------------+--------+---------+------------------+---------------+---------------+
        |      3|2010-10-13|       6|   261.54|   Regular Air| -213.25|    38.94|Muhammed MacIntyre| Small Business|Office Supplies|
        +-------+----------+--------+---------+--------------+--------+---------+------------------+---------------+---------------+
        only showing top all rows
        */

        //TODO: Done
        // 2- Get Order By Customer Name
        /*Dataset<Orders> names = ordersDataset
                .filter((FilterFunction<Orders>) order -> order.getCustomerName().equals("Liz Pelletier"));
        names.show((int) ordersDataset.count());*/
        //.map((MapFunction<Orders, String>) (Orders order) -> order.getCustomerName(), Encoders.STRING())


        //TODO: Done
        // 3- Get Order(Name,Sales,Profit,Category)  By Customer Name and Order Date
        /*Dataset<Row> orders = ordersDataset
                .filter((FilterFunction<Orders>) order -> order.getCustomerName().equals("Liz Pelletier"))
                .select("customerName","date","sales","profit","productCategory")
                .where("date == \"2008-07-15\"");
        orders.show();*/


        //TODO: Done
        // 4- Get Order By Product Category When Profit must be grater than 0 And Sorted by Customer Name
        /*Dataset<Orders> orders = ordersDataset
                .filter((FilterFunction<Orders>) order -> order.getProductCategory().equals("Office Supplies"))
                .filter("profit > 0.0")
                .sort("customerName"); // Or create a Comparator

        orders.show();*/
        //TODO:
        // 5-Stream :Get Order By Product Category In period and sorted by Sales
        /*Dataset<Row> orders = ordersDataset
                .filter((FilterFunction<Orders>) order -> order.getProductCategory().equals("Office Supplies"))
                .sort("customerName","date")
                .where("date < \"2011-08-31\" and date > \"2010-03-16\"")
                .select("customerName","date","sales","quantity","profit","unitPrice","customerSegment");

        orders.show();*/
    }

}
