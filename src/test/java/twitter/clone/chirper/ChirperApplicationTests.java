// package twitter.clone.chirper;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertNull;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;

// import twitter.clone.chirper.domain.ChirperUser;
// import twitter.clone.chirper.domain.Message;
// import twitter.clone.chirper.service.MessageManager;
// import twitter.clone.chirper.service.UserManager;

// @SpringBootTest
// class ChirperApplicationTests {
// @Autowired
// MessageManager mm;
// @Autowired
// UserManager um;

// @Test
// void contextLoads() {
// assertNotNull(mm);
// assertNotNull(um);
// }

// @Test
// void UserCRUDTest() {
// ChirperUser userToInsert = new ChirperUser();
// userToInsert.setFirstName("imie");
// userToInsert.setLastName("nazwisko");
// userToInsert.setNick("nickname");
// userToInsert.setEmail("email@gmail.com");
// userToInsert.setPassword("haslohaslo");

// um.save(userToInsert);

// ChirperUser userToCompare = um.findById(userToInsert.getId());

// assertEquals(userToCompare.getFirstName(), userToInsert.getFirstName());

// ChirperUser editRef = um.getOneById(userToInsert.getId());
// editRef.setNick("different");
// um.save(editRef);
// userToCompare = um.findById(userToInsert.getId());

// assertFalse(userToCompare.getNick().equals(userToInsert.getNick()));

// um.deleteById(userToInsert.getId());

// ChirperUser userIsntThere = um.findById(userToInsert.getId());
// assertNull(userIsntThere);
// }

// @Test
// void MessageCRUDTest() {
// Message msgToInsert = new Message();
// msgToInsert.setContent("content");

// mm.save(msgToInsert);

// Message msgToCompare = mm.findById(msgToInsert.getId());

// assertEquals(msgToCompare.getContent(), msgToInsert.getContent());

// Message editRef = mm.getOneById(msgToInsert.getId());
// editRef.setContent("different");
// mm.save(editRef);
// msgToCompare = mm.findById(msgToInsert.getId());

// assertFalse(msgToCompare.getContent().equals(msgToInsert.getContent()));

// mm.deleteById(msgToInsert.getId());

// Message msgIsntThere = mm.findById(msgToInsert.getId());
// assertNull(msgIsntThere);
// }
// }
