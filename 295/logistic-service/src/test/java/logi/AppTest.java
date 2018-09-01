package logi;

import logi.domain.model.Address;
import logi.domain.model.User;
import logi.domain.repository.AddressRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {

  @Autowired
  private TestRestTemplate restTemplate;
  @Autowired
  private AddressRepository ar;

  @Test
  public void register() {
    User u = new User();
    u.setName("ccc");
    u.setPwd("ppp");
    Address a1 = new Address();
    a1.setZip("95050");
    Address a2 = new Address();
    a2.setCity("Santa Clara");
    List<Address> as = new ArrayList<>();
    as.add(a1);
    as.add(a2);
    as.add(ar.findById(89L).get());
    u.setAddresses(as);

    ResponseEntity<User> entity = this.restTemplate.exchange(
        RequestEntity.post(uri("/api/pub/register")).contentType(MediaType.APPLICATION_JSON).body(u),
            User.class);
    assertEquals(HttpStatus.OK, entity.getStatusCode());
    u = entity.getBody();
    assertEquals("ccc", u.getName());
  }

  private URI uri(String path) {
    return restTemplate.getRestTemplate().getUriTemplateHandler().expand(path);
  }

}