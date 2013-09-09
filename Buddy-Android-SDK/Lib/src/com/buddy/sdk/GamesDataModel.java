/*
 * Copyright (C) 2012 Buddy Platform, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.buddy.sdk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.buddy.sdk.Callbacks.OnCallback;
import com.buddy.sdk.Callbacks.OnResponseCallback;
import com.buddy.sdk.exceptions.BuddyServiceException;
import com.buddy.sdk.exceptions.ServiceUnknownErrorException;
import com.buddy.sdk.json.responses.GamePlayerDataResponse;
import com.buddy.sdk.json.responses.GamePlayerDataResponse.GamePlayerData;
import com.buddy.sdk.json.responses.GameScoreDataResponse;
import com.buddy.sdk.json.responses.GameScoreDataResponse.GameScoreData;
import com.buddy.sdk.json.responses.GameStateDataResponse;
import com.buddy.sdk.json.responses.GameStateDataResponse.GameStateData;

import com.buddy.sdk.responses.ListResponse;
import com.buddy.sdk.responses.Response;
import com.buddy.sdk.web.BuddyWebWrapper;

class GamesDataModel extends BaseDataModel {
    private AuthenticatedUser authUser = null;
    private User user = null;

    private String tokenOrId = "";

    GamesDataModel(BuddyClient client) {
        this.client = client;
    }

    GamesDataModel(BuddyClient client, AuthenticatedUser authUser, User user) {
        this.client = client;
        this.authUser = authUser;
        this.user = user;

        tokenOrId = this.authUser != null ? this.authUser.getToken() : String.valueOf(this.user
                .getId());
    }

    public void getHighScores(String scoreBoardName, Integer recordLimit, Object state,
            final OnCallback<ListResponse<GameScore>> callback) {
        
        BuddyWebWrapper.Game_Score_GetBoardHighScores(client.getAppName(), client.getAppPassword(),
                tokenOrId, scoreBoardName, recordLimit, this.RESERVED, state,
                new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<GameScore> listResponse = new ListResponse<GameScore>();

                        if (response != null) {
                            if (response.completed) {
                                GameScoreDataResponse result = getJson(response.response,
                                        GameScoreDataResponse.class);
                                if (result != null) {
                                    List<GameScore> scoreList = new ArrayList<GameScore>(
                                            result.data.size());
                                    for (GameScoreData data : result.data) {
                                        GameScore gameScore = new GameScore(client, data);
                                        scoreList.add(gameScore);
                                    }
                                    listResponse.setList(scoreList);
                                } else {
                                    listResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                listResponse.setThrowable(response.exception);
                            }
                        } else {
                            listResponse.setThrowable(new ServiceUnknownErrorException());
                        }

                        callback.OnResponse(listResponse, state);

                    }

                });
    }


    public void getLowScores(String scoreBoardName, Integer recordLimit, Object state,
            final OnCallback<ListResponse<GameScore>> callback) {
        
        BuddyWebWrapper.Game_Score_GetBoardLowScores(client.getAppName(), client.getAppPassword(),
                tokenOrId, scoreBoardName, recordLimit, this.RESERVED, state,
                new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<GameScore> listResponse = new ListResponse<GameScore>();

                        if (response != null) {
                            if (response.completed) {
                                GameScoreDataResponse result = getJson(response.response,
                                        GameScoreDataResponse.class);
                                if (result != null) {
                                    List<GameScore> scoreList = new ArrayList<GameScore>(
                                            result.data.size());
                                    for (GameScoreData data : result.data) {
                                        GameScore gameScore = new GameScore(client, data);
                                        scoreList.add(gameScore);
                                    }
                                    listResponse.setList(scoreList);
                                } else {
                                    listResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                listResponse.setThrowable(response.exception);
                            }
                        } else {
                            listResponse.setThrowable(new ServiceUnknownErrorException());
                        }

                        callback.OnResponse(listResponse, state);

                    }

                });
    }

    public void getAll(Integer recordLimit, Object state,
            final OnCallback<ListResponse<GameScore>> callback) {
        
        BuddyWebWrapper.Game_Score_GetScoresForUser(client.getAppName(), client.getAppPassword(),
                tokenOrId, recordLimit, this.RESERVED, state, new OnResponseCallback() {

                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<GameScore> listResponse = new ListResponse<GameScore>();

                        if (response != null) {
                            if (response.completed) {
                                GameScoreDataResponse result = getJson(response.response,
                                        GameScoreDataResponse.class);
                                if (result != null) {
                                    List<GameScore> scoreList = new ArrayList<GameScore>(
                                            result.data.size());
                                    for (GameScoreData data : result.data) {
                                        GameScore gameScore = new GameScore(client, data);
                                        scoreList.add(gameScore);
                                    }
                                    listResponse.setList(scoreList);
                                } else {
                                    listResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                listResponse.setThrowable(response.exception);
                            }
                        } else {
                            listResponse.setThrowable(new ServiceUnknownErrorException());
                        }

                        callback.OnResponse(listResponse, state);

                    }

                });
    }

    public void add(double scoreLatitude, double scoreLongitude, String scoreRank, double scoreValue,
            String scoreBoardName, String applicationTag, boolean oneScorePerPlayerBit,
            Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Game_Score_Add(client.getAppName(), client.getAppPassword(), tokenOrId,
                (float) scoreLatitude, (float) scoreLongitude, scoreRank, (float) scoreValue, scoreBoardName,
                applicationTag, oneScorePerPlayerBit ? 1 : 0, this.RESERVED, state,
                new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }
                });
    }

    public void deleteAll(Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Game_Score_DeleteAllScoresForUser(client.getAppName(),
                client.getAppPassword(), tokenOrId, this.RESERVED, state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }
                });
    }

    public void findScores(User user, Integer searchDistance, double searchLatitude,
            double searchLongitude, Integer recordLimit, String searchBoard, Integer timeFilter,
            Integer minScore, String applicationTag, Object state,
            final OnCallback<ListResponse<GameScore>> callback) {
        
        String userId = user == null ? "-1" : String.valueOf(user.getId());
        BuddyWebWrapper.Game_Score_SearchScores(client.getAppName(), client.getAppPassword(),
                userId, searchDistance, (float) searchLatitude, (float) searchLongitude, recordLimit, searchBoard,
                timeFilter, minScore, applicationTag, RESERVED, state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<GameScore> listResponse = new ListResponse<GameScore>();

                        if (response != null) {
                            if (response.completed) {
                                GameScoreDataResponse result = getJson(response.response,
                                        GameScoreDataResponse.class);
                                if (result != null) {
                                    List<GameScore> scoreList = new ArrayList<GameScore>(
                                            result.data.size());
                                    for (GameScoreData data : result.data) {
                                        GameScore gameScore = new GameScore(client, data);
                                        scoreList.add(gameScore);
                                    }
                                    listResponse.setList(scoreList);
                                } else {
                                    listResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                listResponse.setThrowable(response.exception);
                            }
                        } else {
                            listResponse.setThrowable(new ServiceUnknownErrorException());

                        }

                        callback.OnResponse(listResponse, state);

                    }

                });
    }

    public void add(String gameStateKey, String gameStateValue, String applicationTag,
            Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Game_State_Add(client.getAppName(), client.getAppPassword(), tokenOrId,
                gameStateKey, gameStateValue, applicationTag, this.RESERVED, state,
                new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void get(String gameStateKey, Object state,
            final OnCallback<Response<GameState>> callback) {
        
        BuddyWebWrapper.Game_State_Get(client.getAppName(), client.getAppPassword(), tokenOrId,
                gameStateKey, this.RESERVED, state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<GameState> stateResponse = new Response<GameState>();

                        if (response != null) {
                            if (response.completed) {
                                GameStateDataResponse result = getJson(response.response,
                                        GameStateDataResponse.class);
                                if (result != null) {
                                    if (result.data.size() > 0) {
                                        GameState gameState = new GameState(result.data.get(0));
                                        stateResponse.setResult(gameState);
                                    } else {
                                        stateResponse.setResult(null);
                                    }
                                } else {
                                    stateResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                stateResponse.setThrowable(response.exception);
                            }
                        } else {
                            stateResponse.setThrowable(new ServiceUnknownErrorException());

                        }

                        callback.OnResponse(stateResponse, state);

                    }

                });
    }

    public void getAll(Object state, final OnCallback<Response<Map<String, GameState>>> callback) {
        
        BuddyWebWrapper.Game_State_GetAll(client.getAppName(), client.getAppPassword(), tokenOrId,
                this.RESERVED, state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Map<String, GameState>> listResponse = new Response<Map<String, GameState>>();

                        if (response != null) {
                            if (response.completed) {
                                GameStateDataResponse result = getJson(response.response,
                                        GameStateDataResponse.class);
                                if (result != null) {
                                    Map<String, GameState> map = new HashMap<String, GameState>();

                                    for (GameStateData data : result.data) {
                                        GameState gameState = new GameState(data);
                                        map.put(data.stateKey, gameState);
                                    }
                                    listResponse.setResult(map);
                                } else {
                                    listResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                listResponse.setThrowable(response.exception);
                            }
                        } else {
                            listResponse.setThrowable(new ServiceUnknownErrorException());

                        }

                        callback.OnResponse(listResponse, state);
                    }

                });
    }

    public void remove(String gameStateKey, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Game_State_Remove(client.getAppName(), client.getAppPassword(), tokenOrId,
                gameStateKey, this.RESERVED, state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void update(String gameStateKey, String gameStateValue, String applicationTag,
            Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Game_State_Update(client.getAppName(), client.getAppPassword(), tokenOrId,
                gameStateKey, gameStateValue, applicationTag, this.RESERVED, state,
                new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void add(String playerName, double playerLatitude, double playerLongitude,
            String playerRank, String playerBoardName, String applicationTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Game_Player_Add(client.getAppName(), client.getAppPassword(), tokenOrId,
                playerName, (float) playerLatitude, (float) playerLongitude, playerRank, playerBoardName,
                applicationTag, this.RESERVED, state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void update(String playerName, double playerLatitude, double playerLongitude,
            String playerRank, String playerBoardName, String applicationTag, Object state,
            final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Game_Player_Update(client.getAppName(), client.getAppPassword(), tokenOrId,
                playerName, (float) playerLatitude, (float) playerLongitude, playerRank, playerBoardName,
                applicationTag, this.RESERVED, state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void delete(Object state, final OnCallback<Response<Boolean>> callback) {
        
        BuddyWebWrapper.Game_Player_Delete(client.getAppName(), client.getAppPassword(), tokenOrId,
                this.RESERVED, state, new OnResponseCallback() {
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<Boolean> booleanResponse = getBooleanResponse(response);
                        callback.OnResponse(booleanResponse, state);
                    }

                });
    }

    public void getInfo(Object state, final OnCallback<Response<GamePlayer>> callback) {
        
        BuddyWebWrapper.Game_Player_GetPlayerInfo(client.getAppName(), client.getAppPassword(),
                tokenOrId, this.RESERVED, state, new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        Response<GamePlayer> playerResponse = new Response<GamePlayer>();

                        if (response != null) {
                            if (response.completed) {
                                GamePlayerDataResponse result = getJson(response.response,
                                        GamePlayerDataResponse.class);
                                if (result != null) {
                                	if(result.data.size() > 0) {
	                                    GamePlayer gameState = new GamePlayer(client, authUser,
	                                    		result.data.get(0));
	                                    playerResponse.setResult(gameState);
                                	} else {
                                		playerResponse.setResult(null);
                                	}
                                } else {
                                	playerResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                            	playerResponse.setThrowable(response.exception);
                            }
                        } else {
                        	playerResponse.setThrowable(new ServiceUnknownErrorException());

                        }

                        callback.OnResponse(playerResponse, state);
                    }

                });
    }

    public void find(Integer searchDistance, double searchLatitude, double searchLongitude,
            Integer recordLimit, String searchBoard, Integer timeFilter, Integer minimumScore,
            String applicationTag, String playerRank, Object state, final OnCallback<ListResponse<GamePlayer>> callback) {
        
        BuddyWebWrapper.Game_Player_SearchPlayers(client.getAppName(), client.getAppPassword(),
                tokenOrId, searchDistance, (float) searchLatitude, (float) searchLongitude, recordLimit,
                searchBoard, timeFilter, minimumScore, applicationTag, playerRank == null ? "" : playerRank, this.RESERVED, state,
                new OnResponseCallback() {
        	
                    @Override
                    public void OnResponse(BuddyCallbackParams response, Object state) {
                        ListResponse<GamePlayer> listResponse = new ListResponse<GamePlayer>();

                        if (response != null) {
                            if (response.completed) {
                                GamePlayerDataResponse result = getJson(response.response,
                                        GamePlayerDataResponse.class);
                                if (result != null) {
                                    List<GamePlayer> playerList = new ArrayList<GamePlayer>(
                                            result.data.size());
                                    for (GamePlayerData data : result.data) {
                                        GamePlayer gameState = new GamePlayer(client, authUser,
                                                data);
                                        playerList.add(gameState);
                                    }
                                    listResponse.setList(playerList);
                                } else {
                                    listResponse.setThrowable(new BuddyServiceException(
                                            response.response));
                                }
                            } else {
                                listResponse.setThrowable(response.exception);
                            }
                        } else {
                            listResponse.setThrowable(new ServiceUnknownErrorException());

                        }

                        callback.OnResponse(listResponse, state);
                    }

                });
    }
}
