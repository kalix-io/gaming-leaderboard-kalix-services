package spelling.game.api;

import com.google.protobuf.Empty;
import kalix.javasdk.eventsourcedentity.EventSourcedEntity;
import kalix.javasdk.eventsourcedentity.EventSourcedEntityContext;
import kalix.javasdk.testkit.EventSourcedResult;
import org.junit.Ignore;
import org.junit.Test;
import spelling.game.domain.SpellingGameDomain;

import static org.junit.Assert.*;

// This class was initially generated based on the .proto definition by Kalix tooling.
//
// As long as this file exists it will not be overwritten: you can maintain it yourself,
// or delete it so it is regenerated as needed.

public class GamingLeaderboardEntityTest {

  @Test
  @Ignore("to be implemented")
  public void exampleTest() {
    GamingLeaderboardEntityTestKit service = GamingLeaderboardEntityTestKit.of(GamingLeaderboardEntity::new);
    // // use the testkit to execute a command
    // // of events emitted, or a final updated state:
    // SomeCommand command = SomeCommand.newBuilder()...build();
    // EventSourcedResult<SomeResponse> result = service.someOperation(command);
    // // verify the emitted events
    // ExpectedEvent actualEvent = result.getNextEventOfType(ExpectedEvent.class);
    // assertEquals(expectedEvent, actualEvent);
    // // verify the final state after applying the events
    // assertEquals(expectedState, service.getState());
    // // verify the reply
    // SomeReply reply = result.getReply();
    // assertEquals(expectedReply, reply);
  }

  @Test
  @Ignore("to be implemented")
  public void newPlayerTest() {
    GamingLeaderboardEntityTestKit service = GamingLeaderboardEntityTestKit.of(GamingLeaderboardEntity::new);
    // AddPlayer command = AddPlayer.newBuilder()...build();
    // EventSourcedResult<Empty> result = service.newPlayer(command);
  }


  @Test
  @Ignore("to be implemented")
  public void fetchScoresTest() {
    GamingLeaderboardEntityTestKit service = GamingLeaderboardEntityTestKit.of(GamingLeaderboardEntity::new);
    // GetScores command = GetScores.newBuilder()...build();
    // EventSourcedResult<LeaderBoard> result = service.fetchScores(command);
  }

}
