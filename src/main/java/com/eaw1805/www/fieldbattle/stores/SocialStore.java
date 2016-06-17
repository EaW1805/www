package com.eaw1805.www.fieldbattle.stores;


import com.google.gwt.user.client.Timer;
import com.eaw1805.www.fieldbattle.stores.dto.SocialFriend;
import com.eaw1805.www.fieldbattle.stores.dto.SocialGame;
import com.eaw1805.www.fieldbattle.views.layout.infopanels.NationSocialInfoPanel;
import com.eaw1805.www.fieldbattle.views.layout.social.SocialLoadingPanel;
import com.eaw1805.www.fieldbattle.views.layout.social.StandAloneSocialPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocialStore {
    private SocialFriend me = new SocialFriend();
    private SocialFriend none = new SocialFriend();
    private List<SocialFriend> friends = new ArrayList<SocialFriend>();
    private List<SocialGame> scenarioGames = new ArrayList<SocialGame>();
    private Map<String, SocialFriend> friendsMap = new HashMap<String, SocialFriend>();
    private static SocialStore instance = null;
    private SocialStore() {
        none.setName("Open position");
        none.setId("");
        //make this private...
    }

    public static SocialStore getInstance() {
        if (instance == null) {
            instance = new SocialStore();
        }
        return instance;
    }

    public void addFriend(final String id, final String name) {
        SocialFriend friend = new SocialFriend();
        friend.setId(id);
        friend.setName(name);
        friends.add(friend);
        friendsMap.put(id, friend);
    }

    public void friendsLoaded() {
        SocialLoadingPanel.getInstance().nextStep("Friends loaded...");
    }

    public void init(String facebookId, final String name) {
        me.setId(facebookId);
        me.setName(name);
        friendsMap.put(facebookId, me);
        SocialLoadingPanel.getInstance().nextStep("Connected to facebook...");
        init();
    }

//    public void init() {
//
//
//    }


    public native void initPersonalData(SocialStore ss) /*-{
        $wnd.FB.api('/me', function(response) {
            ss.@com.eaw1805.www.fieldbattle.stores.SocialStore::init(Ljava/lang/String;Ljava/lang/String;)(response.id, response.name)

        });

    }-*/;

    public void tryToGetFriendsAgain() {
        new Timer() {

            @Override
            public void run() {
                getFriends(SocialStore.getInstance());
            }
        }.schedule(1000);
    }

    public native void getFriends(SocialStore ss) /*-{
        $wnd.FB.api('/me/friends', function(response) {
            if(response.data) {
                $wnd.$.each(response.data,function(index,friend) {
                    ss.@com.eaw1805.www.fieldbattle.stores.SocialStore::addFriend(Ljava/lang/String;Ljava/lang/String;)(friend.id, friend.name);
                });
                ss.@com.eaw1805.www.fieldbattle.stores.SocialStore::friendsLoaded()();
            } else {
                ss.@com.eaw1805.www.fieldbattle.stores.SocialStore::tryToGetFriendsAgain()();
            }
        });
    }-*/;

    public SocialFriend getUser(final String userId) {
        if (friendsMap.containsKey(userId)) {
            return friendsMap.get(userId);
        }
        return null;
    }

    public native void getUser(final String userId, final SocialStore ss, final NationSocialInfoPanel nsip) /*-{
        $wnd.FB.api('/' + userId, function(response) {
            ss.@com.eaw1805.www.fieldbattle.stores.SocialStore::addFriend(Ljava/lang/String;Ljava/lang/String;)(response.id, response.name);
            nsip.@com.eaw1805.www.fieldbattle.views.layout.infopanels.NationSocialInfoPanel::updateLabel()();
        });
    }-*/;


    /**
     * init facebook friends.
     **/
    public void init() {
        friends.clear();
        getFriends(this);
        StandAloneSocialPanel.getInstance().updateMyGames();
        StandAloneSocialPanel.getInstance().updatePending();
        StandAloneSocialPanel.getInstance().autoUpdate();

    }

    public native void inviteFriend(final String facebookId) /*-{
        try {
        $wnd.FB.ui({method: 'apprequests',
            message: 'Play Empires at War field battle with me.',
            to: facebookId
        }, requestCallback);
        } catch (e) {
            alert("f? " + e);
        }

        function requestCallback() {
            //do nothing here yet
//            alert("ok");
        }

    }-*/;

    public native void initToFb() /*-{
        try {
            $wnd.fbAsyncInit = function() {
                $wnd.FB.init({
                    appId      : '1467405233487625',
                    status     : true,
                    xfbml      : true
            });
                $wnd.FB.Event.subscribe('auth.authResponseChange', function(response) {
                alert("response: " + response.status);
                if (response.status === 'connected') {
//                    console.log('Logged in');
                    alert("logged in!")
                } else {
                    $wnd.FB.login();
                }
            });
        };
        } catch (e) {
            alert(e);
        }

    }-*/;


    public List<SocialFriend> getFriends() {
        return friends;
    }

    public List<SocialGame> getScenarioGames() {
        return scenarioGames;
    }

    public SocialFriend getMe() {
        return me;
    }

    public SocialFriend getNone() {
        return none;
    }
}
