/*
 * Copyright 2016 HiveQLUnit Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.finra.hiveqlunit.rules;

import org.finra.hiveqlunit.resources.TextResource;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This TestRule class provides utility functions for extracting testing data from resources
 * and loading it into Hive tables.
 */
public class TestDataLoader implements TestRule {

    private TestHiveServer hiveServer;
    private TemporaryFolder stagingLocation;

    /**
     * Constructs a new TestDataLoader.
     *
     * @param hiveServer the TestHiveServer to load data into
     */
    public TestDataLoader(TestHiveServer hiveServer) {
        this.hiveServer = hiveServer;
        stagingLocation = new TemporaryFolder();
    }

    /**
     * Uses a TemporaryFolder rule to process a given statement. The TemporaryFolder will be
     * used to construct staging files for loading data into Hive.
     *
     * @param base the statement to apply a TemporaryFolder to
     * @param description the description to apply a TemporaryFolder to
     * @return the output of the TemporaryFolder rule
     */
    @Override
    public Statement apply(Statement base, Description description) {
        return stagingLocation.apply(base, description);
    }

    /**
     * Loads data from a TextResource into a hive table. A temporary file on the local file
     * system is used as a staging ground for the data.
     *
     * @param tableName the name of the table
     * @param tableDataResource the resource to extract data from
     */
    public void loadDataIntoTable(String tableName, TextResource tableDataResource) {
        loadDataIntoTable(tableName, tableDataResource, "");
    }

    /**
     * Loads data from a TextResource into a hive table. A temporary file on the local file
     * system is used as a staging ground for the data.
     *
     * @param tableName the name of the table
     * @param tableDataResource the resource to extract data from
     * @param partitionInfo the optional partitioning commands at the end of a 'LOAD DATA' query
     */
    public void loadDataIntoTable(String tableName, TextResource tableDataResource, String partitionInfo) {
        try {
            File stagingFile = stagingLocation.newFile(tableName + ".dat");
            String tableData = tableDataResource.resourceText();
            PrintWriter writer = new PrintWriter(stagingFile.getAbsoluteFile());
            writer.print(tableData);
            writer.close();

            hiveServer.getHiveContext().sql("LOAD DATA LOCAL INPATH '"
                    + stagingFile.getAbsolutePath().replace("\\", "/")
                    + "' INTO TABLE "
                    + tableName
                    + " "
                    + partitionInfo);
        } catch (IOException e) {
            throw new RuntimeException("Failure to load table data");
        }
    }
}
