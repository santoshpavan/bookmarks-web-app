package com.webapp.util;

import java.io.*;
import java.util.List;

public class IOUtil {

    public static void read(List<String> data, String filename) { //reading from a file
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {
            String line;
            // if it is null then we are done
            while ((line = bufferedReader.readLine()) != null) {
                data.add(line);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(InputStream inputStream) {//reading from a webpage - to cache weblinks
        StringBuilder text = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line).append("\n");
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return text.toString();
    }

    public static void write(String webpage, long id) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("C:/MyStuff/Projects/java/bookmarks-web-app/pages/" + String.valueOf(id) + ".html"), "UTF-8"))) {
            writer.write(webpage);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
