package com.unwin.joodbot

import net.dean.jraw.RedditClient
import net.dean.jraw.http.UserAgent
import net.dean.jraw.http.oauth.Credentials
import net.dean.jraw.http.oauth.OAuthData

/**
 * Created by unwin on 30-Apr-16.
 */
class reddit_client {

    private var userAgent = UserAgent.of("discord-bot", "com.unwin.discordbot", "v3.0", "joodbot")
    var client = RedditClient(userAgent)

    private var credentials: Credentials? = null
    private var auth_data: OAuthData? = null


    fun authenticate() {
        var j_reader = json_reader()
        j_reader.read_json_config()

        if(j_reader.r_enabled == false) {
            println("Reddit client not enabled")
            return
        }

        credentials = Credentials.script(j_reader.r_username, j_reader.r_password, j_reader.r_client_id, j_reader.r_client_secret)
        auth_data = client.oAuthHelper.easyAuth(credentials)
        client.authenticate(auth_data)

    }



}