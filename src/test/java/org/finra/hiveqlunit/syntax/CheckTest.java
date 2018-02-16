package org.finra.hiveqlunit.syntax;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.finra.hiveqlunit.syntax.Check.verify;

/**
 * Created by ciuccioj on 5/8/17.
 */
public class CheckTest {

    /**
     * Check Tester.
     *
     * @author <Authors name>
     * @since <pre>May 8, 2017</pre>
     * @version 1.0
     */


        @Before
        public void before() throws Exception {
        }

        @After
        public void after() throws Exception {
        }

        /**
         *
         * Method: verify(String createTableStatement, String sqlStatement)
         * dont run becuase no memeory on github build server
         */
        @Ignore
        public void testVerify() throws Exception {

            Boolean isValid = verify("Create table if not exists trial (cola string, colb string) " +
                            "ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' ",
                    "select * from trial");

            assert isValid;



        }

        //failure case with bad syntax
        @Ignore
    public void testVerify2() throws Exception {

        Boolean isValid = null;
        try {
            isValid = verify("Create table if not exists trial (cola string, colb string) " +
                            "ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' ",
                    "select z from trial");
        } catch (Exception e) {
            System.out.println("this should Fail");
            e.printStackTrace();
            isValid = false;
        }

        assert !isValid;



    }

    
    }


