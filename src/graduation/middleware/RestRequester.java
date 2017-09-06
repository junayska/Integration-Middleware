package graduation.middleware;


import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import graduation.middleware.model.StudentInfo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Oletus
 */
public class RestRequester {
    
    
    
    
    public StudentInfo getStudentInfo(int studentnro){
         StudentInfo si;
	  try {
              
              Gson gson = new Gson();

		URL url = new URL("http://localhost:8080/StudentAdministration/rest/students/" + studentnro);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
					+ conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));

                si = gson.fromJson(br.readLine(), StudentInfo.class);
                System.out.println("(RestRequester.java) getStudentInfoFromRESTService: (StudentInfo object) Mentor: " + si.getMentor());
		System.out.println("(RestRequester.java) getStudentInfoFromRESTService: (StudentInfo object) ECs: " + si.getEcs());
		conn.disconnect();
                return si;

	  } catch (MalformedURLException e) {
		e.printStackTrace();
	  } catch (IOException e) {
		e.printStackTrace();
	  }
        return null;
        }
}

