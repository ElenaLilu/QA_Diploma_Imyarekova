package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.Connection;
public class DbUtils {
    private static String url = System.getProperty("datasource.url");
    private static String user = "app";
    private static String password = "pass";

    @SneakyThrows
    public static String getStatus(String status) {
        QueryRunner runner = new QueryRunner();

        try (
                Connection conn = DriverManager.getConnection(
                        url, user, password);

        ) {
            String result = runner.query(conn, status, new ScalarHandler<>());
            System.out.println(result);
            return result;
        }
    }

    @SneakyThrows
    public static String getStatusPayment() {
        String statusSQL = "SELECT status FROM payment_entity ORDER BY created DESC LIMIT 1";
        return getStatus(statusSQL);
    }

    @SneakyThrows
    public static String getStatusCredit() {
        String statusSQL = "SELECT status FROM credit_request_entity ORDER BY created DESC LIMIT 1";
        return getStatus(statusSQL);
    }

    @SneakyThrows
    public static void deleteTables() {
        QueryRunner runner = new QueryRunner();
        String deleteCredit = "DELETE FROM credit_request_entity";
        String deleteOrder = "DELETE FROM order_entity";
        String deletePayment = "DELETE FROM payment_entity";

        try (
                Connection conn = DriverManager.getConnection(
                        url, user, password);
        ) {
            runner.update(conn, deleteCredit);
            runner.update(conn, deleteOrder);
            runner.update(conn, deletePayment);
        }

    }

}
