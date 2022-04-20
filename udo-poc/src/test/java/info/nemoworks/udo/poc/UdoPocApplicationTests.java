package info.nemoworks.udo.poc;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UdoPocApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testAirpurifier() {
        AirPurifier airPurifier = new AirPurifier("fan.mypurifier2",
            "on",
            "idle",
            21,
            37,
            52);
        System.out.println(new Gson().toJson(airPurifier));
    }

}
