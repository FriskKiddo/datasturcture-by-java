package com.kiddo.list;

/**
 * @author FriskKiddo
 */
public class Assert {
    public static void test(boolean value) {
        if (!value) {
            try {
                throw new Exception("test fails");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
