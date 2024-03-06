package config.integracionQTest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ConnectQtest {
    public JSONObject getTestCase(String testCaseID) {
        JSONObject jsonObject = new JSONObject();
        try {
            URL obj = new URL(Constants.API_GET_TESTCASE.replaceAll("\\{testID}",testCaseID));
            System.out.println("Metodo GET: "+obj);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", Constants.AUTHORIZATION); // Se setea el token de autorizacion
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setDoOutput(true);
            BufferedReader res = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();


            while ((inputLine = res.readLine()) != null) {
                response.append(inputLine);
            }
            res.close();
            if (!response.toString().contains("LOGIN_REDIRECT_PAGE_ID")) return new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;

    }


    public JSONObject getModule(String moduleID){
        try {
            URL obj = new URL(Constants.API_GET_MODULE.replaceAll("\\{module}",moduleID));
            System.out.println("Metodo GET: "+obj);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", Constants.AUTHORIZATION); // Se setea la COOKIE de sesion
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setDoOutput(true);
            BufferedReader res = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = res.readLine()) != null) {
                response.append(inputLine);
            }
            res.close();
            if (!response.toString().contains("LOGIN_REDIRECT_PAGE_ID")) return new JSONObject(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray getAllModules(){
        try {
            URL obj = new URL("https://tsoftlatam.qtestnet.com/api/v3/projects/96021/modules?parentId=6989741&expand=descendants");
            System.out.println("Metodo GET: "+obj);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", Constants.AUTHORIZATION); // Se setea la COOKIE de sesion
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setDoOutput(true);
            BufferedReader res = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = res.readLine()) != null) {
                response.append(inputLine);
            }
            res.close();
            if (!response.toString().contains("LOGIN_REDIRECT_PAGE_ID")) return new JSONArray(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
