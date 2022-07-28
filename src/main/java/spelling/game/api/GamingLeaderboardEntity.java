package spelling.game.api;

import kalix.javasdk.eventsourcedentity.EventSourcedEntity;
import kalix.javasdk.eventsourcedentity.EventSourcedEntity.Effect;
import kalix.javasdk.eventsourcedentity.EventSourcedEntityContext;
import com.google.protobuf.Empty;
import spelling.game.domain.SpellingGameDomain;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// This class was initially generated based on the .proto definition by Kalix tooling. This is the implementation for the Event Sourced Entity
// Service described in your spelling/game/api/spelling_game_api.proto file.
//
// As long as this file exists it will not be overwritten: you can maintain it
// yourself, or delete it so it is regenerated as needed.

public class GamingLeaderboardEntity extends AbstractGamingLeaderboardEntity {

  @SuppressWarnings("unused")
  private final String entityId;

  public GamingLeaderboardEntity(EventSourcedEntityContext context) {
    this.entityId = context.entityId();
  }

  @Override
  public SpellingGameDomain.LeaderBoardState emptyState() {
    return SpellingGameDomain.LeaderBoardState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> newPlayer(SpellingGameDomain.LeaderBoardState currentState,
                                 SpellingGameApi.AddPlayer addCommand) {
    SpellingGameDomain.PlayerAdded playerAddedEvent =
            SpellingGameDomain.PlayerAdded.newBuilder()
                    .setPlayer(SpellingGameDomain.PlayerDetails.newBuilder()
                            .setPlayerId(addCommand.getPlayerId())
                            .setPlayerName(addCommand.getPlayerName())
                            .setPlayerScore(addCommand.getPlayerScore())
                            .build())
                    .build();
    return effects().emitEvent(playerAddedEvent).thenReply(__ ->
            Empty.getDefaultInstance());
  }

  @Override
  public Effect<SpellingGameApi.LeaderBoard>
  fetchScores(SpellingGameDomain.LeaderBoardState currentState,
              SpellingGameApi.GetScores getScores) {
    List<SpellingGameApi.PlayerDetails> apiScores = currentState.getScoresList().stream()
            .sorted()
            .map(this::convert)
            .collect(Collectors.toList());
    SpellingGameApi.LeaderBoard apiLeaderboard = SpellingGameApi.LeaderBoard.newBuilder()
            .addAllScores(apiScores).build();
    return effects().reply(apiLeaderboard);
  }

  private SpellingGameApi.PlayerDetails convert(SpellingGameDomain.PlayerDetails playerDetails) {
    return SpellingGameApi.PlayerDetails.newBuilder()
            .setPlayerId(playerDetails.getPlayerId())
            .setPlayerScore(playerDetails.getPlayerScore())
            .setPlayerName(playerDetails.getPlayerName())
            .build();
  }

  @Override
  public SpellingGameDomain.LeaderBoardState
  playerAdded(SpellingGameDomain.LeaderBoardState currentState,
              SpellingGameDomain.PlayerAdded playerAdded) {
    Map<String, SpellingGameApi.PlayerDetails> playerDetailsMap = domainPlayerToMap(currentState);
    SpellingGameDomain.PlayerDetails playerDetails = playerAdded.getPlayer();
    SpellingGameApi.PlayerDetails createdPlayer = domainPlayerToApi(playerDetails);
    // key value
    playerDetailsMap.put(playerDetails.getPlayerId(), createdPlayer);
    return mapPlayerToDomain(playerDetailsMap);
  }

  private SpellingGameApi.PlayerDetails domainPlayerToApi(
          SpellingGameDomain.PlayerDetails playerDetails) {
    return SpellingGameApi.PlayerDetails.newBuilder()
            .setPlayerId(playerDetails.getPlayerId())
            .setPlayerName(playerDetails.getPlayerName())
            .setPlayerScore(playerDetails.getPlayerScore())
            .build();
  }

  private Map<String, SpellingGameApi.PlayerDetails> domainPlayerToMap(
          SpellingGameDomain.LeaderBoardState leaderBoardState) {
    return leaderBoardState.getScoresList().stream().collect(Collectors
            .toMap(SpellingGameDomain.PlayerDetails::getPlayerId, this::domainPlayerToApi));
  }

  private SpellingGameDomain.LeaderBoardState mapPlayerToDomain(
          Map<String, SpellingGameApi.PlayerDetails> playerDetailsMap) {
    return SpellingGameDomain.LeaderBoardState.newBuilder()
            .addAllScores(
                    playerDetailsMap.values().stream().map(this::apiPlayerToDomain).collect(Collectors.toList()))
            .build();
  }

  private SpellingGameDomain.PlayerDetails apiPlayerToDomain(
          SpellingGameApi.PlayerDetails playerDetails) {
    return SpellingGameDomain.PlayerDetails.newBuilder()
            .setPlayerId(playerDetails.getPlayerId())
            .setPlayerName(playerDetails.getPlayerName())
            .setPlayerScore(playerDetails.getPlayerScore())
            .build();
  }

}
