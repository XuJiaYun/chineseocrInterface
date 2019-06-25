
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Encoder;
import sun.misc.Cleaner;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class ToOcr {

    public static String URI = "http://192.168.30.179:7654/ocr";
    public static String testPath = "D:\\test.jpg";

    public static void main(String[] args) {
        doOCR();
    }

    public static String encodeBase64File(String path) {
        FileInputStream inputFile;
        byte[] buffer = null;
        File file = new File(path);
        try {
            inputFile = new FileInputStream(file);
            buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BASE64Encoder().encode(buffer);
    }
    public static void printJson(JSONObject jsonObject){
        StringBuilder sb = new StringBuilder();
        JSONArray jsonArray = jsonObject.getJSONArray("res");
        for(int i = 0 ;i < jsonArray.size();i++){
            JSONObject job = jsonArray.getJSONObject(i);
            sb.append(job.get("text"));
            sb.append("\n");
            //System.out.println(job.get("text"));
        }
        String result = sb.toString();
        System.out.println(result);
        //return result;
    }


    public static void doOCR(){
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(URI);//建立HttpPost对象,改成自己的地址

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imgString", "data:image/jpeg;base64," + encodeBase64File(testPath));
        jsonObject.put("billModel", "通用OCR");
        jsonObject.put("textAngle","true");
        StringEntity entity = new StringEntity(jsonObject.toString(), "utf-8");
        HttpResponse response = null;//发送Post,并返回一个HttpResponse对象
        httpPost.setHeader("Content-type", "text/plain;");
        httpPost.setEntity(entity);
        try {
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {//如果状态码为200,就是正常返回
                String temp = EntityUtils.toString(response.getEntity());
                String result = new String(temp.getBytes("ISO-8859-1"), "UTF-8");
                JSONObject jsonResult = JSONObject.parseObject(result);
                printJson(jsonResult);
                //System.out.print(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
