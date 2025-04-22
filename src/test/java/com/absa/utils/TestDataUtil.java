// src/test/java/com/absa/utils/TestDataUtil.java
package com.absa.utils;

import com.absa.models.UserData;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class TestDataUtil {
    private static UserData latestUser;

    // Reads user data from a CSV file. Assumes the first row is a header.
    public static UserData getUserDataFromCsv(String fileName, int rowIndex) {
        List<UserData> users = new ArrayList<>();
        String filePath = "src/test/resources/testdata/" + fileName;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip header row
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // CSV uses comma separator.
                // CSV columns: FirstName,LastName,UserName,Password,Company,Role,Email,Mobilephone
                if (data.length >= 8) {
                    UserData user = new UserData(
                        data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(),
                        data[4].trim(), data[5].trim(), data[6].trim(), data[7].trim()
                    );
                    users.add(user);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users.get(rowIndex);
    }

    // Save the latest user for later verification.
    public static void setLatestUser(UserData user) {
        latestUser = user;
    }

    public static UserData getLatestUser() {
        return latestUser;
    }
}
