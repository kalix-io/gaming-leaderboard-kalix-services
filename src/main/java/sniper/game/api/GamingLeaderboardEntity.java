package sniper.game.api;

import kalix.javasdk.eventsourcedentity.EventSourcedEntity;
import kalix.javasdk.eventsourcedentity.EventSourcedEntity.Effect;
import kalix.javasdk.eventsourcedentity.EventSourcedEntityContext;
import com.google.protobuf.Empty;
import sniper.game.domain.SniperGameDomain;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// This class was initially generated based on the .proto definition by Akka
// Serverless tooling. This is the implementation for the Event Sourced Entity
// Service described in your sniper/game/api/sniper_game_api.proto file.
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
  public SniperGameDomain.LeaderBoardState emptyState() {
    return SniperGameDomain.LeaderBoardState.getDefaultInstance();
  }

  @Override
  public Effect<Empty> newPlayer(SniperGameDomain.LeaderBoardState currentState,
                                 SniperGameApi.AddPlayer addCommand) {
    SniperGameDomain.PlayerAdded playerAddedEvent =
            SniperGameDomain.PlayerAdded.newBuilder()
                    .setPlayer(SniperGameDomain.PlayerDetails.newBuilder()
                            .setPlayerId(addCommand.getPlayerId())
                            .setPlayerName(addCommand.getPlayerName())
                            .setPlayerScore(addCommand.getPlayerScore())
                            .build())
                    .build();
    return effects().emitEvent(playerAddedEvent).thenReply(__ ->
            Empty.getDefaultInstance());
  }

  @Override
  public Effect<SniperGameApi.LeaderBoard>
  fetchScores(SniperGameDomain.LeaderBoardState currentState,
              SniperGameApi.GetScores getScores) {
    List<SniperGameApi.PlayerDetails> apiScores = currentState.getScoresList().stream()
            .sorted()
            .map(this::convert)
            .collect(Collectors.toList());
    SniperGameApi.LeaderBoard apiLeaderboard = SniperGameApi.LeaderBoard.newBuilder()
            .addAllScores(apiScores).build();
    return effects().reply(apiLeaderboard);
  }

  private SniperGameApi.PlayerDetails convert(SniperGameDomain.PlayerDetails playerDetails) {
    return SniperGameApi.PlayerDetails.newBuilder()
            .setPlayerId(playerDetails.getPlayerId())
            .setPlayerScore(playerDetails.getPlayerScore())
            .setPlayerName(playerDetails.getPlayerName())
            .build();
  }

  @Override
  public SniperGameDomain.LeaderBoardState
  playerAdded(SniperGameDomain.LeaderBoardState currentState,
              SniperGameDomain.PlayerAdded playerAdded) {
    Map<String, SniperGameApi.PlayerDetails> playerDetailsMap = domainPlayerToMap(currentState);
    SniperGameDomain.PlayerDetails playerDetails = playerAdded.getPlayer();
    SniperGameApi.PlayerDetails createdPlayer = domainPlayerToApi(playerDetails);
    // key value
    playerDetailsMap.put(playerDetails.getPlayerId(), createdPlayer);
    return mapPlayerToDomain(playerDetailsMap);
  }

  private SniperGameApi.PlayerDetails domainPlayerToApi(
          SniperGameDomain.PlayerDetails playerDetails) {
    return SniperGameApi.PlayerDetails.newBuilder()
            .setPlayerId(playerDetails.getPlayerId())
            .setPlayerName(playerDetails.getPlayerName())
            .setPlayerScore(playerDetails.getPlayerScore())
            .build();
  }

  private Map<String, SniperGameApi.PlayerDetails> domainPlayerToMap(
          SniperGameDomain.LeaderBoardState leaderBoardState) {
    return leaderBoardState.getScoresList().stream().collect(Collectors
            .toMap(SniperGameDomain.PlayerDetails::getPlayerId, this::domainPlayerToApi));
  }

  private SniperGameDomain.LeaderBoardState mapPlayerToDomain(
          Map<String, SniperGameApi.PlayerDetails> playerDetailsMap) {
    return SniperGameDomain.LeaderBoardState.newBuilder()
            .addAllScores(
                    playerDetailsMap.values().stream().map(this::apiPlayerToDomain).collect(Collectors.toList()))
            .build();
  }

  private SniperGameDomain.PlayerDetails apiPlayerToDomain(
          SniperGameApi.PlayerDetails playerDetails) {
    return SniperGameDomain.PlayerDetails.newBuilder()
            .setPlayerId(playerDetails.getPlayerId())
            .setPlayerName(playerDetails.getPlayerName())
            .setPlayerScore(playerDetails.getPlayerScore())
            .build();
  }

}
