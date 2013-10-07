package com.applications.frodo.networking;

import com.applications.frodo.GlobalParameters;
import com.applications.frodo.blocks.SocialNetworks;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by siddharth on 29/09/13.
 */
public class SignupFactory {

    private static SignupFactory ourInstance = new SignupFactory();
    private Map<SocialNetworks, ISignup> signups=new EnumMap<SocialNetworks, ISignup>(SocialNetworks.class);

    public static SignupFactory getInstance() {
        return ourInstance;
    }

    private SignupFactory() {
    }

    public ISignup getSignup(SocialNetworks socialNet){
        if(signups.containsKey(socialNet)){
            return signups.get(socialNet);
        }else{
            synchronized (signups){
                if(signups.containsKey(socialNet)){
                    return signups.get(socialNet);
                }else{
                    ISignup signup=new SignupWithSocialNetworkID(GlobalParameters.getInstance().getUser(),
                            BackendRequestParameters.getInstance().getIp(),BackendRequestParameters.getInstance().getPort(),
                            BackendRequestParameters.getInstance().getSingupQuery(),BackendRequestParameters.getInstance().getGetUserDataQuery(),
                            BackendRequestParameters.getInstance().getTimeout());
                    signups.put(socialNet,signup);
                    return signup;
                }
            }
        }
    }
}
