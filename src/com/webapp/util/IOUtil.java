package com.webapp.util;

import java.io.*;

public class IOUtil {

    public static void read(String[] data, String filename) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
            String line;
            int count = 0;
            // if it is null then we are done
            while ((line = bufferedReader.readLine()) != null) {
                data[count] = line;
                count++;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
