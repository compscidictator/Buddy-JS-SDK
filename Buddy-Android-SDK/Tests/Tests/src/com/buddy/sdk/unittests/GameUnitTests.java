
package com.buddy.sdk.unittests;

import java.util.List;
import java.util.Map;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.GamePlayer;
import com.buddy.sdk.GameScore;
import com.buddy.sdk.GameState;
import com.buddy.sdk.responses.Response;

import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.web.BuddyHttpClientFactory;

public class GameUnitTests extends BaseUnitTest {

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        createAuthenticatedUser();
    }

    public void testGameScoresAdd() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getGameScores().add(testScoreValue, testScoreBoardName, testScoreRank,
                testGameLatitude, testGameLongitude, true, "", null,
                new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }

    public void testGameScoresGetScoresUser() {
        String jsonResponse = readDataFromFile("DataResponses/GameUnitTests-UserScores.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getGameScores().getAll(testRecordLimit, null,
                new OnCallback<ListResponse<GameScore>>() {
                    public void OnResponse(ListResponse<GameScore> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.isCompleted());

                        List<GameScore> list = response.getList();
                        GameScore score = list.get(0);
                        assertEquals(testUserIdInteger.compareTo(score.getUserID()), 0);
                    }

                });
    }

    public void testGameScoresGetBoardHighScores() {
        String jsonResponse = readDataFromFile("DataResponses/GameUnitTests-BoardHighScores.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testClient.getGameBoards().getHighScores(testScoreBoardName, testRecordLimit, null,
                new OnCallback<ListResponse<GameScore>>() {
                    public void OnResponse(ListResponse<GameScore> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.isCompleted());

                        List<GameScore> list = response.getList();
                        GameScore score = list.get(0);
                        assertEquals(testUserIdInteger.compareTo(score.getUserID()), 0);
                    }

                });
    }

    public void testGameSearchScores() {
        String jsonResponse = readDataFromFile("DataResponses/GameUnitTests-SearchScores.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testClient.getGameBoards().findScores(testAuthUser, testGameSearchDistance,
                testGameLatitude, testGameLongitude, testRecordLimit, "", -1, -1, "", null,
                new OnCallback<ListResponse<GameScore>>() {
                    public void OnResponse(ListResponse<GameScore> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.isCompleted());

                        List<GameScore> list = response.getList();
                        GameScore score = list.get(0);
                        assertEquals(testUserIdInteger.compareTo(score.getUserID()), 0);
                    }

                });
    }

    public void testGameScoresDeleteAllScores() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getGameScores().deleteAll(null, new OnCallback<Response<Boolean>>() {
            public void OnResponse(Response<Boolean> response, Object state) {
                assertNotNull(response);
                assertTrue(response.getResult());
            }

        });
    }

    // Game states
    public void testGameStateAdd() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getGameStates().add(testStateKey, testStateValue, "", null,
                new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }

    public void testGameStateRemove() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getGameStates().remove(testStateKey, null,
                new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }

    public void testGameStateGet() {
        String jsonResponse = readDataFromFile("DataResponses/GameUnitTests-GetState.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getGameStates().get(testStateKey, null, new OnCallback<Response<GameState>>() {
            public void OnResponse(Response<GameState> response, Object state) {
                assertNotNull(response);
                assertTrue(response.isCompleted());

                GameState gameState = response.getResult();
                assertEquals("Test Key", gameState.getKey());
            }

        });
    }

    public void testGameStateGetAll() {
        String jsonResponse = readDataFromFile("DataResponses/GameUnitTests-GetState.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getGameStates().getAll(null,
                new OnCallback<Response<Map<String, GameState>>>() {
                    public void OnResponse(Response<Map<String, GameState>> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.isCompleted());

                        Map<String, GameState> list = response.getResult();
                        GameState gameState = list.get("Test Key");
                        assertEquals("Test State Value", gameState.getValue());
                    }

                });
    }

    public void testGameStateUpdate() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getGameStates().update(testStateKey, testStateValue, "", null,
                new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }

    // Game Players
    public void testGamePlayerAdd() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getGamePlayers().add(testPlayerName, testPlayerBoard, testPlayerRank,
                testGameLatitude, testGameLongitude, "", null, new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }

    public void testGamePlayerUpdate() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getGamePlayers().update(testPlayerName, testPlayerBoard, "", testGameLatitude,
                testGameLongitude, "", null, new OnCallback<Response<Boolean>>() {
                    public void OnResponse(Response<Boolean> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.getResult());
                    }

                });
    }

    public void testGamePlayerDelete() {
        String jsonResponse = readDataFromFile("DataResponses/GenericSuccessResponse.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getGamePlayers().delete(null, new OnCallback<Response<Boolean>>() {
            public void OnResponse(Response<Boolean> response, Object state) {
                assertNotNull(response);
                assertTrue(response.getResult());
            }

        });
    }

    public void testGamePlayerGetInfo() {
        String jsonResponse = readDataFromFile("DataResponses/GameUnitTests-PlayerInfo.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getGamePlayers().getInfo(null, new OnCallback<Response<GamePlayer>>() {
            public void OnResponse(Response<GamePlayer> response, Object state) {
                assertNotNull(response);
                assertTrue(response.isCompleted());

                GamePlayer player = response.getResult();
                assertEquals("Test Player Board", player.getBoardName());
                assertEquals("best", player.getRank());
            }

        });
    }

    public void testGamePlayerSearch() {
        String jsonResponse = readDataFromFile("DataResponses/GameUnitTests-SearchPlayers.json");
        BuddyHttpClientFactory.addDummyResponse(jsonResponse);

        testAuthUser.getGamePlayers().find(testGameSearchDistance, testGameLatitude,
                testGameLongitude, testRecordLimit, testPlayerBoard, -1, -1, "", "", null,
                new OnCallback<ListResponse<GamePlayer>>() {
                    public void OnResponse(ListResponse<GamePlayer> response, Object state) {
                        assertNotNull(response);
                        assertTrue(response.isCompleted());

                        List<GamePlayer> list = response.getList();
                        GamePlayer player = list.get(0);
                        assertEquals("Test Player", player.getName());
                        assertEquals("best", player.getRank());
                    }

                });
    }
}
