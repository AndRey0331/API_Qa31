package contactokhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ResponseDeleteByIdDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;

public class DeleteByIdTestOkHttp {
    public static final MediaType JSON = MediaType.get("application/json;charset=utf-8");
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6Im5vYUBnbWFpbC5jb20ifQ.G_wfK7FRQLRTPu9bs2iDi2fcs69FHmW-0dTY4v8o5Eo";
    int id;
    @BeforeMethod
    public void preCondition() throws IOException {
        // add contact --- getId
        ContactDto contactDto = ContactDto.builder()
                .name("Maya")
                .lastName("Dow")
                .email("ya@gmail.com")
                .phone("11331111111")
                .address("Haifa")
                .description("university").build();
        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);

        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/contact")
                .post(body)
                .addHeader("Authorization",token)
                .build();

        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),200);

        ContactDto contact  = gson.fromJson(response.body().string(),ContactDto.class);
        id = contact.getId();

    }

    @Test
    public void deleteByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contacts-telran.herokuapp.com/api/contact/"+id)
                .delete()
                .addHeader("Authorization",token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());

        ResponseDeleteByIdDto statusDto= gson.fromJson(response.body().string(),ResponseDeleteByIdDto.class);
        System.out.println(statusDto.getStatus());
        Assert.assertEquals(statusDto.getStatus(),"Contact was deleted!");


    }
}
